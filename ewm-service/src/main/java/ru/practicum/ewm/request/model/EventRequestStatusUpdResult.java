package ru.practicum.ewm.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.request.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

}
