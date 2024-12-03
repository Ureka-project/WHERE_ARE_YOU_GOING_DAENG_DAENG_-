package com.daengdaeng_eodiga.project.Global.S3.dto;

import java.util.List;

import com.daengdaeng_eodiga.project.Global.S3.enums.S3Prefix;

public record PresignURLRequest(S3Prefix prefix, List<String> fileNames) {
}
