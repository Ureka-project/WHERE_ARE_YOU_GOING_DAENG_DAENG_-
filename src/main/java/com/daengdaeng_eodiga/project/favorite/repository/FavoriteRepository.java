package com.daengdaeng_eodiga.project.favorite.repository;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer>, FavoriteRepositoryCustom {
    List<Favorite> findByUser_UserIdAndPlace_PlaceId(int userId, int placeId);
}
