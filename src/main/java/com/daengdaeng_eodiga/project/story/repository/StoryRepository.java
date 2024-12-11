package com.daengdaeng_eodiga.project.story.repository;

import com.daengdaeng_eodiga.project.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Integer> {
    Optional<Story> findByStoryId(int storyId);

    @Query("SELECT COUNT(s) FROM Story s WHERE DATE(s.createdAt) = CURRENT_DATE")
    long countByTodayCreated();

    @Query("SELECT s.user.nickname, s.storyId, s.city, s.cityDetail, s.path " +
            "FROM Story s " +
            "WHERE s.user.userId = :userId " +
            "AND s.endAt > CURRENT_TIMESTAMP " +
            "ORDER BY s.createdAt ASC")
    List<Object[]> findMyActiveStoriesByUserId(@Param("userId") Integer userId);

    @Query("SELECT s.storyId, s.user.nickname, s.path " +
            "FROM Story s " +
            "WHERE s.user.userId = :landOwnerId " +
            "AND s.city = :city " +
            "AND s.cityDetail = :cityDetail " +
            "AND s.endAt > CURRENT_TIMESTAMP " +
            "ORDER BY s.createdAt ASC")
    List<Object[]> findActiveStoriesByLandOwnerId(@Param("landOwnerId") Integer landOwnerId,
                                                  @Param("city") String city,
                                                  @Param("cityDetail") String cityDetail);
}
