package ru.practicum.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

@Service
public class StatClient {

    String serverUrl;

    protected final RestTemplate rest;

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.serverUrl = serverUrl;
        this.rest = builder.build();
    }

    public List<ViewStats> getStats(String start, String end, String uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", false
        );
        ResponseEntity<List<ViewStats>> response = rest
                .exchange(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                        GET, null, new ParameterizedTypeReference<>() {
                        }, parameters);
        List<ViewStats> result = response.getBody();
        if (result == null) {
            return List.of();
        }
        return result;
    }

    public void createHit(EndpointHit endpointHit) {

        HttpEntity<EndpointHit> request = new HttpEntity<>(endpointHit);
        ResponseEntity<EndpointHit> response = rest
                .exchange(serverUrl + "/hit", HttpMethod.POST, request, EndpointHit.class);
        response.getBody();
    }
}
