<template>
  <section>
    <table>
      <tr>
        <th>
          <div class="header-wrap">#</div>
        </th>
        <th>
          <div class="header-wrap">
            Название<img src="@/assets/sort.png" @click="sort('name')" />
          </div>
        </th>
        <th>
          <div class="header-wrap">
            Участники<img src="@/assets/sort.png" @click="sort('attendees')" />
          </div>
        </th>
        <th>
          <div class="header-wrap">Действия<img src="@/assets/sort.png" /></div>
        </th>
      </tr>
      <tr>
        <th><input type="text" disabled /></th>
        <th>
          <input
            type="text"
            @input="handleSort"
            v-model="sortingInput"
            min="1"
            max="10"
          />
        </th>
        <th><input type="text" disabled /></th>
        <th><input type="text" disabled /></th>
      </tr>
      <tr v-for="(event, idx) in myEvents.myEvents.value" :key="idx">
        <td v-if="flagSet(event.start as string, event.end as string)">
          {{ idx }}
        </td>
        <td v-if="flagSet(event.start as string, event.end as string)">
          {{ event.title }}
        </td>
        <td v-if="flagSet(event.start as string, event.end as string)">
          {{ event.participants }}
        </td>
        <td v-if="flagSet(event.start as string, event.end as string)">
          <div class="actions">
            <img
              src="@/assets/star.png"
              @click="handleAddToFav(event.id as string)"
            />
            <img
              src="@/assets/share.png"
              @click="handleShare(event.id as string)"
            />
            <img
              src="@/assets/pencil.png"
              @click="handleOpenEditor(event.id as string)"
            />
            <img
              src="@/assets/delete.png"
              @click="handleDeleteEvent(event.id as string)"
            />
          </div>
        </td>
      </tr>
    </table>
    <div v-if="isSharing" class="share-modal">
      <div class="field">
        <input
          type="text"
          placeholder="Enter user email"
          v-model="inputEmail"
        />
        <div class="actions">
          <button @click="isSharing = false">Cancel</button>
          <button @click="acceptShare">Share</button>
        </div>
      </div>
    </div>
    <div class="event-wrapper" v-if="isEditing">
      <form @submit.prevent>
        <input type="date" id="date" v-model="input.date" />
        <div class="fullday-block">
          <input type="checkbox" id="fullday" v-model="input.isFullDay" />
          <span>Full day event?</span>
        </div>

        <div class="time-block" v-if="!input.isFullDay">
          <input type="time" id="start" v-model="input.start" />
          <input type="time" id="end" v-model="input.end" />
        </div>
        <input
          type="text"
          placeholder="Title"
          id="title"
          v-model="input.title"
        />
        <input
          type="number"
          id="employees"
          placeholder="Employees"
          v-model="input.attendees"
        />
        <input
          type="text"
          id="description"
          placeholder="description"
          v-model="input.description"
        />
        <div class="actions">
          <div class="deny-btn custom-btn" @click="isEditing = false">
            Cancel
          </div>
          <div class="accept-btn custom-btn" @click="handleEdit">Edit</div>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import { useDate } from "@/composables/useDate";
