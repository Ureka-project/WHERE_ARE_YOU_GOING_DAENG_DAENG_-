package com.daengdaeng_eodiga.project.Global.S3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.daengdaeng_eodiga.project.Global.S3.enums.S3Prefix;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Uploader {

	private final AmazonS3 amazonS3;

	@Value("${cloud.s3.bucket}")
	private String bucket;

	public Map<String, String> getPresignedUrl(S3Prefix prefix, List<String> fileNames) {
		Map<String,String> urls = new HashMap<>();
		for(String name : fileNames) {
			String fileName = createPath(prefix.toString(), name);
			GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, fileName);
			URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
			urls.put(name, url.toString());
		}
		return urls;
	}

	private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPresignedUrlExpiration());

		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString()
		);

		return generatePresignedUrlRequest;
	}

	/**
	 * S3에 이미지 업로드
	 *
	 * @author 김가은
	 *
	 * */

	public String putObject(String filePath,String fileName, InputStream inputStream, ObjectMetadata metadata) {
		String key = filePath + "/" + fileName;

		PutObjectRequest request = new PutObjectRequest(bucket, key,inputStream, metadata)
			.withCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3.putObject(request);
		return amazonS3.getUrl(bucket, key).toString();
	}

	private Date getPresignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);

		return expiration;
	}

	private String createFileId() {
		return UUID.randomUUID().toString();
	}

	private String createPath(String prefix, String fileName) {
		String fileId = createFileId();
		return String.format("%s/%s", prefix, fileId + "-" + fileName);
	}
}