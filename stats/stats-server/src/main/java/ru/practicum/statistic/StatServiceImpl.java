package ru.practicum.statistic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.statistic.model.Hit;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @Override
    @Transactional
    public void createHit(EndpointHit endpointHit) {
        Hit newHit = HitMapper.toHit(endpointHit);
        statRepository.save(newHit);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String encodedStart = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String encodedEnd = URLDecoder.decode(end, StandardCharsets.UTF_8);
        List<ViewStats> viewStats;
        if (unique) {
            viewStats = statRepository.findDistinctByUriAndTimestamp(uris, LocalDateTime.parse(encodedStart, formatter),
                    LocalDateTime.parse(encodedEnd, formatter));
        } else {
            viewStats = statRepository.findByUriAndTimestamp(uris, LocalDateTime.parse(encodedStart, formatter),
                    LocalDateTime.parse(encodedEnd, formatter));
        }
        viewStats.sort(Comparator.comparing(ViewStats::getHits).reversed());
        return viewStats;
    }
}
