package com.daengdaeng_eodiga.project.Global.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
	PLACE_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 장소입니다."),
	PET_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 반려견입니다."),
	GROUP_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 그룹코드입니다."),
	COMMON_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공통코드입니다."),
	DATE_NOT_FOUND(HttpStatus.NOT_FOUND,"유효하지 않는 날짜입니다."),
	USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"권한이 없습니다."),
	FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 즐겨찾기입니다."),
	DAY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 휴무일입니다."),
	OPENING_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 영업 시간입니다."),
	DUPLICATE_PET(HttpStatus.CONFLICT,"이미 등록된 반려동물입니다."),
	DUPLICATE_USER(HttpStatus.CONFLICT,"이미 등록된 유저입니다."),
	USER_FAILED_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "유저 저장에 실패했습니다."),
	USER_FAILED_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "유저 삭제에 실패했습니다."),
	USER_FAILED_ADJUST(HttpStatus.INTERNAL_SERVER_ERROR, "유저 수정에 실패했습니다."),
	NOTI_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알립입니다."),
	REVIEW_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 장소에 대한 리뷰 요약 정보를 찾을 수 없습니다."),
	COOKIE_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "쿠키 삭제 실패"),
	BANNER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배너입니다."),
	PUSH_TOKEN_IS_EXISTS(HttpStatus.CONFLICT, "이미 등록된 푸시 토큰입니다."),
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정보입니다."),
	MISSING_COORDINATES(HttpStatus.BAD_REQUEST, "위도와 경도 값이 필요합니다."),
	DUPLICATE_PREFERENCE(HttpStatus.CONFLICT, "이미 등록된 선호도입니다."),
	DUPLICATE_FAVORITE(HttpStatus.CONFLICT, "이미 등록된 즐겨찾기입니다."),
	DAILY_STORY_UPLOAD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "오늘 업로드 가능한 데이터 개수를 초과했습니다."),
	USER_STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "스토리가 존재하지 않습니다."),
	USER_LAND_NOT_FOUND(HttpStatus.NOT_FOUND, "유저의 땅이 존재하지 않습니다."),
	OWNER_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "땅 주인 히스토리에 존재하지 않습니다."),
	DUPLICATE_REVIEW_DAY(HttpStatus.CONFLICT,"오늘 등록된 리뷰가 있습니다."),
	DUPLICATE_VISIT(HttpStatus.CONFLICT,"동일한 시간대에 등록된 방문 예정 일정이 있습니다.");
	private final HttpStatus errorCode;
	private final String message;

}
