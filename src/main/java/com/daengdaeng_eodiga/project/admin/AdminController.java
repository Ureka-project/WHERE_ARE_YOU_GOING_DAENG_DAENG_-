package com.daengdaeng_eodiga.project.admin;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.admin.dto.PlaceRegister;
import com.daengdaeng_eodiga.project.admin.service.AdminService;
import com.daengdaeng_eodiga.project.place.entity.Place;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	/**
	 * 애견 동반 가능한 장소를 등록
	 *
	 * @author 김가은
	 * @return Place : 등록된 장소 정보
	 *
	 * */
	@PostMapping("/place")
	public ResponseEntity<ApiResponse<Place>>  savePlace(@RequestBody PlaceRegister request) {
		Place response = adminService.savePlace(request);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	/**
	 * 애견 동반 가능한 장소 이미지 업로드
	 *
	 * 이미지를 받아, 원본 이미지와 썸네일용 이미지를 리사이징해서 따로 S3에 업로드
	 *
	 * @author 김가은
	 * @return Map<String,String> : 원본 이미지 경로, 썸네일 이미지 경로
	 * */
	@PostMapping("/placeImage")
	public ResponseEntity<ApiResponse<Map<String,String>>> resizeImage(@RequestParam("image") MultipartFile file) throws IOException {
		Map<String,String> response = adminService.uploadPlaceImage(file);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

}
