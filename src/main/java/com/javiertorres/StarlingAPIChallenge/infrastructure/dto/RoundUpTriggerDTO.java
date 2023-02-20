package com.javiertorres.StarlingAPIChallenge.infrastructure.dto;

import java.time.ZonedDateTime;

public record RoundUpTriggerDTO(ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) {
}
