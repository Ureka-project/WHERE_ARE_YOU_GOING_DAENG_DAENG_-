package com.daengdaeng_eodiga.project.story.entity;

import com.daengdaeng_eodiga.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "story_view")
public class StoryView {

    @EmbeddedId
    private StoryViewId storyViewId;

    @MapsId("storyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", insertable = false, updatable = false)
    private Story story;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public StoryView(StoryViewId storyViewId, Story story, User user, LocalDateTime createdAt) {
        this.storyViewId = storyViewId;
        this.story = story;
        this.user = user;
        this.createdAt = createdAt;
    }
    public StoryView () {}
}