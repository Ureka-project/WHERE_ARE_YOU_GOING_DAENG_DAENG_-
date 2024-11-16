package com.daengdaeng_eodiga.project.domain.User.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
@Embeddable
public class PreferenceId implements Serializable {

    @Column(name = "preference_info")
    private String preferenceInfo;

    @Column(name = "user_id")
    private int userId;  // Integer에서 int로 변경

    // 기본 생성자
    public PreferenceId() {
    }

    // 생성자
    public PreferenceId(String preferenceInfo, int userId) {
        this.preferenceInfo = preferenceInfo;
        this.userId = userId;
    }

    // equals()와 hashCode() 구현 (복합 키에 필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferenceId that = (PreferenceId) o;

        if (!preferenceInfo.equals(that.preferenceInfo)) return false;
        return userId == that.userId;  // userId 비교 시 Integer에서 int로 변경
    }

    @Override
    public int hashCode() {
        int result = preferenceInfo.hashCode();
        result = 31 * result + Integer.hashCode(userId);  // Integer.hashCode로 변경
        return result;
    }

    // Getter, Setter
    public String getPreferenceInfo() {
        return preferenceInfo;
    }

    public void setPreferenceInfo(String preferenceInfo) {
        this.preferenceInfo = preferenceInfo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
