package com.daengdaeng_eodiga.project.common.repository;

import com.daengdaeng_eodiga.project.common.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    List<CommonCode> findByGroupCode_GroupIdAndNameIn(String groupId, Set<String> names);
    Optional<CommonCode> findByCodeId(String codeId);
}
