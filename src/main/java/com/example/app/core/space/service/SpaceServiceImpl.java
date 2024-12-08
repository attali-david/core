package com.example.app.core.space.service;

import com.example.app.core.auth.model.AppUser;
import com.example.app.core.security.SecurityUtils;
import com.example.app.core.space.model.AcknowledgeRequest;
import com.example.app.core.space.model.Space;
import com.example.app.core.space.model.SpaceUserRelationship;
import com.example.app.core.space.model.SpaceUserRelationshipId;
import com.example.app.core.space.repository.SpaceRepository;
import com.example.app.core.space.repository.SpaceUserRelationshipRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceServiceImpl implements SpaceService {
    private final SpaceRepository spaceRepository;
    private final SpaceUserRelationshipRepository spaceUserRelationshipRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository,
                            SpaceUserRelationshipRepository spaceUserRelationshipRepository) {
        this.spaceRepository = spaceRepository;
        this.spaceUserRelationshipRepository = spaceUserRelationshipRepository;
    }

    public Space createSpace(Space space) {
        AppUser user = getUserFromContext();
        space.setUserId(user.getId());

        return spaceRepository.save(space);
    }

    public void deleteSpace(long id) throws Exception {
        if (!spaceRepository.existsById(id)) {
            throw new Exception("Entity not found.");
        }

        if (!this.userBelongsToSpace(id)) {
            throw new Exception("User lacks permission.");
        }
        spaceRepository.deleteById(id);
    }

    public List<Space> findSpaces() throws Exception {
        AppUser user = getUserFromContext();

        return spaceRepository.findAll()
                .stream()
                .filter(space -> user.getId() == (space.getUserId()))
                .toList();
    }

    @Override
    public void sendSpaceInvite(SpaceUserRelationship spaceUserRelationship) throws Exception {
        spaceUserRelationship.setUserId(getUserFromContext().getId());
        spaceUserRelationshipRepository.save(spaceUserRelationship);
    }

    @Override
    public List<SpaceUserRelationship> findSpaceInvite(Long userId) throws Exception {
        return spaceUserRelationshipRepository.findAllByUserId(userId);
    }

    @Override
    public void acknowledgeSpaceInvite(AcknowledgeRequest acknowledgeRequest) throws Exception {
        acknowledgeRequest.setUserId(getUserFromContext().getId());
        SpaceUserRelationshipId relationshipId = new SpaceUserRelationshipId(acknowledgeRequest.getSpaceId(),
                acknowledgeRequest.getUserId());

        if (!spaceUserRelationshipRepository.existsById(relationshipId)) {
            throw new Exception("Entity does not exist");
        }

        spaceUserRelationshipRepository.updateAcknowledgement(acknowledgeRequest.getSpaceId(),
                acknowledgeRequest.getUserId(),
                acknowledgeRequest.getAcknowledgeTimestamp(),
                acknowledgeRequest.getAccepted());
        Optional<Space> space = spaceRepository.findById(acknowledgeRequest.getSpaceId());
    }

    @Override
    public boolean userBelongsToSpace(long spaceId) {
        SpaceUserRelationship userSpace = spaceUserRelationshipRepository.findByIds(spaceId, SecurityUtils.getCurrentUser().getId());
        return userSpace != null;
    }

    public List<SpaceUserRelationship> getUsersInSpace(Long spaceId) {
        return spaceUserRelationshipRepository.findAllBySpaceId(spaceId);
    }

    public List<SpaceUserRelationship> getSpacesForUser(Long userId) {
        return spaceUserRelationshipRepository.findAllByUserId(userId);
    }

    private AppUser getUserFromContext() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
