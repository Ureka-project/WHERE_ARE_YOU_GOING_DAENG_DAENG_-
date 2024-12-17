package com.daengdaeng_eodiga.project.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPushToken is a Querydsl query type for PushToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPushToken extends EntityPathBase<PushToken> {

    private static final long serialVersionUID = -278730097L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPushToken pushToken = new QPushToken("pushToken");

    public final com.daengdaeng_eodiga.project.Global.entity.QBaseEntity _super = new com.daengdaeng_eodiga.project.Global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath pushType = createString("pushType");

    public final StringPath token = createString("token");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public QPushToken(String variable) {
        this(PushToken.class, forVariable(variable), INITS);
    }

    public QPushToken(Path<? extends PushToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPushToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPushToken(PathMetadata metadata, PathInits inits) {
        this(PushToken.class, metadata, inits);
    }

    public QPushToken(Class<? extends PushToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

