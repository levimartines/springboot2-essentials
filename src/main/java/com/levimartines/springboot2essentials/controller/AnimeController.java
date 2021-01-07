package com.levimartines.springboot2essentials.controller;

import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.service.AnimeService;
import com.levimartines.springboot2essentials.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService service;
    private final DateUtils dateUtils;

    @GetMapping
    public ResponseEntity<List<Anime>> findAll() {
        log.info("Formatted LocalDate: {}", dateUtils.formatLocalDateTimeToDbStyle(LocalDateTime.now()));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
