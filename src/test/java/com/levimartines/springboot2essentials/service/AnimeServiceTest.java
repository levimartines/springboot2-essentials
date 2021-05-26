package com.levimartines.springboot2essentials.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.levimartines.springboot2essentials.dto.AnimeDTO;
import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@SpringBootTest
class AnimeServiceTest {

    @Mock
    private AnimeRepository repository;

    @InjectMocks
    private AnimeService service;

    @Test
    void findById() {
        given(repository.findById(1L)).willReturn(Optional.of(new Anime(1L, "Test Anime")));

        Anime anime = service.findById(1L);

        assertNotNull(anime);
        assertEquals("Test Anime", anime.getName());
        then(repository).should().findById(1L);
    }

    @Test
    void findAll() {
        // given
        Anime anime = new Anime(null, "Test Anime");
        Anime anime2 = new Anime(null, "Test Anime 2");
        given(repository.findAll()).willReturn(Arrays.asList(anime, anime2));

        // when
        List<Anime> animes = service.findAll();

        // then
        assertNotNull(animes);
        assertEquals(2, animes.size());
        then(repository).should().findAll();
    }

    @Test
    void findPage() {
        Anime anime = new Anime(1L, "Death Note");
        Anime anime2 = new Anime(2L, "Naruto");
        given(repository.findDistinctByNameContaining(any(), any()))
            .willReturn(new PageImpl<>(List.of(anime, anime2)));

        Page<Anime> animes = service.search("", 0, 2, "name", "ASC");

        assertNotNull(animes);
        assertEquals(2, animes.getTotalElements());
        then(repository).should().findDistinctByNameContaining(any(), any());
    }

    @Test
    void save() {
        given(repository.save(any())).willReturn(new Anime(1L, "Attack on Titan"));

        Anime anime = service.save(new Anime(null, "Attack on Titan"));

        assertNotNull(anime.getId());
        then(repository).should().save(any(Anime.class));
    }

    @Test
    void update() {
        Anime anime = new Anime(1L, "Attack on Titan");
        given(repository.findById(anyLong())).willReturn(Optional.of(anime));
        given(repository.save(any()))
            .willReturn(new Anime(1L, "Attack on Titan 2"));

        Anime response = service.update(1L, new AnimeDTO("Attack on Titan 2"));

        assertEquals("Attack on Titan 2", response.getName());

        then(repository).should().save(any(Anime.class));
    }

    @Test
    void delete() {
        given(repository.findById(1L)).willReturn(Optional.of(new Anime(1L, "Test Anime")));
        service.delete(1L);

        then(repository).should().delete(any(Anime.class));
    }

}
