package de.ithoc.atmosphere.service.products.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "product")
@Getter
@Setter
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  private String description;
  private BigDecimal price;
  private String image;

  @ManyToOne
  private ConditionEntity condition;

  @ManyToOne
  private CategoryEntity category;

}

