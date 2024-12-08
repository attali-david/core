package com.example.app.core.channel.service;

import com.example.app.core.auth.model.AppUser;
import com.example.app.core.channel.model.Channel;
import com.example.app.core.channel.repository.ChannelRespository;
import com.example.app.core.security.SecurityUtils;
import com.example.app.core.space.model.Space;
import com.example.app.core.space.model.SpaceUserRelationship;
import com.example.app.core.space.repository.SpaceRepository;
import com.example.app.core.space.repository.SpaceUserRelationshipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRespository channelRespository;
    private final SpaceRepository spaceRepository;
    private final SpaceUserRelationshipRepository spaceUserRelationshipRepository;

    public ChannelServiceImpl(ChannelRespository channelRespository, SpaceRepository spaceRepository, SpaceUserRelationshipRepository spaceUserRelationshipRepository) {
        this.channelRespository = channelRespository;
        this.spaceRepository = spaceRepository;
        this.spaceUserRelationshipRepository = spaceUserRelationshipRepository;
    }

    @Override
    public List<Channel> findChannels(long spaceId) {
        List<Channel> channels = channelRespository.findBySpaceId(spaceId);
        return channels;
    }

    @Override
    public void createChannel(Channel channel) throws Exception {
        AppUser user = SecurityUtils.getCurrentUser();
        Optional<Space> space = spaceRepository.findById(channel.getSpaceId());

        if (space.isEmpty()) {
            throw new Exception("Space does not exist.");
        }

        boolean userSpace = spaceUserRelationshipRepository.existsByIds(space.get().getId(), user.getId());
        if (space.get().getUserId() != user.getId() && !userSpace) {
            throw new Exception("Insufficient permissions.");
        }

        channelRespository.save(channel);
    }

    @Override
    public void deleteChannel(long channelId) throws Exception {
        Optional<Channel> channel = channelRespository.findById(channelId);
        if (channel.isEmpty()) {
            throw new Exception("Channel does not exist.");
        }

        boolean userSpace = spaceUserRelationshipRepository.existsByIds(channel.get().getSpaceId(), SecurityUtils.getCurrentUser().getId());
        if (!userSpace) {
            throw new Exception("Insufficient permissions.");
        }

        channelRespository.deleteById(channelId);
    }
}
