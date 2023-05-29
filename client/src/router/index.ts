import {
  createRouter,
  createWebHistory,
  NavigationGuardNext,
  RouteRecordRaw,
} from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import ProfileViewVue from "@/views/ProfileView.vue";
import FriendViewVue from "@/views/FriendView.vue";
import AdminView from "@/views/AdminView.vue";
const routes: Array<RouteRecordRaw> = [
  {
    path: "/home",
    name: "home",
    component: HomeView,
    beforeEnter: function (to, from, next: NavigationGuardNext): void {
      const userStringData: string | null = localStorage.getItem("user");
      if (userStringData === null) next("/login");
      next();
    },
  },
  {
    path: "/profile",
    name: "profile",
    component: ProfileViewVue,
    beforeEnter: function (to, from, next: NavigationGuardNext): void {
      const userStringData: string | null = localStorage.getItem("user");
      if (userStringData === null) next("/login");
      next();
    },
  },
  {
    path: "/friends",
    name: "friends",
    component: FriendViewVue,
    beforeEnter: function (to, from, next: NavigationGuardNext): void {
      const userStringData: string | null = localStorage.getItem("user");
      if (userStringData === null) next("/login");
      next();
    },
  },
  {
    path: "/admin",
    name: "admin",
    component: AdminView,
    beforeEnter: function (to, from, next: NavigationGuardNext): void {
      const userStringData: string | null = localStorage.getItem("user");
      if (userStringData === null) next("/login");
      next();
    },
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/registration",
    name: "registration",
    alias: ["/register", "/signup"],
    component: RegisterView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
