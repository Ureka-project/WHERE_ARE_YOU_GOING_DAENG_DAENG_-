package com.daengdaeng_eodiga.project.visit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVisitPet is a Querydsl query type for VisitPet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisitPet extends EntityPathBase<VisitPet> {

    private static final long serialVersionUID = 1441954900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVisitPet visitPet = new QVisitPet("visitPet");

    public final com.daengdaeng_eodiga.project.pet.entity.QPet pet;

    public final QVisit visit;

    public final NumberPath<Integer> visitPetId = createNumber("visitPetId", Integer.class);

    public QVisitPet(String variable) {
        this(VisitPet.class, forVariable(variable), INITS);
    }

    public QVisitPet(Path<? extends VisitPet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVisitPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVisitPet(PathMetadata metadata, PathInits inits) {
        this(VisitPet.class, metadata, inits);
    }

    public QVisitPet(Class<? extends VisitPet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new com.daengdaeng_eodiga.project.pet.entity.QPet(forProperty("pet"), inits.get("pet")) : null;
        this.visit = inits.isInitialized("visit") ? new QVisit(forProperty("visit"), inits.get("visit")) : null;
    }

}

