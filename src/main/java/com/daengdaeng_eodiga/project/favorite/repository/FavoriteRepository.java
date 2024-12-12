package com.daengdaeng_eodiga.project.favorite.repository;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    @Query("SELECT f.favoriteId, p.placeId, p.name, pm.path, p.placeType, p.streetAddresses, " +
            "       p.latitude, p.longitude, od.startTime, od.endTime " +
            "FROM Favorite f " +
            "JOIN f.place p " +
            "LEFT JOIN PlaceMedia pm ON pm.place.placeId = p.placeId " +
            "LEFT JOIN OpeningDate od ON od.place.placeId = p.placeId " +
            "WHERE f.user.userId = :userId " +
            "ORDER BY f.updatedAt DESC")
    Page<Object[]> findFavoriteResponse(int userId, Pageable pageable);
    List<Favorite> findByUser_UserIdAndPlace_PlaceId(int userId, int placeId);
}
