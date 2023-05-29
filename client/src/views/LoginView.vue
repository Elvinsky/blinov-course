<template>
  <section>
    <h2>Login</h2>
    <form :class="error ? 'error' : ''" @submit.prevent>
      <input
        id="email"
        v-model="input.email"
        placeholder="e-mail"
        name="email"
        type="text"
      />
      <input
        id="password"
        v-model="input.password"
        placeholder="password"
        name="password"
        type="password"
      />
      <div v-if="error">Error! Incorrect creds</div>
      <div class="actions">
        <button @click="submit">Login</button>
        <RouterLink to="/registration" class="link">
          Noe yet have an account?
        </RouterLink>
      </div>
    </form>
  </section>
</template>

<script setup lang="ts">
import { useUser } from "@/composables/useUser";
import { auth } from "@/firebase";
import router from "@/router";
import { UserDataCompos } from "@/types/interfaces/composInterfaces";
import { UserInput } from "@/types/interfaces/userInterfaces";
import { signInWithEmailAndPassword } from "firebase/auth";
import { Ref, reactive, ref } from "vue";

const input: UserInput = reactive({
  email: "",
  password: "",
});
const user: UserDataCompos = useUser();
const error: Ref<boolean> = ref(false);

const submit = (): void => {
  signInWithEmailAndPassword(auth, input.email, input.password)
    .then((creds) => {
      creds.user.getIdToken().then((data) => {
        fetch("http://localhost:8070/api/users/isBlocked", {
          method: "GET",
          headers: {
            Authorization: `Bearer ${data}`,
          },
        })
          .then((r) => r.json())
          .then((data) => {
            if (data) {
              error.value = true;
              input.email = "";
              input.password = "";
            } else {
              user.setUser(creds.user);
              creds.user.getIdToken().then((data) => {
                fetch("http://localhost:8070/api/users/login", {
                  method: "POST",
                  headers: {
                    Authorization: `Bearer ${data}`,
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    email: input.email,
                    username: auth.currentUser?.displayName,
                  }),
                })
                  .then(() => {
                    router.push("/home");
                  })
                  .catch((err) => {
                    console.error(err);
                  });
              });
            }
          });
      });
    })
    .catch((error) => {
      error.value = true;
      input.email = "";
      input.password = "";
    });
};
</script>

<style scoped>
section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 3em;
  gap: 1em;
}
form {
  display: inherit;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.6em;
}
form.error input {
  border-color: red;
  border-width: 1px;
  border-style: solid;
}
form button {
  align-self: flex-start;
  justify-self: start;
  background-color: rgba(18, 219, 18, 0.4);
  padding: 0.4em 0.6em 0.4em 0.6em;
  border-radius: 5px;
  font-size: 0.9em;
}
input {
  padding: 0.4em;
  width: 200px;
}
.actions {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1em;
}
.link {
  font-size: 0.7em;
}
</style>
