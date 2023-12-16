package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.ToString;
import lombok.experimental.PackagePrivate;

import java.util.UUID;

@PackagePrivate
@ToString
public class ConditionID {

    UUID uuid;

    public ConditionID() {
        this.uuid = UUID.randomUUID();
    }

}
