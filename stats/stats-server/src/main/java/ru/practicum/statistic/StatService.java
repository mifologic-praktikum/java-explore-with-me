package ru.practicum.statistic;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.util.List;

public interface StatService {

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);

    void createHit(EndpointHit endpointHit);

}
