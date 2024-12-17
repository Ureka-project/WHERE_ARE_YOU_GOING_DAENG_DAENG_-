package com.daengdaeng_eodiga.project.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceMedia is a Querydsl query type for PlaceMedia
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceMedia extends EntityPathBase<PlaceMedia> {

    private static final long serialVersionUID = 2050703449L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceMedia placeMedia = new QPlaceMedia("placeMedia");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath path = createString("path");

    public final QPlace place;

    public final NumberPath<Integer> placeMediaId = createNumber("placeMediaId", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPlaceMedia(String variable) {
        this(PlaceMedia.class, forVariable(variable), INITS);
    }

    public QPlaceMedia(Path<? extends PlaceMedia> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceMedia(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceMedia(PathMetadata metadata, PathInits inits) {
        this(PlaceMedia.class, metadata, inits);
    }

    public QPlaceMedia(Class<? extends PlaceMedia> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

