import { EventInputExtended } from "@/types/interfaces/CalendarInterfaces";
import { ref } from "vue";
import { useDate } from "./useDate";
const date = useDate();
const myEvents = ref<EventInputExtended[]>([]);
export const useEvents = () => {
  const setEvents = (data: EventInputExtended[]) => {
    myEvents.value = data;
  };
  const addEvent = (data: EventInputExtended) => {
    myEvents.value.push(data);
  };
  const deleteEvent = (data: string) => {
    myEvents.value = myEvents.value.filter(
      (el: EventInputExtended) => el.title !== data
    );
  };
  const setFlags = () => {
    myEvents.value.map((el: EventInputExtended) => {
      el.dateFlag =
        Date.parse(el.start as string) >=
          new Date(date.date.value.setHours(0, 0, 0, 0)).getTime() &&
        Date.parse(el.end as string) <=
          new Date(date.date.value.setHours(23, 59, 59, 999)).getTime();
    });
  };
  return {
    myEvents,
    setEvents,
    addEvent,
    deleteEvent,
    setFlags,
  };
};
