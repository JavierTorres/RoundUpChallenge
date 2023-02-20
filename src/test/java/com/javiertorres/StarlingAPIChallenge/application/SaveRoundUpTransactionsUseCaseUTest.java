package com.javiertorres.StarlingAPIChallenge.application;

import com.javiertorres.StarlingAPIChallenge.domain.feature.AccountFeature;
import com.javiertorres.StarlingAPIChallenge.domain.feature.TransactionsFeedFeature;
import com.javiertorres.StarlingAPIChallenge.domain.service.AccountService;
import com.javiertorres.StarlingAPIChallenge.domain.service.SavingGoalService;
import com.javiertorres.StarlingAPIChallenge.domain.service.TransactionFeedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveRoundUpTransactionsUseCaseUTest {

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionFeedService transactionFeedService;
    @Mock
    private SavingGoalService savingGoalService;
    @InjectMocks
    private SaveRoundUpTransactionsUseCase testObj;

    @Test
    void givenIntervalDateTime_whenRunningUseCase_thenReturnsResult() {
        // Given
        var minTransactionTimestamp = ZonedDateTime.now().minusDays(7);
        var maxTransactionTimestamp = ZonedDateTime.now();
        var account = Mono.just(AccountFeature.INSTANCE);
        var transactionFeeds = Mono.just(TransactionsFeedFeature.create());

        when(accountService.getPrimaryAccount()).thenReturn(account);
        when(transactionFeedService.getTransactionFeed(account, minTransactionTimestamp, maxTransactionTimestamp))
            .thenReturn(transactionFeeds);
        when(savingGoalService.addRoundUp(new BigDecimal("2.33"), account)).thenReturn(Mono.just(Boolean.TRUE));

        // When
        var result = testObj.run(minTransactionTimestamp, maxTransactionTimestamp).block();

        // Then
        assertThat(result).isTrue();
    }
}