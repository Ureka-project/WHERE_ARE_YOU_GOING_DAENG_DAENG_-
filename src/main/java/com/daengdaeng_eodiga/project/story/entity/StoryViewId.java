package com.daengdaeng_eodiga.project.story.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class StoryViewId implements Serializable {
    @Column(name = "story_id")
    private int storyId;

    @Column(name = "user_id")
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryViewId that = (StoryViewId) o;
        return storyId == that.storyId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyId, userId);
    }

    @Builder
    public StoryViewId(int storyId, int userId) {
        this.storyId = storyId;
        this.userId = userId;
    }
    public StoryViewId(){}
}
