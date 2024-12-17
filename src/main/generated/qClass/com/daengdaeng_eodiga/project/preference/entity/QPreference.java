package com.daengdaeng_eodiga.project.preference.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPreference is a Querydsl query type for Preference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPreference extends EntityPathBase<Preference> {

    private static final long serialVersionUID = 2022023483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPreference preference = new QPreference("preference");

    public final QPreferenceId id;

    public final StringPath preferenceType = createString("preferenceType");

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public QPreference(String variable) {
        this(Preference.class, forVariable(variable), INITS);
    }

    public QPreference(Path<? extends Preference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPreference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPreference(PathMetadata metadata, PathInits inits) {
        this(Preference.class, metadata, inits);
    }

    public QPreference(Class<? extends Preference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QPreferenceId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

