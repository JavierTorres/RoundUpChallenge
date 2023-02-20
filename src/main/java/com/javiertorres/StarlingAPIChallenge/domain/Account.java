package com.javiertorres.StarlingAPIChallenge.domain;

import java.util.Currency;
import java.util.Objects;

public record Account(String accountUid, String defaultCategory, String accountType, Currency currency) {
    private static final String PRIMARY_ACCOUNT_TYPE = "PRIMARY";
    public boolean isPrimaryAccountType() {
        return Objects.equals(accountType, PRIMARY_ACCOUNT_TYPE);
    }
}
