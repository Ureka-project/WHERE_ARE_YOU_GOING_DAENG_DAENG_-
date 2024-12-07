package com.daengdaeng_eodiga.project.oauth.service;

import java.time.LocalDateTime;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.exception.DuplicateUserException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.notification.service.NotificationService;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import com.daengdaeng_eodiga.project.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OauthUserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final CommonCodeService commonCodeService;
    private final UserService userService;
    public void registerUser(SignUpForm userDTO) {
        if (userRepository.findByEmailAndOauthProvider(userDTO.getEmail(), userDTO.getOauthProvider()).isPresent()) {
            throw new DuplicateUserException();
        }
        User user = new User();
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        commonCodeService.isCommonCode(userDTO.getGender());
        user.setGender(userDTO.getGender());
        user.setCity(userDTO.getCity());
        user.setCityDetail(userDTO.getCityDetail());
        user.setOauthProvider(userDTO.getOauthProvider());
        userRepository.save(user);
    }
    public void AdjustUser(SignUpForm AdjustuserDTO, String email, OauthProvider provider) {
        User user = userService.findUserByemailAndProvider(email,provider);
        if (user!=null) {
            user.setNickname(AdjustuserDTO.getNickname());
            commonCodeService.isCommonCode(AdjustuserDTO.getGender());
            user.setGender(AdjustuserDTO.getGender());
            user.setCity(AdjustuserDTO.getCity());
            user.setCityDetail(AdjustuserDTO.getCityDetail());
            userRepository.save(user);

        }
        else
            throw new UserNotFoundException();
    }
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    public UserDto UserToDto(String email, OauthProvider provider) {
        User user =userService.findUserByemailAndProvider(email,provider);
        if (user != null)
        {
            UserDto userDto = new UserDto();
            userDto.setEmail(user.getEmail());
            userDto.setNickname(user.getNickname());
            userDto.setCity(user.getCity());
            String genderCode = "GND_01".equals(user.getGender()) ? "남자" : "여자";
            userDto.setGender(genderCode);
            userDto.setCityDetail(user.getCityDetail());
            userDto.setCreatedAt(user.getCreatedAt());
            userDto.setUserId(user.getUserId());
            userDto.setOauthProvider(user.getOauthProvider());;
            return userDto;
        }
        else
        {
            throw new UserNotFoundException();
        }
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
