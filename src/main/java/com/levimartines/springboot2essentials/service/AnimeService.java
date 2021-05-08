package com.levimartines.springboot2essentials.service;

import com.levimartines.springboot2essentials.dto.AnimeDTO;
import com.levimartines.springboot2essentials.exception.ResourceNotFoundException;
import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository repository;

    public List<Anime> findAll() {
        return repository.findAll();
    }

    public Anime findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Anime não encontrado."));
    }

    @Transactional(readOnly = true)
    public Page<Anime> search(String name, Integer page, Integer linesPerPage, String orderBy,
        String direction) {
        PageRequest pageRequest = PageRequest
            .of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return repository.findDistinctByNameContaining(name, pageRequest);
    }

    @Transactional
    public void save(Anime anime) {
        repository.save(anime);
    }

    public void delete(Long id) {
        Anime anime = findById(id);
        repository.delete(anime);
    }

    public Anime update(Long id, AnimeDTO dto) {
        Anime animeToUpdate = findById(id);
        animeToUpdate.setName(dto.getName());
        save(animeToUpdate);
        return animeToUpdate;
    }
}
