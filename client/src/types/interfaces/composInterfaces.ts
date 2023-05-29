import { Ref } from "vue";
import { User } from "firebase/auth";

export interface UserDataCompos {
  user: Ref<User | null>;
  setUser: (userData: User | null) => void;
}
