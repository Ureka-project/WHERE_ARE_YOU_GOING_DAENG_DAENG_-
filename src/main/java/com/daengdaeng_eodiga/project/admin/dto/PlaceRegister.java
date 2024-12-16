package com.daengdaeng_eodiga.project.admin.dto;

import lombok.Getter;

@Getter
public class PlaceRegister {
	private String name;

	private String city;

	private String cityDetail;

	private String postCode;

	private String streetAddresses;

	private String telNumber;

	private String url;

	private String placeType;

	private String description;

	private String weightLimit;

	private Boolean parking;

	private Boolean indoor;

	private Boolean outdoor;

	private String thumbImgPath;

	private String imgPath;

	private Double latitude;

	private Double longitude;

	private String townShip;
}
