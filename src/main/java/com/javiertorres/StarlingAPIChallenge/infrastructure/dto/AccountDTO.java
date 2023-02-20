package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountDTO(String accountUid, String defaultCategory, String accountType, Currency currency) {
}
