package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private Long id;

    @NotNull
    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

}
