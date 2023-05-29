<template>
  <section>
    <HeaderComponent />
    <div class="main-block">
      <div class="calendar-container">
        <FullCalendar
          :options="calendarOptions"
          ref="calendarRef"
          :key="refKey"
        />
      </div>
      <div class="event-table"><EventTable /></div>
    </div>

    <div @click="handleOpenAdder(true)" class="add-btn custom-btn">
      Add event
    </div>
    <div @click="createReport" class="add-btn custom-btn">Create Report</div>
    <div class="event-wrapper" v-if="isAdderOpened">
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
          <div class="deny-btn custom-btn" @click="handleOpenAdder(false)">
            Cancel
          </div>
          <div class="accept-btn custom-btn" @click="handleAddEvent">Add</div>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import FullCalendar from "@fullcalendar/vue3";
import { DateClickArg } from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { ref, Ref } from "vue";
import { CalendarOptions } from "@fullcalendar/core";
import {
  UserCalendarInput,
  EventInputExtended,
} from "@/types/interfaces/CalendarInterfaces";
import { useEvents } from "@/composables/useEvents";
import { useDate } from "@/composables/useDate";
import EventTable from "@/components/EventTable.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import { auth } from "@/firebase";
const date = useDate();
const isAdderOpened: Ref<boolean> = ref(false);
const refKey: Ref<number> = ref(0);
const events = useEvents();
const handlePickDate = (arg: DateClickArg) => {
  date.setDate(arg.date);
};
auth.currentUser?.getIdToken(true).then((data) => {
  fetch("http://localhost:8070/api/events", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${data}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      const tmp = data;
      tmp.forEach((el: any) => {
        el.startDate = el.startDate.replace(" ", "T");
        el.endDate = el.endDate.replace(" ", "T");
      });
      events.setEvents(tmp);
      calendarOptions.value.events = tmp;
    });
});
const calendarOptions: Ref<CalendarOptions> = ref({
  plugins: [dayGridPlugin, interactionPlugin],
  initialView: "dayGridMonth",
  dateClick: handlePickDate,
  events: [],
});

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

const handleOpenAdder = (flag: boolean) => {
  isAdderOpened.value = flag;
};
const handleAddEvent = () => {
  if (!calendarOptions.value) return;

  const dataInput: EventInputExtended = {
    title: input.value.title,
    startDate: `${input.value.date} ${input.value.start}`,
    endDate: `${input.value.date} ${input.value.end}`,
    participants: +input.value.attendees,
    description: input.value.description,
  };
  auth.currentUser?.getIdToken().then((data) => {
    fetch("http://localhost:8070/api/events", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${data}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dataInput),
    })
      .then((r) => r.json())
      .then((data) => console.log(data));
  });
  events.addEvent(dataInput);

  isAdderOpened.value = false;
};
const createReport = () => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch("http://localhost:8070/api/events/report", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${data}`,
      },
    })
      .then((r) => r.json())
      .then((data) => console.log(data));
  });
};
</script>

<style scoped>
section {
  width: 80%;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: start;
  justify-content: center;
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
.add-actions {
  display: flex;
  flex-direction: row;
  gap: 1em;
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
