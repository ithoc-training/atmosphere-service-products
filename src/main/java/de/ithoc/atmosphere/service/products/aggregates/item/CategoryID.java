package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.PackagePrivate;

import java.util.UUID;

@AllArgsConstructor
@PackagePrivate
@ToString
public class CategoryID {

    UUID uuid;

    public CategoryID() {
        this.uuid = UUID.randomUUID();
    }

}
