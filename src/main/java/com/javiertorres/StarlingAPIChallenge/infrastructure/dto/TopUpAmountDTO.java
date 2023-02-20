package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import java.util.Currency;

public record TopUpAmountDTO(Currency currency, Long minorUnits) {
}
