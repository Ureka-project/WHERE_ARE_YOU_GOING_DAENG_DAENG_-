package com.daengdaeng_eodiga.project.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewKeyword is a Querydsl query type for ReviewKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewKeyword extends EntityPathBase<ReviewKeyword> {

    private static final long serialVersionUID = -187949900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewKeyword reviewKeyword = new QReviewKeyword("reviewKeyword");

    public final QReviewKeywordId id;

    public final QReview review;

    public QReviewKeyword(String variable) {
        this(ReviewKeyword.class, forVariable(variable), INITS);
    }

    public QReviewKeyword(Path<? extends ReviewKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewKeyword(PathMetadata metadata, PathInits inits) {
        this(ReviewKeyword.class, metadata, inits);
    }

    public QReviewKeyword(Class<? extends ReviewKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QReviewKeywordId(forProperty("id")) : null;
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
    }

}

