package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.events.model.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Integer> categoryIds,
                                        @RequestParam(required = false, defaultValue = "false") Boolean paid,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam String sort,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        HttpServletRequest request) {
        return publicEventService.getEvents(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request.getRemoteAddr(), request.getRequestURI());

    }

    @GetMapping("{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return publicEventService.getEvent(id, request.getRemoteAddr(), request.getRequestURI());
    }
}
