package ru.practicum.ewm.event;

import ru.practicum.ewm.DataFormatter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;

import java.time.LocalDateTime;

public class EventMapper {

    public static EventFullDto toFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventFullDto.Category(event.getCategory().getId(), event.getCategory().getName()),
                event.getPaid(),
                DataFormatter.fromDateToString(event.getEventDate()),
                new EventFullDto.User(event.getInitiator().getId(), event.getInitiator().getName()),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState(),
                DataFormatter.fromDateToString(event.getCreatedOn()),
                new EventFullDto.Location(event.getLocation().getLat(), event.getLocation().getLon()),
                event.getRequestModeration(),
                event.getConfirmedRequests(),
                event.getPublishedOn() != null ?
                        DataFormatter.fromDateToString(event.getPublishedOn()) : null,
                event.getViews()
        );
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new EventShortDto.Category(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(DataFormatter.fromDateToString(event.getEventDate()))
                .paid(event.getPaid())
                .initiator(new EventShortDto.User(event.getInitiator().getId(), event.getInitiator().getName()))
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public static Event toEventFromNewDto(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(DataFormatter.fromStringToDate(newEventDto.getEventDate()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .publishedOn(LocalDateTime.now())
                .build();
    }
}
