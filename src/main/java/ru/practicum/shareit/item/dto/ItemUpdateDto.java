package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Boolean isRequested;
    private User owner;
}
