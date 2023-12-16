package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.PackagePrivate;

@AllArgsConstructor
@PackagePrivate
@ToString
public class CategoryEntity {

        CategoryID id;
        String name;

        public CategoryEntity(String name) {
            this.id = new CategoryID();
            this.name = name;
        }

}
