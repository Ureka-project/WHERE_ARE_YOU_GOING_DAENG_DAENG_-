package com.daengdaeng_eodiga.project.favorite.repository;

import com.querydsl.core.Tuple;

import java.time.LocalDateTime;
import java.util.List;

public interface FavoriteRepositoryCustom {
    List<Tuple> findCustomFavorites(int userId, LocalDateTime lastUpdatedAt, int lastFavoriteId, int size);
}