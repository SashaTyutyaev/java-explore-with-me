package ru.practicum.users.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.users.UserRepository;
import ru.practicum.admin.users.model.User;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.events.EventRepository;
import ru.practicum.users.events.model.Event;
import ru.practicum.users.events.model.State;
import ru.practicum.users.requests.model.Request;
import ru.practicum.users.requests.model.RequestStatus;
import ru.practicum.users.requests.model.dto.ParticipationRequestDto;
import ru.practicum.users.requests.model.dto.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            log.error("Initiator cant sent request to his own event");
            throw new DataIntegrityViolationException("Initiator cant sent request to his own event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Event is not published");
            throw new DataIntegrityViolationException("Event is not published");
        }
        if (event.getConfirmedRequests() != null) {
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                log.error("Participant limit exceeded");
                throw new DataIntegrityViolationException("Participant limit exceeded");
            }
        }
        if (requestRepository.findByRequesterAndEvent(userId, eventId).isPresent()) {
            log.error("Request already exists");
            throw new DataIntegrityViolationException("Request already exists");
        }
        Request request;
        if (!event.getRequestModeration()) {
            request = Request.builder()
                    .status(RequestStatus.CONFIRMED)
                    .created(LocalDateTime.now())
                    .event(event)
                    .user(user)
                    .build();
            requestRepository.save(request);
            if (event.getConfirmedRequests() == null) {
                event.setConfirmedRequests(1);
                eventRepository.save(event);
            } else {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            }
        } else {
            request = Request.builder()
                    .status(RequestStatus.PENDING)
                    .created(LocalDateTime.now())
                    .event(event)
                    .user(user)
                    .build();
            requestRepository.save(request);
        }
        log.info("Request created successful");
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        getUserById(userId);
        Request request = getRequestById(requestId);
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = getEventById(request.getEvent().getId());
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        requestRepository.delete(request);
        log.info("Request cancelled successful");
        return RequestMapper.toParticipationRequestDto(request);
    }

    public List<ParticipationRequestDto> getRequests(Long userId) {
        User user = getUserById(userId);
        List<Request> requests = requestRepository.findAllByUser(user);
        log.info("Get requests from user {}", userId);
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });
    }

    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            log.error("Request with id {} not found", requestId);
            return new NotFoundException("Request with id " + requestId + " not found");
        });
    }
}
