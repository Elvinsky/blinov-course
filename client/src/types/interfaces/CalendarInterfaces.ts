import { EventInput } from "@fullcalendar/core";
import { DateClickArg } from "@fullcalendar/interaction";
export interface EventInputExtended extends EventInput {
  participants: number;
  description: string;
}
export interface CalendarOptions {
  plugins: any[];
  initialView: string;
  dateClick: (arg: DateClickArg) => void;
  events: EventInputExtended[];
}
export interface UserCalendarInput {
  date: string;
  title: string;
  start: string;
  end: string;
  isFullDay: boolean;
  attendees: number;
  description: string;
}
export interface CalendarInput {
  title: string;
  start: string;
  end: string;
  attendees: number;
}
export interface TimeBoundaries {
  start: string;
  end: string;
}
