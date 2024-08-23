package ru.practicum.admin.compilations.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned;

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
