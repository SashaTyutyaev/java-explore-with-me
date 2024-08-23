package ru.practicum.admin.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.compilations.model.Compilation;
import ru.practicum.admin.compilations.model.CompilationEvent;
import ru.practicum.admin.compilations.model.dto.CompilationDto;
import ru.practicum.admin.compilations.model.dto.CompilationMapper;
import ru.practicum.admin.compilations.model.dto.NewCompilationDto;
import ru.practicum.admin.compilations.model.dto.UpdateCompilationRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.events.EventRepository;
import ru.practicum.users.events.model.Event;
import ru.practicum.users.events.model.dto.EventMapper;
import ru.practicum.users.events.model.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final CompilationsEventRepository compilationsEventRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getPinned() == null || !newCompilationDto.getPinned()) {
            compilation.setPinned(false);
        } else {
            compilation.setPinned(true);
        }
        compilationRepository.save(compilation);
        if (newCompilationDto.getEvents() != null) {
            for (Long eventId : newCompilationDto.getEvents()) {
                Event event = getEventById(eventId);
                CompilationEvent compilationEvent = CompilationEvent.builder()
                        .compilation(compilation)
                        .event(event)
                        .build();
                compilationsEventRepository.save(compilationEvent);
            }
        }
        List<CompilationEvent> compilationEventList = compilationsEventRepository.findAllByCompilationId(compilation.getId());
        List<EventShortDto> events = new ArrayList<>();
        if (!compilationEventList.isEmpty()) {
            for (CompilationEvent compilationEvent : compilationEventList) {
                events.add(EventMapper.toEventShortDto(compilationEvent.getEvent()));
            }
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        compilationDto.setEvents(events);
        log.info("Add compilation: {}", compilationDto);
        return compilationDto;
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getCompilationById(compId);
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (updateCompilationRequest.getEvents() != null) {
            List<CompilationEvent> compilationEvents = compilationsEventRepository.findAllByCompilationId(compId);
            compilationsEventRepository.deleteAll(compilationEvents);
            if (!updateCompilationRequest.getEvents().isEmpty()) {
                for (Long eventId : updateCompilationRequest.getEvents()) {
                    Event event = getEventById(eventId);
                    CompilationEvent compilationEvent = CompilationEvent.builder()
                            .compilation(compilation)
                            .event(event)
                            .build();
                    compilationsEventRepository.save(compilationEvent);
                }
            }

            List<CompilationEvent> updatedCompilationEventList = compilationsEventRepository.findAllByCompilationId(compilation.getId());
            List<EventShortDto> updatedEvents = new ArrayList<>();
            if (!updatedCompilationEventList.isEmpty()) {
                for (CompilationEvent compilationEvent : updatedCompilationEventList) {
                    updatedEvents.add(EventMapper.toEventShortDto(compilationEvent.getEvent()));
                }
            }
            compilationDto.setEvents(updatedEvents);
        }
        log.info("Update compilation: {}", compilationDto);
        return compilationDto;
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilationById(compId);
        log.info("Delete compilation: {}", getCompilationById(compId));
        compilationRepository.delete(getCompilationById(compId));
        compilationsEventRepository.deleteAllByCompilationId(compId);
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new NotFoundException("Event with id " + eventId + " not found");
        });
    }

    private Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            log.error("Compilation with id {} not found", compId);
            return new NotFoundException("Compilation with id " + compId + " not found");
        });
    }
}
