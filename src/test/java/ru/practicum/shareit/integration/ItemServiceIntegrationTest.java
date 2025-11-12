package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@ActiveProfiles("test")
class ItemServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByOwner_Id_RepositoryMethod_ShouldReturnItems() {
        User owner = new User();
        owner.setName("Test Owner");
        owner.setEmail("owner@example.com");
        entityManager.persistAndFlush(owner);

        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);
        item1.setOwner(owner);
        entityManager.persistAndFlush(item1);

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setAvailable(false);
        item2.setOwner(owner);
        entityManager.persistAndFlush(item2);

        List<Item> foundItems = itemRepository.findByOwner_Id(owner.getId(), null).getContent();

        assertThat(foundItems, hasSize(2));
        assertThat(foundItems, hasItem(hasProperty("id", is(item1.getId()))));
        assertThat(foundItems, hasItem(hasProperty("id", is(item2.getId()))));
        assertThat(foundItems, everyItem(hasProperty("owner", hasProperty("id", is(owner.getId())))));
    }
}