package com.daengdaeng_eodiga.project.preference.repository;

import com.daengdaeng_eodiga.project.preference.dto.UserRequsetPrefernceDto;
import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.preference.entity.PreferenceId;
import com.daengdaeng_eodiga.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, PreferenceId> {
    void deleteByUserAndPreferenceType(User user, String preferenceType);
    List<Preference> findByUser_UserIdAndPreferenceType(Integer userId, String preferenceType);
    List<Preference> findByUser(User userId);
    @Query("SELECT new com.daengdaeng_eodiga.project.preference.dto.UserRequsetPrefernceDto(p.preferenceType) " +
            "FROM Preference p " +
            "JOIN p.user u " +
            "WHERE u.userId = :userId")
    List<UserRequsetPrefernceDto> findPreferenceTypesByUserId(@Param("userId") Integer userId);
}
