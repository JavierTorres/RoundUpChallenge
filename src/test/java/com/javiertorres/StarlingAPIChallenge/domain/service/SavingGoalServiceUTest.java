package com.javiertorres.StarlingAPIChallenge.domain.service;

import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import com.javiertorres.StarlingAPIChallenge.domain.feature.AccountFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavingGoalServiceUTest {

    @Mock
    private StarlingClient starlingClient;

    @InjectMocks
    private SavingGoalService testObj;

    @Test
    void givenANotExistingSavingsGoal_whenAddRoundUp_thenSavingsGoalsIsCreatedAndTopUp() {
        // Given
        var roundUp = new BigDecimal("0.20");
        var account = Mono.just(AccountFeature.INSTANCE);
        var accountId = AccountFeature.INSTANCE.accountUid();
        var currency = AccountFeature.INSTANCE.currency();
        var accountName = "Savings for the account %s".formatted(accountId);
        var savingsGoalUid = UUID.randomUUID().toString();
        when(starlingClient.getSavingsGoal(accountId)).thenReturn(Mono.just(Optional.empty()));
        when(starlingClient.putNewSavingGoal(accountId, accountName, currency, 20L)).thenReturn(Mono.just(savingsGoalUid));
        when(starlingClient.topUpExistingSavingGoal(any(), any(), any(), any(), any())).thenReturn(Mono.just(Boolean.TRUE));

        // When
        var result = testObj.addRoundUp(roundUp, account).block();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void givenAExistingSavingsGoal_whenAddRoundUp_thenSavingsGoalsIsJustTopUp() {
        // Given
        var roundUp = new BigDecimal("0.20");
        var account = Mono.just(AccountFeature.INSTANCE);
        var accountId = AccountFeature.INSTANCE.accountUid();
        var savingsGoalUid = UUID.randomUUID().toString();

        when(starlingClient.getSavingsGoal(accountId)).thenReturn(Mono.just(Optional.of(savingsGoalUid)));
        when(starlingClient.topUpExistingSavingGoal(any(), any(), any(), any(), any())).thenReturn(Mono.just(Boolean.TRUE));

        // When
        var result = testObj.addRoundUp(roundUp, account).block();

        // Then
        assertThat(result).isTrue();
    }
}