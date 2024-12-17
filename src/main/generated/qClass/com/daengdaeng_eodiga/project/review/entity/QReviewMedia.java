package com.daengdaeng_eodiga.project.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewMedia is a Querydsl query type for ReviewMedia
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewMedia extends EntityPathBase<ReviewMedia> {

    private static final long serialVersionUID = -163732113L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewMedia reviewMedia = new QReviewMedia("reviewMedia");

    public final StringPath path = createString("path");

    public final QReview review;

    public final NumberPath<Integer> reviewMediaId = createNumber("reviewMediaId", Integer.class);

    public QReviewMedia(String variable) {
        this(ReviewMedia.class, forVariable(variable), INITS);
    }

    public QReviewMedia(Path<? extends ReviewMedia> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewMedia(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewMedia(PathMetadata metadata, PathInits inits) {
        this(ReviewMedia.class, metadata, inits);
    }

    public QReviewMedia(Class<? extends ReviewMedia> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
    }

}

