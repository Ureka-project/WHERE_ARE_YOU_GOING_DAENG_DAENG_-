package com.daengdaeng_eodiga.project.Global.Redis.Repository;

import com.daengdaeng_eodiga.project.story.dto.RedisGroupedUserStoriesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class RedisStoryRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisStoryRepository(@Qualifier("storyRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 레디스에 스토리를 저장하는 메소드
     * @param city
     * @param cityDetail
     * @param userId
     * @param storyId
     * @param story
     */
    public void saveStory(String city, String cityDetail, int userId, int storyId, RedisGroupedUserStoriesDto story) {
        String key = generateKey(city, cityDetail, userId, storyId);
        redisTemplate.opsForValue().set(key, story, Duration.ofHours(24)); // TTL 24시간 설정
    }

    /**
     * 레디스로부터 모든 스토리를 조회하는 메소드
     * @return
     */
    public List<RedisGroupedUserStoriesDto> getAllStories() {
        Set<String> keys = redisTemplate.keys("story:*");
        List<RedisGroupedUserStoriesDto> allStories = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {
                RedisGroupedUserStoriesDto story = (RedisGroupedUserStoriesDto) redisTemplate.opsForValue().get(key);
                if (story != null) {
                    allStories.add(story);
                }
            }
        }
        return allStories;
    }

    /**
     * 레디스에서 스토리 삭제하는 메소드
     * @param city
     * @param cityDetail
     * @param userId
     * @param storyId
     */
    public void deleteStory(String city, String cityDetail, int userId, int storyId) {
        String key = generateKey(city, cityDetail, userId, storyId);
        redisTemplate.delete(key);
    }

    /**
     * 키 생성 메소드
     * @param city
     * @param cityDetail
     * @param userId
     * @param storyId
     * @return
     */
    private String generateKey(String city, String cityDetail, int userId, int storyId) {
        return String.format("story:%s:%s:%d:%d", city, cityDetail, userId, storyId);
    }
}