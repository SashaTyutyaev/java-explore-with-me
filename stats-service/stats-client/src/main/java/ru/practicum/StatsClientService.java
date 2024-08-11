package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitForView;

import java.util.List;

@Service
public class StatsClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${service.url}")
    private String serviceUrl;

    public HitDto saveHit(HitDto hitDto) {
        return restTemplate.postForObject(serviceUrl + "hit",
                hitDto, HitDto.class);
    }

    public List<HitForView> getStats(String start, String end, List<String> uris, Boolean unique) {
        return restTemplate.getForObject(serviceUrl + "stats?start=" + start + "&end=" +
                end + "&uris=" + uris + "&unique=" + unique, List.class);
    }
}
