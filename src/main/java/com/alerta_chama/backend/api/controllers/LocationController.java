package com.alerta_chama.backend.api.controllers;

import com.alerta_chama.backend.api.dtos.LocationRequest;
import com.alerta_chama.backend.api.dtos.LocationResponse;
import com.alerta_chama.backend.api.dtos.UserResponse;
import com.alerta_chama.backend.core.services.LocationService;
import com.alerta_chama.backend.model.Location;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationRequest request) {
        Location newLocation = locationService.createLocation(request);
        return new ResponseEntity<>(convertToLocationResponse(newLocation), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        return locationService.findLocationById(id)
                .map(this::convertToLocationResponse)
                .map(locationResponse -> new ResponseEntity<>(locationResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        List<LocationResponse> locations = locationService.findAllLocations().stream()
                .map(this::convertToLocationResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LocationResponse>> getLocationsByUserId(@PathVariable Long userId) {
        List<LocationResponse> locations = locationService.findAllLocationsByUserId(userId).stream()
                .map(this::convertToLocationResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    private LocationResponse convertToLocationResponse(Location location) {

        UserResponse ownerUserResponse = new UserResponse(
                location.getOwnerUser().getId(),
                location.getOwnerUser().getUsername(),
                location.getOwnerUser().getEmail(),
                location.getOwnerUser().getRole(),
                location.getOwnerUser().getPassword() // Apenas para demonstração academica
        );
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getDescription(),
                ownerUserResponse
        );
    }
}