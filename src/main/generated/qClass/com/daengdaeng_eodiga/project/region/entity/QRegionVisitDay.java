package com.daengdaeng_eodiga.project.region.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegionVisitDay is a Querydsl query type for RegionVisitDay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegionVisitDay extends EntityPathBase<RegionVisitDay> {

    private static final long serialVersionUID = -442517730L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegionVisitDay regionVisitDay = new QRegionVisitDay("regionVisitDay");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    public final StringPath city = createString("city");

    public final StringPath cityDetail = createString("cityDetail");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public QRegionVisitDay(String variable) {
        this(RegionVisitDay.class, forVariable(variable), INITS);
    }

    public QRegionVisitDay(Path<? extends RegionVisitDay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegionVisitDay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegionVisitDay(PathMetadata metadata, PathInits inits) {
        this(RegionVisitDay.class, metadata, inits);
    }

    public QRegionVisitDay(Class<? extends RegionVisitDay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

