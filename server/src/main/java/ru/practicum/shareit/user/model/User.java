package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "id_gen",
        sequenceName = "user_seq",
        allocationSize = 1)
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    long id;

    @Column(name = "name")
    String name;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "created_at", nullable = false)
    Instant created;

    @Column(name = "updated_at", nullable = false)
    Instant updated;
}