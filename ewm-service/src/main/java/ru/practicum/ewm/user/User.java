package ru.practicum.ewm.user;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 55, nullable = false)
    private String name;
    @Column(length = 50, nullable = false)
    private String email;
}
