package com.daengdaeng_eodiga.project.user.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
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

}
