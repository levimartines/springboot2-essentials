package com.levimartines.springboot2essentials.service;

import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.model.User;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import com.levimartines.springboot2essentials.repository.UserRepository;
import java.util.Arrays;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    private final AnimeRepository animeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public DbService(AnimeRepository animeRepository,
        UserRepository userRepository,
        BCryptPasswordEncoder encoder) {
        this.animeRepository = animeRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void instantiateTestDatabase() {
        userRepository.save(
            new User(null, "Admin", "admin", encoder.encode("admin"), "ROLE_USER,ROLE_ADMIN"));
        userRepository.save(
            new User(null, "User", "user", encoder.encode("user"), "ROLE_USER"));

        animeRepository.saveAll(Arrays.asList(
            new Anime(null, "Berzeker"),
            new Anime(null, "Dragon Ball Kai"),
            new Anime(null, "Dragon Ball Z"),
            new Anime(null, "Dragon Ball GT"),
            new Anime(null, "Dragon Ball Super")
        ));
    }

}
