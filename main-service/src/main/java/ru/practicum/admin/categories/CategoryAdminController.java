package ru.practicum.admin.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.categories.model.dto.CategoryDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryAdminService.addCategory(categoryDto);
    }

    @PatchMapping("{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        return categoryAdminService.updateCategory(categoryDto, catId);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryAdminService.deleteCategory(catId);
    }
}
