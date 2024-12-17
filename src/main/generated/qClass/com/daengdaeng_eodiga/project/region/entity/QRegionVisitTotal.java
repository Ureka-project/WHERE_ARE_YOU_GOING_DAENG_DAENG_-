package com.daengdaeng_eodiga.project.region.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegionVisitTotal is a Querydsl query type for RegionVisitTotal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegionVisitTotal extends EntityPathBase<RegionVisitTotal> {

    private static final long serialVersionUID = -42584506L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegionVisitTotal regionVisitTotal = new QRegionVisitTotal("regionVisitTotal");

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

    public QRegionVisitTotal(String variable) {
        this(RegionVisitTotal.class, forVariable(variable), INITS);
    }

    public QRegionVisitTotal(Path<? extends RegionVisitTotal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegionVisitTotal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegionVisitTotal(PathMetadata metadata, PathInits inits) {
        this(RegionVisitTotal.class, metadata, inits);
    }

    public QRegionVisitTotal(Class<? extends RegionVisitTotal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

