package de.ithoc.atmosphere.service.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    @Query("SELECT item FROM item item WHERE " +
            "(item.name like concat('%', :term, '%') or item.description like concat('%', :term, '%')) " +
            "and (:fromPrice <= item.price and item.price <= :toPrice " +
            "and item.category.id = :categoryId and item.condition.id = :conditionId)"
    )
    Page<ItemEntity> findItems(
            Pageable pageable,
            @Param("term") String term,
            @Param("fromPrice") String fromPrice, @Param("toPrice") String toPrice,
            @Param("categoryId") UUID categoryId, @Param("conditionId") UUID conditionId
    );

}
