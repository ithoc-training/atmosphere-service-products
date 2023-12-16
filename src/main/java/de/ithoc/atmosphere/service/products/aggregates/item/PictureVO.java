package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.PackagePrivate;

import java.io.File;

/**
 * Domain-driven Design (DDD) Value Object for a picture of an item.
 */
@AllArgsConstructor
@PackagePrivate
@EqualsAndHashCode
@ToString
public class PictureVO {

    String filename;
    String link;

    String getUrl() {
        return filename + File.separator + link;
    }

}
