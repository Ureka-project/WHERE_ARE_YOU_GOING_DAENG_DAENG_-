package com.daengdaeng_eodiga.project.banner;

import com.daengdaeng_eodiga.project.banner.dto.BannersDto;
import com.daengdaeng_eodiga.project.banner.service.BannerService;
import com.daengdaeng_eodiga.project.event.entity.Event;
import com.daengdaeng_eodiga.project.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BannerServiceTest {

    @InjectMocks
    private BannerService bannerService;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchBanners_ShouldReturnListOfBannersDto() {
        Event event1 = Event.builder()
                .eventName("Event 1")
                .eventImage("https://example.com/event1.png")
                .eventDescription("Description 1")
                .placeName("Place 1")
                .placeAddress("Address 1")
                .startDate(LocalDate.of(2024, 12, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .build();
        event1.setEventId(1);

        Event event2 = Event.builder()
                .eventName("Event 2")
                .eventImage("https://example.com/event2.png")
                .eventDescription("Description 2")
                .placeName("Place 2")
                .placeAddress("Address 2")
                .startDate(LocalDate.of(2024, 11, 1))
                .endDate(LocalDate.of(2024, 11, 30))
                .build();
        event2.setEventId(2);

        when(eventRepository.findActiveEvents(any(LocalDate.class))).thenReturn(Arrays.asList(event1, event2));

        List<BannersDto> result = bannerService.fetchBanners();

        assertNotNull(result);
        assertEquals(2, result.size());

        BannersDto banner1 = result.get(0);
        assertEquals(1, banner1.getEventId());
        assertEquals("Event 1", banner1.getEventName());
        assertEquals("https://example.com/event1.png", banner1.getEventImage());
        assertEquals("Description 1", banner1.getEventDescription());
        assertEquals("Place 1", banner1.getPlaceName());
        assertEquals("Address 1", banner1.getPlaceAddress());
        assertEquals("2024-12-01", banner1.getStartDate());
        assertEquals("2024-12-31", banner1.getEndDate());

        BannersDto banner2 = result.get(1);
        assertEquals(2, banner2.getEventId());
        assertEquals("Event 2", banner2.getEventName());
        assertEquals("https://example.com/event2.png", banner2.getEventImage());
        assertEquals("Description 2", banner2.getEventDescription());
        assertEquals("Place 2", banner2.getPlaceName());
        assertEquals("Address 2", banner2.getPlaceAddress());
        assertEquals("2024-11-01", banner2.getStartDate());
        assertEquals("2024-11-30", banner2.getEndDate());

        verify(eventRepository, times(1)).findActiveEvents(any(LocalDate.class));
    }
}
