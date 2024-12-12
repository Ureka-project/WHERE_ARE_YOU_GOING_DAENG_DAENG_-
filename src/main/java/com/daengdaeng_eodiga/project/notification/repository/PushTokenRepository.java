package com.daengdaeng_eodiga.project.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.notification.entity.PushToken;
import com.daengdaeng_eodiga.project.user.entity.User;

public interface PushTokenRepository extends JpaRepository<PushToken, Integer> {

	List<PushToken> findByTokenAndUserAndPushType(String token, User user, String pushType);
	List<PushToken> findByUser(User user);
	List<PushToken> findByUser_UserId(int userId);
	List<PushToken> findByUser_UserIdInAndPushType(List<Integer> userIds, String pushType);

	void deleteByUser(User user);
}
