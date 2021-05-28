package com.levimartines.springboot2essentials.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.levimartines.springboot2essentials.model.Anime;
import com.levimartines.springboot2essentials.repository.AnimeRepository;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbServiceTest {

    @Mock
    private AnimeRepository repository;

    @InjectMocks
    private DbService service;


    @Test
    void findById() {
        given(repository.saveAll(any()))
            .willReturn(Collections.singletonList(new Anime(1L, "Test")));

        service.instantiateTestDatabase();

        then(repository).should().saveAll(anyList());
    }

}