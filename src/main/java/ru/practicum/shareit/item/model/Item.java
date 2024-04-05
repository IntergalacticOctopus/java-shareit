package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@Component
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;

}
