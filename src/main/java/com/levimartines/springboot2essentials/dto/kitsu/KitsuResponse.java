package com.levimartines.springboot2essentials.dto.kitsu;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitsuResponse {

    private List<KitsuResponseData> data;
}
