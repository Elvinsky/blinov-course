<template>
  <section>
    <HeaderComponent />
    <div class="main">
      <div class="add-friend">
        <button :disabled="input.length === 0" @click="addFriend">
          Add friend
        </button>
        <input type="text" v-model="input" />
      </div>

      <div class="friend-list">
        <div class="friend-item" v-for="(friend, idx) in friends" :key="idx">
          {{ friend }}
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import HeaderComponent from "@/components/HeaderComponent.vue";
import { ref } from "vue";
const friends = ref<any>();
const input = ref<string>("");
fetch(
  `http://localhost:5000/userFriends?firstUserId=${
    JSON.parse(localStorage.getItem("user")!).email
  }`
)
  .then((r) => r.json())
  .then((data) => {
    const emails: any[] = [];
    data.map((el: any) => emails.push(el.secondUserId));
    friends.value = emails;
  });
const addFriend = () => {
  fetch("http://localhost:5000/userFriends", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      firstUserId: JSON.parse(localStorage.getItem("user")!).email,
      secondUserId: input.value,
    }),
  }).then(() => {
    fetch("http://localhost:5000/userFriends", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        firstUserId: input.value,
        secondUserId: JSON.parse(localStorage.getItem("user")!).email,
      }),
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
  justify-content: center;
}
.main {
  display: flex;
  flex-direction: column;
  gap: 2em;
}
.main button {
  background-color: rgba(0, 255, 255, 0.273);
  padding: 0.4em;
}
.add-friend {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: center;
  justify-content: center;
}
.friend-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.4em;
  background-color: rgba(180, 180, 180, 0.39);
}
</style>
