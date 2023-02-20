package com.javiertorres.StarlingAPIChallenge.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class AmountUTest {

    @Test
    void givenAmount_whenCallingRoundUp_returnsRoundUp() {
        var amount = new Amount(Currency.getInstance("GBP"), new BigDecimal(1240));
        assertThat(amount.roundUp()).isEqualTo(new BigDecimal("0.60"));
    }
}