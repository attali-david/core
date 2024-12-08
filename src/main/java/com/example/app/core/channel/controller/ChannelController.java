package com.example.app.core.channel.controller;

import com.example.app.core.channel.model.Channel;
import com.example.app.core.channel.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    static Logger LOGGER = Logger.getLogger(ChannelController.class.getName());

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping()
    public ResponseEntity<?> createChannel(@RequestBody Channel channel) {
        try {
           channelService.createChannel(channel);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable long channelId) {
        try {
            channelService.deleteChannel(channelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{spaceId}")
    public ResponseEntity<List<Channel>> getChannels(@PathVariable long spaceId) {
        try {
            List<Channel> channels = channelService.findChannels(spaceId);
            return new ResponseEntity<>(channels, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
