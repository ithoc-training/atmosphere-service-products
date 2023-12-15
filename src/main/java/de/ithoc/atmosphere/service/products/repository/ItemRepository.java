package de.ithoc.atmosphere.service.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    // SELECT * FROM PRODUCT WHERE name like '%Pro%' OR description like '%Pro%';
    Page<ItemEntity> findByNameContainingOrDescriptionContaining(
            Pageable pageable, String searchName, String searchDescription);

    Page<ItemEntity> findByCategory(Pageable pageable, CategoryEntity category);

    Page<ItemEntity> findByPriceBetween(Pageable pageable, BigDecimal fromPrice, BigDecimal toPrice);

    Page<ItemEntity> findByCondition(Pageable pageable, ConditionEntity condition);

    Page<ItemEntity> findByCategoryAndConditionAndPriceBetween(
            Pageable pageable,
            CategoryEntity category, ConditionEntity condition,
            BigDecimal fromPrice, BigDecimal toPrice);
}
