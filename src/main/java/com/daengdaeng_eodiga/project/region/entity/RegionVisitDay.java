package com.daengdaeng_eodiga.project.region.entity;
import jakarta.persistence.*;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.user.entity.User;

@Entity
@Table(name = "Region_visit_day")
@Getter
@NoArgsConstructor
public class RegionVisitDay extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "city")
	private String city;

	@Column(name = "city_detail")
	private String cityDetail;

	@Column(name = "count")
	private int count;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public RegionVisitDay(User user, String city, String cityDetail, int count) {
		this.user = user;
		this.city = city;
		this.cityDetail = cityDetail;
		this.count = count;
	}

	public void addCount() {
		this.count +=1;
	}

	public void decrementCount() {
		this.count -=1;
	}
}
