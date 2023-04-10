package ru.practicum.ewm.event.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.ewm.DataFormatter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.stats.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
@ComponentScan
public class EventPublicController {

    private final EventService eventService;
    private final StatClient statClient;
    @Value("${spring.application.name}")
    private String appName;

    public EventPublicController(EventService eventService, StatClient statClient) {
        this.eventService = eventService;
        this.statClient = statClient;
    }

    @GetMapping
    public List<EventShortDto> getSuitableEventsList(@RequestParam(required = false) String text,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) Boolean onlyAvailable,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false, defaultValue = "0") int from,
                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                     HttpServletRequest request) {
        log.info("Get info events list that matches the conditions");

       return eventService.getSuitableEventsList(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, statClient, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventInfo(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Get info about event with id = " + eventId);
        statClient.createHit(
                new EndpointHit(
                        0L,
                        appName,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        DataFormatter.fromDateToString(LocalDateTime.now()))
        );
        log.info("Create hit for url=" + request.getRequestURI());
        return eventService.getEventInfo(eventId, statClient, request.getRequestURI());
    }
}
