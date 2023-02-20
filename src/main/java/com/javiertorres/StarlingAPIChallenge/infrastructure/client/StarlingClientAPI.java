package com.javiertorres.StarlingAPIChallenge.infrastructure.client;

import com.javiertorres.StarlingAPIChallenge.domain.Account;
import com.javiertorres.StarlingAPIChallenge.domain.Accounts;
import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionsFeed;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.GetAccountsResponseV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.GetSavingsGoalsResponseV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.GetTransactionsFeedResponseV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.PutSavingGoalRequestV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.PutSavingGoalResponseV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.SavingGoalDTO;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.TargetDTO;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.TopUpAmountDTO;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.TopUpRequestV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.TopUpResponseV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.mapper.AccountsMapper;
import com.javiertorres.StarlingAPIChallenge.infrastructure.mapper.TransactionsFeedMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class StarlingClientAPI implements StarlingClient {
    private static final String URI_FEED_ACCOUNT_CATEGORY = "/feed/account/{param}/category/{param}/transactions-between";
    private static final String URI_ACCOUNTS = "/accounts";
    private static final String URI_SAVINGS_GOALS = "/account/{param}/savings-goals";

    private static final String URI_SAVINGS_GOALS_TOPUP = "/account/{param}/savings-goals/{param}/add-money/{param}";
    private final WebClient starlingWebClient;

    @Override
    public Mono<Accounts> getAccounts() {
        return starlingWebClient.get()
            .uri(URI_ACCOUNTS)
            .retrieve()
            .bodyToMono(GetAccountsResponseV2.class)
            .map(AccountsMapper::mapToAccounts);
    }

    @Override
    public Mono<TransactionsFeed> getTransactionFeed(
        Mono<Account> accountMono, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) {

        return accountMono.zipWhen(account -> {
                var accountId = account.accountUid();
                var categoryId = account.defaultCategory();

                return starlingWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path(URI_FEED_ACCOUNT_CATEGORY)
                        .queryParam("minTransactionTimestamp", minTransactionTimestamp)
                        .queryParam("maxTransactionTimestamp", maxTransactionTimestamp)
                        .build(accountId, categoryId))
                    .retrieve()
                    .bodyToMono(GetTransactionsFeedResponseV2.class)
                    .map(TransactionsFeedMapper::toListTransactionFeed)
                    .map(transactionFeeds -> new TransactionsFeed(transactionFeeds));
            },
            (accounts, transactionsFeed) -> transactionsFeed);
    }

    @Override
    public Mono<String> putNewSavingGoal(String accountId, String name, Currency currency, Long minorUnits) {
        var targetDTO = new TargetDTO(currency, minorUnits);
        var savingGoalRequest = new PutSavingGoalRequestV2(name, currency, targetDTO);

        return starlingWebClient.put()
            .uri(uriBuilder -> uriBuilder.path(URI_SAVINGS_GOALS).build(accountId))
            .bodyValue(savingGoalRequest)
            .retrieve()
            .bodyToMono(PutSavingGoalResponseV2.class)
            .map(PutSavingGoalResponseV2::savingsGoalUid);
    }

    @Override
    public Mono<Optional<String>> getSavingsGoal(String accountId) {
        return starlingWebClient.get()
            .uri(uriBuilder -> uriBuilder.path(URI_SAVINGS_GOALS).build(accountId))
            .retrieve()
            .bodyToMono(GetSavingsGoalsResponseV2.class)
            .map(getSavingsGoalsResponseV2 -> {
                if (getSavingsGoalsResponseV2.savingsGoalList() == null) {
                    return Optional.empty();
                } else {
                    return getSavingsGoalsResponseV2.savingsGoalList().stream()
                        .findAny()
                        .map(SavingGoalDTO::savingsGoalUid);
                }
            });
    }

    @Override
    public Mono<Boolean> topUpExistingSavingGoal(String accountId, String savingGoalUid, Currency currency, Long minorUnits, String transferUid) {
        var topUpRequestV2 = new TopUpRequestV2(new TopUpAmountDTO(currency, minorUnits));

        return starlingWebClient.put()
            .uri(uriBuilder -> uriBuilder.path(URI_SAVINGS_GOALS_TOPUP)
                .build(accountId, savingGoalUid, transferUid))
            .bodyValue(topUpRequestV2)
            .retrieve()
            .bodyToMono(TopUpResponseV2.class)
            .map(TopUpResponseV2::success);
    }
}
