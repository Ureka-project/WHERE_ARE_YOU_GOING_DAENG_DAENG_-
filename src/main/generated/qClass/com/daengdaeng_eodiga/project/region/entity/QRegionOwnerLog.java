package com.daengdaeng_eodiga.project.region.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegionOwnerLog is a Querydsl query type for RegionOwnerLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegionOwnerLog extends EntityPathBase<RegionOwnerLog> {

    private static final long serialVersionUID = -364029858L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegionOwnerLog regionOwnerLog = new QRegionOwnerLog("regionOwnerLog");

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

    public QRegionOwnerLog(String variable) {
        this(RegionOwnerLog.class, forVariable(variable), INITS);
    }

    public QRegionOwnerLog(Path<? extends RegionOwnerLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegionOwnerLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegionOwnerLog(PathMetadata metadata, PathInits inits) {
        this(RegionOwnerLog.class, metadata, inits);
    }

    public QRegionOwnerLog(Class<? extends RegionOwnerLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

