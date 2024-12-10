package com.daengdaeng_eodiga.project.region.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.pet.dto.PetResponse;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerCity;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerCityDetail;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerInfo;
import com.daengdaeng_eodiga.project.region.repository.RegionOwnerLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionService {
	private final RegionOwnerLogRepository regionOwnerLogRepository;

	public RegionOwnerCity fetchRegionOwners() {
		List<RegionOwnerInfo> regionOwnerInfos = regionOwnerLogRepository.fetchRegionOwner();
		HashMap<String,HashMap<String, RegionOwnerCityDetail>> regionOwners = new HashMap<>();
		Map<String, Set<Integer>> regionOwnerLogIdsByCity = new HashMap<>();
		Map<Integer,String> cityDetailByRegionOwnerLogIds = new HashMap<>();
		Map<Integer,RegionOwnerCityDetail> regionOwnerCityDetails = new HashMap<>();
		Map<Integer,List<PetResponse>> petsByRegionOwnerLogIds = new HashMap<>();
		regionOwnerInfos.stream().forEach(roi ->{
			Set<Integer> ids = regionOwnerLogIdsByCity.getOrDefault(roi.getCity(),new HashSet<>());
			ids.add(roi.getId());
			regionOwnerLogIdsByCity.put(roi.getCity(),ids);
			cityDetailByRegionOwnerLogIds.put(roi.getId(),roi.getCityDetail());
			regionOwnerCityDetails.put(roi.getId(),new RegionOwnerCityDetail(roi.getUserId(),roi.getUserNickname(),roi.getCount(),null));
			List<PetResponse> pets = petsByRegionOwnerLogIds.getOrDefault(roi.getId(),new ArrayList<>());
			pets.add(new PetResponse(roi.getPetId(),roi.getPetName(),roi.getPetImage()));
			petsByRegionOwnerLogIds.put(roi.getId(),pets);
		});
		regionOwnerLogIdsByCity.forEach((city,ids) ->{
			HashMap<String,RegionOwnerCityDetail> regionOwnerCities = new HashMap<>();
			ids.forEach(id ->{
				RegionOwnerCityDetail regionOwnerCityDetail = regionOwnerCityDetails.get(id);
				regionOwnerCityDetail.setPets(petsByRegionOwnerLogIds.get(id));
				regionOwnerCities.put(cityDetailByRegionOwnerLogIds.get(id), regionOwnerCityDetail);
			});
			regionOwners.put(city,regionOwnerCities);
		});
		RegionOwnerCity regionOwnerCity = new RegionOwnerCity();
		regionOwnerCity.setRegionOwners(regionOwners);


		return regionOwnerCity;
	}
}
