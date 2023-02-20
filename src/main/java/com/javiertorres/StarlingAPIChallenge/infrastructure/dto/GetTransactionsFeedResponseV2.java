package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetTransactionsFeedResponseV2(List<TransactionFeedDTO> feedItems) {
}
