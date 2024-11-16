package com.daengdaeng_eodiga.project.domain.Review.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "Review_Keyword")
//ReviewKeyword 엔티티에서 reviewId 외래 키를 명시적으로 삽입하거나 업데이트할 수 없도록 하여,
// 복합키에 의한 외래 키 관리와 @ManyToOne 관계에서 발생할 수 있는 충돌을 방지하는 역할을 한다.
public class ReviewKeyword {

    @EmbeddedId
    private ReviewKeywordId id;  // 복합 기본 키
    @ManyToOne
    @JoinColumn(name = "review_id", insertable = false, updatable = false)  // FK 설정
    private Review review;

    // 추가적인 컬럼을 정의할 수 있습니다.
    // 예를 들어, 키워드에 대한 설명이나 추가 정보를 포함할 수 있습니다.


}
