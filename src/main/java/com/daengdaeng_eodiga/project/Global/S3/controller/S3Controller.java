package com.daengdaeng_eodiga.project.Global.S3.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.S3.dto.PresignURLRequest;
import com.daengdaeng_eodiga.project.Global.S3.service.S3Uploader;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.review.dto.ReviewsResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/S3")
public class S3Controller {

	private final S3Uploader s3Uploader;

	@PostMapping("")
	public ResponseEntity<ApiResponse<Map<String,String>>> getS3UploadUrl(@RequestBody PresignURLRequest request) {
		Map<String,String> urls = s3Uploader.getPresignedUrl(request.prefix(), request.fileNames());
		return ResponseEntity.ok(ApiResponse.success(urls));
	}

}
