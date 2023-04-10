package ru.practicum.ewm.request;

import ru.practicum.ewm.DataFormatter;
import ru.practicum.ewm.request.model.ParticipationRequest;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                DataFormatter.fromDateToString(participationRequest.getCreated()),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus()
        );
    }
}
