package com.daengdaeng_eodiga.project.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceScore is a Querydsl query type for PlaceScore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceScore extends EntityPathBase<PlaceScore> {

    private static final long serialVersionUID = 2056195847L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceScore placeScore = new QPlaceScore("placeScore");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QPlace place;

    public final NumberPath<Integer> placeId = createNumber("placeId", Integer.class);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Double> score = createNumber("score", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlaceScore(String variable) {
        this(PlaceScore.class, forVariable(variable), INITS);
    }

    public QPlaceScore(Path<? extends PlaceScore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceScore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceScore(PathMetadata metadata, PathInits inits) {
        this(PlaceScore.class, metadata, inits);
    }

    public QPlaceScore(Class<? extends PlaceScore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

