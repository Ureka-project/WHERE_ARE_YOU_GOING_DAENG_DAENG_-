package com.daengdaeng_eodiga.project.visit.dto;

import java.time.LocalDateTime;

public interface VisitInfo {
	LocalDateTime getVisitAt();
	int getPetId();
	String getPetImg();
	String getPetName();
	int getPlaceId();
	String getPlaceName();

	int getVisitId();

}
