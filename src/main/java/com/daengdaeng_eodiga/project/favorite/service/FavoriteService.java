package com.daengdaeng_eodiga.project.favorite.service;

import com.daengdaeng_eodiga.project.Global.enums.OpenHoursType;
import com.daengdaeng_eodiga.project.Global.exception.*;
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

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final OpeningDateRepository openingDateRepository;

    public FavoriteResponseDto registerFavorite(int userId, FavoriteRequestDto favoriteRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        int placeId = favoriteRequestDto.getPlaceId();
        if ( !favoriteRepository.findByUser_UserIdAndPlace_PlaceId(userId, placeId).isEmpty() ) {
            throw new DuplicateFavoriteException();
        }

        Place place = placeRepository.findById(favoriteRequestDto.getPlaceId()).orElseThrow(PlaceNotFoundException::new);

        Favorite favorite = Favorite.builder()
                .user(user)
                .place(place)
                .build();
        favoriteRepository.save(favorite);
        
        return makeFavoriteResponseDto(place, favorite);
    }

    public void deleteFavorite(int favoriteId) {
        if (!favoriteRepository.existsById(favoriteId)) {
            throw new FavoriteNotFoundException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    public Page<FavoriteResponseDto> fetchFavoriteList(int userId, Pageable pageable) {
        Page<Favorite> favoritesPage = favoriteRepository.findByUser_UserIdOrderByUpdatedAtDesc(userId, pageable);

        return favoritesPage.map(favorite -> {
            Place place = placeRepository.findById(favorite.getPlace().getPlaceId())
                    .orElseThrow(PlaceNotFoundException::new);

            return makeFavoriteResponseDto(place, favorite);
        });
    }

    /**
     * 즐겨찾기 응답 dto 생성 메소드
     * @param place
     * @param favorite
     * @return FavoriteResponseDto
     */
    private FavoriteResponseDto makeFavoriteResponseDto(Place place, Favorite favorite) {
        
        OpeningDate openingDate = openingDateRepository.findByPlace_PlaceId(place.getPlaceId())
                .stream()
                .findFirst()
                .orElseThrow(OpeningDateNotFoundException::new);

        String startTime = openingDate != null ? openingDate.getStartTime() : OpenHoursType.NO_INFO.getDescription();
        String endTime = openingDate != null ? openingDate.getEndTime() : OpenHoursType.NO_INFO.getDescription();

        return FavoriteResponseDto.builder()
                .favoriteId(favorite.getFavoriteId())
                .placeId(place.getPlaceId())
                .name(place.getName())
                .streetAddresses(place.getStreetAddresses())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}