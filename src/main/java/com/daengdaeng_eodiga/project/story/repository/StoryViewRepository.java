package com.daengdaeng_eodiga.project.story.repository;

import com.daengdaeng_eodiga.project.story.entity.StoryView;
import com.daengdaeng_eodiga.project.story.entity.StoryViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryViewRepository extends JpaRepository<StoryView, StoryViewId> {
}
