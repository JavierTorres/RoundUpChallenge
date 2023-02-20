package com.javiertorres.StarlingAPIChallenge.domain;

import java.util.Objects;

public record TransactionFeed(Amount amount, String direction, String source) {
    private static final String OUTBOUND_TRANSACTION = "OUT";
    private static final String INTERNAL_TRANSACTION = "INTERNAL_TRANSFER";

    public boolean isOutbound() {
        return Objects.equals(direction, OUTBOUND_TRANSACTION) ? true : false;
    }

    public boolean isNotInternalTransfer() {
        return !Objects.equals(source, INTERNAL_TRANSACTION) ? true : false;
    }
}
