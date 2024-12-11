package com.daengdaeng_eodiga.project.common.service;

import com.daengdaeng_eodiga.project.Global.exception.CommonCodeNotFoundException;
import com.daengdaeng_eodiga.project.common.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonCodeService {
    private final CommonCodeRepository commonCodeRepository;

    /**
     * 공통 코드를 이름으로 변환하는 메소드
     * @param codeId
     * @return CommonName
     */
    @Cacheable(cacheNames = "commonCode", cacheManager = "commonCodeCacheManager")
    public String getCommonCodeName(String codeId) {
        return commonCodeRepository.findByCodeId(codeId)
                .map(commonCode -> commonCode.getName())
                .orElseThrow(CommonCodeNotFoundException::new);
    }

    @Cacheable(cacheNames = "checkCommonCodeExist", cacheManager = "commonCodeCacheManager")
    public void isCommonCode(String codeId) {
        if (!commonCodeRepository.findByCodeId(codeId).isPresent()) {
            throw new CommonCodeNotFoundException();
        }
    }
}
