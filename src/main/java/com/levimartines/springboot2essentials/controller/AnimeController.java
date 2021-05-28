package com.levimartines.springboot2essentials.controller;

import com.levimartines.springboot2essentials.dto.AnimeDTO;
import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.service.AnimeService;
import com.levimartines.springboot2essentials.service.KitsuAnimesService;
import com.levimartines.springboot2essentials.util.DateUtils;
import com.levimartines.springboot2essentials.util.URL;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService service;
    private final KitsuAnimesService kitsuAnimesService;
    private final DateUtils dateUtils;

    @GetMapping
    public ResponseEntity<List<Anime>> findAll() {
        log.info("Formatted LocalDate: {}",
            dateUtils.formatLocalDateTimeToDbStyle(LocalDateTime.now()));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Anime>> findPage(
        @RequestParam(value = "name", defaultValue = "") String name,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
        @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        String nameDecoded = URL.decodeParam(name);
        Page<Anime> list = service.search(nameDecoded, page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/kitsu")
    public ResponseEntity<List<Anime>> getKitsuAnimesList(
        @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(kitsuAnimesService.getAnimesKitsuApi(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User logged in: {}", userDetails);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Anime> create(@Valid @RequestBody AnimeDTO dto) {
        Anime anime = new Anime(dto);
        service.save(anime);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(anime.getId()).toUri();
        return ResponseEntity.created(uri).body(anime);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> update(@PathVariable Long id, @RequestBody AnimeDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
