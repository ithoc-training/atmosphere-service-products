package de.ithoc.atmosphere.service.searchandfiltering.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "category")
@Getter
@Setter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

}
