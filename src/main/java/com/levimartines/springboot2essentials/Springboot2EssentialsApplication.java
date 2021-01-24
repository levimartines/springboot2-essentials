package com.levimartines.springboot2essentials;

import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Springboot2EssentialsApplication implements CommandLineRunner {

    private final AnimeRepository repository;

    public Springboot2EssentialsApplication(AnimeRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Springboot2EssentialsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.saveAll(Arrays.asList(
            new Anime(null, "Berzeker"),
            new Anime(null, "Dragon Ball Kai"),
            new Anime(null, "Dragon Ball Z"),
            new Anime(null, "Dragon Ball GT"),
            new Anime(null, "Dragon Ball Super")
        ));
    }
}
