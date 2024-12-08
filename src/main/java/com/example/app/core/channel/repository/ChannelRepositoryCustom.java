package com.example.app.core.channel.repository;

import com.example.app.core.channel.model.Channel;

import java.util.List;

public interface ChannelRepositoryCustom {
    List<Channel> findBySpaceId(long spaceId);
}
