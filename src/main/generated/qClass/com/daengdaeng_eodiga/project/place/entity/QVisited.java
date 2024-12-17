package com.daengdaeng_eodiga.project.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVisited is a Querydsl query type for Visited
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisited extends EntityPathBase<Visited> {

    private static final long serialVersionUID = 1934200686L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVisited visited = new QVisited("visited");

    public final com.daengdaeng_eodiga.project.pet.entity.QPet pet;

    public final QPlace place;

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public final DateTimePath<java.util.Date> visitedAt = createDateTime("visitedAt", java.util.Date.class);

    public final NumberPath<Integer> visitedId = createNumber("visitedId", Integer.class);

    public QVisited(String variable) {
        this(Visited.class, forVariable(variable), INITS);
    }

    public QVisited(Path<? extends Visited> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVisited(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVisited(PathMetadata metadata, PathInits inits) {
        this(Visited.class, metadata, inits);
    }

    public QVisited(Class<? extends Visited> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new com.daengdaeng_eodiga.project.pet.entity.QPet(forProperty("pet"), inits.get("pet")) : null;
        this.place = inits.isInitialized("place") ? new QPlace(forProperty("place"), inits.get("place")) : null;
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

