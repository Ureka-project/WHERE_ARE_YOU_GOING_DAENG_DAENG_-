package com.daengdaeng_eodiga.project.preference.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

@Data
public class PreferenceRequestDto {
    @NotNull
    private String preferenceInfo;

    @NotNull
    private Set<String> preferenceTypes;
}
