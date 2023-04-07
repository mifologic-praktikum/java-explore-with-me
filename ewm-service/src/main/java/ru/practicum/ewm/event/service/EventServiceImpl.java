package ru.practicum.ewm.event.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.ewm.DataFormatter;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.client.StatisticClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.enumerate.EventState;
import ru.practicum.ewm.event.enumerate.EventStateAction;
import ru.practicum.ewm.event.location.Location;
import ru.practicum.ewm.event.location.LocationRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConditionException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.ParticipantRequestRepository;
import ru.practicum.ewm.request.ParticipationRequestDto;
import ru.practicum.ewm.request.ParticipationRequestMapper;
import ru.practicum.ewm.request.ParticipationRequestStatus;
import ru.practicum.ewm.request.model.EventRequestStatusUpd;
import ru.practicum.ewm.request.model.EventRequestStatusUpdResult;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.stats.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final DateTimeFormatter formatter = DataFormatter.getFormatter();

    private final ParticipantRequestRepository requestRepository;
    @Value("${spring.application.name}")
    private String appName;


    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            LocationRepository locationRepository,
                            ParticipantRequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<EventFullDto> findAllSuitableEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories, String rangeStart, String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of((from / size), size);
        if (rangeStart == null) {
            rangeStart = DataFormatter.fromDateToString(LocalDateTime.now());
        }
        if (rangeEnd == null) {
            rangeEnd = DataFormatter.fromDateToString(LocalDateTime.now().plusYears(50));
        }
        List<Event> events = eventRepository.findAllByAdmin(users, states, categories,
                DataFormatter.fromStringToDate(rangeStart), DataFormatter.fromStringToDate(rangeEnd), pageable);
        List<EventFullDto> foundedEvents = new ArrayList<>();
        for (Event event : events) {
            foundedEvents.add(EventMapper.toFullDto(event));
        }
        return foundedEvents;
    }

    @Override
    @Transactional
    public EventFullDto adminUpdateEventInfo(Long eventId, UpdateEventDto updateEventDto) {
        Event event = checkEventExisting(eventId);
        if (event.getEventDate().plusHours(1).isBefore(event.getCreatedOn())) {
            throw new ConflictException(
                    "The date of the event can't be earlier than plus hour after now");
        }
        if (updateEventDto.getEventDate() != null) {
            if (DataFormatter.fromStringToDate(updateEventDto.getEventDate()).isBefore(LocalDateTime.now())) {
                throw new ConflictException("Event date can't be in the past");
            }
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Can't update event in different status");
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event has been already published");
        }
        if (updateEventDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
            event.setState(EventState.PUBLISHED);
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            event.setState(EventState.CANCELED);
        }
        if (updateEventDto.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
            event.setState(EventState.CANCELED);
        }
        return updateEventInfo(event, updateEventDto);
    }

    @Override
    public List<EventShortDto> getSuitableEventsList(
            String text, List<Long> categories, Boolean paid,
            String rangeStart, String rangeEnd, Boolean onlyAvailable,
            String sort, int from, int size,
            StatClient statClient, HttpServletRequest request) {
        Pageable pageable;
        if (sort == null) {
            pageable = PageRequest.of((from / size), size);
        } else {
            pageable = PageRequest.of((from / size), size, Sort.by(sort));
        }
        if (rangeStart == null) {
            rangeStart = DataFormatter.fromDateToString(LocalDateTime.now());
        }
        if (rangeEnd == null) {
            rangeEnd = DataFormatter.fromDateToString(LocalDateTime.now().plusYears(50));
        }
        List<Event> events = eventRepository.findAllFilter(text, categories, paid,
                DataFormatter.fromStringToDate(rangeStart), DataFormatter.fromStringToDate(rangeEnd), pageable);
        List<Event> eventsWithViewsAndRequests = new ArrayList<>();
        StatisticClient.createHit(statClient,
                new EndpointHit(
                        0L,
                        appName,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        DataFormatter.fromDateToString(LocalDateTime.now()))
        );
        for (Event event : events) {
            String url = request.getRequestURI() + "/" + event.getId();
            StatisticClient.createHit(statClient,
                    new EndpointHit(
                            0L,
                            appName,
                            url,
                            request.getRemoteAddr(),
                            DataFormatter.fromDateToString(LocalDateTime.now()))
            );
            List<ViewStats> stats = StatisticClient.getStats(url, statClient);
            if (stats.size() != 0) {
                event.setViews(stats.get(0).getHits());
            } else {
                event.setViews(0L);
            }
            event.setConfirmedRequests(requestRepository.findAllByEventId(event.getId()).size());
            eventsWithViewsAndRequests.add(event);
        }
        return eventsWithViewsAndRequests.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventInfo(Long eventId, StatClient statClient, String url) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new BadRequestException("Only published events can be returned");
        }
        List<ViewStats> stats = StatisticClient.getStats(url, statClient);
        if (stats.size() != 0) {
            event.setViews(stats.get(0).getHits());
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsAddedByUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Pageable pageable = PageRequest.of((from / size), size);
        List<Event> userEvents = eventRepository.findAllByInitiator(user, pageable);
        List<EventShortDto> userEventsDto = new ArrayList<>();
        for (Event event : userEvents) {
            userEventsDto.add(EventMapper.toShortDto(event));
        }
        return userEventsDto;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id = " + newEventDto.getCategory() + " not found")
        );

        LocalDateTime eventDate;
        LocalDateTime conditionDate = LocalDateTime.now().plusHours(2);
        if (newEventDto.getEventDate() != null) {
            eventDate = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        } else {
            throw new ConditionException("Event date can't be blank.");
        }
        if (eventDate.isBefore(conditionDate)) {
            throw new ConflictException(String.format(
                    "The date of the event can't be earlier than in %s",
                    conditionDate.format(formatter)));
        }
        Location location = locationRepository.findByLatAndLon(
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon()).orElse(
                locationRepository.save(new Location(0L,
                        newEventDto.getLocation().getLat(),
                        newEventDto.getLocation().getLon()))
        );
        Event event = EventMapper.toEventFromNewDto(newEventDto);
        event.setCategory(category);
        event.setLocation(location);
        event.setEventDate(eventDate);
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto getAllInfoAboutEventAddedByUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found"));
        if (!event.getInitiator().equals(user)) {
            throw new ConditionException("Event added by different user");
        }
        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto updateEventAddedByCurrentUser(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Event event = checkEventExisting(eventId);
        if (event.getEventDate().plusHours(1).isBefore(event.getCreatedOn())) {
            throw new ConflictException(
                    "The date of the event can't be earlier than plus hour after now");
        }
        if (updateEventDto.getEventDate() != null) {
            if (DataFormatter.fromStringToDate(updateEventDto.getEventDate()).isBefore(LocalDateTime.now())) {
                throw new ConflictException("Event date can't be in the past");
            }
        }
        if (!event.getState().equals(EventState.PENDING) && !event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Only event in pending or cancelled can be change");
        }
        if (updateEventDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
            event.setState(EventState.CANCELED);
        }
        if (updateEventDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
            event.setState(EventState.PENDING);
        }
        return updateEventInfo(event, updateEventDto);
    }

    @Override
    public List<ParticipationRequestDto> getInfoAboutRequestsInUserEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found")
        );
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("User is not event initiator");
        }
        List<ParticipationRequest> participationRequestList = requestRepository.findAllByEventId(eventId);

        return participationRequestList.stream().map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdResult updateRequestStatusForUserEvent(Long userId, Long eventId, EventRequestStatusUpd eventRequestStatus) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found"));

        int participantLimit = event.getParticipantLimit();
        int currentRequests = event.getConfirmedRequests();
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(eventRequestStatus.getRequestIds());
        if (participantLimit == currentRequests) {
            throw new ConflictException("Participant limit has been reached");
        }
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new ConflictException("Can accept only requests in pending status");
            }
            if (participantLimit > currentRequests) {
                if (eventRequestStatus.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                    request.setStatus(ParticipationRequestStatus.CONFIRMED);
                    currentRequests++;
                    event.setConfirmedRequests(currentRequests);
                    eventRepository.save(event);
                    requestRepository.save(request);
                    confirmedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(request));
                } else {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    requestRepository.save(request);
                    rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(request));
                }


            } else {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                requestRepository.save(request);
                rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(request));
            }

        }
        return new EventRequestStatusUpdResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<ParticipationRequestDto> getInfoAboutUserRequestsInOtherEvents(Long userId) {

        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        List<ParticipationRequest> participationRequest = requestRepository.findAllByRequesterId(userId);
        return participationRequest.stream().map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createUserRequestForParticipationInEvent(Long userId, Long eventId) {

        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ConflictException("Request for participation from this user already exists.");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found")
        );
        int participantLimit = event.getParticipantLimit();
        int currentRequests = event.getConfirmedRequests();
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("This user is event initiator");
        }
        if (participantLimit == currentRequests) {
            throw new ConflictException("Requests limit has been reached");
        }
        if (event.getState() == EventState.PENDING) {
            throw new ConflictException("Requests available only for published events");
        }
        ParticipationRequest request = new ParticipationRequest(
                0L,
                LocalDateTime.now(),
                event,
                user,
                ParticipationRequestStatus.PENDING);
        if (!event.getRequestModeration()) {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
            event.setConfirmedRequests(currentRequests + 1);
            eventRepository.save(event);
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest participationRequest = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new NotFoundException("Participation request with id = " + requestId + " not found."));
        participationRequest.setStatus(ParticipationRequestStatus.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }

    private EventFullDto updateEventInfo(Event event, UpdateEventDto updateEventDto) {

        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }
        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventDto.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category not found")));
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getParticipantLimit() != 0) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getLocation() != null) {
            Location location = locationRepository.findById(event.getLocation().getId()).orElseThrow(
                    () -> new NotFoundException("Location not found")
            );
            location.setLat(updateEventDto.getLocation().getLat());
            location.setLon(updateEventDto.getLocation().getLon());
            event.setLocation(location);
        }
        if (updateEventDto.getEventDate() != null) {
            event.setEventDate(DataFormatter.fromStringToDate(updateEventDto.getEventDate()));
        }
        eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }


    private Event checkEventExisting(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found"));
    }
}
