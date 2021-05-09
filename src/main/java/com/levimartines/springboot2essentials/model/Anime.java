package com.levimartines.springboot2essentials.model;

import com.levimartines.springboot2essentials.dto.AnimeDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ANIME")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANIME_ID")
    private Long id;
    @Column(name = "ANIME_NAME", nullable = false)
    private String name;

    public Anime(AnimeDTO dto) {
        this.name = dto.getName();
    }
}
