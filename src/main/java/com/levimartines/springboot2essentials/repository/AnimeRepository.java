package com.levimartines.springboot2essentials.repository;

import com.levimartines.springboot2essentials.model.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Page<Anime> findDistinctByNameContaining(String name, Pageable p);
}
