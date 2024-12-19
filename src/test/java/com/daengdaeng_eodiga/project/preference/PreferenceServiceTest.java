package com.daengdaeng_eodiga.project.preference;

import com.daengdaeng_eodiga.project.Global.exception.CommonCodeNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.DuplicatePreferenceException;
import com.daengdaeng_eodiga.project.common.entity.CommonCode;
import com.daengdaeng_eodiga.project.common.entity.GroupCode;
import com.daengdaeng_eodiga.project.common.repository.CommonCodeRepository;
import com.daengdaeng_eodiga.project.common.repository.GroupCodeRepository;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceRequestDto;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceResponseDto;
import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.preference.entity.PreferenceId;
import com.daengdaeng_eodiga.project.preference.repository.PreferenceRepository;
import com.daengdaeng_eodiga.project.preference.service.PreferenceService;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreferenceServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private CommonCodeRepository commonCodeRepository;

    @Mock
    private GroupCodeRepository groupCodeRepository;

    @InjectMocks
    private PreferenceService preferenceService;

    private User sampleUser;
    private Preference samplePreference;
    private CommonCode sampleCommonCode;
    private GroupCode sampleGroupCode;

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

        sampleGroupCode = GroupCode.builder()
                .groupId("PLACE_TYP")
                .name("시설종류")
                .build();

        sampleCommonCode = CommonCode.builder()
                .codeId("PLACE_TYP_02")
                .name("카페")
                .groupCode(sampleGroupCode)
                .build();

        samplePreference = new Preference();
        samplePreference.setUser(sampleUser);
        samplePreference.setPreferenceType("PLACE_TYP");
        samplePreference.setId(new PreferenceId("PLACE_TYP_02", 1));
    }

    @Test
    void testRegisterPreference_Success() {
        PreferenceRequestDto requestDto = new PreferenceRequestDto("PLACE_TYP", Set.of("PLACE_TYP_02"));

        when(userService.findUser(1)).thenReturn(sampleUser);
        when(preferenceRepository.findByUser_UserIdAndPreferenceType(1, "PLACE_TYP")).thenReturn(Collections.emptyList());
        when(commonCodeRepository.findByGroupCode_GroupIdAndCodeIdIn("PLACE_TYP", Set.of("PLACE_TYP_02")))
                .thenReturn(List.of(sampleCommonCode));

        PreferenceResponseDto result = preferenceService.registerPreference(1, requestDto);

        assertNotNull(result);
        assertEquals("PLACE_TYP", result.getPreferenceInfo());
        assertEquals(1, result.getPreferenceTypes().size());
        assertTrue(result.getPreferenceTypes().contains("PLACE_TYP_02"));

        verify(preferenceRepository, times(1)).saveAll(anySet());
    }

    @Test
    void testRegisterPreference_Duplicate() {
        PreferenceRequestDto requestDto = new PreferenceRequestDto("PLACE_TYP", Set.of("PLACE_TYP_02"));

        when(userService.findUser(1)).thenReturn(sampleUser);
        when(preferenceRepository.findByUser_UserIdAndPreferenceType(1, "PLACE_TYP"))
                .thenReturn(List.of(samplePreference));

        assertThrows(DuplicatePreferenceException.class, () -> preferenceService.registerPreference(1, requestDto));
    }

    @Test
    void testRegisterPreference_CommonCodeNotFound() {
        PreferenceRequestDto requestDto = new PreferenceRequestDto("PLACE_TYP", Set.of("INVALID_CODE"));

        when(userService.findUser(1)).thenReturn(sampleUser);
        when(preferenceRepository.findByUser_UserIdAndPreferenceType(1, "PLACE_TYP")).thenReturn(Collections.emptyList());
        when(commonCodeRepository.findByGroupCode_GroupIdAndCodeIdIn("PLACE_TYP", Set.of("INVALID_CODE")))
                .thenReturn(Collections.emptyList());

        assertThrows(CommonCodeNotFoundException.class, () -> preferenceService.registerPreference(1, requestDto));
    }

    @Test
    void testUpdatePreference_Success() {
        PreferenceRequestDto requestDto = new PreferenceRequestDto("PLACE_TYP", Set.of("PLACE_TYP_02"));

        when(userService.findUser(1)).thenReturn(sampleUser);
        when(commonCodeRepository.findByGroupCode_GroupIdAndCodeIdIn("PLACE_TYP", Set.of("PLACE_TYP_02")))
                .thenReturn(List.of(sampleCommonCode));

        PreferenceResponseDto result = preferenceService.updatePreference(1, requestDto);

        assertNotNull(result);
        assertEquals("PLACE_TYP", result.getPreferenceInfo());
        assertTrue(result.getPreferenceTypes().contains("PLACE_TYP_02"));

        verify(preferenceRepository, times(1)).deleteByUserAndPreferenceType(sampleUser, "PLACE_TYP");
        verify(preferenceRepository, times(1)).saveAll(anySet());
    }

    @Test
    void testFetchPreferences_Success() {
        when(userService.findUser(1)).thenReturn(sampleUser);
        when(preferenceRepository.findByUser(sampleUser)).thenReturn(List.of(samplePreference));
        when(groupCodeRepository.findByGroupId("PLACE_TYP")).thenReturn(Optional.of(sampleGroupCode));
        when(commonCodeRepository.findByCodeId("PLACE_TYP_02")).thenReturn(Optional.of(sampleCommonCode));

        List<PreferenceResponseDto> result = preferenceService.fetchPreferences(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("시설종류", result.get(0).getPreferenceInfo());
        assertTrue(result.get(0).getPreferenceTypes().contains("카페"));

        verify(preferenceRepository, times(1)).findByUser(sampleUser);
    }

    @Test
    void testFetchPreferences_CommonCodeNotFound() {
        when(userService.findUser(1)).thenReturn(sampleUser);
        when(preferenceRepository.findByUser(sampleUser)).thenReturn(List.of(samplePreference));
        when(groupCodeRepository.findByGroupId("PLACE_TYP")).thenReturn(Optional.of(sampleGroupCode));
        when(commonCodeRepository.findByCodeId("PLACE_TYP_02")).thenReturn(Optional.empty());

        assertThrows(CommonCodeNotFoundException.class, () -> preferenceService.fetchPreferences(1));
    }
}
