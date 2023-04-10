package ru.practicum.ewm.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.request.ParticipationRequestStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EventRequestStatusUpd {

    private List<Long> requestIds;
    private ParticipationRequestStatus status;
}
