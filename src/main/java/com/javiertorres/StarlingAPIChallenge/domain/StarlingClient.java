package com.javiertorres.StarlingAPIChallenge.domain;

import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Optional;

public interface StarlingClient {

    Mono<Accounts> getAccounts();

    Mono<TransactionsFeed> getTransactionFeed(
        Mono<Account> accountMono, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp);

    Mono<String> putNewSavingGoal(String accountId, String name, Currency currency, Long minorUnits);

    Mono<Optional<String>> getSavingsGoal(String accountId);

    Mono<Boolean> topUpExistingSavingGoal(String accountId, String savingGoalUid, Currency currency, Long minorUnits, String transferUid);

}
