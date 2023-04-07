package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.enumerate.EventState;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT COUNT (e) FROM Event e WHERE e.category.id = ?1")
    int findAllByCategoryId(Long categoryId);

    @Query(value = "SELECT e FROM  Event as e " +
            "WHERE (:users is null or e.initiator.id in :users) " +
            "AND (:states is null or e.state in :states) " +
            "AND (:categories is null or e.category.id in :categories) " +
            "AND e.eventDate between :rangeStart AND :rangeEnd")
    List<Event> findAllByAdmin(@Param("users") List<Long> users, @Param("states") List<EventState> states,
                               @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);


    @Query(
            value = "SELECT e.* FROM Events as e " +
                    "WHERE (:text is null or lower(e.annotation) like concat('%', lower(:text), '%') " +
                    "OR (:text is null or lower(e.description) like concat('%', lower(:text), '%'))) " +
                    "AND (:categories is null or e.category_id in (:categories)) " +
                    "AND (:paid is null or e.paid = :paid) " +
                    "AND e.event_date between :rangeStart AND :rangeEnd and e.state = 'PUBLISHED'", nativeQuery = true
    )
    List<Event> findAllFilter(@Param("text") String text, @Param("categories") List<Long> categories,
                              @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);

    List<Event> findAllByIdIn(List<Long> eventsIds);

    List<Event> findAllByInitiator(User user, Pageable pageable);

}
