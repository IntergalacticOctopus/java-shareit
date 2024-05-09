package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BookingCreateDto {

    private Long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    @NotNull
    private Long itemId;

    @AssertTrue
    private boolean isTimeValid() throws ValidationException {
        if (start == null || end == null) {
            return false;
        }
        boolean returnSt = (!(start.equals(end) || end.isBefore(start)));
        return returnSt;
    }
}
