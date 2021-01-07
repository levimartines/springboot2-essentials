package com.levimartines.springboot2essentials.service;

import com.levimartines.springboot2essentials.exception.ResourceNotFoundException;
import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository repository;

    public List<Anime> findAll() {
        return repository.findAll();
    }

    public Anime findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Anime não encontrado."));
    }

    public void delete(Long id) {
        Anime anime = findById(id);
        repository.delete(anime);
    }

    public void update(Anime anime) {

    }
}
