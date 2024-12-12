package com.daengdaeng_eodiga.project.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PushType {
	VISIT("새 친구가 찾아와요!", "NOTI_TYP_01"),
	EVENT("새로운 이벤트가 열렸어요!", "NOTI_TYP_02"),
	OWNER("새로운 땅 주인이 되었어요!", "NOTI_TYP_03"),
	PAST_OWNER("땅 주인이 바뀌었어요!", "NOTI_TYP_04");

	private String title;
	private String commonCode;
}