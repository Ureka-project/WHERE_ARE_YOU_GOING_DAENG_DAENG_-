package com.daengdaeng_eodiga.project.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewSummary is a Querydsl query type for ReviewSummary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewSummary extends EntityPathBase<ReviewSummary> {

    private static final long serialVersionUID = 1020128882L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewSummary reviewSummary = new QReviewSummary("reviewSummary");

    public final StringPath badSummary = createString("badSummary");

    public final StringPath goodSummary = createString("goodSummary");

    public final QPlace place;

    public final NumberPath<Integer> placeId = createNumber("placeId", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public QReviewSummary(String variable) {
        this(ReviewSummary.class, forVariable(variable), INITS);
    }

    public QReviewSummary(Path<? extends ReviewSummary> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewSummary(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewSummary(PathMetadata metadata, PathInits inits) {
        this(ReviewSummary.class, metadata, inits);
    }

    public QReviewSummary(Class<? extends ReviewSummary> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

