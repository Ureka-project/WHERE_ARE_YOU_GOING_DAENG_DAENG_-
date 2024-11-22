package com.daengdaeng_eodiga.project.user.repository;

import com.daengdaeng_eodiga.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    boolean existsByNickname(String nickname);  // 닉네임 중복 체크
    boolean existsByEmail(String email);        // 이메일 중복 체크
}