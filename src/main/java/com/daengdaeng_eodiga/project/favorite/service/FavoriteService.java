package com.daengdaeng_eodiga.project.favorite.service;

import com.daengdaeng_eodiga.project.Global.exception.FavoriteNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.favorite.repository.FavoriteRepository;
import com.daengdaeng_eodiga.project.place.entity.OpeningDate;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.OpeningDateRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final OpeningDateRepository openingDateRepository;

    public void registerFavorite(int userId, FavoriteRequestDto favoriteRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Place place = placeRepository.findById(favoriteRequestDto.getPlaceId()).orElseThrow(PlaceNotFoundException::new);

        Favorite favorite = Favorite.builder()
                .user(user)
                .place(place)
                .build();

        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(int favoriteId) {
        if (!favoriteRepository.existsById(favoriteId)) {
            throw new FavoriteNotFoundException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    public Page<FavoriteResponseDto> fetchFavoriteList(int userId, Pageable pageable) {
        Page<Favorite> favoritesPage = favoriteRepository.findByUser_UserId(userId, pageable);

        String today = getTodayDayType();

        return favoritesPage.map(favorite -> {
            Place place = placeRepository.findById(favorite.getPlace().getPlaceId())
                    .orElseThrow(PlaceNotFoundException::new);

            String openHours = getOpenHours(favorite.getPlace().getPlaceId(), today);

            return FavoriteResponseDto.builder()
                    .favoriteId(favorite.getFavoriteId())
                    .placeId(place.getPlaceId())
                    .name(place.getName())
                    .streetAddresses(place.getStreetAddresses())
                    .latitude(place.getLatitude())
                    .longitude(place.getLongitude())
                    .openHours(openHours)
                    .build();
        });
    }

    private String getTodayDayType() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY: return "월";
            case TUESDAY: return "화";
            case WEDNESDAY: return "수";
            case THURSDAY: return "목";
            case FRIDAY: return "금";
            case SATURDAY: return "토";
            case SUNDAY: return "일";
            default: throw new IllegalStateException("요일을 찾지 못하였습니다.");
        }
    }

    private String getOpenHours(int placeId, String today) {
        List<OpeningDate> openingDates = openingDateRepository.findByPlace_PlaceId(placeId);

        for (OpeningDate openingDate : openingDates) {
            if ("연중무휴".equals(openingDate.getDayType())) {
                return openingDate.getStartTime() + " - " + openingDate.getEndTime();
            }

            if (openingDate.getDayType().contains(today)) {
                return "오늘 휴무";
            }
        }

        for (OpeningDate openingDate : openingDates) {
            if (!openingDate.getDayType().contains(today)) {
                return openingDate.getStartTime() + " - " + openingDate.getEndTime();
            }
        }

        return "정보 없음";
    }
}
