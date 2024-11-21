package com.daengdaeng_eodiga.project.preference.service;

import com.daengdaeng_eodiga.project.Global.exception.CommonCodeNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.GroupCodeNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
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
@Transactional
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final GroupCodeRepository groupCodeRepository;
    private final UserRepository userRepository;

    public PreferenceResponseDto registerPreference(int hardcodedUserId, PreferenceRequestDto preferenceRequestDto) {
        User user = findUser((long) hardcodedUserId);

        List<CommonCode> commonCodes = findCommonCode(
                preferenceRequestDto.getPreferenceInfo(),
                preferenceRequestDto.getPreferenceTypes());

        if (commonCodes.isEmpty()) {
            throw new CommonCodeNotFoundException();
        }
        Set<Preference> preferences = createPreferences(commonCodes, hardcodedUserId, user);
        preferenceRepository.saveAll(preferences);
        return mapToDto(preferenceRequestDto.getPreferenceInfo(), preferences);
    }

    public PreferenceResponseDto updatePreference(long hardcodedUserId, PreferenceRequestDto preferenceRequestDto) {
        User user = findUser(hardcodedUserId);

        preferenceRepository.deleteByUserAndPreferenceType(user, preferenceRequestDto.getPreferenceInfo());
        List<CommonCode> commonCodes = findCommonCode(
                preferenceRequestDto.getPreferenceInfo(),
                preferenceRequestDto.getPreferenceTypes());

        if (commonCodes.isEmpty()) {
            throw new CommonCodeNotFoundException();
        }
        Set<Preference> preferences = createPreferences(commonCodes, (int) hardcodedUserId, user);
        preferenceRepository.saveAll(preferences);
        return mapToDto(preferenceRequestDto.getPreferenceInfo(), preferences);
    }

    public List<PreferenceResponseDto> fetchPreferences(long userId) {
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
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
    /**
     * 공통코드 조회 메소드
     * @param groupId
     * @param commonIds
     * @return List<CommonCode>
     */
    private List<CommonCode> findCommonCode(String groupId, Set<String> commonIds) {
        List<CommonCode> commonCodes =  commonCodeRepository.findByGroupCode_GroupIdAndCodeIdIn(groupId, commonIds);
        if (commonCodes.isEmpty()) {
            throw new CommonCodeNotFoundException();
        }
        return commonCodes;
    }
    /**
     * 그룹코드 이름 조회 메소드
     * @param groupId
     * @return String
     */
    private String findGroupName(String groupId) {
        GroupCode groupCode = groupCodeRepository.findByGroupId(groupId)
                .orElseThrow(GroupCodeNotFoundException::new);
        return groupCode.getName();
    }
    /**
     * 공통코드 이름 조회 메소드
     * @param codeId
     * @return String
     */
    private String findCommonCodeName(String codeId) {
        CommonCode commonCode = commonCodeRepository.findByCodeId(codeId)
                .orElseThrow(CommonCodeNotFoundException::new);
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
