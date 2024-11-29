package com.daengdaeng_eodiga.project.oauth.service;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.exception.DuplicateUserException;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedDeleteException;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedSaveException;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedSaveException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.notification.service.NotificationService;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OauthUserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public void registerUser(SignUpForm userDTO) {
        if (userRepository.findByEmailAndOauthProvider(userDTO.getEmail(), userDTO.getOauthProvider()).isPresent()) {
            throw new DuplicateUserException();
        }
        User user = new User();
        user.setNickname(userDTO.getNickname());
        user.setGender(userDTO.getGender());
        user.setCity(userDTO.getCity());
        user.setCityDetail(userDTO.getCityDetail());
        user.setOauthProvider(userDTO.getOauthProvider());
        userRepository.save(user);
    }
    public void AdjustUser(SignUpForm userDTO) {
        Optional<User> existingUserOpt = userRepository.findByEmail(userDTO.getEmail());
        User user;
        if (existingUserOpt.isPresent()) {
            user = new User();
            user.setNickname(userDTO.getNickname());
            user.setEmail(userDTO.getEmail());
            user.setGender(userDTO.getGender());
            user.setCity(userDTO.getCity());
            user.setCityDetail(userDTO.getCityDetail());
            user.setOauthProvider(userDTO.getOauthProvider());
            userRepository.save(user);

        }
        else
            throw new UserNotFoundException();
    }
    public void deleteUserByName(String email) {
        //TODO : orElseThrow로 변경해서 UserNotFoundException 발생시키기
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User user1 = user.get();
            userRepository.deleteById(user1.getUserId());
        } else {
            throw new UserNotFoundException();
        }
    }
    public UserDto UserToDto(String email) {

        User user =userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setNickname(user.getNickname());
        userDto.setCity(user.getCity());
        String genderCode = "GND_01".equals(user.getGender()) ? "남자" : "여자";
        userDto.setGender(genderCode);
        userDto.setCityDetail(user.getCityDetail());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUserId(user.getUserId());
        boolean pushAgreement = notificationService.findPushTokenByUser(user).isEmpty();
        userDto.setPushAgreement(!pushAgreement);
        return userDto;
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
