package com.levimartines.springboot2essentials.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.levimartines.springboot2essentials.model.Anime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@DisplayName("Anime Repository tests")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository repository;

    @Test
    void shouldFindDistinctByNameContaining() {
        Anime anime = new Anime(null, "Naruto");
        Anime anime2 = new Anime(null, "Naruto Shippuden");
        repository.saveAll(Arrays.asList(anime, anime2));

        Page<Anime> page = repository.findDistinctByNameContaining("arut",
            PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void shouldPersist() {
        Anime anime = new Anime(null, "My Test Anime");
        repository.save(anime);
        assertNotNull(anime.getId());
    }

    @Test
    void shouldThrowAnException() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(new Anime()));

    }

}
