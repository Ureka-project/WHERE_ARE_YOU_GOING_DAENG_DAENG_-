package com.daengdaeng_eodiga.project.preference.service;

import com.daengdaeng_eodiga.project.common.entity.CommonCode;
import com.daengdaeng_eodiga.project.common.entity.GroupCode;
import com.daengdaeng_eodiga.project.common.repository.CommonCodeRepository;
import com.daengdaeng_eodiga.project.common.repository.GroupCodeRepository;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceRequestDto;
import com.daengdaeng_eodiga.project.preference.dto.PreferenceResponseDto;
import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.preference.entity.PreferenceId;
import com.daengdaeng_eodiga.project.preference.repository.PreferenceRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final GroupCodeRepository groupCodeRepository;
    private final UserRepository userRepository;

    private final int hardcodedUserId = 1;
    // TODO : header 로부터 유저정보 가져오는 걸로 변경해야 함

    /**
     * 선호도 등록 서비스
     * @param preferenceRequestDto
     * @return PreferenceResponseDto
     */
    public PreferenceResponseDto registerPreference(PreferenceRequestDto preferenceRequestDto) {
        User user = findUser(hardcodedUserId);

        List<CommonCode> commonCodes = findCommonCode(
                findGroupId(preferenceRequestDto.getPreferenceInfo()),
                preferenceRequestDto.getPreferenceTypes());

        if (commonCodes.isEmpty()) {
            throw new IllegalArgumentException("No CommonCodes found for groupId: " + preferenceRequestDto.getPreferenceInfo() + " and names: " + preferenceRequestDto.getPreferenceTypes());
        }
        Set<Preference> preferences = createPreferences(commonCodes, hardcodedUserId, user);

        preferenceRepository.saveAll(preferences);
        return mapToDto(preferenceRequestDto.getPreferenceInfo(), preferences);
    }

    /**
     * user 조회 메소드
     * @param userId
     * @return User
     */
    private User findUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id [" + userId + "] not found"));
    }

    /**
     * 공통코드 조회 메소드
     * @param groupId
     * @param commonNames
     * @return List<CommonCode>
     */
    private List<CommonCode> findCommonCode(String groupId, Set<String> commonNames) {
        return commonCodeRepository.findByGroupCode_GroupIdAndNameIn(
                groupId, commonNames);
    }

    /**
     * 그룹코드 ID 조회 메소드
     * @param preferenceInfo
     * @return String
     */
    private String findGroupId(String preferenceInfo) {
        GroupCode groupCode = groupCodeRepository.findByName(preferenceInfo)
                .orElseThrow(() -> new IllegalArgumentException("No GroupCode found for name: " + preferenceInfo));
        return groupCode.getGroupId();
    }

    /**
     * 선호도 객체 생성 메소드
     * @param commonCodes
     * @param hardcodedUserId
     * @param user
     * @return Set<Preference>
     */
    private Set<Preference> createPreferences(List<CommonCode> commonCodes, int hardcodedUserId, User user) {
        return commonCodes.stream().map(code -> {
            PreferenceId preferenceId = new PreferenceId();
            preferenceId.setUserId(hardcodedUserId);
            preferenceId.setPreferenceInfo(code.getCodeId());

            Preference preference = new Preference();
            preference.setId(preferenceId);
            preference.setUser(user);
            preference.setPreferenceType(code.getGroupCode().getGroupId());
            return preference;
        }).collect(Collectors.toSet());
    }

    /**
     * 엔티티를 Dto로 변환하는 메소드
     * @param preferenceInfo
     * @param preferences
     * @return PreferenceResponseDto
     */
    private PreferenceResponseDto mapToDto(String preferenceInfo, Set<Preference> preferences) {
        PreferenceResponseDto dto = new PreferenceResponseDto();
        dto.setPreferenceInfo(preferenceInfo);
        dto.setPreferenceTypes(preferences.stream().map(preference -> preference.getId().getPreferenceInfo()).collect(Collectors.toSet()));
        return dto;
    }
}
