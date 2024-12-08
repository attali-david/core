package com.example.app.core.space.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "space_user_relationship")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SpaceUserRelationshipId.class) // Composite primary key
public class SpaceUserRelationship {

    @Id
    @Column(name = "space_fk")
    private Long spaceId;

    @Id
    @Column(name = "user_fk")
    private Long userId;

    @Column(name = "request_timestamp", nullable = false)
    private ZonedDateTime requestTimestamp;

    @Column(name = "acknowledge_timestamp")
    private ZonedDateTime acknowledgeTimestamp;

    @Column(name = "accepted", nullable = true)
    private Boolean accepted;
}
