package ru.practicum.ewm.event.client;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.ewm.DataFormatter;
import ru.practicum.stats.StatClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StatisticClient {

    public static List<ViewStats> getStats(String url, StatClient statClient) {
        String start = DataFormatter.fromDateToString(LocalDateTime.now().minusYears(2));
        String end = DataFormatter.fromDateToString(LocalDateTime.now().plusYears(10));
        List<String> urls = new ArrayList<>();
        urls.add(url);
        return statClient.getStats(start, end, urls);
    }

    public static void createHit(StatClient statClient, EndpointHit endpointHit) {
        statClient.createHit(
                new EndpointHit(
                        endpointHit.getId(),
                        endpointHit.getApp(),
                        endpointHit.getUri(),
                        endpointHit.getIp(),
                        endpointHit.getTimestamp()
                )
        );
    }


}
