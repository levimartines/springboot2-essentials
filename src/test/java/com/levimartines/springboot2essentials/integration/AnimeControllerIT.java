package com.levimartines.springboot2essentials.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.levimartines.springboot2essentials.dto.AnimeDTO;
import com.levimartines.springboot2essentials.exception.ValidationError;
import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import com.levimartines.springboot2essentials.util.PageableResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate template;

    @LocalServerPort
    private int port;

    @MockBean
    private AnimeRepository repository;

    @BeforeEach
    void setUp() {
        Anime anime = new Anime(1L, "Death Note");
        Anime anime2 = new Anime(2L, "Naruto");

        given(repository.findDistinctByNameContaining(any(), any()))
            .willReturn(new PageImpl<>(List.of(anime, anime2)));
        given(repository.findAll()).willReturn(Arrays.asList(anime, anime2));
        given(repository.findById(1L)).willReturn(Optional.of(anime));
        given(repository.save(any())).willReturn(anime);
    }

    @Test
    void getPage() {
        Page<Anime> animes = template
            .exchange("/animes/search", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        assertNotNull(animes);
        assertEquals(2, animes.getTotalElements());
        then(repository).should().findDistinctByNameContaining(anyString(), any());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<Anime> response = template
            .exchange("/animes/2", HttpMethod.GET, null, Anime.class);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        then(repository).should().findById(anyLong());
    }

    @Test
    void getByIdOk() {
        ResponseEntity<Anime> response = template
            .exchange("/animes/1", HttpMethod.GET, null, Anime.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        then(repository).should().findById(anyLong());
    }

    @Test
    void postInvalidData() {
        AnimeDTO dto = new AnimeDTO("");
        ResponseEntity<ValidationError> response = template
            .postForEntity("/animes", dto, ValidationError.class);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getErrors().size());
        then(repository).should(times(0)).findById(anyLong());
    }
}
