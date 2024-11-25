package com.daengdaeng_eodiga.project.Global.enums;

import lombok.Getter;

@Getter
public enum OpenHoursType {
    TODAY_OFF("오늘 휴무"),
    NO_INFO("시설에 문의");

    private final String description;

    OpenHoursType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
