package com.javiertorres.StarlingAPIChallenge.domain;

import com.javiertorres.StarlingAPIChallenge.domain.feature.TransactionsFeedFeature;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionsFeedUTest {

    @Test
    void givenTransactionsFeed_whenAddingRoundUpFeeds_thenReturnsRoundUp() {
        var roundUp = TransactionsFeedFeature.INSTANCE.addRoundUpFeeds();
        assertThat(roundUp).isEqualTo(new BigDecimal("2.33"));
    }

    @Test
    void givenNullTransactionsFeed_whenAddingRoundUpFeeds_thenReturnsZero() {
        var roundUp = new TransactionsFeed(null).addRoundUpFeeds();
        assertThat(roundUp).isEqualTo(new BigDecimal("0"));
    }

    @Test
    void givenEmptyTransactionsFeed_whenAddingRoundUpFeeds_thenReturnsZero() {
        var roundUp = new TransactionsFeed(Collections.emptyList()).addRoundUpFeeds();
        assertThat(roundUp).isEqualTo(new BigDecimal("0"));
    }
}