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

    @Query("SELECT COUNT(s) FROM Story s WHERE s.createdAt BETWEEN :todayStart AND :tomorrowStart")
    long countByTodayCreated(@Param("todayStart") LocalDateTime todayStart,
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
WITH RecentStory AS (
    SELECT
        s.story_id,
        s.user_id AS landOwnerId,
        s.city,
        s.city_detail,
        s.created_at,
        s.end_at
    FROM
        story s
    WHERE
        s.end_at > NOW()
        AND s.user_id != :userId
),
StoryStatus AS (
    SELECT
        rs.*,
        sv.created_at AS story_viewed_at,
        CASE
            WHEN sv.story_id IS NOT NULL THEN 'viewed'
            ELSE 'unviewed'
        END AS story_type
    FROM
        RecentStory rs
    LEFT JOIN
        story_view sv ON rs.story_id = sv.story_id AND sv.user_id = :userId
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
        MIN(created_at) AS group_created_at,
        MAX(CASE WHEN story_type = 'viewed' THEN story_viewed_at ELSE NULL END) AS latest_story_viewed_at
    FROM
        StoryStatus
    GROUP BY
        landOwnerId, city, city_detail
),
FinalStories AS (
    SELECT
        gss.landOwnerId,
        gss.city,
        gss.city_detail,
        gss.group_story_type AS story_type,
        gss.group_created_at,
        gss.latest_story_viewed_at,
        (SELECT p.image FROM pet p WHERE p.user_id = gss.landOwnerId ORDER BY p.pet_id ASC LIMIT 1) AS petImage
    FROM
        GroupedStoryStatus gss
)
SELECT DISTINCT
    fs.landOwnerId,
    u.nickname,
    fs.city,
    fs.city_detail,
    fs.petImage,
    fs.story_type,
    fs.group_created_at,
    fs.latest_story_viewed_at
FROM
    FinalStories fs
JOIN
    users u ON fs.landOwnerId = u.user_id
ORDER BY
    CASE
        WHEN fs.story_type = 'unviewed' THEN 1
        WHEN fs.story_type = 'viewed' THEN 2
    END,
    CASE
        WHEN fs.story_type = 'unviewed' THEN fs.group_created_at
        ELSE NULL
    END DESC,
    CASE
        WHEN fs.story_type = 'viewed' THEN fs.latest_story_viewed_at
        ELSE NULL
    END ASC,
    fs.landOwnerId
""", nativeQuery = true)
    List<Object[]> findMainPriorityStories(@Param("userId") Integer userId);
}
