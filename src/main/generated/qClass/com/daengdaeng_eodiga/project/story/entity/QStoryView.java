package com.daengdaeng_eodiga.project.story.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoryView is a Querydsl query type for StoryView
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoryView extends EntityPathBase<StoryView> {

    private static final long serialVersionUID = -929465776L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoryView storyView = new QStoryView("storyView");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QStory story;

    public final QStoryViewId storyViewId;

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public QStoryView(String variable) {
        this(StoryView.class, forVariable(variable), INITS);
    }

    public QStoryView(Path<? extends StoryView> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoryView(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoryView(PathMetadata metadata, PathInits inits) {
        this(StoryView.class, metadata, inits);
    }

    public QStoryView(Class<? extends StoryView> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QStory(forProperty("story"), inits.get("story")) : null;
        this.storyViewId = inits.isInitialized("storyViewId") ? new QStoryViewId(forProperty("storyViewId")) : null;
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

