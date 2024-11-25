package com.daengdaeng_eodiga.project.favorite.service;

import com.daengdaeng_eodiga.project.Global.enums.DayType;
import com.daengdaeng_eodiga.project.Global.enums.OpenHoursType;
import com.daengdaeng_eodiga.project.Global.exception.DayTypeNotFoundException;
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

        DayType today = getTodayDayType();

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

    private DayType getTodayDayType() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY: return DayType.MONDAY;
            case TUESDAY: return DayType.TUESDAY;
            case WEDNESDAY: return DayType.WEDNESDAY;
            case THURSDAY: return DayType.THURSDAY;
            case FRIDAY: return DayType.FRIDAY;
            case SATURDAY: return DayType.SATURDAY;
            case SUNDAY: return DayType.SUNDAY;
            default: throw new DayTypeNotFoundException();
        }
    }

    private String getOpenHours(int placeId, DayType today) {
        List<OpeningDate> openingDates = openingDateRepository.findByPlace_PlaceId(placeId);

        for (OpeningDate openingDate : openingDates) {
            if (DayType.EVERYDAY.getValue().equals(openingDate.getDayType())) {
                return openingDate.getStartTime() + " - " + openingDate.getEndTime();
            }

            if (openingDate.getDayType().contains(today.getValue())) {
                return OpenHoursType.TODAY_OFF.getDescription();
            }
        }

        for (OpeningDate openingDate : openingDates) {
            if (!openingDate.getDayType().contains(today.getValue())) {
                return openingDate.getStartTime() + " - " + openingDate.getEndTime();
            }
        }

        return OpenHoursType.NO_INFO.getDescription();
    }
}