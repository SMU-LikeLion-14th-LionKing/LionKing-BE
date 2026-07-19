package com.LionKing.Teamply.domain.projects.entity;

import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "projects")
public class Projects extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_Id")
    private Long id;

    @Column(name = "project_Name")
    private String name;

    @Column(name = "project_Type")
    private String projectType;

    private String title;

    private LocalDateTime deadline;

    @Column(name = "progress_rate")
    private Float progressRate;

    @Column(name = "ai_progress_rate")
    private Float aiProgressRate;

}
