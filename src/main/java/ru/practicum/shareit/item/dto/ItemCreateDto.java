package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateDto {
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    private Long userId;

    private Long requestId;
}
