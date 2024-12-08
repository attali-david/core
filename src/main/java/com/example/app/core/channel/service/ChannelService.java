package com.example.app.core.channel.service;

import com.example.app.core.channel.model.Channel;

import java.util.List;

public interface ChannelService {
    List<Channel> findChannels(long spaceId);

    void createChannel(Channel channel) throws Exception;

    void deleteChannel(long channelId) throws Exception;
}
