package com.daengdaeng_eodiga.project.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Common_Code")
public class CommonCode {

    @Id
    @Column(name = "code_id")
    private String codeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupCode groupCode;

    private String name;

    @Builder
    public CommonCode(String codeId, GroupCode groupCode, String name) {
        this.codeId = codeId;
        this.groupCode = groupCode;
        this.name = name;
    }
    public CommonCode() {}
}
