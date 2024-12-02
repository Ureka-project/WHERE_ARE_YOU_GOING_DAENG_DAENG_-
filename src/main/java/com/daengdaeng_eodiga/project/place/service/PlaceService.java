package com.daengdaeng_eodiga.project.place.service;

import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.dto.PlaceDtoMapper;
import com.daengdaeng_eodiga.project.place.dto.PlaceRcommendDto;
import com.daengdaeng_eodiga.project.place.dto.PlaceWithScore;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.entity.ReviewSummary;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceScoreRepository;
import com.daengdaeng_eodiga.project.preference.dto.UserRequsetPrefernceDto;
import com.daengdaeng_eodiga.project.preference.repository.PreferenceRepository;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.review.repository.ReviewRepository;
import com.daengdaeng_eodiga.project.review.repository.ReviewSummaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
@Transactional
@RequiredArgsConstructor
@EnableScheduling
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceScoreRepository placeScoreRepository;
    private final PreferenceRepository preferenceRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewSummaryRepository reviewSummaryRepository;
    private final OpenAiService openAiService;
    private final CommonCodeService commonCodeService;

    public List<PlaceDto> filterPlaces(String city, String cityDetail, String placeTypeCode, Double latitude, Double longitude, Integer userId) {
        Integer effectiveUserId = userId != null ? userId : -1;
        List<Object[]> results = placeRepository.findByFiltersAndLocation(city, cityDetail, placeTypeCode, latitude, longitude, effectiveUserId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude, Integer userId) {
        Integer effectiveUserId = userId != null ? userId : -1;
        List<Object[]> results = placeRepository.findByKeywordAndLocation(keyword, latitude, longitude, effectiveUserId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public PlaceDto getPlaceDetails(int placeId) {

        List<Object[]> results = placeRepository.findPlaceDetailsById(placeId);

        if (results.isEmpty()) {
            throw new PlaceNotFoundException();
        }

        return PlaceDtoMapper.convertToPlaceDto(results.get(0));
    }

    public Place findPlace(int placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }

    public Double findPlaceScore(int placeId) {
        return placeScoreRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new).getScore();
    }

    public List<PlaceDto> getTopFavoritePlaces() {
        List<Object[]> results = placeRepository.findTopFavoritePlaces();
        return results.stream()
                .map(PlaceDtoMapper::convertToPlaceDto)
                .collect(Collectors.toList());
    }

    public List<PlaceDto> getTopScoredPlacesWithinRadius(Double latitude, Double longitude) {
        List<Object[]> results = placeRepository.findTopScoredPlacesWithinRadius(latitude, longitude);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public List<PlaceDto> getNearestPlaces(Double latitude, Double longitude) {
        List<Object[]> results = placeRepository.findNearestPlaces(latitude, longitude);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }


    public List<PlaceWithScore> RecommendPlaces(String MyPlace,double latitude, double longitude,Integer userId) {
        List<PlaceWithScore> placeArr = new ArrayList<>();
        List<UserRequsetPrefernceDto> UserPerferenceDto =preferenceRepository.findPreferenceTypesByUserId(userId);
        String region1 = getRegionValue(MyPlace, "region_1depth_name");
        String region2 = getRegionValue(MyPlace, "region_2depth_name");
        String region3 = getRegionValue(MyPlace, "region_3depth_name");
        List<PlaceRcommendDto> RecommnedArray = new ArrayList<>();
        List<Object[]> QueryArray =placeRepository.findPlaceRecommendationsWithKeywords();
        for(Object[] arr : QueryArray) {
            Integer placeId = (Integer) arr[0];
            String name = (String) arr[1];
            String city = (String) arr[2];
            String cityDetail = (String) arr[3];
            String township = (String) arr[4];
            Double Placelatitude = (Double) arr[5];
            Double Placelongitude = (Double) arr[6];
            String postCode = (String) arr[7];
            String streetAddresses = (String) arr[8];
            String telNumber = (String) arr[9];
            String url = (String) arr[10];
            String placeType = (String) arr[11];
            String description = (String) arr[12];
            String weightLimit = (String) arr[13];
            Boolean parking = (Boolean) arr[14];
            Boolean indoor = (Boolean) arr[15];
            Boolean outdoor = (Boolean) arr[16];
            Double score = (Double) arr[17]; // COALESCE(ps.score, 5)
            String keywordsStr = (String) arr[18]; // Grouped keywords (e.g., "keyword1,keyword2")
            Long reviewCount = (Long) arr[19];

            Map<String, Integer> keywords = new HashMap<>();
            if (keywordsStr != null && !keywordsStr.isEmpty()) {
                String[] keywordArray = keywordsStr.split(",");
                for (String keyword : keywordArray) {
                    keywords.put(keyword, keywords.getOrDefault(keyword, 0) + 1);
                }
            }
            PlaceRcommendDto dto = new PlaceRcommendDto(
                    placeId, name, city, cityDetail, township, Placelatitude, Placelongitude,
                    postCode, streetAddresses, telNumber, url, placeType, description,
                    weightLimit, parking, indoor, outdoor, score, keywords, reviewCount
            );

            RecommnedArray.add(dto);
        }
        for(PlaceRcommendDto place : RecommnedArray) {

            double Placelatitude= place.getLatitude();
            double Placelongitude= place.getLongitude();
            double score =calculateDistance(latitude, longitude,Placelatitude, Placelongitude);
            String place1 = place.getCity();
            String place2 = place.getCityDetail();
            String place3 = place.getTownship();
            score+= calculateRegionScore(region1, region2, region3, place1, place2, place3);
            score+= place.getScore() / 10.0;
            if (place.getReviewCount()<10)
            {
                score+= place.getReviewCount() / 10.0;
            }
            Map<String,Integer> placePer= place.getKeywords();
            for(UserRequsetPrefernceDto userPer : UserPerferenceDto) {
                String placeValue = userPer.getPreferenceTypes();
                if (placePer.containsKey(placeValue)) {
                    score+=1;
                }
            }
            PlaceWithScore placeWithScore = new PlaceWithScore(place, min(score,10.0));
            if (placeArr.size() < 3) {
                placeArr.add(placeWithScore);
            } else {
                placeArr.add(placeWithScore);
                PlaceWithScore minPlaceWithScore = Collections.min(placeArr, Comparator.comparing(PlaceWithScore::getScore));
                placeArr.remove(minPlaceWithScore);
            }

        }
        for(PlaceWithScore place : placeArr) {
           PlaceRcommendDto placeDto = place.getPlaceRcommendDto();
            String placeType=commonCodeService.getCommonCodeName(placeDto.getPlaceType());
            placeDto.setPlaceType(placeType);
        }
        return placeArr;
    }
    private String getRegionValue(String roadName, String regionKey) {

        String pattern = regionKey + "=(.*?),";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(roadName);

        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double maxDistance=100.0;
        return 5.0 / (1.0 + (R * c) / maxDistance);
    }
    private double calculateRegionScore(String region1, String region2, String region3, String place1, String place2, String place3) {
        double score = 0;

        if (region1 != null && !region1.equals(place1)) {
            return 0;

        }
        else
        {
            if (region1 != null && region1.equals(place1)) {
                score= 0.5;
            }
             if (region2 != null && region2.equals(place2)) {
                score=1;
            }
             if (region3 != null && region3.equals(place3)) {
                 score=2;
            }
        }
        return score;
    }


    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledReviewSummaryUpdate() {
        Logger logger = LoggerFactory.getLogger(PlaceService.class);
        logger.info("Scheduled task started.");

        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        List<Integer> placeIds = reviewRepository.findDistinctPlaceIdsByUpdatedAtAfter(lastDay);

        for (int placeId : placeIds) {
            logger.info("Updating review summary for placeId: {}", placeId);
            generateReviewSummary(placeId);
        }

        logger.info("Scheduled task completed.");
    }

    public void generateReviewSummary(int placeId) {
        Logger logger = LoggerFactory.getLogger(PlaceService.class);

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + placeId));

        ReviewSummary existingSummary = reviewSummaryRepository.findById(placeId).orElse(null);

        String existingGoodSummary = existingSummary != null ? existingSummary.getGoodSummary() : "";
        String existingBadSummary = existingSummary != null ? existingSummary.getBadSummary() : "";

        List<String> recentReviewContents = reviewRepository.findByPlace_PlaceId(placeId).stream()
                .map(Review::getContent)
                .collect(Collectors.toList());

        if (recentReviewContents.isEmpty() && existingSummary == null) {
            logger.info("No reviews or existing summary found for placeId: {}. Skipping update.", placeId);
            return;
        }

        String combinedGoodContent = existingGoodSummary + " " + String.join(" ", recentReviewContents);
        String combinedBadContent = existingBadSummary + " " + String.join(" ", recentReviewContents);

        String pros = openAiService.summarizePros(combinedGoodContent);
        String cons = openAiService.summarizeCons(combinedBadContent);

        if (existingSummary == null) {
            ReviewSummary newSummary = new ReviewSummary();
            newSummary.setPlace(place);
            newSummary.setGoodSummary(pros);
            newSummary.setBadSummary(cons);
            newSummary.setUpdateDate(LocalDateTime.now());
            reviewSummaryRepository.save(newSummary);
            logger.info("New review summary created for placeId: {}", placeId);
        } else {
            existingSummary.setGoodSummary(pros);
            existingSummary.setBadSummary(cons);
            existingSummary.setUpdateDate(LocalDateTime.now());
            reviewSummaryRepository.save(existingSummary);
            logger.info("Review summary updated for placeId: {}", placeId);
        }
    }

    private String summarizeInChunks(List<String> reviews, boolean isPros) {
        int chunkSize = 10;
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < reviews.size(); i += chunkSize) {
            List<String> chunk = reviews.subList(i, Math.min(i + chunkSize, reviews.size()));
            String combinedReviews = String.join(" ", chunk);
            if (isPros) {
                chunks.add(openAiService.summarizePros(combinedReviews));
            } else {
                chunks.add(openAiService.summarizeCons(combinedReviews));
            }
        }


        String finalSummary = String.join(" ", chunks);
        if (isPros) {
            return openAiService.summarizePros(finalSummary);
        } else {
            return openAiService.summarizeCons(finalSummary);
        }
    }

}
