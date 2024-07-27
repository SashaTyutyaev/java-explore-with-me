package ru.practicum;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitForView;

import java.util.List;

@Service
public class StatsClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    public HitDto saveHit(HitDto hitDto) {
        return restTemplate.postForObject("http://localhost:9090/hit",
                hitDto, HitDto.class);
    }

    public List<HitForView> getStats(String start, String end, List<String> uris, Boolean unique) {
        return restTemplate.getForObject("http://localhost:9090/stats?start=" + start + "&end=" +
                end + "&uris=" + uris + "&unique=" + unique, List.class);
    }
}
