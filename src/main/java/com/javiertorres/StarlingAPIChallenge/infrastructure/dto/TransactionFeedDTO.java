package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionFeedDTO(AmountDTO amount, String direction, String source) {}