import { useEvents } from "@/composables/useEvents";
import { auth } from "@/firebase";
import { UserCalendarInput } from "@/types/interfaces/CalendarInterfaces";
import { Ref, ref } from "vue";
const myEvents = useEvents();
const date = useDate();
const sortingInput = ref<string>("");
const isSharing: Ref<boolean> = ref(false);
const isEditing: Ref<boolean> = ref(false);
const eventId: Ref<string> = ref("");
const inputEmail: Ref<string> = ref("");
const sortedBy: Ref<string> = ref("");
const input: Ref<UserCalendarInput> = ref({
  date:
    date.date.value.getFullYear() +
    "-" +
    date.date.value.getMonth() +
    "-" +
    date.date.value.getDate(),
  title: "",
  start: "00:00:00",
  end: "23:59:59",
  isFullDay: false,
  attendees: 0,
  description: "",
});
const handleOpenEditor = (id: string) => {
  eventId.value = id;
  isEditing.value = true;
};
const handleShare = (id: string) => {
  eventId.value = id;
  isSharing.value = true;
};
const handleEdit = () => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch(`http://localhost:8070/api/events/${eventId.value}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${data}`,
      },
      body: JSON.stringify({
        title: input.value.title,
        startDate: `${input.value.date} ${input.value.start}`,
        endDate: `${input.value.date} ${input.value.end}`,
        participants: input.value.attendees,
        description: input.value.description,
        favourite: false,
      }),
    })
      .then((r) => r.json())
      .then((data) => console.log(data));
  });

  isEditing.value = false;
};
const sort = (field: string) => {
  if (field === "name") {
    if (sortedBy.value === "name") {
      myEvents.setEvents(
        myEvents.myEvents.value.sort((a: any, b: any) =>
          b.title.localeCompare(a.title)
        )
      );
      sortedBy.value = "name";
    } else {
      myEvents.setEvents(
        myEvents.myEvents.value.sort((a: any, b: any) =>
          a.title.localeCompare(b.title)
        )
      );
      sortedBy.value = "";
    }
  } else if (field === "attendees") {
    if (sortedBy.value === "attendees") {
      myEvents.setEvents(
        myEvents.myEvents.value.sort(
          (a: any, b: any) => a.participants + b.participants
        )
      );

      sortedBy.value = "attendees";
    } else {
      myEvents.setEvents(
        myEvents.myEvents.value.sort(
          (a: any, b: any) => a.participants - b.participants
        )
      );
      sortedBy.value = "";
    }
  }
};
const handleSort = () => {
  const eventIds: any[] = [];
  const tmp = myEvents.myEvents.value.filter((el: any) =>
    el.title.includes(sortingInput.value)
  );
  myEvents.setEvents(tmp);
  if (sortingInput.value === "") {
    auth.currentUser?.getIdToken().then((data) => {
      fetch("http://localhost:8070/api/events", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${data}`,
        },
      })
        .then((r) => r.json())
        .then((data) => myEvents.setEvents(data));
    });
  }
};
const acceptShare = () => {
  fetch(`http://localhost:5000/users?email=${inputEmail.value}`)
    .then((r) => r.json())
    .then((data) =>
      fetch("http://localhost:5000/userEvents", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId: data[0].id, eventId: eventId.value }),
      })
    );
  isSharing.value = false;
};
const handleDeleteEvent = (id: string) => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch(`http://localhost:8070/api/events/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${data}`,
      },
    });
  });
};
const flagSet = (start: string, end: string) => {
  // return Boolean(
  //   Date.parse(start as string) >=
  //     new Date(date.date.value.setHours(0, 0, 0, 0)).getTime() &&
  //     Date.parse(end as string) <=
  //       new Date(date.date.value.setHours(23, 59, 59, 999)).getTime()
  // );
  return true;
};
const handleAddToFav = (id: string) => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch(`http://localhost:8070/api/events/makeFavourite/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${data}`,
      },
    });
  });
};
</script>

<style scoped>
table {
  display: table;
  border-collapse: collapse;
  width: 100%;
  max-width: 600px;
  margin: auto;
}

th,
td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

th {
  background-color: #f2f2f2;
  text-align: center;
}
th img {
  width: 15px;
}
td img {
  width: 20px;
}
img {
  cursor: pointer;
  transition: all;
  transition-duration: 200ms;
}
img:hover {
  transform: scale(1.1);
}
.header-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1em;
}
.actions {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.share-modal {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-color: rgba(184, 184, 184, 0.719);

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1em;
}
.field {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1em;
}
.field input {
  padding: 0.7em;
  font-size: 1.2em;
}
.actions {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.actions button {
  padding: 0.5em;
  font-size: 1em;
}
.custom-btn {
  cursor: pointer;
  transition: all;
  transition-duration: 200ms;
  border-radius: 5px;
}
.custom-btn:hover {
  transform: scale(1.1);
}
.time-block {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
input#start,
input#end {
  width: fit-content;
}
.add-btn {
  padding: 0.5em;
  background-color: rgba(0, 255, 0, 0.467);
  width: fit-content;
}
.calendar-container {
  width: 400px;
  height: 400px;
}
.fullday-block {
  display: flex;
  flex-direction: row;
  align-items: center;
  background-color: white;
  padding: 0.5em;
  border: 1px solid black;
  border-radius: 3px;
  gap: 1em;
  font-size: 1.2em;
}
.event-wrapper {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  background-color: rgba(134, 134, 134, 0.584);
  z-index: 99999;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.event-wrapper form {
  display: flex;
  flex-direction: column;
  justify-content: start;
  gap: 1em;
}
.event-wrapper form input {
  padding: 0.5em;
  font-size: 1.2em;
  width: 300px;
  border: 1px solid black;
  border-radius: 3px;
}
.actions {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
}
.actions div {
  padding: 0.4em;
}
.deny-btn {
  background-color: rgb(255, 0, 0);
}
.accept-btn {
  background-color: rgb(26, 255, 0);
}
#date {
  width: 300px;
}
#fullday {
  align-self: flex-start;
  justify-self: start;
}
input#fullday {
  width: fit-content;
  transform: scale(1.8);
}
.main-block {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 5em;
}
</style>
