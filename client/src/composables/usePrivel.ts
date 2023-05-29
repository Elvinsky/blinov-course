import { ref } from "vue";
const privel = ref<string>("");

export const usePrivel = () => {
  const setPrivel = (data: string) => {
    privel.value = data;
  };
  return { privel, setPrivel };
};
