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
UnviewedStories AS (
    SELECT
        rs.*,
        NULL AS viewed_at
    FROM
        RecentStory rs
    LEFT JOIN
        story_view sv ON rs.story_id = sv.story_id AND sv.user_id = :userId
    WHERE
        sv.story_id IS NULL
),
ViewedStories AS (
    SELECT
        rs.*,
        sv.created_at AS viewed_at
    FROM
        RecentStory rs
    INNER JOIN
        story_view sv ON rs.story_id = sv.story_id
    WHERE
        sv.user_id = :userId
),
GroupedStories AS (
    SELECT
        landOwnerId,
        city,
        city_detail,
        MIN(created_at) AS earliest_created_at
    FROM (
        SELECT landOwnerId, city, city_detail, created_at FROM UnviewedStories
        UNION ALL
        SELECT landOwnerId, city, city_detail, created_at FROM ViewedStories
    ) grouped
    GROUP BY landOwnerId, city, city_detail
),
CombinedStories AS (
    SELECT
        us.landOwnerId,
        us.city,
        us.city_detail,
        gs.earliest_created_at AS group_created_at,
        us.created_at,
        us.viewed_at,
        (SELECT p.image FROM pet p WHERE p.user_id = us.landOwnerId ORDER BY p.pet_id ASC LIMIT 1) AS petImage,
        'unviewed' AS story_type
    FROM
        UnviewedStories us
    INNER JOIN
        GroupedStories gs ON us.landOwnerId = gs.landOwnerId AND us.city = gs.city AND us.city_detail = gs.city_detail
    UNION ALL
    SELECT
        vs.landOwnerId,
        vs.city,
        vs.city_detail,
        gs.earliest_created_at AS group_created_at,
        vs.created_at,
        vs.viewed_at,
        (SELECT p.image FROM pet p WHERE p.user_id = vs.landOwnerId ORDER BY p.pet_id ASC LIMIT 1) AS petImage,
        'viewed' AS story_type
    FROM
        ViewedStories vs
    INNER JOIN
        GroupedStories gs ON vs.landOwnerId = gs.landOwnerId AND vs.city = gs.city AND vs.city_detail = gs.city_detail
)
SELECT DISTINCT
    cs.landOwnerId,
    u.nickname,
    cs.city,
    cs.city_detail,
    cs.petImage,
    cs.story_type,
    cs.viewed_at,
    cs.group_created_at
FROM
    CombinedStories cs
JOIN
    users u ON cs.landOwnerId = u.user_id
ORDER BY
    CASE
        WHEN cs.story_type = 'unviewed' THEN 1
        WHEN cs.story_type = 'viewed' THEN 2
    END,
    CASE
        WHEN cs.viewed_at IS NULL THEN 0
        ELSE 1
    END ASC,
    cs.viewed_at ASC,
    cs.group_created_at DESC;
""", nativeQuery = true)
    List<Object[]> findMainPriorityStories(@Param("userId") Integer userId);
}
