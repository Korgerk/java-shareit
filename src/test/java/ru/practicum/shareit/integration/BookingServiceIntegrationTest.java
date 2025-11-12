package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@ActiveProfiles("test")
class BookingServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findPastBookingsForItem_RepositoryMethod_ShouldReturnBookings() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("user@example.com");
        entityManager.persistAndFlush(user);

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user);
        entityManager.persistAndFlush(item);

        Booking booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().minusDays(2));
        booking1.setEnd(LocalDateTime.now().minusDays(1));
        booking1.setItem(item);
        booking1.setBooker(user);
        booking1.setStatus(BookingStatus.APPROVED);
        entityManager.persistAndFlush(booking1);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.now().minusDays(3));
        booking2.setEnd(LocalDateTime.now().minusDays(2));
        booking2.setItem(item);
        booking2.setBooker(user);
        booking2.setStatus(BookingStatus.APPROVED);
        entityManager.persistAndFlush(booking2);

        List<Booking> foundBookings = bookingRepository.findPastBookingsForItem(item.getId(), LocalDateTime.now());

        assertThat(foundBookings, hasSize(2));
        assertThat(foundBookings, hasItem(hasProperty("id", is(booking1.getId()))));
        assertThat(foundBookings, hasItem(hasProperty("id", is(booking2.getId()))));
        assertThat(foundBookings, everyItem(hasProperty("status", is(BookingStatus.APPROVED))));
    }
}