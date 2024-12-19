package com.daengdaeng_eodiga.project.favorite.service;

import com.daengdaeng_eodiga.project.Global.enums.OpenHoursType;
import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.favorite.entity.QFavorite;
import com.daengdaeng_eodiga.project.favorite.repository.FavoriteRepository;
import com.daengdaeng_eodiga.project.place.entity.*;
import com.daengdaeng_eodiga.project.place.entity.QOpeningDate;
import com.daengdaeng_eodiga.project.place.entity.QPlace;
import com.daengdaeng_eodiga.project.place.repository.OpeningDateRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceMediaRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final OpeningDateRepository openingDateRepository;
    private final PlaceMediaRepository placeMediaRepository;
    private final CommonCodeService commonCodeService;

    public FavoriteResponseDto registerFavorite(int userId, FavoriteRequestDto favoriteRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        int placeId = favoriteRequestDto.getPlaceId();
        if ( !favoriteRepository.findByUser_UserIdAndPlace_PlaceId(userId, placeId).isEmpty() ) {
            throw new DuplicateFavoriteException();
        }

        Place place = placeRepository.findById(favoriteRequestDto.getPlaceId()).orElseThrow(PlaceNotFoundException::new);
        String placeImage = placeMediaRepository.findByPlace_PlaceId(placeId)
                .map(PlaceMedia::getPath)
                .orElse("");

        Favorite favorite = Favorite.builder()
                .user(user)
                .place(place)
                .build();
        favoriteRepository.save(favorite);
        
        return makeRegisterFavoriteDto(place, placeImage, favorite);
    }

    public void deleteFavorite(int favoriteId) {
        if (!favoriteRepository.existsById(favoriteId)) {
            throw new FavoriteNotFoundException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    public List<FavoriteResponseDto> fetchFavoriteList(int userId, LocalDateTime lastUpdatedAt, int lastFavoriteId, int size) {
        return makeFetchFavoriteDto(favoriteRepository.findCustomFavorites(userId, lastUpdatedAt, lastFavoriteId, size));
    }

    /**
     * 즐겨찾기 조회 시, 응답 DTO 생성 메소드
     * @param favorites
     * @return FavoriteResponseDto
     */
    private List<FavoriteResponseDto> makeFetchFavoriteDto(List<Tuple> favorites) {
        return favorites.stream().map(favorite -> {

            String startTime = favorite.get(QOpeningDate.openingDate.startTime) != null
                    ? favorite.get(QOpeningDate.openingDate.startTime) : OpenHoursType.NO_INFO.getDescription();
            String endTime = favorite.get(QOpeningDate.openingDate.endTime) != null
                    ? favorite.get(QOpeningDate.openingDate.endTime) : OpenHoursType.NO_INFO.getDescription();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String updatedAt = (favorite.get(QFavorite.favorite.updatedAt)).format(formatter);

            return FavoriteResponseDto.builder()
                    .favoriteId(favorite.get(QFavorite.favorite.favoriteId))
                    .placeId(favorite.get(QPlace.place.placeId))
                    .name(favorite.get(QPlace.place.name))
                    .placeImage(favorite.get(QPlace.place.thumbImgPath))
                    .placeType(favorite.get(QPlace.place.placeType))
                    .streetAddresses(favorite.get(QPlace.place.streetAddresses))
                    .latitude(favorite.get(QPlace.place.latitude))
                    .longitude(favorite.get(QPlace.place.longitude))
                    .startTime(startTime)
                    .endTime(endTime)
                    .updatedAt(updatedAt)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 즐겨찾기 등록 시, 응답 dto 생성 메소드
     * @param place
     * @param favorite
     * @return FavoriteResponseDto
     */
    private FavoriteResponseDto makeRegisterFavoriteDto(Place place, String placeImage, Favorite favorite) {

        OpeningDate openingDate = openingDateRepository.findByPlace_PlaceId(place.getPlaceId())
                .stream()
                .findFirst()
                .orElseThrow(OpeningDateNotFoundException::new);
        String startTime = openingDate != null ? openingDate.getStartTime() : OpenHoursType.NO_INFO.getDescription();
        String endTime = openingDate != null ? openingDate.getEndTime() : OpenHoursType.NO_INFO.getDescription();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String updatedAt = favorite.getUpdatedAt() != null ? favorite.getUpdatedAt().format(formatter) : "N/A";

        return FavoriteResponseDto.builder()
                .favoriteId(favorite.getFavoriteId())
                .placeId(place.getPlaceId())
                .name(place.getName())
                .placeImage(placeImage)
                .placeType(commonCodeService.getCommonCodeName(place.getPlaceType()))
                .streetAddresses(place.getStreetAddresses())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .startTime(startTime)
                .endTime(endTime)
                .updatedAt(updatedAt)
                .build();
    }
}