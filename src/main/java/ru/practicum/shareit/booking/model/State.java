package ru.practicum.shareit.booking.model;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    UNKNOWN;

    public static State valueOfEnum(String state) {
        for (State s : State.values()) {
            if (s.name().equalsIgnoreCase(state)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + state);
    }
}