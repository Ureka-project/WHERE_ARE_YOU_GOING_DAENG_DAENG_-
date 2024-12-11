package com.daengdaeng_eodiga.project.review.service;

import com.daengdaeng_eodiga.project.Global.exception.RealtimeReviewException;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import com.daengdaeng_eodiga.project.review.dto.RealTimeReviewRequest;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RealtimeReviewService {

    private final UserService userService;
    private final PlaceService placeService;

    public void checkRealtimeReviewEligibility(RealTimeReviewRequest request, int userId) {
        User user = userService.findUser(userId);
        Place place = placeService.findPlace(request.getPlaceId());


        double distance = calculateDistance(
                request.getLatitude(),
                request.getLongitude(),
                place.getLatitude(),
                place.getLongitude()
        );

        if (distance > 1) {
            throw new RealtimeReviewException("실시간 리뷰를 작성할 수 없습니다. 장소를 확인해주세요.");
        }
    }


    private double calculateDistance(double userLat, double userLon, double placeLat, double placeLon) {
        final int EARTH_RADIUS = 6371;

        double latDistance = Math.toRadians(placeLat - userLat);
        double lonDistance = Math.toRadians(placeLon - userLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(placeLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
