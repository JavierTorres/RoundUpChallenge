package com.javiertorres.StarlingAPIChallenge.domain.service;

import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionsFeed;
import com.javiertorres.StarlingAPIChallenge.domain.feature.AccountFeature;
import com.javiertorres.StarlingAPIChallenge.domain.feature.TransactionsFeedFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionFeedServiceUTest {

    @Mock
    private StarlingClient starlingClient;

    @InjectMocks
    private TransactionFeedService testObj;

    @Test
    void givenTransactionsFeeds_whenGetTransactionFeed_returnsOutboundAndNotInternal() {
        // Given
        var account = Mono.just(AccountFeature.INSTANCE);
        var minTransactionTimestamp = ZonedDateTime.now().minusDays(7);
        var maxTransactionTimestamp = ZonedDateTime.now();
        var transactionsFeed = Mono.just(TransactionsFeedFeature.INSTANCE);

        when(starlingClient
            .getTransactionFeed(account, minTransactionTimestamp, maxTransactionTimestamp))
            .thenReturn(transactionsFeed);

        // When
        var transactionFeeds = testObj.getTransactionFeed(account, minTransactionTimestamp, maxTransactionTimestamp).block();

        // Then
        assertThat(transactionFeeds.feedItems())
            .extracting("source", "direction")
            .containsOnly(tuple("FASTER_PAYMENTS_OUT", "OUT"));
    }

    @Test
    void givenATransactionsFeedsEmpty_whenGetTransactionFeed_returnsEmpty() {
        // Given
        var account = Mono.just(AccountFeature.INSTANCE);
        var minTransactionTimestamp = ZonedDateTime.now().minusDays(7);
        var maxTransactionTimestamp = ZonedDateTime.now();
        var transactionsFeed = Mono.just(new TransactionsFeed(Collections.emptyList()));

        when(starlingClient
            .getTransactionFeed(account, minTransactionTimestamp, maxTransactionTimestamp))
            .thenReturn(transactionsFeed);

        // When
        var transactionFeeds = testObj.getTransactionFeed(account, minTransactionTimestamp, maxTransactionTimestamp).block();

        // Then
        assertThat(transactionFeeds.feedItems()).isEmpty();
    }
}