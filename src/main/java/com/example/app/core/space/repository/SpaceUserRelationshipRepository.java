package com.example.app.core.space.repository;

import com.example.app.core.space.model.SpaceUserRelationship;
import com.example.app.core.space.model.SpaceUserRelationshipId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface SpaceUserRelationshipRepository extends JpaRepository<SpaceUserRelationship, SpaceUserRelationshipId> {

    List<SpaceUserRelationship> findAllBySpaceId(Long spaceId);

    List<SpaceUserRelationship> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE SpaceUserRelationship sur " +
            "SET sur.acknowledgeTimestamp = :acknowledgeTimestamp, " +
            "sur.accepted = :accepted " +
            "WHERE sur.spaceId = :spaceId AND sur.userId = :userId")
    int updateAcknowledgement(Long spaceId, Long userId, ZonedDateTime acknowledgeTimestamp, Boolean accepted);

    @Query("SELECT COUNT(sur) > 0 FROM SpaceUserRelationship sur " +
            "WHERE sur.spaceId = :spaceId AND sur.userId = :userId")
    boolean existsByIds(@Param("spaceId") long spaceId, @Param("userId") long userId);
}
