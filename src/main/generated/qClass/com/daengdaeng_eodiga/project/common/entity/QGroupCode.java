package com.daengdaeng_eodiga.project.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupCode is a Querydsl query type for GroupCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupCode extends EntityPathBase<GroupCode> {

    private static final long serialVersionUID = 1205178780L;

    public static final QGroupCode groupCode = new QGroupCode("groupCode");

    public final ListPath<CommonCode, QCommonCode> commonCodes = this.<CommonCode, QCommonCode>createList("commonCodes", CommonCode.class, QCommonCode.class, PathInits.DIRECT2);

    public final StringPath groupId = createString("groupId");

    public final StringPath name = createString("name");

    public QGroupCode(String variable) {
        super(GroupCode.class, forVariable(variable));
    }

    public QGroupCode(Path<? extends GroupCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupCode(PathMetadata metadata) {
        super(GroupCode.class, metadata);
    }

}

