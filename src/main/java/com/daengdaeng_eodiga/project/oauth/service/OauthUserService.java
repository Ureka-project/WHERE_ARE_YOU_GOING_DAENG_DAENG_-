package com.daengdaeng_eodiga.project.oauth.service;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedDeleteException;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedSaveException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OauthUserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public void registerOrUpdateUser(SignUpForm userDTO) {
        Optional<User> existingUserOpt = userRepository.findByEmail(userDTO.getEmail());

        User user;
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
            user.setNickname(userDTO.getNickname());
            user.setGender(userDTO.getGender());
            user.setCity(userDTO.getCity());
            user.setCityDetail(userDTO.getCityDetail());
        } else {
            user = new User();
            user.setNickname(userDTO.getNickname());
            user.setEmail(userDTO.getEmail());
            user.setGender(userDTO.getGender());
            user.setCity(userDTO.getCity());
            user.setCityDetail(userDTO.getCityDetail());
            user.setOauthProvider(userDTO.getOauthProvider());
        }

        userRepository.save(user);

    }
    public void deleteUserByName(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User user1 = user.get();
            userRepository.deleteById(user1.getUserId());
        } else {
            throw new UserFailedDeleteException();
        }
    }
    public UserDto UserToDto(String email) {

        Optional<User> user =userRepository.findByEmail(email);
        UserDto userDto = new UserDto();
        if (user.isPresent()) {
            User user1 = user.get();
            userRepository. deleteById(user1.getUserId());
            userDto.setEmail(user1.getEmail());
            userDto.setNickname(user1.getNickname());
            userDto.setGender(user1.getGender());
            userDto.setCity(user1.getCity());
            userDto.setCityDetail(user1.getCityDetail());
            userDto.setCreatedAt(LocalDateTime.now());
            userDto.setUserId(user1.getUserId());
        }
        return userDto;
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
