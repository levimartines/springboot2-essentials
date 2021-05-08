package com.levimartines.springboot2essentials.dto.kitsu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitsuDataAttributes {

    private String canonicalTitle;
    private Double averageRating;

}
