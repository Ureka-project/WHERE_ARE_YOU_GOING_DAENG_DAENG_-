package com.daengdaeng_eodiga.project.region.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import com.daengdaeng_eodiga.project.user.entity.User;

@Entity
@Table(name = "Region_owner_log")
@Data
@NoArgsConstructor
public class RegionOwnerLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "city")
	private String city;

	@Column(name = "city_detail")
	private String cityDetail;

	@Column(name = "count")
	private int count;

	@Builder
	public RegionOwnerLog(User user, String city, String cityDetail, int count) {
		this.user = user;
		this.city = city;
		this.cityDetail = cityDetail;
		this.count = count;
	}

}