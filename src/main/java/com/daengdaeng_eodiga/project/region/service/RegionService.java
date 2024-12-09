package com.daengdaeng_eodiga.project.region.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.UserLandNotFoundException;
import com.daengdaeng_eodiga.project.pet.dto.PetResponse;
import com.daengdaeng_eodiga.project.region.dto.CityDetailVisit;
import com.daengdaeng_eodiga.project.region.dto.RegionVisit;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerCityDetail;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerInfo;
import com.daengdaeng_eodiga.project.region.enums.Regions;
import com.daengdaeng_eodiga.project.region.repository.RegionOwnerLogRepository;
import com.daengdaeng_eodiga.project.story.dto.MyLandsDto;
import com.daengdaeng_eodiga.project.story.dto.UserMyLandsDto;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionService {
	private final RegionOwnerLogRepository regionOwnerLogRepository;
	private final RedisTemplate<String, Integer> redisTemplate2;
	private final UserService userService;
	
	private final String REGION_VISIT_KEY_PREFIX = "RegionVisit";

	/**
	 * resion_owner_log 테이블에서 지역별 땅 주인을 조회한다.
	 * @return RegionOwnerCity
	 * @author 김가은
	 */

	public RegionVisit<RegionOwnerCityDetail> fetchRegionOwners() {
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
		RegionVisit<RegionOwnerCityDetail> regionVisit = new RegionVisit();
		regionVisit.setVisitInfo(regionOwners);

		return regionVisit;
	}

	/**
	 * 유저의 지역 방문횟수를 증가시킨다.
	 *
	 * 지역별 유저 방문횟수는 Redis에서 실시간으로 정렬된다. (ZSet 타입)
	 * @author 김가은
	 */

	public void addCountVisitRegion(String city, String cityDetail, int userId) {
		String key = createCountVisitRegionKey(city,cityDetail);

		Double count = redisTemplate2.opsForZSet().score(key, userId);
		redisTemplate2.opsForZSet().add(key, userId, (count==null?0:(int) Math.floor(count)) + 1);
	}

	/**
	 * 지역별 유저 방문횟수를 늘리기 위해, 키 값을 생성한다.
	 *
	 * @author 김가은
	 */

	private String createCountVisitRegionKey(String city, String cityDetail){
		return REGION_VISIT_KEY_PREFIX+":"+city + ":" + cityDetail;
	}

	/**
	 * Redis에서 지역별 땅 주인을 조회한다.
	 *
	 * @return RegionOwnerCity
	 * @author 김가은
	 */

	public RegionVisit<RegionOwnerCityDetail> fetchCountVisitAllRegion() {
		Map<String,Integer> cityDetailOwners = new HashMap<>();
		Map<String, Integer> cityDetailOwnerVisitCount = new HashMap<>();
		fetchCityDetailOwnerUserIds(cityDetailOwners,cityDetailOwnerVisitCount);

		List<Integer> userIds = new ArrayList<>(new HashSet<>(cityDetailOwners.values()));
		Map<Integer,List<PetResponse>> userPets = new HashMap<>();
		Map<Integer,String> userNicknames = new HashMap<>();
		putUsersPetsAndNicknameMap(userIds,userNicknames,userPets);


		HashMap<String,HashMap<String, RegionOwnerCityDetail>> regionOwners = new HashMap<>();

		for(Regions region : Regions.values()) {


			HashMap<String, RegionOwnerCityDetail> regionOwnerCities = new HashMap<>();
			String city = region.name();
			region.getCityDetails().forEach(cityDetail ->{
				Integer ownerUserId = cityDetailOwners.getOrDefault(cityDetail,null);
				if(ownerUserId != null) {
					String nickname = userNicknames.get(ownerUserId);
					RegionOwnerCityDetail regionOwnerCityDetail = new RegionOwnerCityDetail(ownerUserId, nickname,cityDetailOwnerVisitCount.getOrDefault(cityDetail,0),userPets.get(ownerUserId));
					regionOwnerCities.put(cityDetail, regionOwnerCityDetail);
				}else {
					regionOwnerCities.put(cityDetail, null);
				}
			});
			regionOwners.put(city, regionOwnerCities);
		}

		RegionVisit<RegionOwnerCityDetail> regionVisit = new RegionVisit();
		regionVisit.setVisitInfo(regionOwners);

		return regionVisit;
	}

	/**
	 * 유저별 닉네임과 펫 정보를 Map에 저장한다.
	 *
	 * @author 김가은
	 */

	private void putUsersPetsAndNicknameMap(List<Integer> userIds, Map<Integer,String> userNicknames, Map<Integer,List<PetResponse>> userPets ) {
		userService.findUsersByUserIds(userIds).stream().forEach(user ->{
			List<PetResponse> pets = userPets.getOrDefault(user.getUserId(),new ArrayList<>());
			pets.add(new PetResponse(user.getPetId(),user.getPetName(),user.getPetImage()));
			userNicknames.put(user.getUserId(),user.getNickname());
			userPets.put(user.getUserId(),pets);
		});

	}

	/**
	 * Redis에서 지역별 땅 주인의 userId와 방문횟수를 조회한다.
	 *
	 * @author 김가은
	 */

	private void fetchCityDetailOwnerUserIds(Map<String, Integer> cityDetailOwners,Map<String, Integer> cityDetailOwnerVisitCount) {
		for (Regions region : Regions.values()) {
			String city = region.name();
			region.getCityDetails().forEach(cityDetail -> {
				String key = createCountVisitRegionKey(city, cityDetail);
				redisTemplate2.opsForZSet().reverseRangeWithScores(key, 0, 0).stream().forEach(owner -> {
					Integer userId = owner.getValue();
					Double visitCount = owner.getScore();
					cityDetailOwners.put(cityDetail, userId);
					cityDetailOwnerVisitCount.put(cityDetail, visitCount.intValue());
				});
			});
		}
	}

	/**
	 * 유저가 주인인 cityDetail을 조회한다.
	 *
	 * @author 김가은
	 */

	public UserMyLandsDto fetchUserCityDetail(int userId) {

		List<MyLandsDto> lands = new ArrayList<>();
		for (Regions region : Regions.values()) {
			String city = region.name();
			List<CityDetailVisit> cityDetails = new ArrayList<>();
			region.getCityDetails().forEach(cityDetail -> {
				String key = createCountVisitRegionKey(city, cityDetail);
				redisTemplate2.opsForZSet().reverseRangeWithScores(key, 0, 0).stream().forEach(owner -> {
					if(owner.getValue() == userId) {
						cityDetails.add(new CityDetailVisit(cityDetail, owner.getScore().intValue()));
					}
				});
			});
			if(!cityDetails.isEmpty()) {
				lands.add(new MyLandsDto(city, cityDetails));
			}
		}
		if(lands.isEmpty()) {
			throw new UserLandNotFoundException();
		}
		User user = userService.findUser(userId);
		return new UserMyLandsDto(user.getNickname(), lands);

	}

	/**
	 * 유저의 지역별(cityDetail) 방문횟수를 조회한다.
	 *
	 * @author 김가은
	 */

	public RegionVisit<Integer> fetchUserCityDetailVisitCount(int userId) {
  		HashMap<String, HashMap<String, Integer>> cityVisitCount = new HashMap<>();
		for (Regions region : Regions.values()) {
			String city = region.name();
			HashMap<String, Integer> cityDetailVisitCount = new HashMap<>();
			region.getCityDetails().forEach(cityDetail -> {
				String key = createCountVisitRegionKey(city, cityDetail);
				cityDetailVisitCount.put(cityDetail, 0);
				redisTemplate2.opsForZSet().reverseRangeWithScores(key, 0, -1).stream().filter(owner -> owner.getValue() == userId).findFirst().ifPresent(owner -> {
					Integer visitCount = owner.getScore().intValue();
					cityDetailVisitCount.put(cityDetail, visitCount);
				});
			});
			cityVisitCount.put(region.name(),cityDetailVisitCount);
		}
		RegionVisit<Integer> regionVisit = new RegionVisit();
		regionVisit.setVisitInfo(cityVisitCount);
		return regionVisit;

	}



}
