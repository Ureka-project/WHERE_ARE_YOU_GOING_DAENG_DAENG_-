package com.daengdaeng_eodiga.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daengdaeng_eodiga.project.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
