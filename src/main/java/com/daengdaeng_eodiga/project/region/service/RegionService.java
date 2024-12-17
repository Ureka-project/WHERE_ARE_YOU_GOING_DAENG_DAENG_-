package com.daengdaeng_eodiga.project.region.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.UserLandNotFoundException;
import com.daengdaeng_eodiga.project.notification.service.NotificationService;
import com.daengdaeng_eodiga.project.pet.dto.PetResponse;
import com.daengdaeng_eodiga.project.region.dto.CityDetailVisit;
import com.daengdaeng_eodiga.project.region.dto.RegionVisit;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerCityDetail;
import com.daengdaeng_eodiga.project.region.dto.RegionOwnerInfo;
import com.daengdaeng_eodiga.project.region.entity.RegionOwnerLog;
import com.daengdaeng_eodiga.project.region.entity.RegionVisitDay;
import com.daengdaeng_eodiga.project.region.entity.RegionVisitTotal;
import com.daengdaeng_eodiga.project.region.enums.Regions;
import com.daengdaeng_eodiga.project.region.repository.RegionOwnerLogRepository;
import com.daengdaeng_eodiga.project.region.repository.RegionVisitDayRepository;
import com.daengdaeng_eodiga.project.region.repository.RegionVisitTotalRepository;
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
	private final RegionVisitDayRepository regionVisitDayRepository;
	private final RegionVisitTotalRepository regionVisitTotalRepository;
	private final NotificationService notificationService;
	
	private final String REGION_VISIT_KEY_PREFIX = "RegionVisit";

	/**
	 * resion_owner_log 테이블에서 지역별 땅 주인을 조회한다.
	 * @return RegionOwnerCity
	 * @author 김가은
	 */

	public RegionVisit<RegionOwnerCityDetail> fetchRegionOwners() {
		List<RegionOwnerInfo> regionOwnerInfos = regionOwnerLogRepository.findRegionOwner();
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
			if(roi.getPetId() != null) {
				pets.add(new PetResponse(roi.getPetId(),roi.getPetName(),roi.getPetImage()));
				petsByRegionOwnerLogIds.put(roi.getId(),pets);
			}

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
		for (Regions region : Regions.values()) {
			if(!regionOwners.containsKey(region.name())) {
				HashMap<String, RegionOwnerCityDetail> regionOwnerCities = new HashMap<>();
				region.getCityDetails().forEach(cityDetail ->{
					regionOwnerCities.put(cityDetail, null);
				});
				regionOwners.put(region.name(),regionOwnerCities);
			}else{
				region.getCityDetails().forEach(cityDetail ->{
					if(!regionOwners.get(region.name()).containsKey(cityDetail)){
						regionOwners.get(region.name()).put(cityDetail,null);
					}
				});
			}
		}
		RegionVisit<RegionOwnerCityDetail> regionVisit = new RegionVisit();
		regionVisit.setVisitInfo(regionOwners);

		return regionVisit;
	}

	/**
	 * 유저 자신의 땅 목록 조회
	 * @param userId
	 * @return UserMyLandsDto
	 * @author 하진서, 김가은
	 */
	public UserMyLandsDto fetchUserLands(int userId) {

		String nickname = userService.findUser(userId).getNickname();
		List<Object[]> results = regionOwnerLogRepository.findCityAndCityDetailByUserId(userId);
		if( results.isEmpty() ) {
			throw new UserLandNotFoundException();
		}

		Map<String, List<CityDetailVisit>> myLands = new LinkedHashMap<>();
		for (Object[] row : results) {
			String city = (String) row[0];
			String cityDetail = (String) row[1];
			int count = (int) row[2];
			CityDetailVisit cityDetailVisit = new CityDetailVisit(cityDetail, count);
			List<CityDetailVisit> cityDetailVisits = myLands.getOrDefault(city, new ArrayList<>());
			cityDetailVisits.add(cityDetailVisit);
			myLands.put(city,cityDetailVisits);
		}
		List<MyLandsDto> myLandsDtos = myLands.entrySet().stream()
			.map(entry -> new MyLandsDto(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
		UserMyLandsDto userMyLandsDto = UserMyLandsDto.builder()
			.nickname(nickname)
			.lands(myLandsDtos)
			.build();
		return userMyLandsDto;
	}

	/**
	 * 유저의 지역별(cityDetail) 방문횟수를 조회한다.
	 *
	 * @author 김가은
	 */

	public RegionVisit<Integer> fetchUserCityDetailVisitCountForDB(int userId) {
		User user = userService.findUser(userId);
		HashMap<String, HashMap<String, Integer>> cityVisitCount = new HashMap<>();
		List<RegionVisitTotal> regionVisitTotals = regionVisitTotalRepository.findByUser(user);
		regionVisitTotals.stream().forEach(regionVisitTotal -> {
			String city = regionVisitTotal.getCity();
			String cityDetail = regionVisitTotal.getCityDetail();
			Integer count = regionVisitTotal.getCount();
			HashMap<String, Integer> cityDetailVisitCount = cityVisitCount.getOrDefault(city, new HashMap<>());
			cityDetailVisitCount.put(cityDetail, count);
			cityVisitCount.put(city, cityDetailVisitCount);
		});



		for (Regions region : Regions.values()) {
			String city = region.name();
			cityVisitCount.putIfAbsent(city, new HashMap<>());
			region.getCityDetails().forEach(cityDetail -> {
				cityVisitCount.get(city).putIfAbsent(cityDetail, 0);
			});
		}
		RegionVisit<Integer> regionVisit = new RegionVisit();
		regionVisit.setVisitInfo(cityVisitCount);
		return regionVisit;

	}




	/**
	 * 유저의 지역 방문횟수를 증가시킨다.
	 *
	 * 지역별 유저 방문횟수는 [1일 지역별 유저 방문 횟수] -> [최종 지역별 유저 방문횟수] -> [땅 주인 변경 히스토리] 순으로 테이블에 가공되어 저장된다.
	 * @author 김가은
	 */

	public void addCountVisitRegionForDB(String city, String cityDetail, User user) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow  = today.plusDays(1);
		regionVisitDayRepository.findByCityAndCityDetailAndUserAndCreatedAt(city, cityDetail, user, today.atStartOfDay(),tomorrow.atStartOfDay()).ifPresentOrElse(regionVisitDay -> {
			regionVisitDay.addCount();
			regionVisitDayRepository.save(regionVisitDay);
		}, () -> {
			RegionVisitDay regionVisitDay = RegionVisitDay.builder()
				.city(city)
				.cityDetail(cityDetail)
				.count(1)
				.user(user)
				.build();
			regionVisitDayRepository.save(regionVisitDay);
		});

		RegionVisitTotal total = regionVisitTotalRepository.findByCityAndCityDetailAndUser(city, cityDetail, user).map(regionVisitTotal -> {
			regionVisitTotal.addCount();
			return regionVisitTotalRepository.save(regionVisitTotal);
		})
			.orElseGet(() -> {
				RegionVisitTotal regionVisitTotal = RegionVisitTotal.builder()
					.city(city)
					.cityDetail(cityDetail)
					.count(1)
					.user(user)
					.build();
				return regionVisitTotalRepository.save(regionVisitTotal);
			});

		regionOwnerLogRepository.findRegionOwnerByCityAndCityDetail(city, cityDetail).ifPresentOrElse(regionOwnerLog -> {
			if(regionOwnerLog.getCount()<total.getCount()){
				RegionOwnerLog newRegionOwnerLog = RegionOwnerLog.builder()
					.city(city)
					.cityDetail(cityDetail)
					.count(total.getCount())
					.user(user)
					.build();
				regionOwnerLogRepository.save(newRegionOwnerLog);
				if(regionOwnerLog.getUser().getUserId() != user.getUserId()) {
					String region = city + " " + cityDetail;
					notificationService.sendOwnerNotification(user.getUserId(), regionOwnerLog.getUser().getUserId(), region);
				}
			}
		}, () -> {
			RegionOwnerLog regionOwnerLog = RegionOwnerLog.builder()
				.city(city)
				.cityDetail(cityDetail)
				.count(total.getCount())
				.user(user)
				.build();
			regionOwnerLogRepository.save(regionOwnerLog);
			String region = city + " " + cityDetail;
			notificationService.sendOwnerNotification(user.getUserId(), null, region);

		}
		);
	}


	/**
	 * 유저의 지역 방문횟수를 감소시킨다.
	 *
	 * 만약, 감소시켰을 때 순위가 바뀐다면 유저에게 알림 메시지를 보낸다.
	 *
	 * @author 김가은
	 */


	public void decrementCountVisitRegionForDB(String city, String cityDetail, User user,LocalDate createdAt) {
		LocalDate endDate  = createdAt.plusDays(1);
		regionVisitDayRepository.findByCityAndCityDetailAndUserAndCreatedAt(city, cityDetail, user, createdAt.atStartOfDay(),endDate.atStartOfDay()).ifPresent(regionVisitDay -> {
			regionVisitDay.decrementCount();
			regionVisitDayRepository.save(regionVisitDay);
		});

		Optional<RegionVisitTotal> total = regionVisitTotalRepository.findByCityAndCityDetailAndUser(city, cityDetail, user);

		if(total.isPresent()) {
			RegionVisitTotal regionVisitTotal = total.get();
			regionVisitTotal.decrementCount();
			regionVisitTotalRepository.save(regionVisitTotal);
			Optional<RegionOwnerLog> userRecentLog = regionOwnerLogRepository.findTop1UserRegionOwnerLogAtCreated(user.getUserId(),city, cityDetail);

			List<RegionOwnerLog> logs = regionOwnerLogRepository.findTop2RegionOwnerByCityAndCityDetail(city,cityDetail);

			userRecentLog.ifPresent(regionOwnerLogRepository::delete);
			if(logs.size() == 2) {
				if(userRecentLog.get().getId() == logs.get(0).getId()) {
					if(userRecentLog.get().getUser() != logs.get(1).getUser()){
						String region = city + " " + cityDetail;
						notificationService.sendOwnerNotification(logs.get(1).getUser().getUserId(),userRecentLog.get().getUser().getUserId(), region);
					}
				}
			}
		}
	}




	/**
	 * 유저의 지역 방문횟수를 증가시킨다.(Redis에서 관리)
	 *
	 * 지역별 유저 방문횟수는 Redis에서 실시간으로 정렬된다. (ZSet 타입)
	 * @author 김가은
	 * @deprecated
	 */

	public void addCountVisitRegionForRedis(String city, String cityDetail, int userId) {
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
	 * @deprecated
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
	 * @deprecated
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
	 * @deprecated
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
	 * @deprecated
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
	 * @deprecated
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
