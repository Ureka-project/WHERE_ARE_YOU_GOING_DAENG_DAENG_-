package com.daengdaeng_eodiga.project.place.dto;

import com.daengdaeng_eodiga.project.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceWithScore
{   private PlaceRcommendDto placeRcommendDto;
    private double score;
    public PlaceWithScore(PlaceRcommendDto placeRcommendDto, double score) {
        this.placeRcommendDto = placeRcommendDto;
        this.score = score;
    }
}
