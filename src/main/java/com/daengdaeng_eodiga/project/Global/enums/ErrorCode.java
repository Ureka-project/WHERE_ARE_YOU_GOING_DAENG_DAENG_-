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
	USER_FAILED_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "유저 저장에 실패했습니다."),
	USER_FAILED_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "유저 삭제에 실패했습니다."),
	USER_FAILED_ADJUST(HttpStatus.INTERNAL_SERVER_ERROR, "유저 수정에 실패했습니다."),
	NOTI_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
	BANNER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배너입니다."),
	PUSH_TOKEN_IS_EXISTS(HttpStatus.CONFLICT, "이미 등록된 푸시 토큰입니다.");
	private final HttpStatus errorCode;
	private final String message;

}
