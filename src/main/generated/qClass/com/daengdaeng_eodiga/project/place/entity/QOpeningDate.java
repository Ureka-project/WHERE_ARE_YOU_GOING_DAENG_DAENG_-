package com.daengdaeng_eodiga.project.place.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOpeningDate is a Querydsl query type for OpeningDate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOpeningDate extends EntityPathBase<OpeningDate> {

    private static final long serialVersionUID = -584915094L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOpeningDate openingDate = new QOpeningDate("openingDate");

    public final StringPath dayType = createString("dayType");

    public final StringPath endTime = createString("endTime");

    public final NumberPath<Integer> openingDateId = createNumber("openingDateId", Integer.class);

    public final QPlace place;

    public final StringPath startTime = createString("startTime");

    public QOpeningDate(String variable) {
        this(OpeningDate.class, forVariable(variable), INITS);
    }

    public QOpeningDate(Path<? extends OpeningDate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOpeningDate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOpeningDate(PathMetadata metadata, PathInits inits) {
        this(OpeningDate.class, metadata, inits);
    }

    public QOpeningDate(Class<? extends OpeningDate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new QPlace(forProperty("place"), inits.get("place")) : null;
    }

}

