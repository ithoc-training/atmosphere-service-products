package de.ithoc.atmosphere.service.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConditionRepository extends JpaRepository<ConditionEntity, UUID> {

    Optional<ConditionEntity> findByName(String condition);

}
