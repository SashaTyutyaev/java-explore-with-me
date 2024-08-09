package ru.practicum.admin.users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @Email
    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;
}
