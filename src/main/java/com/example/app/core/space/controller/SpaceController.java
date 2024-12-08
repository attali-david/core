package com.example.app.core.space.controller;

import com.example.app.core.space.model.AcknowledgeRequest;
import com.example.app.core.space.model.Space;
import com.example.app.core.space.model.SpaceUserRelationship;
import com.example.app.core.space.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/space")
public class SpaceController {
    static Logger LOGGER = Logger.getLogger(SpaceController.class.getName());
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping()
    public ResponseEntity<List<Space>> getSpaces() {
        try {
            List<Space> spaces = spaceService.findSpaces();
            return new ResponseEntity<>(spaces, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<Space> createSpace(@RequestBody Space space) {
        try {
            Space createdGroup = spaceService.createSpace(space);

            return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Space> deleteSpace(@PathVariable Long id) {
        try {
            spaceService.deleteSpace(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/invite")
    public ResponseEntity<?> getSpaceInvites(@RequestParam("user_id") Long userId) {
        try {
            List<SpaceUserRelationship> invites = spaceService.findSpaceInvite(userId);
            return new ResponseEntity<>(invites, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/invite")
    public ResponseEntity<?> acknowledgeSpaceInvite(@RequestBody AcknowledgeRequest acknowledgeRequest) {
        try {
            spaceService.acknowledgeSpaceInvite(acknowledgeRequest);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<?> sendSpaceInvite(@RequestBody SpaceUserRelationship spaceUserRelationship) {
        try {
             spaceService.sendSpaceInvite(spaceUserRelationship);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            LOGGER.info(exception.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
