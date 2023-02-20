package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AmountDTO(Currency currency, BigDecimal minorUnits) {}
