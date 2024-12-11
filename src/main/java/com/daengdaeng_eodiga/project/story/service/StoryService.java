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

    /**
     * 유저 자신의 땅 목록 조회
     * @param userId
     * @return UserMyLandsDto
     * @deprecated
     */

    // TODO :  지역별 주인 조회 로직 완성 후, cityDetail Null 수정
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
                .map(entry -> new MyLandsDto(entry.getKey(), null))
                .collect(Collectors.toList());
        UserMyLandsDto userMyLandsDto = UserMyLandsDto.builder()
                .nickname(nickname)
                .lands(myLandsDtos)
                .build();
        return userMyLandsDto;
    }

    /**
     * 스토리 업로드
     * @param userId
     * @param storyRequestDto
     */
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

    /**
     * 본인 제외 전체 유저 스토리 목록 조회
     * @param userId
     * @return
     */
    public List<GroupedUserStoriesDto> fetchGroupedUserStories(int userId){
        List<Object[]> results = storyRepository.findMainPriorityStories(userId);

        return results.stream()
                .map(row -> GroupedUserStoriesDto.builder()
                        .landOwnerId((Integer) row[0])
                        .nickname((String) row[1])
                        .city((String) row[2])
                        .cityDetail((String) row[3])
                        .petImage((String) row[4])
                        .storyType((String) row[5])
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 내 스토리 목록 조회
     * @param userId
     * @return
     */
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

    /**
     * 유저별 스토리 상세목록 조회
     * @param landOwnerId
     * @param city
     * @param cityDetail
     * @return
     */
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

    /**
     * 스토리 확인
     * @param storyId
     * @param userId
     */
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

    /**
     * 스토리 삭제
     * @param storyId
     */
    public void deleteStory(int storyId){
        storyRepository.deleteById(storyId);
    }
}