package com.daengdaeng_eodiga.project.region.dto;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RegionVisit<T> {
	private HashMap<String,HashMap<String,T>> visitInfo;
}
