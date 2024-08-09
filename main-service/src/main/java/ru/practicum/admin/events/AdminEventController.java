package ru.practicum.admin.events;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.events.model.UpdateEventAdminRequest;
import ru.practicum.users.events.model.dto.EventFullDto;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Integer> initiatorIds, @RequestParam List<String> states,
                                        @RequestParam List<Integer> categoriesIds, @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd, @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return adminEventService.getEvents(initiatorIds, states, categoriesIds, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest event) {
        return adminEventService.updateEvent(eventId, event);
    }
}
