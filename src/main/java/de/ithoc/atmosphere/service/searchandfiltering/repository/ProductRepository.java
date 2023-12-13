package de.ithoc.atmosphere.service.searchandfiltering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    // SELECT * FROM PRODUCT WHERE name like '%Pro%' OR description like '%Pro%';
    List<ProductEntity> findByNameContainingOrDescriptionContaining(String searchName, String searchDescription);

    List<ProductEntity> findByCategory(CategoryEntity category);

}
