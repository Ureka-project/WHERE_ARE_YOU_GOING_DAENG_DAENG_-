package com.daengdaeng_eodiga.project.place;


import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisLocationRepository;
import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.place.dto.PlaceDto;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import com.daengdaeng_eodiga.project.place.repository.PlaceScoreRepository;
import com.daengdaeng_eodiga.project.place.service.OpenAiService;
import com.daengdaeng_eodiga.project.place.service.PlaceService;
import com.daengdaeng_eodiga.project.preference.repository.PreferenceRepository;
import com.daengdaeng_eodiga.project.review.entity.Review;
import com.daengdaeng_eodiga.project.review.repository.ReviewRepository;
import com.daengdaeng_eodiga.project.review.repository.ReviewSummaryRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceScoreRepository placeScoreRepository;

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewSummaryRepository reviewSummaryRepository;

    @Mock
    private OpenAiService openAiService;

    @Mock
    private CommonCodeService commonCodeService;

    @Mock
    private RedisLocationRepository redisLocationRepository;

    @InjectMocks
    private PlaceService placeService;

    private Place samplePlace;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePlace = Place.builder()
                .name("Sample Place")
                .city("Seoul")
                .cityDetail("Gangnam")
                .township("Sinsa-dong")
                .latitude(37.5165)
                .longitude(127.0190)
                .streetAddresses("123 Sample Street")
                .telNumber("010-1234-5678")
                .url("http://sampleplace.com")
                .placeType("Cafe")
                .description("A nice place to relax.")
                .parking(true)
                .indoor(true)
                .outdoor(false)
                .thumbImgPath("sample.jpg")
                .build();
    }

    @Test
    void testGetPlaceDetails_Success() {
        when(placeRepository.findPlaceDetailsById(1)).thenReturn(Arrays.asList(new Object[][] {
                {
                        1, "Sample Place", "Seoul", "Gangnam", "Sinsa-dong", 37.5165, 127.0190,
                        "123 Sample Street", "010-1234-5678", "http://sampleplace.com",
                        "Cafe", "A nice place to relax.", true, true, false,
                        null, false, null, null, 0, 4.5, "sample.jpg"
                }
        }));



        PlaceDto result = placeService.getPlaceDetails(1, 1);

        assertNotNull(result);
        assertEquals("Sample Place", result.getName());
        verify(placeRepository, times(1)).findPlaceDetailsById(1);
    }

    @Test
    void testGetPlaceDetails_NotFound() {
        when(placeRepository.findPlaceDetailsById(1)).thenReturn(Collections.emptyList());

        assertThrows(PlaceNotFoundException.class, () -> placeService.getPlaceDetails(1, 1));
        verify(placeRepository, times(1)).findPlaceDetailsById(1);
    }

    @Test
    void testFindPlace_Success() {
        when(placeRepository.findById(1)).thenReturn(Optional.of(samplePlace));

        Place result = placeService.findPlace(1);

        assertNotNull(result);
        assertEquals("Sample Place", result.getName());
        verify(placeRepository, times(1)).findById(1);
    }

    @Test
    void testFindPlace_NotFound() {
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PlaceNotFoundException.class, () -> placeService.findPlace(1));
        verify(placeRepository, times(1)).findById(1);
    }

    @Test
    void testGenerateReviewSummary_ValidReviews() {
        User mockUser = mock(User.class);

        when(placeRepository.findById(1)).thenReturn(Optional.of(samplePlace));
        when(reviewRepository.findByPlace_PlaceId(1)).thenReturn(List.of(
                Review.builder().score(5).content("좋아요!").visitedAt(LocalDate.now()).place(samplePlace).user(mockUser).reviewtype("Positive").build(),
                Review.builder().score(2).content("별로예요!").visitedAt(LocalDate.now()).place(samplePlace).user(mockUser).reviewtype("Negative").build()
        ));

        placeService.generateReviewSummary(1);

        verify(reviewRepository, times(1)).findByPlace_PlaceId(1);
    }

    @Test
    void testSavePlace() {
        when(placeRepository.save(any(Place.class))).thenReturn(samplePlace);

        Place result = placeService.savePlace(samplePlace);

        assertNotNull(result);
        assertEquals("Sample Place", result.getName());
        verify(placeRepository, times(1)).save(samplePlace);
    }
}