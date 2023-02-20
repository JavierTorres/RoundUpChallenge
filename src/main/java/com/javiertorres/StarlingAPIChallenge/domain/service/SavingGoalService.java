package com.javiertorres.StarlingAPIChallenge.domain.service;

import com.javiertorres.StarlingAPIChallenge.domain.Account;
import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class SavingGoalService {
    private final StarlingClient starlingClient;

    public Mono<Boolean> addRoundUp(BigDecimal roundUp, Mono<Account> accountMono) {
        return accountMono.zipWhen(account -> {
            var accountId = account.accountUid();
            var currency = account.currency();

            log.info("Adding the roundUp %s for the account %s".formatted(roundUp, accountId));

            return starlingClient.getSavingsGoal(accountId)
                .zipWhen(savingGolUId -> {
                    var roundUpLong = roundUp.movePointRight(2).longValue();

                    if (savingGolUId.isEmpty()) {
                        return putNewAndTopUpSavingsGoal(accountId, currency, roundUpLong);
                    }

                    return topUp(accountId, currency, savingGolUId.get(), roundUpLong);
                }, (getSavingGoalResponseV2, addRoundUp) -> addRoundUp);

        }, (account, savingGoal) -> savingGoal);
    }

    private Mono<Boolean> putNewAndTopUpSavingsGoal(String accountId, Currency currency, long roundUpLong) {
        var name = "Savings for the account %s".formatted(accountId);

        return starlingClient.putNewSavingGoal(accountId, name, currency, roundUpLong)
            .zipWhen(newSavingUId -> topUp(accountId, currency, newSavingUId, roundUpLong),
                (newSavinGoalResponse, topUpResponse) -> topUpResponse);
    }

    private Mono<Boolean> topUp(String accountId, Currency currency, String savingGolUId, long roundUpLong) {
        var transferUid = UUID.randomUUID().toString();
        return starlingClient.topUpExistingSavingGoal(accountId, savingGolUId, currency, roundUpLong, transferUid);
    }
}
