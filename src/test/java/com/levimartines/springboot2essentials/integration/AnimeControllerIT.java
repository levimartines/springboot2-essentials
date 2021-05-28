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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AnimeControllerIT {

    @Autowired
    @Qualifier(value = "templateAdmin")
    private TestRestTemplate templateAdmin;

    @Autowired
    @Qualifier(value = "templateUser")
    private TestRestTemplate templateUser;

    @MockBean
    private AnimeRepository repository;

    @Lazy
    @TestConfiguration
    static class config {

        @Bean(name = "templateUser")
        public TestRestTemplate templateRoleUser(@Value("${local.server.port}") int port) {
            RestTemplateBuilder templateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .basicAuthentication("user", "user");
            return new TestRestTemplate(templateBuilder);
        }

        @Bean(name = "templateAdmin")
        public TestRestTemplate templateRoleAdmin(@Value("${local.server.port}") int port) {
            RestTemplateBuilder templateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port)
                .basicAuthentication("admin", "admin");
            return new TestRestTemplate(templateBuilder);
        }
    }

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
        Page<Anime> animes = templateUser
            .exchange("/animes/search", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        assertNotNull(animes);
        assertEquals(2, animes.getTotalElements());
        then(repository).should().findDistinctByNameContaining(anyString(), any());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<Anime> response = templateUser
            .exchange("/animes/2", HttpMethod.GET, null, Anime.class);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        then(repository).should().findById(anyLong());
    }

    @Test
    void getByIdOk() {
        ResponseEntity<Anime> response = templateUser
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
        ResponseEntity<ValidationError> response = templateAdmin
            .postForEntity("/animes", dto, ValidationError.class);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getErrors().size());
        then(repository).should(times(0)).save(any(Anime.class));
    }

    @Test
    void postNoAuthorization() {
        AnimeDTO dto = new AnimeDTO("");
        ResponseEntity<Void> response = templateUser
            .postForEntity("/animes", dto, Void.class);

        assertNotNull(response);
        assertEquals(403, response.getStatusCodeValue());
        then(repository).should(times(0)).save(any(Anime.class));
    }

    @Test
    void postSuccess() {
        AnimeDTO dto = new AnimeDTO("Death Note");
        ResponseEntity<Anime> response = templateAdmin
            .postForEntity("/animes", dto, Anime.class);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        then(repository).should(times(1)).save(any(Anime.class));
    }
}
