package com.daengdaeng_eodiga.project.story;

import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisStoryRepository;
import com.daengdaeng_eodiga.project.Global.exception.*;
import com.daengdaeng_eodiga.project.pet.entity.Pet;
import com.daengdaeng_eodiga.project.pet.repository.PetRepository;
import com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog;
import com.daengdaeng_eodiga.project.region.repository.RegionOwnerLogRepository;
import com.daengdaeng_eodiga.project.story.dto.*;
import com.daengdaeng_eodiga.project.story.entity.Story;
import com.daengdaeng_eodiga.project.story.entity.StoryView;
import com.daengdaeng_eodiga.project.story.repository.StoryRepository;
import com.daengdaeng_eodiga.project.story.repository.StoryViewRepository;
import com.daengdaeng_eodiga.project.story.service.StoryService;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private StoryViewRepository storyViewRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private RegionOwnerLogRepository regionOwnerLogRepository;

    @Mock
    private UserService userService;

    @Mock
    private RedisStoryRepository redisStoryRepository;

    @InjectMocks
    private StoryService storyService;

    private Story sampleStory;
    private User sampleUser;
    private RegionOwnerLog sampleRegionOwnerLog;
    private Pet samplePet;

    @BeforeEach
    void setUp() throws ParseException {
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = dateFormat.parse("2020-12-24");
        samplePet = Pet.builder()
                .image("https://example.com/pet1.png")
                .size("PET_SIZ_01")
                .user(sampleUser)
                .birthday(birthday)
                .gender("GND_02")
                .name("user1Pet")
                .neutering(true)
                .species("PET_TYP_10")
                .build();

        sampleRegionOwnerLog = RegionOwnerLog.builder()
                .user(sampleUser)
                .city("광주")
                .cityDetail("광산구")
                .count(10)
                .build();

        sampleStory = Story.builder()
                .user(sampleUser)
                .city("광주")
                .cityDetail("광산구")
                .path("https://example.com/path/review1.png")
                .createdAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(24))
                .build();
        sampleStory.setStoryId(1);
    }

    @Test
    void testRegisterStory_Success() {
        StoryRequestDto storyRequestDto = new StoryRequestDto("https://example.com/path/review1.png","광주", "광산구");
        when(storyRepository.countByTodayCreated(anyInt(), any(), any())).thenReturn(0L);
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[] {
                1,
                sampleUser,
                "광주",
                "광산구",
                10
        });
        when(regionOwnerLogRepository.findByUserIdAndCityAndCityDetailForUpload(1, "광주", "광산구")).thenReturn(mockResult);
        when(userService.findUser(1)).thenReturn(sampleUser);
        List<Pet> mockResult2 = new ArrayList<>();
        mockResult2.add(samplePet);
        when(petRepository.findAllByUser(sampleUser)).thenReturn(mockResult2);

        storyService.registerStory(1, storyRequestDto);

        verify(storyRepository, times(1)).save(any(Story.class));
        verify(redisStoryRepository, times(1)).saveStory(anyString(), anyString(), anyInt(), anyInt(), any(RedisGroupedUserStoriesDto.class));
    }

    @Test
    void testRegisterStory_DailyLimitExceeded() {
        StoryRequestDto storyRequestDto = new StoryRequestDto("광주", "광산구", "https://example.com/path/review1.png");
        when(storyRepository.countByTodayCreated(anyInt(), any(), any())).thenReturn(10L);

        assertThrows(DailyStoryUploadLimitException.class, () -> storyService.registerStory(1, storyRequestDto));
    }

    @Test
    void testRegisterStory_OwnerHistoryNotFound() {
        StoryRequestDto storyRequestDto = new StoryRequestDto("광주", "광산구", "/sample/path");
        when(storyRepository.countByTodayCreated(anyInt(), any(), any())).thenReturn(0L);
        when(regionOwnerLogRepository.findByUserIdAndCityAndCityDetailForUpload(anyInt(), anyString(), anyString())).thenReturn(Collections.emptyList());

        assertThrows(OwnerHistoryNotFoundException.class, () -> storyService.registerStory(1, storyRequestDto));
    }

    @Test
    void testFetchGroupedUserStories_Success() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[] {
                1,
                "user1",
                "광주",
                "광산구",
                "https://example.com/pet1.png",
                "unviewed"
        });
        when(storyRepository.findMainPriorityStories(1)).thenReturn(mockResult);

        List<GroupedUserStoriesDto> result = storyService.fetchGroupedUserStories(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getNickname());
        verify(storyRepository, times(1)).findMainPriorityStories(1);
    }

    @Test
    void testFetchMyStories_Success() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[] {
                "user1",
                1,
                "광주",
                "광산구",
                "https://example.com/path/review1.png"
        });
        when(storyRepository.findMyActiveStoriesByUserId(1)).thenReturn(mockResult);

        MyStoriesDto result = storyService.fetchMyStories(1);

        assertNotNull(result);
        assertEquals("user1", result.getNickname());
        assertEquals(1, result.getContent().size());
        verify(storyRepository, times(1)).findMyActiveStoriesByUserId(1);
    }

    @Test
    void testFetchMyStories_NotFound() {
        when(storyRepository.findMyActiveStoriesByUserId(1)).thenReturn(Collections.emptyList());

        assertThrows(UserStoryNotFoundException.class, () -> storyService.fetchMyStories(1));
    }

    @Test
    void testViewStory_Success() {
        when(storyRepository.findByStoryId(1)).thenReturn(Optional.of(sampleStory));
        when(userService.findUser(1)).thenReturn(sampleUser);

        storyService.viewStory(1, 1);

        verify(storyViewRepository, times(1)).save(any(StoryView.class));
    }

    @Test
    void testViewStory_StoryNotFound() {
        when(storyRepository.findByStoryId(1)).thenReturn(Optional.empty());

        assertThrows(UserStoryNotFoundException.class, () -> storyService.viewStory(1, 1));
    }

    @Test
    void testDeleteStory_Success() {
        when(storyRepository.findByStoryId(1)).thenReturn(Optional.of(sampleStory));

        storyService.deleteStory(1);

        verify(storyRepository, times(1)).deleteById(1);
        verify(redisStoryRepository, times(1)).deleteStory(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void testDeleteStory_StoryNotFound() {
        when(storyRepository.findByStoryId(1)).thenReturn(Optional.empty());

        assertThrows(UserStoryNotFoundException.class, () -> storyService.deleteStory(1));
    }
}
