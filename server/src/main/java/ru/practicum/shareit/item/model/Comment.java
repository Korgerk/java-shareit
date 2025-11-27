package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

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
        sequenceName = "comment_seq",
        allocationSize = 1)
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    long id;

    @Column(name = "text", nullable = false, length = 65535)
    String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User author;

    @Column(name = "created_at", nullable = false)
    Instant created;
}