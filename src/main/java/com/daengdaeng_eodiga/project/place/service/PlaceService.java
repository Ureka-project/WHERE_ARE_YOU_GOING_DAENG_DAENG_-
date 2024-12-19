package com.daengdaeng_eodiga.project.place.service;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisLocationRepository;
import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.place.dto.*;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.entity.PlaceMedia;
import com.daengdaeng_eodiga.project.place.entity.PlaceScore;
import com.daengdaeng_eodiga.project.place.entity.ReviewSummary;
import com.daengdaeng_eodiga.project.place.repository.PlaceMediaRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceScoreRepository placeScoreRepository;
    private final PreferenceRepository preferenceRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewSummaryRepository reviewSummaryRepository;
    private final OpenAiService openAiService;
    private final CommonCodeService commonCodeService;
    private final RedisLocationRepository redisLocationRepository;
    private final PlaceMediaRepository placeMediaRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    public List<PlaceDto> filterPlaces(String city, String cityDetail, String placeTypeCode, Double latitude, Double longitude, Integer userId) {
        Integer effectiveUserId = userId != null ? userId : -1;
        List<Object[]> results = placeRepository.findByFiltersAndLocation(city, cityDetail, placeTypeCode, latitude, longitude, effectiveUserId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }

    public List<String> getAutocompleteSuggestions(String keyword) {
        return placeRepository.findPlaceNamesByPartialKeyword(keyword);
    }

    public List<PlaceDto> searchPlaces(String keyword, Double latitude, Double longitude, Integer userId) {
        Integer effectiveUserId = userId != null ? userId : -1;

        if (latitude == 0.0 && longitude == 0.0) {
            latitude = 37.5664056;
            longitude = 126.9778222;
        }

        String formattedKeyword = Arrays.stream(keyword.split("\\s+"))
                .map(word -> word + "*")
                .collect(Collectors.joining(" "));

        List<Object[]> results = placeRepository.findByKeywordAndLocation(keyword, formattedKeyword, latitude, longitude, effectiveUserId);
        return results.stream().map(PlaceDtoMapper::convertToPlaceDto).collect(Collectors.toList());
    }



    private boolean checkIfUserFavoritedPlace(int placeId, Integer userId) {
        return placeRepository.existsFavoriteByPlaceIdAndUserId(placeId, userId);
    }

    public PlaceDto getPlaceDetails(int placeId, Integer userId) {
        List<Object[]> results = placeRepository.findPlaceDetailsById(placeId);

        if (results.isEmpty()) {
            throw new PlaceNotFoundException();
        }
        PlaceDto placeDto = PlaceDtoMapper.convertToPlaceDto(results.get(0));
        if (userId != null) {

            boolean isFavorite = checkIfUserFavoritedPlace(placeId, userId);
            placeDto.setIsFavorite(isFavorite);
        } else {
            placeDto.setIsFavorite(false);
        }
        return placeDto;
    }

    public Place findPlace(int placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }

    public Double findPlaceScore(int placeId) {
        return placeScoreRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new).getScore();
    }

    public List<PlaceDto> getTopFavoritePlaces(Integer userId) {
        List<Object[]> results = placeRepository.findTopFavoritePlaces();
        return results.stream()
                .map(row -> {
                    PlaceDto dto = PlaceDtoMapper.convertToPlaceDto(row);
                    dto.setIsFavorite(userId != null && checkIfUserFavoritedPlace(dto.getPlaceId(), userId));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<PlaceDto> getTopScoredPlacesWithinRadius(Double latitude, Double longitude, Integer userId) {
        List<Object[]> results = placeRepository.findTopScoredPlacesWithinRadius(latitude, longitude);
        return results.stream()
                .map(row -> {
                    PlaceDto dto = PlaceDtoMapper.convertToPlaceDto(row);
                    dto.setIsFavorite(userId != null && checkIfUserFavoritedPlace(dto.getPlaceId(), userId));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<NearestPlaceDto> getNearestPlaces(Double latitude, Double longitude, Integer userId) {
        return placeRepository.findNearestPlaces(latitude, longitude, userId)
                .stream()
                .map(NearestPlaceDtoMapper::convertToNearestPlaceDto)
                .toList();
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
            Double score = (Double) arr[17];
            String keywordsStr = (String) arr[18];
            Long reviewCount = (Long) arr[19];
            String imageUrl=(String) arr[20];
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
                    weightLimit, parking, indoor, outdoor, score, keywords, reviewCount,imageUrl
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
        redisLocationRepository.saveLocation(userId, latitude , longitude , MyPlace,placeArr);
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
        logger.info("Scheduled task started.");

        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        List<Integer> placeIds = reviewRepository.findDistinctPlaceIdsByUpdatedAtAfter(lastDay);

        if (placeIds.isEmpty()) {
            logger.info("No places with updated reviews found. Skipping summary generation.");
            return;
        }

        logger.info("Place IDs to update: {}", placeIds);

        for (int placeId : placeIds) {
            logger.info("Generating review summary for placeId: {}", placeId);
            generateReviewSummary(placeId);
        }

        logger.info("Scheduled task completed.");
    }

    public void generateReviewSummary(int placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("Place not found with id: " + placeId));

        ReviewSummary existingSummary = reviewSummaryRepository.findById(placeId).orElse(null);

        String existingGoodSummary = existingSummary != null ? existingSummary.getGoodSummary() : "";
        String existingBadSummary = existingSummary != null ? existingSummary.getBadSummary() : "";


        List<String> recentReviewContents = reviewRepository.findByPlace_PlaceId(placeId).stream()
                .map(Review::getContent)
                .filter(this::isValidReview)
                .collect(Collectors.toList());

        if (recentReviewContents.size() > 100) {
            recentReviewContents = getRandomReviews(recentReviewContents, 100);
        }


        List<String> validReviews = new ArrayList<>();
        for (String review : recentReviewContents) {
            String pros = openAiService.summarizePros(review);
            String cons = openAiService.summarizeCons(review);

            if (!isInvalidAiResponse(pros) && !isInvalidAiResponse(cons)) {
                validReviews.add(review);
            } else {
                logger.info("Excluding invalid review: {}", review);
            }
        }

        if (validReviews.isEmpty() && existingSummary == null) {
            logger.info("No valid reviews or existing summary found for placeId: {}. Creating default summary.", placeId);
            saveDefaultSummary(place);
            return;
        }

        String combinedGoodContent = existingGoodSummary + " " + String.join(" ", validReviews);
        String combinedBadContent = existingBadSummary + " " + String.join(" ", validReviews);

        String pros = openAiService.summarizePros(combinedGoodContent);
        String cons = openAiService.summarizeCons(combinedBadContent);

        if (pros == null || pros.trim().isEmpty()) {
            pros = "장점 요약을 생성할 수 없습니다.";
        }
        if (cons == null || cons.trim().isEmpty()) {
            cons = "단점 요약을 생성할 수 없습니다.";
        }

        if (existingSummary == null) {
            saveNewSummary(place, pros, cons);
        } else {
            updateExistingSummary(existingSummary, pros, cons);
        }
    }

    private void saveDefaultSummary(Place place) {
        ReviewSummary newSummary = new ReviewSummary();
        newSummary.setPlace(place);
        newSummary.setGoodSummary("리뷰 데이터가 없습니다.");
        newSummary.setBadSummary("리뷰 데이터가 없습니다.");
        newSummary.setUpdateDate(LocalDateTime.now());
        reviewSummaryRepository.save(newSummary);
    }

    private void saveNewSummary(Place place, String pros, String cons) {
        ReviewSummary newSummary = new ReviewSummary();
        newSummary.setPlace(place);
        newSummary.setGoodSummary(pros);
        newSummary.setBadSummary(cons);
        newSummary.setUpdateDate(LocalDateTime.now());
        reviewSummaryRepository.save(newSummary);
    }

    private void updateExistingSummary(ReviewSummary summary, String pros, String cons) {
        summary.setGoodSummary(pros);
        summary.setBadSummary(cons);
        summary.setUpdateDate(LocalDateTime.now());
        reviewSummaryRepository.save(summary);
    }


    private boolean isValidReview(String review) {
        return review.length() >= 3 && !Pattern.matches("^[^a-zA-Z0-9가-힣]*$", review) && !Pattern.matches("^(.)\\1+$", review);
    }


    private boolean isInvalidAiResponse(String response) {
        if (response == null) {
            return true;
        }
        String lowerCaseResponse = response.trim().toLowerCase();
        return lowerCaseResponse.contains("요약할 수 없습니다") ||
                lowerCaseResponse.contains("구체적인 내용이 없어") ||
                lowerCaseResponse.contains("긍정적인 점을")||
                lowerCaseResponse.contains("명확하지 않아");
    }


    private List<String> getRandomReviews(List<String> reviews, int limit) {
        Collections.shuffle(reviews);
        return reviews.subList(0, limit);
    }

    public Place savePlace(Place place) {
        PlaceScore placeScore = new PlaceScore();
        placeScore.setPlace(place);
        place.setPlaceScores(placeScore);
        Place savedPlace = placeRepository.save(place);
        return savedPlace;
    }
    public void savePlaceMedia(Place place, String imagePath) {
        PlaceMedia placeMedia = PlaceMedia.builder().place(place).path(imagePath).build();
        placeMediaRepository.save(placeMedia);
    }
}
