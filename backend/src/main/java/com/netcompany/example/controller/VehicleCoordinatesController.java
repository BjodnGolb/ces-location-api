package com.netcompany.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netcompany.example.domain.dto.CoordinatesDto;
import com.netcompany.example.service.CoordinatesService;

import io.swagger.annotations.ApiOperation;

/**
 * TODO
 *
 * @author Bjørn Golberg
 */
@RequestMapping("/api/coordinates")
@RestController
public class VehicleCoordinatesController {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleCoordinatesController.class);
    private final CoordinatesService service;

    private static final String API_EAST_INDIA_KEY = "EASTINDIAR0FLM40Y0L0SW4G";
    private static final String API_SOULTRAIN_KEY = "SOULTRAINR0FLM40Y0L0SW4G";

    @Autowired
    public VehicleCoordinatesController(final CoordinatesService coordinatesService) {
        this.service = coordinatesService;
    }

    @ApiOperation(value = "Get all coordinates")
    @GetMapping()
    public ResponseEntity<List<CoordinatesDto>> getCoordinates(@RequestParam final String key) {
        LOG.info("getCoordinates() called");
        if (key.equals(API_EAST_INDIA_KEY)) {
            return ResponseEntity.ok(service.getCoordinates("soultrain"));
        } else if (key.equals(API_SOULTRAIN_KEY)) {
            return ResponseEntity.ok(service.getCoordinates("eastindia"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
