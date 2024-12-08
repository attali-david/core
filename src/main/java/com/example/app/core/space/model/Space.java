package com.example.app.core.space.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "space")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_fk")
    private long userId;

    @Column(name = "created_at_server")
    private Date createdAtServer;

    @Column(name = "title")
    private String title;
}
