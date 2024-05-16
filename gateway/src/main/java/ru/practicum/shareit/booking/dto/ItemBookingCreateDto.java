package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingCreateDto {

    private long itemId;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    @AssertTrue(message = "Time validation error")
    private boolean isTimeValid() throws ValidationException {
        if (start == null || end == null) {
            return false;
        }
        boolean returnSt = end.isAfter(start);
        return returnSt;
    }
}
