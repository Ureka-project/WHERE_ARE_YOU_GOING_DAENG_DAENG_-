package com.daengdaeng_eodiga.project.preference.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Embeddable
@Getter
@Setter
public class PreferenceId implements Serializable {

    @Column(name = "preference_info")
    private String preferenceInfo;

    @Column(name = "user_id")
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferenceId that = (PreferenceId) o;

        if (!preferenceInfo.equals(that.preferenceInfo)) return false;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        int result = preferenceInfo.hashCode();
        result = 31 * result + Integer.hashCode(userId);
        return result;
    }

    @Builder
    public PreferenceId(String preferenceInfo, int userId) {
        this.preferenceInfo = preferenceInfo;
        this.userId = userId;
    }
    public PreferenceId(){}
}
