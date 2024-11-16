package com.daengdaeng_eodiga.project.domain.Common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Common_Code")
public class CommonCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private int commonCodeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupCode groupCode;

    private String codeName;


}
