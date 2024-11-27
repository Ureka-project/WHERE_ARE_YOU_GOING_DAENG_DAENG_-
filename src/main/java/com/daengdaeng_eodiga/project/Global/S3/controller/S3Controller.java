package com.daengdaeng_eodiga.project.Global.S3.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.S3.service.S3Uploader;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/S3")
public class S3Controller {

	private final S3Uploader s3Uploader;

	@GetMapping("")
	public Map<String,String> getS3UploadUrl(@RequestParam String prefix, @RequestParam String fileName) {
		return s3Uploader.getPresignedUrl(prefix, fileName);
	}
}
