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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 선호도 등록 서비스
     * @param preferenceRequestDto
     * @return PreferenceResponseDto
     */
    public PreferenceResponseDto registerPreference(int hardcodedUserId, PreferenceRequestDto preferenceRequestDto) {
        User user = findUser(hardcodedUserId);

        List<CommonCode> commonCodes = findCommonCode(
                preferenceRequestDto.getPreferenceInfo(),
                preferenceRequestDto.getPreferenceTypes());

        // TODO : 예외처리 가은님 코드로 통일하기
        if (commonCodes.isEmpty()) {
            throw new IllegalArgumentException("No CommonCodes found for groupId: " + preferenceRequestDto.getPreferenceInfo() + " and names: " + preferenceRequestDto.getPreferenceTypes());
        }
        Set<Preference> preferences = createPreferences(commonCodes, hardcodedUserId, user);
        preferenceRepository.saveAll(preferences);
        return mapToDto(preferenceRequestDto.getPreferenceInfo(), preferences);
    }

    /**
     * 선호도 갱신 서비스
     * @param preferenceRequestDto
     * @return PreferenceResponseDto
     */
    @Transactional
    public PreferenceResponseDto updatePreference(int hardcodedUserId, PreferenceRequestDto preferenceRequestDto) {
        User user = findUser(hardcodedUserId);

        preferenceRepository.deleteByUserAndPreferenceType(user, preferenceRequestDto.getPreferenceInfo());
        List<CommonCode> commonCodes = findCommonCode(
                preferenceRequestDto.getPreferenceInfo(),
                preferenceRequestDto.getPreferenceTypes());

        // TODO : 예외처리 가은님 코드로 통일하기
        if (commonCodes.isEmpty()) {
            throw new IllegalArgumentException("No CommonCodes found for groupId: " + preferenceRequestDto.getPreferenceInfo() + " and names: " + preferenceRequestDto.getPreferenceTypes());
        }
        Set<Preference> preferences = createPreferences(commonCodes, hardcodedUserId, user);
        preferenceRepository.saveAll(preferences);
        return mapToDto(preferenceRequestDto.getPreferenceInfo(), preferences);
    }

    /**
     * 선호도 조회 서비스
     * @param userId
     * @return List<PreferenceResponseDto>
     */
    @Transactional(readOnly = true)
    public List<PreferenceResponseDto> fetchPreferences(int userId) {
        User user = findUser(userId);
        List<Preference> preferences = preferenceRepository.findByUser(user);

        return preferences.stream()
                .collect(Collectors.groupingBy(preference -> preference.getPreferenceType()))
                .entrySet().stream()
                .map(entry -> {
                    String preferenceInfo = findGroupName(entry.getKey());
                    Set<String> preferenceTypes = entry.getValue().stream()
                            .map(preference -> findCommonCodeName(preference.getId().getPreferenceInfo()))
                            .collect(Collectors.toSet());
                    return new PreferenceResponseDto(preferenceInfo, preferenceTypes);
                })
                .collect(Collectors.toList());
    }

    /**
     * user 조회 메소드
     * @param userId
     * @return User
     */
    private User findUser(int userId) {
        // TODO : 예외처리 가은님 코드로 통일하기
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id [" + userId + "] not found"));
    }
    /**
     * 공통코드 조회 메소드
     * @param groupId
     * @param commonIds
     * @return List<CommonCode>
     */
    private List<CommonCode> findCommonCode(String groupId, Set<String> commonIds) {
        return commonCodeRepository.findByGroupCode_GroupIdAndCodeIdIn(
                groupId, commonIds);
    }
    /**
     * 그룹코드 이름 조회 메소드
     * @param groupId
     * @return String
     */
    private String findGroupName(String groupId) {
        // TODO : 예외처리 가은님 코드로 통일하기
        GroupCode groupCode = groupCodeRepository.findByGroupId(groupId)
                .orElseThrow(() -> new IllegalArgumentException("No GroupCode found for groupId: " + groupId));
        return groupCode.getName();
    }
    /**
     * 공통코드 이름 조회 메소드
     * @param codeId
     * @return String
     */
    private String findCommonCodeName(String codeId) {
        // TODO : 예외처리 가은님 코드로 통일하기
        CommonCode commonCode = commonCodeRepository.findByCodeId(codeId)
                .orElseThrow(() -> new IllegalArgumentException("No CommonCode found for codeId: " + codeId));
        return commonCode.getName();
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
