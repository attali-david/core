package com.example.app.core.space.service;

import com.example.app.core.space.model.AcknowledgeRequest;
import com.example.app.core.space.model.Space;
import com.example.app.core.space.model.SpaceUserRelationship;

import java.util.List;

public interface SpaceService {
    Space createSpace(Space space) throws Exception;

    void deleteSpace(Long id) throws Exception;

    List<Space> findSpaces() throws Exception;

    void sendSpaceInvite(SpaceUserRelationship spaceUserRelationship) throws Exception;

    List<SpaceUserRelationship> findSpaceInvite(Long userId) throws Exception;

    void acknowledgeSpaceInvite(AcknowledgeRequest acknowledgeRequest) throws Exception;
}
