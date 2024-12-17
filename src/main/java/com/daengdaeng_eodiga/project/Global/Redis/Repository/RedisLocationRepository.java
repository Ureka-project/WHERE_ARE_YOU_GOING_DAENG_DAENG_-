package com.daengdaeng_eodiga.project.Global.Redis.Repository;

import com.daengdaeng_eodiga.project.Global.Redis.Dto.RedisPlaceDto;
import com.daengdaeng_eodiga.project.place.dto.PlaceWithScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class RedisLocationRepository {

    private final RedisTemplate<String, RedisPlaceDto> redisTemplate;

    @Autowired
    public RedisLocationRepository(@Qualifier(value = "listObjectRedisTemplate") RedisTemplate<String, RedisPlaceDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveLocation(Integer userId, double latitude, double longitude, String myplace, List<PlaceWithScore> placeWithScore) {
        String locationKey = "user_location:" + userId;
        redisTemplate.opsForValue().set(locationKey,new RedisPlaceDto(latitude,longitude,myplace,placeWithScore), Duration.ofHours(24));
    }

    public RedisPlaceDto getLocation(Integer userId) {
        String locationKey = "user_location:" + userId;
        return redisTemplate.opsForValue().get(locationKey);
    }

    public void deleteLocation(Integer userId) {
        String locationKey = "user_location:" + userId;
        redisTemplate.delete(locationKey);
    }
}