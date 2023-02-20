package com.javiertorres.StarlingAPIChallenge.domain;

import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class AccountUTest {

    @Test
    void givenAPrimaryAccount_whenCallingIsPrimaryAccountType_returnsTrue() {
        var account = new Account("accountId", "category", "PRIMARY", Currency.getInstance("GBP"));
        assertThat(account.isPrimaryAccountType()).isEqualTo(true);
    }

    @Test
    void givenANotPrimaryAccount_whenCallingIsPrimaryAccountType_returnsFalse() {
        var account = new Account("accountId", "category", "SOMETHING", Currency.getInstance("GBP"));
        assertThat(account.isPrimaryAccountType()).isEqualTo(false);
    }

    @Test
    void givenAnAccountWithEmptyType_whenCallingIsPrimaryAccountType_returnsFalse() {
        var account = new Account("accountId", "category", null, Currency.getInstance("GBP"));
        assertThat(account.isPrimaryAccountType()).isEqualTo(false);
    }
}