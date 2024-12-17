package com.daengdaeng_eodiga.project.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPet is a Querydsl query type for Pet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPet extends EntityPathBase<Pet> {

    private static final long serialVersionUID = 50545227L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPet pet = new QPet("pet");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    public final DateTimePath<java.util.Date> birthday = createDateTime("birthday", java.util.Date.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath gender = createString("gender");

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final BooleanPath neutering = createBoolean("neutering");

    public final NumberPath<Integer> petId = createNumber("petId", Integer.class);

    public final ListPath<com.daengdaeng_eodiga.project.review.entity.ReviewPet, com.daengdaeng_eodiga.project.review.entity.QReviewPet> reviewPets = this.<com.daengdaeng_eodiga.project.review.entity.ReviewPet, com.daengdaeng_eodiga.project.review.entity.QReviewPet>createList("reviewPets", com.daengdaeng_eodiga.project.review.entity.ReviewPet.class, com.daengdaeng_eodiga.project.review.entity.QReviewPet.class, PathInits.DIRECT2);

    public final StringPath size = createString("size");

    public final StringPath species = createString("species");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public final ListPath<com.daengdaeng_eodiga.project.visit.entity.VisitPet, com.daengdaeng_eodiga.project.visit.entity.QVisitPet> visitPets = this.<com.daengdaeng_eodiga.project.visit.entity.VisitPet, com.daengdaeng_eodiga.project.visit.entity.QVisitPet>createList("visitPets", com.daengdaeng_eodiga.project.visit.entity.VisitPet.class, com.daengdaeng_eodiga.project.visit.entity.QVisitPet.class, PathInits.DIRECT2);

    public QPet(String variable) {
        this(Pet.class, forVariable(variable), INITS);
    }

    public QPet(Path<? extends Pet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPet(PathMetadata metadata, PathInits inits) {
        this(Pet.class, metadata, inits);
    }

    public QPet(Class<? extends Pet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

