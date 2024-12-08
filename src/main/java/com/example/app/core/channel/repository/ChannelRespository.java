package com.example.app.core.channel.repository;

import com.example.app.core.channel.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRespository extends JpaRepository<Channel, Long>, ChannelRepositoryCustom {
}
