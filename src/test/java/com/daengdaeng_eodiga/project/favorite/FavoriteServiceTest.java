package com.daengdaeng_eodiga.project.favorite;

import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteRequestDto;
import com.daengdaeng_eodiga.project.favorite.dto.FavoriteResponseDto;
import com.daengdaeng_eodiga.project.favorite.entity.Favorite;
import com.daengdaeng_eodiga.project.favorite.repository.FavoriteRepository;
import com.daengdaeng_eodiga.project.favorite.service.FavoriteService;
import com.daengdaeng_eodiga.project.place.entity.OpeningDate;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.entity.PlaceMedia;
import com.daengdaeng_eodiga.project.place.repository.OpeningDateRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceMediaRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.daengdaeng_eodiga.project.place.entity.QOpeningDate.openingDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OpeningDateRepository openingDateRepository;

    @Mock
    private PlaceMediaRepository placeMediaRepository;

    @Mock
    private CommonCodeService commonCodeService;

    @InjectMocks
    private FavoriteService favoriteService;

    private User sampleUser;
    private Place samplePlace;
    private Favorite sampleFavorite;
    private PlaceMedia samplePlaceMedia;
    private OpeningDate sampleOpeningDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = User.builder()
                .userId(1)
                .nickname("user1")
                .email("user1@example.com")
                .gender("GND_01")
                .city("광주")
                .cityDetail("광산구")
                .oauthProvider("google")
                .build();

        samplePlace = Place.builder()
                .name("Sample Place")
                .latitude(35.12345)
                .longitude(127.56789)
                .placeType("PLACE_TYPE")
                .streetAddresses("123 Sample St")
                .thumbImgPath("https://example.com/thumb.png")
                .build();
        samplePlace.setPlaceId(1);

        samplePlaceMedia = PlaceMedia.builder()
                .place(samplePlace)
                .path("https://example.com/place.png")
                .build();

        sampleOpeningDate = OpeningDate.builder()
                .dayType("매주 일요일 휴무")
                .startTime("9:00")
                .endTime("17:00")
                .place(samplePlace)
                .build();
        sampleOpeningDate.setOpeningDateId(1);

        sampleFavorite =  Favorite.builder()
                .user(sampleUser)
                .place(samplePlace)
                .build();
        sampleFavorite.setFavoriteId(1);
        sampleFavorite.setCreatedAt(LocalDateTime.now());
        sampleFavorite.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testRegisterFavorite_Success() {
        FavoriteRequestDto requestDto = new FavoriteRequestDto(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(favoriteRepository.findByUser_UserIdAndPlace_PlaceId(1, 1)).thenReturn(new ArrayList<>());
        when(placeRepository.findById(1)).thenReturn(Optional.of(samplePlace));
        when(placeMediaRepository.findByPlace_PlaceId(1)).thenReturn(Optional.of(samplePlaceMedia));
        when(openingDateRepository.findByPlace_PlaceId(1)).thenReturn(List.of(sampleOpeningDate));

        FavoriteResponseDto result = favoriteService.registerFavorite(1, requestDto);

        assertNotNull(result);
        assertEquals(1, result.getPlaceId());
        assertEquals("Sample Place", result.getName());
        assertEquals("https://example.com/place.png", result.getPlaceImage());

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }


    @Test
    void testRegisterFavorite_Duplicate() {
        FavoriteRequestDto requestDto = new FavoriteRequestDto(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(favoriteRepository.findByUser_UserIdAndPlace_PlaceId(1, 1)).thenReturn(List.of(sampleFavorite));

        assertThrows(DuplicateFavoriteException.class, () -> favoriteService.registerFavorite(1, requestDto));
    }

    @Test
    void testRegisterFavorite_UserNotFound() {
        FavoriteRequestDto requestDto = new FavoriteRequestDto(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> favoriteService.registerFavorite(1, requestDto));
    }

    @Test
    void testRegisterFavorite_PlaceNotFound() {
        FavoriteRequestDto requestDto = new FavoriteRequestDto(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(favoriteRepository.findByUser_UserIdAndPlace_PlaceId(1, 1)).thenReturn(Collections.emptyList());
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PlaceNotFoundException.class, () -> favoriteService.registerFavorite(1, requestDto));
    }

    @Test
    void testDeleteFavorite_Success() {
        when(favoriteRepository.existsById(1)).thenReturn(true);

        favoriteService.deleteFavorite(1);

        verify(favoriteRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteFavorite_FavoriteNotFound() {
        when(favoriteRepository.existsById(1)).thenReturn(false);

        assertThrows(FavoriteNotFoundException.class, () -> favoriteService.deleteFavorite(1));
    }

    @Test
    void testFetchFavoriteList_Success() {
        LocalDateTime now = LocalDateTime.now();
        when(favoriteRepository.findCustomFavorites(1, now, 0, 10)).thenReturn(Collections.emptyList());

        List<FavoriteResponseDto> result = favoriteService.fetchFavoriteList(1, now, 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(favoriteRepository, times(1)).findCustomFavorites(1, now, 0, 10);
    }
}
