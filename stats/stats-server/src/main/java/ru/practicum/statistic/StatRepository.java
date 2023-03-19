package ru.practicum.statistic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStats;
import ru.practicum.statistic.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.dto.ViewStats(h.app, h.uri, count (h.ip)) FROM Hit h " +
            "WHERE h.timestamp between :start and :end and h.uri in :uris " +
            "GROUP BY h.uri, h.app")
    List<ViewStats> findDistinctByUriAndTimestamp(@Param("uris") List<String> uris, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value ="SELECT new ru.practicum.dto.ViewStats(h.app, h.uri, count (h.ip)) FROM Hit h " +
            "WHERE h.timestamp between :start and :end and h.uri in :uris " +
            "GROUP BY h.uri, h.app")
    List<ViewStats> findByUriAndTimestamp(@Param("uris") List<String> uris, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

//    @Query(value = "SELECT COUNT (ip) FROM Hit WHERE uri = ?1")
//    Integer findHitCountByUri(String uri);
//
//    @Query(value = "SELECT COUNT (distinct ip) FROM Hit WHERE uri = ?1")
//    Integer findHitCountByUriWithUniqueIp(String uri);
}
