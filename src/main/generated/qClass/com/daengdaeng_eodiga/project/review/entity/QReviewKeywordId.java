package com.daengdaeng_eodiga.project.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewKeywordId is a Querydsl query type for ReviewKeywordId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QReviewKeywordId extends BeanPath<ReviewKeywordId> {

    private static final long serialVersionUID = -231225105L;

    public static final QReviewKeywordId reviewKeywordId = new QReviewKeywordId("reviewKeywordId");

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Integer> reviewId = createNumber("reviewId", Integer.class);

    public QReviewKeywordId(String variable) {
        super(ReviewKeywordId.class, forVariable(variable));
    }

    public QReviewKeywordId(Path<? extends ReviewKeywordId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewKeywordId(PathMetadata metadata) {
        super(ReviewKeywordId.class, metadata);
    }

}

