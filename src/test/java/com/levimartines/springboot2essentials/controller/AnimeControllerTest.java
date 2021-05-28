package com.levimartines.springboot2essentials.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.levimartines.springboot2essentials.dto.AnimeDTO;
import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.service.AnimeService;
import com.levimartines.springboot2essentials.service.KitsuAnimesService;
import com.levimartines.springboot2essentials.util.DateUtils;
import com.levimartines.springboot2essentials.util.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AnimeControllerTest {

  @Mock
  private AnimeService service;
  @Mock
  private KitsuAnimesService kitsuAnimesService;
  @Mock
  private DateUtils dateUtils;
  @Mock
  private URL url;

  @InjectMocks
  private AnimeController controller;

  @Test
  void findById() {
    given(service.findById(1L)).willReturn(new Anime(1L, "Test Anime"));

    Anime anime = controller.findById(1L, null).getBody();

    assertNotNull(anime);
    assertEquals("Test Anime", anime.getName());
    then(service).should().findById(1L);
  }

  @Test
  void findAll() {
    // given
    Anime anime = new Anime(null, "Test Anime");
    Anime anime2 = new Anime(null, "Test Anime 2");
    given(service.findAll()).willReturn(Arrays.asList(anime, anime2));

    // when
    List<Anime> animes = controller.findAll().getBody();

    // then
    assertNotNull(animes);
    assertEquals(2, animes.size());
    then(service).should().findAll();
  }

  @Test
  void findPageKitsu() {
    // given
    Anime anime = new Anime(null, "Test Anime");
    Anime anime2 = new Anime(null, "Test Anime 2");
    given(kitsuAnimesService.getAnimesKitsuApi(any())).willReturn(Arrays.asList(anime, anime2));

    // when
    List<Anime> animes = controller.getKitsuAnimesList(0).getBody();

    // then
    assertNotNull(animes);
    assertEquals(2, animes.size());
    then(kitsuAnimesService).should().getAnimesKitsuApi(any());
  }

  @Test
  void findPage() {
    Anime anime = new Anime(1L, "Death Note");
    Anime anime2 = new Anime(2L, "Naruto");
    given(service.search(any(), any(), any(), any(), any()))
        .willReturn(new PageImpl<>(List.of(anime, anime2)));

    Page<Anime> animes = controller.findPage("", null, null, null, null).getBody();

    assertNotNull(animes);
    assertEquals(2, animes.getTotalElements());
    then(service).should().search(any(), any(), any(), any(), any());
  }

  @Test
  void save() {

    ResponseEntity<Anime> response = controller.create(new AnimeDTO("Attack on Titan"));

    assertEquals(201, response.getStatusCodeValue());
    assertNotNull(response.getBody());

    then(service).should().save(any(Anime.class));
  }

  @Test
  void update() {
    given(service.update(anyLong(), any(AnimeDTO.class)))
        .willReturn(new Anime(1L, "Attack on Titan"));

    ResponseEntity<Anime> response = controller.update(1L, new AnimeDTO("Attack on Titan"));

    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());

    then(service).should().update(anyLong(), any(AnimeDTO.class));
  }

  @Test
  void delete() {
    given(service.findById(1L)).willReturn(new Anime(1L, "Test Anime"));
    ResponseEntity<Void> response = controller.delete(1L);

    assertNotNull(response);
    assertEquals(204, response.getStatusCodeValue());
  }

}
