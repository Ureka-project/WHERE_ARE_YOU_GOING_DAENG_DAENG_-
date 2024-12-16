package com.daengdaeng_eodiga.project.admin.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.daengdaeng_eodiga.project.Global.S3.service.S3Uploader;
import com.daengdaeng_eodiga.project.Global.exception.NotFoundException;
import com.daengdaeng_eodiga.project.admin.dto.PlaceRegister;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.place.entity.Place;
import com.daengdaeng_eodiga.project.place.service.PlaceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

	private final CommonCodeService commonCodeService;
	private final PlaceService placeService;
	private final S3Uploader s3Uploader;

	/**
	 * 애견 동반 가능한 장소를 등록
	 *
	 * @author 김가은
	 * @return Place : 등록된 장소 정보
	 *
	 * */

	public Place savePlace(PlaceRegister placeRegister) {
		commonCodeService.isCommonCode(placeRegister.getPlaceType());

		Place place = Place.builder()
			.name(placeRegister.getName())
			.city(placeRegister.getCity())
			.cityDetail(placeRegister.getCityDetail())
			.postCode(placeRegister.getPostCode())
			.streetAddresses(placeRegister.getStreetAddresses())
			.telNumber(placeRegister.getTelNumber())
			.url(placeRegister.getUrl())
			.placeType(placeRegister.getPlaceType())
			.description(placeRegister.getDescription())
			.weightLimit(placeRegister.getWeightLimit())
			.parking(placeRegister.getParking())
			.indoor(placeRegister.getIndoor())
			.outdoor(placeRegister.getOutdoor())
			.thumbImgPath(placeRegister.getThumbImgPath())
			.latitude(placeRegister.getLatitude())
			.longitude(placeRegister.getLongitude())
			.township(placeRegister.getTownShip())
			.build();
		Place savedPlace = placeService.savePlace(place);
		placeService.savePlaceMedia(savedPlace, placeRegister.getImgPath());
		return savedPlace;

	}

	/**
	 * 애견 동반 가능한 장소 이미지 업로드
	 *
	 * 이미지를 받아, 원본 이미지와 썸네일용 이미지를 리사이징해서 따로 S3에 업로드
	 *
	 * @author 김가은
	 * @return Map<String,String> : 원본 이미지 경로, 썸네일 이미지 경로
	 * */

	public HashMap<String,String> uploadPlaceImage(MultipartFile img) throws IOException {
		String imgPath = uploadImage(img);
		String thumbImgPath = uploadThumbImg(img);
		HashMap<String,String> paths = new HashMap<>();
		paths.put("imgPath", imgPath);
		paths.put("thumbImgPath", thumbImgPath);
		return paths;
	}

	/**
	 * 원본 이미지 업로드
	 *
	 * @author 김가은
	 * @return String : 이미지 경로
	 * */

	public String uploadImage(MultipartFile img) throws IOException {


		String originalFilename = img.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(img.getSize());
		metadata.setContentType(img.getContentType());
		return s3Uploader.putObject("PLACE",originalFilename,img.getInputStream(), metadata);
	}

	/**
	 * 원본 이미지 썸네일용으로 리사이징해서 업로드
	 *
	 * @author 김가은
	 * @return String : 이미지 경로
	 * */


	private String uploadThumbImg(MultipartFile img) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(img.getInputStream());
		String originalFilename = img.getOriginalFilename();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		if(width > 108 || height > 130) {
			BufferedImage resizedImage= Thumbnails.of(bufferedImage)
				.size(108, 130)
				.asBufferedImage();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, getFileExtension(img.getOriginalFilename()), outputStream);
			byte[] resizedImageBytes = outputStream.toByteArray();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(resizedImageBytes);

			ObjectMetadata thumbMetadata = new ObjectMetadata();
			thumbMetadata.setContentLength(resizedImageBytes.length);
			thumbMetadata.setContentType(img.getContentType());
			return s3Uploader.putObject("THUMB", originalFilename,inputStream, thumbMetadata);
		}
		return null;
	}

	/**
	 * 이미지 확장자 추출
	 *
	 * @author 김가은
	 * @return String : 이미지 확장자
	 * */

	public String getFileExtension(String fileName) {
		if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
			throw new NotFoundException("파일 확장자",fileName);
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

}
