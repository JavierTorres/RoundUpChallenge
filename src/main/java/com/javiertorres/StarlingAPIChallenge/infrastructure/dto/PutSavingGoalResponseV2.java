package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PutSavingGoalResponseV2(String savingsGoalUid, boolean success) {}
