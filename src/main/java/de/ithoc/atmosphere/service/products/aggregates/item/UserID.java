package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.PackagePrivate;

/**
 * This class represents the identity of an ItemRoot aggregate root entity.
 * <p>The identity is the username of the user that created the item.</p>
 */
@AllArgsConstructor
@PackagePrivate
@ToString
public class UserID {

    String username;

}
