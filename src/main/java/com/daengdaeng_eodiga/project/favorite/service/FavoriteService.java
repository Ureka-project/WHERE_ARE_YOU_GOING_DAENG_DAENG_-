package com.daengdaeng_eodiga.project.favorite.service;

import com.daengdaeng_eodiga.project.Global.enums.OpenHoursType;
import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.favorite.repository.FavoriteRepository;
import com.daengdaeng_eodiga.project.place.entity.OpeningDate;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.entity.PlaceMedia;
import com.daengdaeng_eodiga.project.place.repository.OpeningDateRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceMediaRepository;
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

    public Page<FavoriteResponseDto> fetchFavoriteList(int userId, Pageable pageable) {
        Page<Object[]> favoritesPage = favoriteRepository.findFavoriteResponse(userId, pageable);
        return favoritesPage.map(favorite -> makeFetchFavoriteDto(favorite));
    }

    /**
     * 즐겨찾기 조회 시, 응답 DTO 생성 메소드
     * @param favorite
     * @return FavoriteResponseDto
     */
    private FavoriteResponseDto makeFetchFavoriteDto(Object[] favorite) {

        String startTime = favorite[8] != null ? (String) favorite[8] : OpenHoursType.NO_INFO.getDescription();
        String endTime = favorite[9] != null ? (String) favorite[9] : OpenHoursType.NO_INFO.getDescription();

        return FavoriteResponseDto.builder()
                .favoriteId((Integer) favorite[0])
                .placeId((Integer) favorite[1])
                .name((String) favorite[2])
                .placeImage((String) favorite[3])
                .placeType(commonCodeService.getCommonCodeName((String) favorite[4]))
                .streetAddresses((String) favorite[5])
                .latitude((Double) favorite[6])
                .longitude((Double) favorite[7])
                .startTime(startTime)
                .endTime(endTime)
                .build();
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
                .build();
    }
}