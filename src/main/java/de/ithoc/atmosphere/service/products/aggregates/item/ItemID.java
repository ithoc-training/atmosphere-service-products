package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.ToString;
import lombok.experimental.PackagePrivate;

import java.util.UUID;

@PackagePrivate
@ToString
public class ItemID {

    private final UUID uuid;

    public ItemID() {
        this.uuid = UUID.randomUUID();
    }

}
