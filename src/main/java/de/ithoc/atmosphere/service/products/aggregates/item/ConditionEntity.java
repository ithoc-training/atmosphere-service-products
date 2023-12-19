package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.ToString;
import lombok.experimental.PackagePrivate;

@PackagePrivate
@ToString
public class ConditionEntity {

    ConditionID id;

    String name;

    public ConditionEntity(String name) {
        this.id = new ConditionID();
        this.name = name;
    }

}
