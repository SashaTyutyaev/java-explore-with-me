package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.Hit;
import ru.practicum.dto.HitForView;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query(value = "select new ru.practicum.dto.HitForView" +
            "(s.app, s.uri, count(s.uri)) from Hit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc ")
    List<HitForView> findHitsByTimeAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.dto.HitForView" +
            "(s.app, s.uri, count(s.uri)) from Hit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc ")
    List<HitForView> findHitsByTimeWithoutUri(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.dto.HitForView" +
            "(s.app, s.uri, count(distinct s.ip)) from Hit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(distinct s.ip) desc ")
    List<HitForView> findHitsByTimeAndUriUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.dto.HitForView" +
            "(s.app, s.uri, count(distinct s.ip)) from Hit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(distinct  s.ip) desc ")
    List<HitForView> findHitsByTimeWithoutUriUnique(LocalDateTime start, LocalDateTime end);
}
