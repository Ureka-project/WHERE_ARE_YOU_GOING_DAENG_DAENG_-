package com.daengdaeng_eodiga.project.preference.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPreferenceId is a Querydsl query type for PreferenceId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPreferenceId extends BeanPath<PreferenceId> {

    private static final long serialVersionUID = 1839351734L;

    public static final QPreferenceId preferenceId = new QPreferenceId("preferenceId");

    public final StringPath preferenceInfo = createString("preferenceInfo");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QPreferenceId(String variable) {
        super(PreferenceId.class, forVariable(variable));
    }

    public QPreferenceId(Path<? extends PreferenceId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPreferenceId(PathMetadata metadata) {
        super(PreferenceId.class, metadata);
    }

}

