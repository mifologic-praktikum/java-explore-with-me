package ru.practicum.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

//    public ResponseEntity<Object> getStats(String start, String end, List<String> uris) {
//        Map<String, Object> parameters = Map.of(
//                "start", start,
//                "end", end,
//                "uris", uris,
//                "unique", false
//        );
//        return get("/stats", parameters);
//    }

    public List<ViewStats> getStats(String start, String end, List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", false
        );
        ObjectMapper objectMapper = new ObjectMapper();
        Object responseBody = get("/stats", parameters).getBody();
        return Collections.singletonList(objectMapper.convertValue(responseBody, new TypeReference<>() {
        }));
    }

    public void createHit(EndpointHit endpointHit) {
        post("/hit", endpointHit);
    }
}
