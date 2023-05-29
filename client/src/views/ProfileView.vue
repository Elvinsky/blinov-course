<template>
  <section>
    <HeaderComponent />
    <div class="main-block">
      <div class="user-info">
        <h2>{{ user.user.value?.displayName }}</h2>
        <h3>{{ user.user.value?.email }}</h3>
      </div>
      <div class="custom-btn" @click="handleDeleteAcc">
        ANOTHER USECASE (DELETE ACC)
      </div>
      <div class="fav">
        <h2>Favourite events</h2>
        <table>
          <tr>
            <th>Название</th>
            <th>Участники</th>
            <th>Время</th>
          </tr>
          <tr v-for="(event, idx) in events" :key="idx">
            <td>{{ event.title }}</td>
            <td>{{ event.participants }}</td>
            <td>{{ event.startDate }} - {{ event.endDate }}</td>
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
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const events: Ref<any> = ref();
const user = useUser();
const myEvents = useEvents();
auth.currentUser?.getIdToken().then((data) => {
  fetch("http://localhost:8070/api/events/favourites", {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${data}`,
    },
  })
    .then((r) => r.json())
    .then((data) => {
      events.value = data;
    });
});
const handleDeleteAcc = () => {
  auth.currentUser?.getIdToken().then((token) => {
    fetch("<ZAPROS>", {
      method: "METHOD",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((result) => {
        console.log(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
};
</script>

<style scoped>
section {
  width: 80%;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
}
.main-block {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  gap: 5em;
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
</style>
