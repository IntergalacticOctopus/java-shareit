package ru.practicum.shareit.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    private Long id;

    @NotBlank
    private String text;

    private LocalDateTime created;
}
