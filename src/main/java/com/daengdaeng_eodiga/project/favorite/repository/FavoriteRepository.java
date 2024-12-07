package com.daengdaeng_eodiga.project.favorite.repository;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Page<Favorite> findByUser_UserIdOrderByUpdatedAtDesc(int userId, Pageable pageable);
    List<Favorite> findByUser_UserIdAndPlace_PlaceId(int userId, int placeId);
}
