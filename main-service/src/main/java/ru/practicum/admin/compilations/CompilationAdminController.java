package ru.practicum.admin.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.compilations.model.dto.CompilationDto;
import ru.practicum.admin.compilations.model.dto.NewCompilationDto;
import ru.practicum.admin.compilations.model.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;

    @PostMapping
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.addCompilation(newCompilationDto);
    }

    @PatchMapping("{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationAdminService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationAdminService.deleteCompilation(compId);
    }
}
