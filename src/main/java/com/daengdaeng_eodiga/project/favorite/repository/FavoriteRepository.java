package com.daengdaeng_eodiga.project.favorite.repository;

import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Page<Favorite> findByUser_UserId(int userId, Pageable pageable);
}
