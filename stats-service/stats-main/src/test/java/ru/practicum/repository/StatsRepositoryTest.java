package ru.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.Hit;
import ru.practicum.dto.HitForView;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class StatsRepositoryTest {

    @Autowired
    private StatsRepository statsRepository;

    @BeforeEach
    void setUp() {
        statsRepository.save(createHit("app", "uri", "110.1"));
        statsRepository.save(createHit("app", "uri", "110.1"));
        statsRepository.save(createHit("app", "uri2", "1101.1"));
        statsRepository.save(createHit("app", "uri", "1102.1"));
        statsRepository.save(createHit("app", "uri2", "1102.1"));
        statsRepository.save(createHit("app", "uri2", "1102.11"));
    }

    @Test
    void findHits_ByTimeAndUri_ReturnHits() {
        List<String> uris = List.of("uri", "uri2");
        List<HitForView> hits = statsRepository.findHitsByTimeAndUri(LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(10), uris);
        assertNotNull(hits);
        assertEquals(2, hits.size());
        assertEquals(3, hits.get(0).getHits());
        assertEquals(3, hits.get(1).getHits());
    }

    @Test
    void findHits_ByTimeWithoutUri_ReturnHits() {
        List<HitForView> hits = statsRepository.findHitsByTimeWithoutUri(LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(10));
        assertNotNull(hits);
        assertEquals(2, hits.size());
        assertEquals(3, hits.get(0).getHits());
        assertEquals(3, hits.get(1).getHits());
    }

    @Test
    void findHits_ByTimeAndUri_ReturnUniqueHits() {
        List<String> uris = List.of("uri", "uri2");
        List<HitForView> hits = statsRepository.findHitsByTimeAndUriUnique(LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(10), uris);
        assertNotNull(hits);
        assertEquals(2, hits.size());
        assertEquals(2, hits.get(0).getHits());
        assertEquals(3, hits.get(1).getHits());
    }

    @Test
    void findHits_ByTimeWithoutUri_ReturnUniqueHits() {
        List<HitForView> hits = statsRepository.findHitsByTimeWithoutUriUnique(LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusMinutes(10));
        assertNotNull(hits);
        assertEquals(2, hits.size());
        assertEquals(2, hits.get(0).getHits());
        assertEquals(3, hits.get(1).getHits());
    }

    private Hit createHit(String app, String uri, String ip) {
        return Hit.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
    }
}