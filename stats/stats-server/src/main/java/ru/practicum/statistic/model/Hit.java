package ru.practicum.statistic.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

}
