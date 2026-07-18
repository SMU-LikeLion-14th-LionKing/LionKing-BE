package com.LionKing.Teamply.domain.project.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberId implements Serializable {
    private Long user;
    private Long project;
}