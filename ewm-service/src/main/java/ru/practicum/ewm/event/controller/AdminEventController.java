package ru.practicum.ewm.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.enumerate.EventState;
import ru.practicum.ewm.event.service.EventService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }



    @GetMapping
    public List<EventFullDto> findAllSuitableEvents(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<EventState> states,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Find info about all events that match the conditions");
        return eventService.findAllSuitableEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventInfo(@PathVariable Long eventId,
                                        @RequestBody AdminUpdateEventDto adminUpdateEventDto) {
        log.info("Update info about event with id = " + eventId);
        return eventService.adminUpdateEventInfo(eventId, adminUpdateEventDto);
    }
}
