package com.levimartines.springboot2essentials.service;

import com.levimartines.springboot2essentials.dto.kitsu.KitsuResponse;
import com.levimartines.springboot2essentials.model.Anime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KitsuAnimesService {

    public List<Anime> getAnimesKitsuApi(Integer page) {
        try {
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/vnd.api+json");
            headers.add("Content-Type", "application/vnd.api+json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String offset = String.valueOf(page * 10);
            KitsuResponse response = template
                .exchange("https://kitsu.io/api/edge/anime?[type]=anime7&page[offset]=" + offset,
                    HttpMethod.GET, entity, KitsuResponse.class).getBody();
            // caso retornasse um array, poderia ser utilizado um new ParameterizedTypeReference<List<Anime>>() {}

            List<Anime> animes = new ArrayList<>();
            if (response != null && response.getData() != null) {
                animes = response.getData().stream()
                    .map(data -> new Anime(null, data.getAttributes().getCanonicalTitle()))
                    .collect(Collectors.toList());
            }
            return animes;
        } catch (RuntimeException e) {
            log.error("Erro ao comunicar com Kitsu API: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

}
