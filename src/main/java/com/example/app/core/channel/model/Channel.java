package com.example.app.core.channel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "channel")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created_at_local")
    private Date createdAtLocal;

    @Column(name = "created_at_server")
    private Date createdAtServer;

    @Column(name = "space_fk")
    private long spaceId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

}
