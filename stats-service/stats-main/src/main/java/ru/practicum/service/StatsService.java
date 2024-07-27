package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Hit;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitForView;
import ru.practicum.dto.HitMapper;
import ru.practicum.exception.IncorrectParameterException;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StatsService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsRepository statsRepository;

    public HitDto saveHit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        log.info("Hit saved successfully: {}", hit);
        return HitMapper.toHitDto(statsRepository.save(hit));
    }

    public List<HitForView> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, FORMATTER);

        if (endTime.isBefore(startTime)) {
            log.error("End time must be greater than start time");
            throw new IncorrectParameterException("End time must be greater than start time");
        }

        if (uris == null) {
            if (unique) {
                log.info("Getting stats without uris with unique");
                return statsRepository.findHitsByTimeWithoutUriUnique(startTime, endTime);
            } else {
                log.info("Getting stats without uris");
                return statsRepository.findHitsByTimeWithoutUri(startTime, endTime);
            }
        }

        if (unique) {
            log.info("Getting stats with uris with unique");
            return statsRepository.findHitsByTimeAndUriUnique(startTime, endTime, uris);
        } else {
            log.info("Getting stats with uris");
            return statsRepository.findHitsByTimeAndUri(startTime, endTime, uris);
        }
    }
}
