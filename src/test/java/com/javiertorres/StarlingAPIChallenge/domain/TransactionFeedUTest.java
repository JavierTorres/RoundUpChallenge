package com.javiertorres.StarlingAPIChallenge.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionFeedUTest {
    @Test
    void givenAnOutTransactionFeed_whenCallingIsBound_returnsTrue() {
        var transactionFeed = new TransactionFeed(null, "OUT", null);
        assertThat(transactionFeed.isOutbound()).isTrue();
    }

    @Test
    void givenAnInTransactionFeed_whenCallingIsBound_returnsFalse() {
        var transactionFeed = new TransactionFeed(null, "IN", null);
        assertThat(transactionFeed.isOutbound()).isFalse();
    }

    @Test
    void givenATransactionFeedWithEmptyDirection_whenCallingIsBound_returnsFalse() {
        var transactionFeed = new TransactionFeed(null, null, null);
        assertThat(transactionFeed.isOutbound()).isFalse();
    }

    @Test
    void givenAInternalTransactionFeed_whenCallingIsNotInternalTransfer_returnsFalse() {
        var transactionFeed = new TransactionFeed(null, "OUT", "INTERNAL_TRANSFER");
        assertThat(transactionFeed.isNotInternalTransfer()).isFalse();
    }

    @Test
    void givenANotInternalTransactionFeed_whenCallingIsNotInternalTransfer_returnsTrue() {
        var transactionFeed = new TransactionFeed(null, "OUT", "FAST_PAYMENT");
        assertThat(transactionFeed.isNotInternalTransfer()).isTrue();
    }

    @Test
    void givenATransactionFeedWithEmptySource_whenCallingIsNotInternalTransfer_returnsTrue() {
        var transactionFeed = new TransactionFeed(null, "OUT", null);
        assertThat(transactionFeed.isNotInternalTransfer()).isTrue();
    }
}