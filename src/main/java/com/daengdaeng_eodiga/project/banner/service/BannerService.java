package com.daengdaeng_eodiga.project.banner.service;

import com.daengdaeng_eodiga.project.Global.exception.BannerNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.banner.dto.BannerDetailDto;
import com.daengdaeng_eodiga.project.banner.dto.BannerListDto;
import com.daengdaeng_eodiga.project.event.entity.Event;
import com.daengdaeng_eodiga.project.event.repository.EventRepository;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    public List<BannerListDto> fetchBannerList() {
        List<Event> activeEvents = eventRepository.findActiveEvents(LocalDate.now());

        return activeEvents.stream()
                .map(event -> new BannerListDto(event.getEventId(), event.getEventImage()))
                .collect(Collectors.toList());
    }

    public BannerDetailDto fetchBannerDetail(int eventId) {
        Event eventDetail = eventRepository.findByEventId(eventId)
                .orElseThrow(BannerNotFoundException::new);

        Place place = placeRepository.findByPlaceId(
                Integer.parseInt(eventDetail.getPlace())).orElseThrow(PlaceNotFoundException::new);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return BannerDetailDto.builder()
                .eventImage(eventDetail.getEventImage())
                .eventName(eventDetail.getEventName())
                .placeName(place.getName())
                .eventDescription(eventDetail.getEventDescription())
                .startDate(eventDetail.getStartDate().format(formatter))
                .endDate(eventDetail.getEndDate().format(formatter))
                .build();
    }
}