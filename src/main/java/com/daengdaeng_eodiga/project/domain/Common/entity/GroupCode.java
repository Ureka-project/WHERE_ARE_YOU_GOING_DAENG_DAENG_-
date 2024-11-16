package com.daengdaeng_eodiga.project.domain.Common.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Group_Code")
public class GroupCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    private String groupName;

    @OneToMany(mappedBy = "groupCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommonCode> commonCodes = new ArrayList<>();

    // Getter, Setter
}
