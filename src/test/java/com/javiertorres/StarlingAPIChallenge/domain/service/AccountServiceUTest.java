package com.javiertorres.StarlingAPIChallenge.domain.service;

import com.javiertorres.StarlingAPIChallenge.domain.Accounts;
import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import com.javiertorres.StarlingAPIChallenge.domain.exception.NotPrimaryAccountException;
import com.javiertorres.StarlingAPIChallenge.domain.feature.AccountFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceUTest {

    @Mock
    private StarlingClient starlingClient;

    @InjectMocks
    private AccountService testObj;

    @Test
    void givenAPrimaryAccountAndOthers_whenGetPrimaryAccount_returnsPrimaryAccount() {
        // Given
        var primaryAccount = AccountFeature.INSTANCE;
        var other = AccountFeature.create("SECONDARY");
        var accounts = new Accounts(List.of(primaryAccount, other));
        when(starlingClient.getAccounts()).thenReturn(Mono.just(accounts));

        // When
        var result = testObj.getPrimaryAccount().block();

        // Then
        assertThat(result).isEqualTo(primaryAccount);
    }

    @Test
    void givenANotPrimaryAccount_whenGetPrimaryAccount_throwsNotPrimaryAccountException() {
        // Given
        var other = AccountFeature.create("SECONDARY");
        var accounts = new Accounts(List.of(other));
        when(starlingClient.getAccounts()).thenReturn(Mono.just(accounts));

        // When
        var result = testObj.getPrimaryAccount();

        // Then
        assertThatThrownBy(() -> result.block()).isInstanceOf(NotPrimaryAccountException.class);
    }

    @Test
    void givenAnEmptyAccounts_whenGetPrimaryAccount_throwsNotPrimaryAccountException() {
        // Given
        when(starlingClient.getAccounts()).thenReturn(null);

        // When/Then
        assertThatThrownBy(
            () -> testObj.getPrimaryAccount())
            .isInstanceOf(NotPrimaryAccountException.class);
    }

}