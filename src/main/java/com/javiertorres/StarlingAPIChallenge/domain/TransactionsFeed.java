package com.javiertorres.StarlingAPIChallenge.domain;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
public record TransactionsFeed(List<TransactionFeed> feedItems) {
    public BigDecimal addRoundUpFeeds() {
        if (Objects.isNull(feedItems )) {
            return new BigDecimal(0);
        }

        return feedItems.stream()
            .map(feedItems -> feedItems.amount().roundUp())
            .peek(roundUp -> log.info("The RoundUp is %s".formatted(roundUp)))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
