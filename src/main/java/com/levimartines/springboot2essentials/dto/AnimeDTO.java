package com.levimartines.springboot2essentials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeDTO {

    @NotEmpty(message = "name should not be empty or null")
    @Length(min = 3, max = 40, message = "name should contain at least 3 characters and max 40")
    @Schema(name = "Anime's name", example = "One Piece", required = true)
    private String name;

}
