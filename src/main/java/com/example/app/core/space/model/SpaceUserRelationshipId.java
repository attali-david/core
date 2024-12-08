package com.example.app.core.space.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceUserRelationshipId implements Serializable {
    private Long spaceId;
    private Long userId;
}
