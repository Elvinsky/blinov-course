package com.by.event.service;

import com.by.event.model.Event;
import com.by.event.model.Stats;
import com.by.event.repository.EventRepository;
import com.by.event.request.CreateEventRequest;
import com.by.event.request.UpdateEventRequest;
import com.by.event.response.EventResponse;
import com.by.user.dto.response.UserResponse;
import com.by.user.model.FriendRequest;
import com.by.user.model.User;
import com.by.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public record EventService(EventRepository eventRepository,
                           UserRepository userRepository) {

    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public EventResponse
    createEvent(CreateEventRequest createEventRequest, String ownerId) {
        Event event = Event.builder()
                .title(createEventRequest.title())
                .startDate(formatter.parse(createEventRequest.startDate()))
                .endDate(formatter.parse(createEventRequest.endDate()))
                .description(createEventRequest.description())
                .participants(createEventRequest.participants())
                .owner(userRepository.findById(ownerId).orElseThrow())
                .ownerId(userRepository.findById(ownerId).orElseThrow().getId())
                .isFavourite(false)
                .build();
        return eventToEventResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAllEvents(String ownerId) {
        return eventRepository.findAllByOwnerId(ownerId).stream().map(this::eventToEventResponse).toList();
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(this::eventToEventResponse).toList();
    }

    public List<EventResponse> getFavourite(String ownerId) {
        List<Event> favourite = eventRepository.findAllByOwnerId(ownerId)
                .stream().filter(Event::isFavourite).toList();
        return favourite.stream().map(this::eventToEventResponse).toList();
    }

    public EventResponse makeFavourite(Integer id) {
        Event updatedEvent = eventRepository.findById(id).orElseThrow();
        updatedEvent.setFavourite(true);
        return eventToEventResponse(eventRepository.save(updatedEvent));
    }

    public EventResponse unmakeFavourite(Integer id) {
        Event updatedEvent = eventRepository.findById(id).orElseThrow();
        updatedEvent.setFavourite(false);
        return eventToEventResponse(eventRepository.save(updatedEvent));
    }

    public EventResponse getEvent(Integer id) {
        return eventToEventResponse(eventRepository.findById(id).orElse(null));
    }

    @SneakyThrows
    public EventResponse updateEvent(Integer id, UpdateEventRequest updateEventRequest, String ownerId) {
        Event updatedEvent = Event.builder()
                .id(id)
                .title(updateEventRequest.title())
                .startDate(formatter.parse(updateEventRequest.startDate()))
                .endDate(formatter.parse(updateEventRequest.endDate()))
                .description(updateEventRequest.description())
                .participants(updateEventRequest.participants())
                .owner(userRepository.findById(ownerId).orElseThrow())
                .ownerId(userRepository.findById(ownerId).orElseThrow().getId())
                .isFavourite(updateEventRequest.isFavourite())
                .build();
        eventRepository.save(updatedEvent);
        return eventToEventResponse(updatedEvent);
    }

    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    public Stats createStats(){
        return Stats.builder()
                .totalEvents(getAllEvents().size())
                .next30DaysAverageEventNumber((double)getAllEvents().size()/30)
                .favouriteRatio((double)getAllEvents().stream()
                        .filter(EventResponse::isFavourite).toList().size()/(double)getAllEvents().size())
                .build();
    }

    public EventResponse eventToEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(formatter.format(event.getStartDate()))
                .endDate(formatter.format(event.getEndDate()))
                .ownerId(event.getOwnerId())
                .isFavourite(event.isFavourite())
                .build();
    }

}
