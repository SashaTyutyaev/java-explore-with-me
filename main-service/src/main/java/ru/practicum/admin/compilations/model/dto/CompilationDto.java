package ru.practicum.admin.compilations.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.users.events.model.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {

    private List<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
