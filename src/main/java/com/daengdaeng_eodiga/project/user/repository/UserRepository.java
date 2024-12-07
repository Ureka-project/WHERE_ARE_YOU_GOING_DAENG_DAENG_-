package com.daengdaeng_eodiga.project.user.repository;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndOauthProviderAndDeletedAtIsNull(String email,OauthProvider oauthProvider);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmailAndOauthProvider(String email, OauthProvider oauthProvider);
}

