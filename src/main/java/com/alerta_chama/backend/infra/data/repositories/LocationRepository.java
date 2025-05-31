package com.alerta_chama.backend.infra.data.repositories;

import com.alerta_chama.backend.model.Location;
import com.alerta_chama.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByOwnerUser(User ownerUser);

    List<Location> findByOwnerUserId(Long ownerUserId);
}