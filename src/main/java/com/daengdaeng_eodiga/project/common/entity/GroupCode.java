package com.daengdaeng_eodiga.project.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Group_Code")
public class GroupCode {
    @Id
    @Column(name = "group_id")
    private String groupId;

    private String name;

    @OneToMany(mappedBy = "groupCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommonCode> commonCodes = new ArrayList<>();

    @Builder
    public GroupCode(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
    public GroupCode() {}
}
