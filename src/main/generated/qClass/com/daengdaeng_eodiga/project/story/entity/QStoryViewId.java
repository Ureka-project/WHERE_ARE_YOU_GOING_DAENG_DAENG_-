package com.daengdaeng_eodiga.project.story.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoryViewId is a Querydsl query type for StoryViewId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QStoryViewId extends BeanPath<StoryViewId> {

    private static final long serialVersionUID = 136589195L;

    public static final QStoryViewId storyViewId = new QStoryViewId("storyViewId");

    public final NumberPath<Integer> storyId = createNumber("storyId", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QStoryViewId(String variable) {
        super(StoryViewId.class, forVariable(variable));
    }

    public QStoryViewId(Path<? extends StoryViewId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoryViewId(PathMetadata metadata) {
        super(StoryViewId.class, metadata);
    }

}

