package ru.practicum.ewm.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.ParticipationRequestDto;
import ru.practicum.ewm.request.model.EventRequestStatusUpd;
import ru.practicum.ewm.request.model.EventRequestStatusUpdResult;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class EventPrivateController {

    EventService eventService;

    public EventPrivateController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllEventsAddedByUser(@PathVariable Long userId,
                                                       @RequestParam(required = false, name = "from", defaultValue = "0") int from,
                                                       @RequestParam(required = false,
                                                               name = "size", defaultValue = "10") int size) {
        log.info("Get info about events added by user with id = " + userId);
        return eventService.getAllEventsAddedByUser(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("User with id = " + userId + " create an event with title " + newEventDto.getTitle());
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getAllInfoAboutEventAddedByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Get info about event with id = " + eventId + " added by user with id = " + userId);
        return eventService.getAllInfoAboutEventAddedByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventAddedByCurrentUser(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody UpdateUserEventDto eventDto) {
        log.info("Update info about event with id = " + eventId + " added by user with id = " + userId);
        return eventService.updateEventAddedByCurrentUser(userId, eventId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getInfoAboutRequestsInUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Get info about requests for participation in current user event");
        return eventService.getInfoAboutRequestsInUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdResult updateRequestStatusForUserEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                                       @RequestBody EventRequestStatusUpd eventRequestStatusDto) {
        log.info("Update status for requests for participation in current user event");
        return eventService.updateRequestStatusForUserEvent(userId, eventId, eventRequestStatusDto);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getInfoAboutUserRequestsInOtherEvents(@PathVariable Long userId) {
        log.info("Get info about current user requests for participation in other events");
        return eventService.getInfoAboutUserRequestsInOtherEvents(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createUserRequestForParticipationInEvent(@PathVariable Long userId,
                                                                            @Positive @RequestParam(name = "eventId") Long eventId) {
        log.info("Create user request for participation in event");

        return eventService.createUserRequestForParticipationInEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Cancel request for participation in event");
        return eventService.cancelParticipationRequest(userId, requestId);
    }
}
