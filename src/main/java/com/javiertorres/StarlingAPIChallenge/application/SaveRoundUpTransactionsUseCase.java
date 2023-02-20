package com.javiertorres.StarlingAPIChallenge.application;

import com.javiertorres.StarlingAPIChallenge.domain.TransactionsFeed;
import com.javiertorres.StarlingAPIChallenge.domain.service.AccountService;
import com.javiertorres.StarlingAPIChallenge.domain.service.SavingGoalService;
import com.javiertorres.StarlingAPIChallenge.domain.service.TransactionFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveRoundUpTransactionsUseCase {
    private final AccountService accountService;
    private final TransactionFeedService transactionFeedService;
    private final SavingGoalService savingGoalService;

    public Mono<Boolean> run(ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) {
        var account = accountService.getPrimaryAccount();
        var transactionFeeds = transactionFeedService.getTransactionFeed(account, minTransactionTimestamp, maxTransactionTimestamp);
        return transactionFeeds
            .map(TransactionsFeed::addRoundUpFeeds)
            .zipWhen(roundUp -> savingGoalService.addRoundUp(roundUp, account), (transactionsFeedDTO, addRoundUp) -> addRoundUp);
    }
}
