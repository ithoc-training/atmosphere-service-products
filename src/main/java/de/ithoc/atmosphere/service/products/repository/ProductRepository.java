package de.ithoc.atmosphere.service.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    // SELECT * FROM PRODUCT WHERE name like '%Pro%' OR description like '%Pro%';
    Page<ProductEntity> findByNameContainingOrDescriptionContaining(
            Pageable pageable, String searchName, String searchDescription);

    Page<ProductEntity> findByCategory(Pageable pageable, CategoryEntity category);

    Page<ProductEntity> findByPriceBetween(Pageable pageable, BigDecimal fromPrice, BigDecimal toPrice);

    Page<ProductEntity> findByCondition(Pageable pageable, ConditionEntity condition);

    Page<ProductEntity> findByCategoryAndConditionAndPriceBetween(
            Pageable pageable,
            CategoryEntity category, ConditionEntity condition,
            BigDecimal fromPrice, BigDecimal toPrice);
}
