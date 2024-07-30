package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitForView;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public HitDto saveHit(@RequestBody HitDto hitDto) {
        log.info("Saving hit {}", hitDto);
        return statsService.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<HitForView> getStats(@RequestParam String start, @RequestParam String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Getting stats");
        return this.statsService.getStats(start, end, uris, unique);
    }
}
