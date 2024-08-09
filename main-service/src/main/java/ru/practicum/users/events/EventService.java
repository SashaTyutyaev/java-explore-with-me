package ru.practicum.users.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.categories.CategoryRepository;
import ru.practicum.admin.categories.model.Category;
import ru.practicum.admin.users.UserRepository;
import ru.practicum.admin.users.model.User;
import ru.practicum.exceptions.IncorrectParameterException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.events.model.Event;
import ru.practicum.users.events.model.State;
import ru.practicum.users.events.model.dto.*;
import ru.practicum.users.requests.RequestRepository;
import ru.practicum.users.requests.model.Request;
import ru.practicum.users.requests.model.RequestStatus;
import ru.practicum.users.requests.model.dto.ParticipationRequestDto;
import ru.practicum.users.requests.model.dto.RequestMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    public Event createEvent(NewEventDto newEventDto, Long userId) {
        validateEventDate(newEventDto.getEventDate());
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        Event event = EventMapper.toEvent(newEventDto);
        User user = getUserById(userId);
        event.setInitiator(user);
        Category category = getCategoryById(newEventDto.getCategory());
        event.setCategory(category);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        log.info("Successfully created new event: {}", event);
        return eventRepository.save(event);
    }

    public List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        getUserById(userId);
        Pageable pageable = validatePageable(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        log.info("Successfully retrieved event by userId: {}", userId);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        getUserById(userId);
        getEventById(eventId);
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            log.error("Event not found for user id {} and event id {}", userId, eventId);
            throw new NotFoundException("Event not found");
        }
        log.info("Successfully retrieved event: {}", event);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updatedEvent, Long userId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            if (event.getState().equals(State.PUBLISHED)) {
                log.error("Event {} already published", eventId);
                throw new DataIntegrityViolationException("Event already published");
            }
            if (updatedEvent.getAnnotation() != null) {
                event.setAnnotation(updatedEvent.getAnnotation());
            }
            if (updatedEvent.getCategory() != null) {
                event.setCategory(getCategoryById(updatedEvent.getCategory()));
            }
            if (updatedEvent.getDescription() != null) {
                event.setDescription(updatedEvent.getDescription());
            }
            if (updatedEvent.getEventDate() != null) {
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                    log.error("Event starting soon, too late do changes");
                    throw new IncorrectParameterException("Event starting soon, too late do changes");
                }
                if (event.getEventDate().isBefore(LocalDateTime.now())) {
                    log.error("Event is finished");
                    throw new IncorrectParameterException("Event is finished");
                }
                LocalDateTime updateEventDate = LocalDateTime.parse(updatedEvent.getEventDate(), FORMATTER);
                if (updateEventDate.isBefore(LocalDateTime.now()) ||
                        updateEventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                    log.error("Cant set event date which is before now or before now + 2 hours");
                    throw new IncorrectParameterException("Cant set event date which is before now + 2 hours");
                }
                event.setEventDate(updateEventDate);
            }
            if (updatedEvent.getLocation() != null) {
                event.setLat(updatedEvent.getLocation().getLat());
                event.setLon(updatedEvent.getLocation().getLon());
            }
            if (updatedEvent.getPaid() != null) {
                event.setPaid(updatedEvent.getPaid());
            }
            if (updatedEvent.getParticipantLimit() != null) {
                event.setParticipantLimit(updatedEvent.getParticipantLimit());
            }
            if (updatedEvent.getRequestModeration() != null) {
                event.setRequestModeration(updatedEvent.getRequestModeration());
            }
            if (updatedEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
            if (updatedEvent.getTitle() != null) {
                event.setTitle(updatedEvent.getTitle());
            }
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } else {
            log.error("User is not owner of event {}", eventId);
            throw new IncorrectParameterException("User is not owner of event " + eventId);
        }
    }

    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        List<Request> requests = requestRepository.findAllByEvent(event);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest updateRequest) {
        getUserById(userId);
        Event event = getEventById(eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        for (Long id : updateRequest.getRequestIds()) {
            Request request = getRequestById(id);
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                log.error("Request {} is not pending", id);
                throw new IncorrectParameterException("Request " + id + " is not pending");
            }
            if (updateRequest.getStatus().equals("CONFIRMED")) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    log.error("The participant limit is reached");
                    request.setStatus(RequestStatus.REJECTED);
                    throw new IncorrectParameterException("The participant limit is reached");
                }
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                result.getConfirmedRequests().add(RequestMapper.toParticipationRequestDto(request));
                requestRepository.save(request);
                log.info("Successfully confirmed request {}", id);
            } else {
                request.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(RequestMapper.toParticipationRequestDto(request));
                requestRepository.save(request);
                log.info("Successfully rejected request {}", id);
            }
        }
        eventRepository.save(event);
        return result;
    }

    private void validateEventDate(String eventDate) {
        LocalDateTime date = LocalDateTime.parse(eventDate, FORMATTER);
        if (date.isBefore(LocalDateTime.now())) {
            log.error("Date must be in future");
            throw new DataIntegrityViolationException("Date must be in future");
        }

        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("Date must be in future more than 2 hours");
            throw new DataIntegrityViolationException("Date must be in future more than 2 hours");
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });
    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(Long.valueOf(categoryId)).orElseThrow(() -> {
            log.error("Category with id {} not found", categoryId);
            return new NotFoundException("Category with id " + categoryId + " not found");
        });
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private PageRequest validatePageable(Integer from, Integer size) {
        if (from == null || from < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }
        if (size == null || size < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }

        return PageRequest.of(from / size, size);
    }

    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            log.error("Request with id {} not found", requestId);
            return new NotFoundException("Request with id " + requestId + " not found");
        });
    }
}
