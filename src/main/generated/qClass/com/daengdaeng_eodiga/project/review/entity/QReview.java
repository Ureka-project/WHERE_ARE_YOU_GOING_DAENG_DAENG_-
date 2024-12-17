package com.daengdaeng_eodiga.project.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 861059957L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.daengdaeng_eodiga.project.place.entity.QPlace place;

    public final NumberPath<Integer> reviewId = createNumber("reviewId", Integer.class);

    public final ListPath<ReviewKeyword, QReviewKeyword> reviewKeywords = this.<ReviewKeyword, QReviewKeyword>createList("reviewKeywords", ReviewKeyword.class, QReviewKeyword.class, PathInits.DIRECT2);

    public final ListPath<ReviewMedia, QReviewMedia> reviewMedias = this.<ReviewMedia, QReviewMedia>createList("reviewMedias", ReviewMedia.class, QReviewMedia.class, PathInits.DIRECT2);

    public final ListPath<ReviewPet, QReviewPet> reviewPets = this.<ReviewPet, QReviewPet>createList("reviewPets", ReviewPet.class, QReviewPet.class, PathInits.DIRECT2);

    public final StringPath reviewtype = createString("reviewtype");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public final DatePath<java.time.LocalDate> visitedAt = createDate("visitedAt", java.time.LocalDate.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new com.daengdaeng_eodiga.project.place.entity.QPlace(forProperty("place"), inits.get("place")) : null;
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

