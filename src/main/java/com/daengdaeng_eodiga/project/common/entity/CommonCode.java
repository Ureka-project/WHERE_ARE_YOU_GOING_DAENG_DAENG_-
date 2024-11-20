package com.daengdaeng_eodiga.project.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Common_Code")
public class CommonCode {

    @Id
    @Column(name = "code_id")
    private String codeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupCode groupCode;

    private String name;


}
