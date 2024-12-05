package com.daengdaeng_eodiga.project.banner.service;

import com.daengdaeng_eodiga.project.Global.exception.PlaceNotFoundException;
import com.daengdaeng_eodiga.project.banner.dto.BannerListDto;
import com.daengdaeng_eodiga.project.event.entity.Event;
import com.daengdaeng_eodiga.project.event.repository.EventRepository;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    public Page<BannerListDto> fetchBannerList(Pageable pageable) {
        Page<Event> activeEvents = eventRepository.findActiveEvents(LocalDate.now(), pageable);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return activeEvents.map(event -> {
            Place place = placeRepository.findByPlaceId(Integer.parseInt(event.getPlace()))
                    .orElseThrow(PlaceNotFoundException::new);

            return BannerListDto.builder()
                    .eventId(event.getEventId())
                    .eventName(event.getEventName())
                    .eventImage(event.getEventImage())
                    .placeName(place.getName())
                    .eventDescription(event.getEventDescription())
                    .startDate(event.getStartDate().format(formatter))
                    .endDate(event.getEndDate().format(formatter))
                    .build();
        });
    }
}