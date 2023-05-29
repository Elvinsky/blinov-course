<template>
  <section>
    <HeaderComponent />
    <div class="event-wrapper" v-if="!isAdmin">
      <form @submit.prevent>
        <input
          type="text"
          placeholder="email"
          id="title"
          v-model="email"
          :class="error ? 'error' : ''"
        />
        <input
          type="password"
          id="employees"
          placeholder="password"
          v-model="pass"
          :class="error ? 'error' : ''"
        />
        <div v-if="error">Incorrect creds!</div>
        <div class="actions">
          <div class="deny-btn custom-btn" @click="handleCancel">Cancel</div>
          <div class="accept-btn custom-btn" @click="handleVer">Verify</div>
        </div>
      </form>
    </div>
    <div class="main-block" v-if="isAdmin">
      <div class="fav">
        <h2>All events</h2>
        <div class="buttons">
          <div class="custom-btn" @click="handleAdminReport">
            Create Admin Report
          </div>
          <div class="custom-btn" @click="handleGetStats">Get Stats</div>
        </div>

        <table>
          <tr>
            <th>#</th>
            <th @click="handleSort('name')" class="sort">Название</th>
            <th @click="handleSort('attend')" class="sort">Участники</th>
            <th>Время</th>
          </tr>
          <tr v-for="(event, idx) in events" :key="idx">
            <td>{{ idx }}</td>
            <td>{{ event.title }}</td>
            <td>{{ event.participants }}</td>
            <td>{{ event.startDate }} - {{ event.endDate }}</td>
          </tr>
        </table>
      </div>
      <div class="users" v-if="statsTotal !== 0">
        <table>
          <tr>
            <th>Bceгo</th>
            <th>B среднем в месяц</th>
            <th>Коэфициент избранного</th>
          </tr>
          <tr>
            <td>{{ statsTotal }}</td>
            <td>{{ statsAvg }}</td>
            <td>{{ statsRatio }}</td>
          </tr>
        </table>
      </div>
      <div class="users">
        <h2>All users</h2>
        <table>
          <tr>
            <th>#</th>
            <th @click="handleSortUser('attend')" class="sort">Имя</th>
            <th @click="handleSortUser('name')" class="sort">Почта</th>
            <th>Действия</th>
          </tr>
          <tr v-for="(user, idx) in users" :key="idx">
            <td>{{ idx }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>
              <img
                src="@/assets/block.png"
                class="img-block"
                @click="handleBlock(user.id)"
              />
              <img
                src="@/assets/setting.png"
                class="img-block"
                @click="handleSetAdmin(user.id)"
              />
            </td>
          </tr>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import HeaderComponent from "@/components/HeaderComponent.vue";
import { useEvents } from "@/composables/useEvents";
import { useUser } from "@/composables/useUser";
import { auth } from "@/firebase";
import { Ref, ref } from "vue";
import { useRouter } from "vue-router";
// const stats: Ref<any> = ref({
//   total: 0,
//   eventsAvg: 0,
//   favRatio: 0,
// });
const statsTotal = ref<number>(0);
const statsAvg = ref<number>(0);
const statsRatio = ref<number>(0);
const error: Ref<boolean> = ref(false);
const events: Ref<any> = ref();
const users: Ref<any> = ref();
const user = useUser();
const myEvents = useEvents();
const email = ref<string>("");
const pass = ref<string>("");
const isAdmin = ref<boolean>(false);
const sorting = ref<string>("");
const sortingUser = ref<string>("");
const router = useRouter();
auth.currentUser?.getIdToken().then((data) => {
  fetch("http://localhost:8070/api/events/admin", {
    headers: {
      Authorization: `Bearer ${data}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      events.value = data;
    });
});
auth.currentUser?.getIdToken().then((data) => {
  fetch("http://localhost:8070/api/users", {
    headers: {
      Authorization: `Bearer ${data}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      users.value = data;
    });
});

const handleSort = (field: string) => {
  if (field === "name") {
    auth.currentUser?.getIdToken().then((data) => {
      fetch("http://localhost:8070/api/events/admin", {
        headers: {
          Authorization: `Bearer ${data}`,
        },
      })
        .then((r) => r.json())
        .then((data) => {
          if (sorting.value === "name") {
            events.value = data.sort((a: any, b: any) =>
              a.name.localeCompare(b.name)
            );
            sorting.value = "";
          } else {
            events.value = data.sort((a: any, b: any) =>
              b.name.localeCompare(a.name)
            );
            sorting.value = "name";
          }
        });
    });
  } else if (field === "attend") {
    auth.currentUser?.getIdToken().then((data) => {
      fetch("http://localhost:8070/api/events/admin", {
        headers: {
          Authorization: `Bearer ${data}`,
        },
      })
        .then((r) => r.json())
        .then((data) => {
          if (sorting.value === "attend") {
            events.value = data.sort(
              (a: any, b: any) => a.attendees - b.attendees
            );
            sorting.value = "";
          } else {
            events.value = data.sort(
              (a: any, b: any) => a.attendees + b.attendees
            );
            sorting.value = "attend";
          }
        });
    });
  }
};
const handleSortUser = (field: string) => {
  if (field === "name") {
    fetch("http://localhost:8070/api/users")
      .then((r) => r.json())
      .then((data) => {
        if (sortingUser.value === "email") {
          users.value = data.sort((a: any, b: any) =>
            a.email.localeCompare(b.email)
          );
          sortingUser.value = "";
        } else {
          users.value = data.sort((a: any, b: any) =>
            b.email.localeCompare(a.email)
          );
          sortingUser.value = "name";
        }
      });
  } else if (field === "attend") {
    fetch("http://localhost:8070/api/users")
      .then((r) => r.json())
      .then((data) => {
        if (sortingUser.value === "attend") {
          users.value = data.sort((a: any, b: any) =>
            a.name.localeCompare(b.name)
          );
          sortingUser.value = "";
        } else {
          users.value = data.sort((a: any, b: any) =>
            b.name.localeCompare(a.name)
          );
          sortingUser.value = "attend";
        }
      });
  }
};
const handleAdminReport = () => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch("http://localhost:8070/api/events/admin/report", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${data}`,
      },
    })
      .then((r) => r.json())
      .then((data) => {
        console.log(data);
      });
  });
};
const handleVer = () => {
  if (email.value === "admin@mail.com" && pass.value === "123456")
    isAdmin.value = true;
  else {
    error.value = true;
  }
};
const handleGetStats = () => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch("http://localhost:8070/api/events/admin/stats", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${data}`,
      },
    })
      .then((r) => r.json())
      .then((data) => {
        statsTotal.value = data.totalEvents;
        statsAvg.value = data.next30DaysAverageEventNumber;
        statsRatio.value = data.favouriteRatio;
      });
  });
};
const handleSetAdmin = (id: string) => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch(`http://localhost:8070/api/admin/makeAdmin/${id}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${data}` },
    });
  });
};
const handleBlock = (id: string) => {
  auth.currentUser?.getIdToken().then((data) => {
    fetch(`http://localhost:8070/api/admin/blockUser/${id}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${data}` },
    });
  });
};
const handleCancel = () => {
  router.push("/home");
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
.main-block {
  display: flex;
  flex-direction: row;
  align-items: start;
  justify-content: space-between;
  gap: 8em;
}
.sort {
  cursor: pointer;
}
.error {
  border: 1px solid red !important;
  background-color: rgba(255, 0, 0, 0.162);
}
.user-info {
  display: flex;
  flex-direction: column;
  gap: 0.3em;
  align-items: start;
  justify-content: start;
  padding: 1em;

  border: 1px solid rgba(0, 0, 0, 0.337);
  box-shadow: 2px 4px 1px 1px rgba(0, 0, 0, 0.133);
}
.buttons {
  display: flex;
  flex-direction: column;
  gap: 1em;
}
.custom-btn {
  background-color: rgba(0, 255, 0, 0.597);
  border-radius: 5px;
  padding: 0.5em;
  cursor: pointer;
  transition: all;
  transition-duration: 200ms;
}
.img-block {
  width: 20px;
}
.custom-btn:hover {
  transform: scale(1.1);
}
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
</style>
