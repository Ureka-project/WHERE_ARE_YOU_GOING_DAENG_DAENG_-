package com.daengdaeng_eodiga.project.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewPet is a Querydsl query type for ReviewPet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewPet extends EntityPathBase<ReviewPet> {

    private static final long serialVersionUID = -2002399894L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewPet reviewPet = new QReviewPet("reviewPet");

    public final com.daengdaeng_eodiga.project.pet.entity.QPet pet;

    public final QReview review;

    public final NumberPath<Integer> reviewPetId = createNumber("reviewPetId", Integer.class);

    public QReviewPet(String variable) {
        this(ReviewPet.class, forVariable(variable), INITS);
    }

    public QReviewPet(Path<? extends ReviewPet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewPet(PathMetadata metadata, PathInits inits) {
        this(ReviewPet.class, metadata, inits);
    }

    public QReviewPet(Class<? extends ReviewPet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new com.daengdaeng_eodiga.project.pet.entity.QPet(forProperty("pet"), inits.get("pet")) : null;
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
    }

}

