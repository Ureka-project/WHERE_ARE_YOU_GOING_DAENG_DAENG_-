package com.daengdaeng_eodiga.project.story.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStory is a Querydsl query type for Story
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStory extends EntityPathBase<Story> {

    private static final long serialVersionUID = -1541256949L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStory story = new QStory("story");

    public final StringPath city = createString("city");

    public final StringPath cityDetail = createString("cityDetail");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endAt = createDateTime("endAt", java.time.LocalDateTime.class);

    public final StringPath path = createString("path");

    public final NumberPath<Integer> storyId = createNumber("storyId", Integer.class);

    public final ListPath<StoryView, QStoryView> storyViews = this.<StoryView, QStoryView>createList("storyViews", StoryView.class, QStoryView.class, PathInits.DIRECT2);

    public final com.daengdaeng_eodiga.project.user.entity.QUser user;

    public QStory(String variable) {
        this(Story.class, forVariable(variable), INITS);
    }

    public QStory(Path<? extends Story> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStory(PathMetadata metadata, PathInits inits) {
        this(Story.class, metadata, inits);
    }

    public QStory(Class<? extends Story> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.daengdaeng_eodiga.project.user.entity.QUser(forProperty("user")) : null;
    }

}

