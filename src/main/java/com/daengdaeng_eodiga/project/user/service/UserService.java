package com.daengdaeng_eodiga.project.user.service;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User findUser(int userId) {
		return userRepository.findById( userId).orElseThrow(()->new UserNotFoundException());
	}
	public User findUserId(String email) {
		return userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException());
	}
}
