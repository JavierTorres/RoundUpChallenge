package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import java.util.Currency;

public record PutSavingGoalRequestV2(String name, Currency currency, TargetDTO target) {}
