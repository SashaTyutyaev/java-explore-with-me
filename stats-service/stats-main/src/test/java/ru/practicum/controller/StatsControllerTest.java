package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.HitForView;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatsService statsService;

    @Autowired
    private MockMvc mockMvc;

    HitDto hitDto;
    HitDto hitDto2;
    HitDto hitDto3;
    HitDto hitDto4;

    @BeforeEach
    void setUp() {
        hitDto = createHit("app", "uri", "110.1");
        hitDto2 = createHit("app", "uri", "110.1");
        hitDto3 = createHit("app", "uri2", "110.1");
        hitDto4 = createHit("app", "uri2", "111.1");
    }

    @Test
    void saveHit_Success() throws Exception {
        when(statsService.saveHit(hitDto)).thenReturn(hitDto);

        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app").value(hitDto.getApp()))
                .andExpect(jsonPath("$.uri").value(hitDto.getUri()));
    }

    @Test
    void getStats_Success() throws Exception {
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

        when(statsService.getStats(anyString(), anyString(), anyList(), anyBoolean())).thenReturn(List.of(hit1, hit2));

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(hit1, hit2)))
                        .param("start", LocalDateTime.now().toString())
                        .param("end", LocalDateTime.now().plusMinutes(5).toString())
                        .param("uris", List.of(hit1, hit2).toString())
                        .param("unique", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].app").value(hit1.getApp()))
                .andExpect(jsonPath("$[1].app").value(hit2.getApp()));
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