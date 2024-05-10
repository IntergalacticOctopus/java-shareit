package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDto {
        private Long id;

        private LocalDateTime start;

        private LocalDateTime end;

        private Long itemId;

        private Long bookerId;

    }
}
