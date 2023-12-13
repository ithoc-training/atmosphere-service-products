package de.ithoc.atmosphere.service.searchandfiltering.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
  private Integer price;
  private String image;

  @ManyToOne
  private CategoryEntity category;

}

