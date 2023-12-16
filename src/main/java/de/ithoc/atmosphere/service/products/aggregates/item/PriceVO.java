package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.PackagePrivate;

import java.math.BigDecimal;

@AllArgsConstructor
@PackagePrivate
@EqualsAndHashCode
@ToString
public class PriceVO {

    BigDecimal amount;

}
