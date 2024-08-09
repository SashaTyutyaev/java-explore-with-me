package ru.practicum.admin.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.categories.CategoryRepository;
import ru.practicum.admin.categories.model.Category;
import ru.practicum.admin.events.model.UpdateEventAdminRequest;
import ru.practicum.exceptions.IncorrectParameterException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.events.EventRepository;
import ru.practicum.users.events.model.Event;
import ru.practicum.users.events.model.State;
import ru.practicum.users.events.model.dto.EventFullDto;
import ru.practicum.users.events.model.dto.EventMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = getEventById(eventId);
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getEventDate() != null) {
            if (LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER).isBefore(LocalDateTime.now())) {
                log.error("Event date is before current date");
                throw new DataIntegrityViolationException("Event date is before current date");
            }
            event.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER));
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getLocation() != null) {
            event.setLat(updateEvent.getLocation().getLat());
            event.setLon(updateEvent.getLocation().getLon());
        }
        if (updateEvent.getCategory() != null) {
            Category category = getCategoryById(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)
                    && event.getState().equals(State.PUBLISHED)) {
                log.error("Event is already published");
                throw new DataIntegrityViolationException("Event is already published");
            }
            if (updateEvent.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)
                    && event.getState().equals(State.CANCELED)) {
                log.error("Event is canceled");
                throw new DataIntegrityViolationException("Event is canceled");
            }
            if (updateEvent.getStateAction().equals(StateActionAdmin.REJECT_EVENT)
                    && event.getState().equals(State.PUBLISHED)) {
                log.error("Event is already published");
                throw new DataIntegrityViolationException("Event is already published");
            }
            if (updateEvent.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        }
        eventRepository.save(event);
        log.info("Event updated by admin successful");
        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getEvents(List<Integer> initiatorIds, List<String> states,
                                        List<Integer> categories, String rangeStart, String rangeEnd,
                                        Integer from, Integer size) {
        Pageable pageable = validatePageable(from, size);
        LocalDateTime start = LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, FORMATTER);
        List<Event> events = eventRepository.findAllByInitiatorIdAndStateAndCategories(initiatorIds, states,
                categories, start, end, pageable);
        log.info("Event found: {}", events.size());
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(Long.valueOf(categoryId)).orElseThrow(() -> {
            log.error("Category with id {} not found", categoryId);
            return new NotFoundException("Category with id " + categoryId + " not found");
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
}
