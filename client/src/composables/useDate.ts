import { Ref, ref } from "vue";

const date: Ref<Date> = ref(new Date());

export const useDate = () => {
  const setDate = (dateVal: Date) => {
    date.value = dateVal;
  };
  return { date, setDate };
};
