package com.daengdaeng_eodiga.project.preference.repository;

import com.daengdaeng_eodiga.project.preference.entity.Preference;
import com.daengdaeng_eodiga.project.preference.entity.PreferenceId;
import com.daengdaeng_eodiga.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, PreferenceId> {
    void deleteByUserAndPreferenceType(User user, String preferenceType);
}
