package com.daengdaeng_eodiga.project.story.repository;

import com.daengdaeng_eodiga.project.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Integer> {
    Optional<Story> findByStoryId(int storyId);

    @Query("SELECT s FROM Story s JOIN FETCH s.user u JOIN FETCH u.pets WHERE s.endAt > :currentTime")
    List<Story> findByEndAtAfter(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT COUNT(s) FROM Story s " +
            "WHERE s.createdAt BETWEEN :todayStart AND :tomorrowStart " +
            "AND s.user.userId = :userId ")
    long countByTodayCreated(@Param("userId") Integer userId,
                             @Param("todayStart") LocalDateTime todayStart,
                             @Param("tomorrowStart") LocalDateTime  tomorrowStart);

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

    @Query(value = """
WITH StoryStatus AS (
    SELECT
        s.user_id AS landOwnerId,
        s.city,
        s.city_detail,
        sv.created_at AS story_viewed_at,
        CASE
            WHEN sv.story_id IS NOT NULL THEN 'viewed'
            ELSE 'unviewed'
        END AS story_type,
        s.created_at AS group_created_at
    FROM
        story s
    LEFT JOIN
        story_view sv
        ON s.story_id = sv.story_id AND sv.user_id = :userId
    WHERE
        s.end_at > NOW()
        AND s.user_id != :userId
),
GroupedStoryStatus AS (
    SELECT
        landOwnerId,
        city,
        city_detail,
        CASE
            WHEN COUNT(CASE WHEN story_type = 'unviewed' THEN 1 END) = 0 THEN 'viewed'
            ELSE 'unviewed'
        END AS group_story_type,
        MIN(group_created_at) AS group_created_at,
        MAX(CASE WHEN story_type = 'viewed' THEN story_viewed_at ELSE NULL END) AS latest_story_viewed_at
    FROM
        StoryStatus
    GROUP BY
        landOwnerId, city, city_detail
    ORDER BY
        CASE
            WHEN COUNT(CASE WHEN story_type = 'unviewed' THEN 1 END) = 0 THEN 2
            ELSE 1
        END,
        MIN(group_created_at) DESC,
        MAX(CASE WHEN story_type = 'viewed' THEN story_viewed_at ELSE NULL END) ASC
)
SELECT
    gss.landOwnerId,
    u.nickname,
    gss.city,
    gss.city_detail,
    (SELECT p.image FROM pet p WHERE p.user_id = gss.landOwnerId ORDER BY p.pet_id ASC LIMIT 1) AS petImage,
    gss.group_story_type AS story_type,
    gss.group_created_at,
    gss.latest_story_viewed_at
FROM
    GroupedStoryStatus gss
JOIN
    users u ON gss.landOwnerId = u.user_id
ORDER BY
    CASE
        WHEN gss.group_story_type = 'unviewed' THEN 1
        WHEN gss.group_story_type = 'viewed' THEN 2
    END,
    CASE
        WHEN gss.group_story_type = 'unviewed' THEN gss.group_created_at
        ELSE NULL
    END DESC,
    CASE
        WHEN gss.group_story_type = 'viewed' THEN gss.latest_story_viewed_at
        ELSE NULL
    END ASC
""", nativeQuery = true)
    List<Object[]> findMainPriorityStories(@Param("userId") Integer userId);

}
