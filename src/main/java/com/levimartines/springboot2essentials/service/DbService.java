package com.levimartines.springboot2essentials.service;

import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    private final AnimeRepository animeRepository;

    public DbService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public void instantiateTestDatabase() {
        animeRepository.saveAll(Arrays.asList(
            new Anime(null, "Berzeker"),
            new Anime(null, "Dragon Ball Kai"),
            new Anime(null, "Dragon Ball Z"),
            new Anime(null, "Dragon Ball GT"),
            new Anime(null, "Dragon Ball Super")
        ));
    }

}
