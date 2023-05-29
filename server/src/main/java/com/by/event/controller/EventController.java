package com.by.event.controller;

import com.by.event.model.Stats;
import com.by.event.request.CreateEventRequest;
import com.by.event.request.UpdateEventRequest;
import com.by.event.response.EventResponse;
import com.by.event.service.EventService;
import com.by.event.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/events")
@Slf4j
@CrossOrigin(origins = {"*"})
public record EventController(EventService eventService, ExcelService excelService) {

    @GetMapping()
    public ResponseEntity<List<EventResponse>> getAllEvents(Principal principal) {
        return new ResponseEntity<>(eventService.getAllEvents(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<EventResponse>> getAllEventsAdmin() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/makeFavourite/{id}")
    public ResponseEntity<EventResponse> makeFavourite(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(eventService.makeFavourite(id), HttpStatus.OK);
    }

    @GetMapping("/unmakeFavourite/{id}")
    public ResponseEntity<EventResponse> unmakeFavourite(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(eventService.unmakeFavourite(id), HttpStatus.OK);
    }

    @PutMapping("/favourites")
    public ResponseEntity<List<EventResponse>> getFavourite(Principal principal) {
        return new ResponseEntity<>(eventService.getFavourite(principal.getName()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(eventService.getEvent(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest createEventRequest, Principal principal) {
        log.info("New event is created: {}", createEventRequest);
        return new ResponseEntity<>(eventService.createEvent(createEventRequest, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public EventResponse updateEvent(@PathVariable("id") Integer id,
                                     @RequestBody UpdateEventRequest updateEventRequest,
                                     Principal principal) {
        return eventService.updateEvent(id, updateEventRequest, principal.getName());
    }

    @DeleteMapping(path = "/{id}")
    public void deleteEvent(@PathVariable("id") Integer id) {
        eventService.deleteEvent(id);
    }

    @GetMapping(path = "/report")
    public ResponseEntity<byte[]> getReport(Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        List<EventResponse> events = eventService.getAllEvents(principal.getName());
        byte[] report = excelService.createReport(events);
        try (OutputStream outputStream = new FileOutputStream("./report.xlsx")) {
            outputStream.write(report);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(report, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/report")
    public ResponseEntity<byte[]> getAdminReport(Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        List<EventResponse> events = eventService.getAllEvents();
        byte[] report = excelService.createReport(events);
        try (OutputStream outputStream = new FileOutputStream("./admin_report.xlsx")) {
            outputStream.write(report);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(report, headers, HttpStatus.OK);
    }

    @GetMapping(path = "admin/stats")
    public Stats getStats() {
        return eventService.createStats();
    }
}
