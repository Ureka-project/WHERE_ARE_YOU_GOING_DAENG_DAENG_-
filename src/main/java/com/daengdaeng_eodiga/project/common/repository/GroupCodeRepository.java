package com.daengdaeng_eodiga.project.common.repository;

import com.daengdaeng_eodiga.project.common.entity.GroupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupCodeRepository extends JpaRepository<GroupCode, String> {
    Optional<GroupCode> findByName(String name);
    Optional<GroupCode> findByGroupId(String groupId);
}