package com.alerta_chama.backend.core.services;

import com.alerta_chama.backend.api.dtos.LocationRequest;
import com.alerta_chama.backend.model.Location;
import com.alerta_chama.backend.model.User;
import com.alerta_chama.backend.infra.data.repositories.LocationRepository;
import com.alerta_chama.backend.infra.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public Location createLocation(LocationRequest request) {

        User ownerUser = userRepository.findById(request.getOwnerUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário proprietário não encontrado."));

        Location newLocation = new Location();
        newLocation.setName(request.getName());
        newLocation.setAddress(request.getAddress());
        newLocation.setDescription(request.getDescription());
        newLocation.setOwnerUser(ownerUser);

        return locationRepository.save(newLocation);
    }

    public Optional<Location> findLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> findAllLocationsByUserId(Long userId) {
        User ownerUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        return locationRepository.findByOwnerUser(ownerUser);
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

}