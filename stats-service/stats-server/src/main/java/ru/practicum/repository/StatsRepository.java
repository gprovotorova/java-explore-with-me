package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.dto.StatsDto(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit as e " +
            "where e.dateTime between :start and :end " +
            "and e.uri in :uris " +
            "group by e.ip, e.app, e.uri " +
            "order by count(e.ip) desc")
    List<StatsDto> getHitsOrderByUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.dateTime between :start and :end " +
            "and e.uri in :uris " +
            "group by e.ip, e.app, e.uri " +
            "order by count(distinct e.ip) desc")
    List<StatsDto> getUniqueHitsOrderByUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.dateTime between :start and :end " +
            "group by e.ip, e.app, e.uri " +
            "order by count(distinct e.ip) desc")
    List<StatsDto> getAllUniqueHits(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsDto(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit as e " +
            "where e.dateTime between :start and :end " +
            "group by e.ip, e.app, e.uri " +
            "order by count(e.ip) desc")
    List<StatsDto> getAllHits(LocalDateTime start, LocalDateTime end);
}
