package ru.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitForView;
import ru.practicum.dto.HitMapper;
import ru.practicum.exception.IncorrectParameterException;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private StatsService statsService;

    @Mock
    private StatsRepository statsRepository;

    HitDto hitDto;
    HitDto hitDto2;
    HitDto hitDto3;
    HitDto hitDto4;

    @BeforeEach
    void setUp() {
        statsService = new StatsService(statsRepository);
        hitDto = createHit("app", "uri", "110.1");
        hitDto2 = createHit("app", "uri", "110.1");
        hitDto3 = createHit("app", "uri2", "110.1");
        hitDto4 = createHit("app", "uri2", "111.1");
    }

    @Test
    void saveHit_ReturnHit() {
        when(statsRepository.save(HitMapper.toHit(hitDto))).thenReturn(HitMapper.toHit(hitDto));
        HitDto result = statsService.saveHit(hitDto);
        assertNotNull(result);
        assertEquals(result, hitDto);
        assertEquals(result.getApp(), hitDto.getApp());
        assertEquals(result.getUri(), hitDto.getUri());
    }

    @Test
    void getStats_WithUriAndTime_ReturnHits() {
        HitForView hit1 = HitForView.builder()
                .app("app")
                .uri("uri")
                .hits(2L)
                .build();

        HitForView hit2 = HitForView.builder()
                .app("app")
                .uri("uri2")
                .hits(2L)
                .build();
        when(statsRepository.findHitsByTimeAndUri(any(LocalDateTime.class), any(LocalDateTime.class),
                any(List.class))).thenReturn(List.of(hit1, hit2));

        List<HitForView> result = statsService.getStats(FORMATTER.format(LocalDateTime.now()),
                FORMATTER.format(LocalDateTime.now().plusMinutes(10)), List.of("uri", "uri2"), false);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0).getHits(), 2);
        assertEquals(result.get(1).getHits(), 2);
    }

    @Test
    void getStats_WithUriAndTime_ReturnUniqueHits() {
        HitForView hit1 = HitForView.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build();

        HitForView hit2 = HitForView.builder()
                .app("app")
                .uri("uri2")
                .hits(2L)
                .build();

        when(statsRepository.findHitsByTimeAndUriUnique(any(LocalDateTime.class), any(LocalDateTime.class),
                any(List.class))).thenReturn(List.of(hit1, hit2));

        List<HitForView> result = statsService.getStats(FORMATTER.format(LocalDateTime.now()),
                FORMATTER.format(LocalDateTime.now().plusMinutes(10)), List.of("uri", "uri2"), true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0).getHits(), 1);
        assertEquals(result.get(1).getHits(), 2);
    }

    @Test
    void getStats_WithTimeWithoutUri_ReturnHits() {
        HitForView hit1 = HitForView.builder()
                .app("app")
                .uri("uri")
                .hits(2L)
                .build();

        HitForView hit2 = HitForView.builder()
                .app("app")
                .uri("uri2")
                .hits(2L)
                .build();
        when(statsRepository.findHitsByTimeWithoutUri(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(hit1, hit2));

        List<HitForView> result = statsService.getStats(FORMATTER.format(LocalDateTime.now()),
                FORMATTER.format(LocalDateTime.now().plusMinutes(10)), null, false);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0).getHits(), 2);
        assertEquals(result.get(1).getHits(), 2);
    }

    @Test
    void getStats_WithTimeWithoutUri_ReturnUniqueHits() {
        HitForView hit1 = HitForView.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build();

        HitForView hit2 = HitForView.builder()
                .app("app")
                .uri("uri2")
                .hits(2L)
                .build();
        when(statsRepository.findHitsByTimeWithoutUriUnique(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(hit1, hit2));

        List<HitForView> result = statsService.getStats(FORMATTER.format(LocalDateTime.now()),
                FORMATTER.format(LocalDateTime.now().plusMinutes(10)), null, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0).getHits(), 1);
        assertEquals(result.get(1).getHits(), 2);
    }

    @Test
    void getStats_Throws400_EndTimeBeforeStartTime() {
        assertThrows(IncorrectParameterException.class, () -> {
            statsService.getStats(FORMATTER.format(LocalDateTime.now()),
                    FORMATTER.format(LocalDateTime.now().minusMinutes(10)), null, true);
        });
    }

    private HitDto createHit(String app, String uri, String ip) {
        return HitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(FORMATTER.format(LocalDateTime.now()))
                .build();
    }
}