package com.daengdaeng_eodiga.project.story.service;

import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.region.repository.RegionOwnerLogRepository;
import com.daengdaeng_eodiga.project.story.dto.*;
import com.daengdaeng_eodiga.project.story.entity.Story;
import com.daengdaeng_eodiga.project.story.entity.StoryView;
import com.daengdaeng_eodiga.project.story.entity.StoryViewId;
import com.daengdaeng_eodiga.project.story.repository.StoryRepository;
import com.daengdaeng_eodiga.project.story.repository.StoryViewRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final StoryViewRepository storyViewRepository;
    private final RegionOwnerLogRepository regionOwnerLogRepository;
    private final UserService userService;

    public UserMyLandsDto fetchUserLands(int userId) {

        String nickname = userService.findUser(userId).getNickname();
        List<Object[]> results = regionOwnerLogRepository.findCityAndCityDetailByUserId(userId);
        if( results.isEmpty() ) {
            throw new UserLandNotFoundException();
        }

        Map<String, List<String>> myLands = new LinkedHashMap<>();
        for (Object[] row : results) {
            String city = (String) row[0];
            String cityDetail = (String) row[1];

            myLands.computeIfAbsent(city, k -> new ArrayList<>()).add(cityDetail);
        }
        List<MyLandsDto> myLandsDtos = myLands.entrySet().stream()
                .map(entry -> new MyLandsDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        UserMyLandsDto userMyLandsDto = UserMyLandsDto.builder()
                .nickname(nickname)
                .lands(myLandsDtos)
                .build();
        return userMyLandsDto;
    }

    public void registerStory(int userId, StoryRequestDto storyRequestDto){
        if( storyRepository.countByTodayCreated() == 10 ) {
            throw new DailyStoryUploadLimitException();
        }
        if( regionOwnerLogRepository.findByUserIdAndCityAndCityDetail(
                userId,
                storyRequestDto.getCity(),
                storyRequestDto.getCityDetail()).isEmpty() ){
            throw new OwnerHistoryNotFoundException();
        }

        User user = userService.findUser(userId);
        Story story = Story.builder()
                .user(user)
                .path(storyRequestDto.getPath())
                .city(storyRequestDto.getCity())
                .cityDetail(storyRequestDto.getCityDetail())
                .createdAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(24))
                .build();
        storyRepository.save(story);
    }

    // TODO(3) : 전체 유저 스토리 목록 조회 (내스토리는 제외)
    public List<GroupedUserStoriesDto> fetchGroupedUserStories(){

        return null;
    }

    public MyStoriesDto fetchMyStories(int userId) {
        List<Object[]> results = storyRepository.findMyActiveStoriesByUserId(userId);
        if( results.isEmpty() ) {
            throw new UserStoryNotFoundException();
        }

        String nickname = (String) results.get(0)[0];
        List<MyStoryContentDto> content = results.stream()
                .map(row -> MyStoryContentDto.builder()
                        .storyId((Integer) row[1])
                        .city((String) row[2])
                        .cityDetail((String) row[3])
                        .path((String) row[4])
                        .build())
                .collect(Collectors.toList());

        return MyStoriesDto.builder()
                .nickname(nickname)
                .content(content)
                .build();
    }

    public IndividualUserStoriesDto fetchIndividualUserStories(int landOwnerId, String city, String cityDetail){
        if( regionOwnerLogRepository.findByUserIdAndCityAndCityDetail(landOwnerId, city, cityDetail).isEmpty() ) {
            throw new OwnerHistoryNotFoundException();
        }

        List<Object[]> results = storyRepository.findActiveStoriesByLandOwnerId(landOwnerId, city, cityDetail);
        if( results.isEmpty() ) {
            throw new UserStoryNotFoundException();
        }

        String nickname = (String) results.get(0)[1];
        List<IndividualStoryContentDto> content = results.stream()
                .map(row -> IndividualStoryContentDto.builder()
                        .storyId((Integer) row[0])
                        .path((String) row[2])
                        .build())
                .collect(Collectors.toList());

        return IndividualUserStoriesDto.builder()
                .nickname(nickname)
                .city(city)
                .cityDetail(cityDetail)
                .content(content)
                .build();
    }

    public void viewStory(int storyId, int userId){
        StoryViewId storyViewId = StoryViewId.builder()
                .userId(userId)
                .storyId(storyId)
                .build();
        Story story = storyRepository.findByStoryId(storyId).orElseThrow(UserStoryNotFoundException::new);
        User user = userService.findUser(userId);
        if( user == null ) {
            throw new UserNotFoundException();
        }

        StoryView storyView = StoryView.builder()
                .storyViewId(storyViewId)
                .story(story)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        storyViewRepository.save(storyView);
    }

    public void deleteStory(int storyId){
        storyRepository.deleteById(storyId);
    }
}
