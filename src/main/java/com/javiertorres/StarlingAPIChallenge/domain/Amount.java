package com.javiertorres.StarlingAPIChallenge.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Amount(Currency currency, BigDecimal minorUnits) {

    private static final BigDecimal HUNDRED = new BigDecimal("100.00");
    public BigDecimal roundUp() {
        return minorUnits
            .divide(HUNDRED, RoundingMode.CEILING)
            .subtract(minorUnits.movePointLeft(2))
            .setScale(2, RoundingMode.CEILING);
    }
}
