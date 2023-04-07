package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.enumerate.EventState;
import ru.practicum.ewm.request.ParticipationRequestDto;
import ru.practicum.ewm.request.model.EventRequestStatusUpd;
import ru.practicum.ewm.request.model.EventRequestStatusUpdResult;
import ru.practicum.stats.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventFullDto> findAllSuitableEventsByAdmin(List<Long> users,
                                                    List<EventState> states,
                                                    List<Long> categories,
                                                    String rangeStart,
                                                    String rangeEnd,
                                                    int from,
                                                    int size);

    EventFullDto adminUpdateEventInfo(Long eventId, AdminUpdateEventDto adminUpdateEventDto);

    List<EventShortDto> getSuitableEventsList(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size, StatClient statClient, HttpServletRequest request);

    EventFullDto getEventInfo(Long eventId, StatClient statClient, String url);

    public List<EventShortDto> getAllEventsAddedByUser(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getAllInfoAboutEventAddedByUser(Long userId, Long eventId);

    EventFullDto updateEventAddedByCurrentUser(Long userId, Long eventId, UpdateUserEventDto eventDto);

    List<ParticipationRequestDto> getInfoAboutRequestsInUserEvent(Long userId, Long eventId);

    EventRequestStatusUpdResult updateRequestStatusForUserEvent(Long userId, Long eventId, EventRequestStatusUpd eventRequestStatusDto);

    List<ParticipationRequestDto> getInfoAboutUserRequestsInOtherEvents(Long userId);

    ParticipationRequestDto createUserRequestForParticipationInEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
