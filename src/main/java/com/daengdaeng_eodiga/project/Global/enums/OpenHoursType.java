package com.daengdaeng_eodiga.project.Global.enums;

import lombok.Getter;

@Getter
public enum OpenHoursType {
    ALWAYS_OPEN("연중무휴"),
    TODAY_OFF("오늘 휴무"),
    HOURS_AVAILABLE("영업 시간"),
    NO_INFO("정보 없음");

    private final String description;

    OpenHoursType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
