package com.daengdaeng_eodiga.project.region.dto;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RegionOwnerCity {
	private HashMap<String,HashMap<String,RegionOwnerCityDetail>> regionOwners;
}
