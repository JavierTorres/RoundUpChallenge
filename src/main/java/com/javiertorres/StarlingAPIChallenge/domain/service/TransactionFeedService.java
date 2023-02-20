package com.javiertorres.StarlingAPIChallenge.domain.service;

import com.javiertorres.StarlingAPIChallenge.domain.Account;
import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionFeed;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionsFeed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionFeedService {
    private final StarlingClient starlingClient;

    public Mono<TransactionsFeed> getTransactionFeed(
        Mono<Account> accountMono, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) {

        return starlingClient.getTransactionFeed(accountMono, minTransactionTimestamp, maxTransactionTimestamp)
            .map(transactionsFeed -> new TransactionsFeed(
                transactionsFeed.feedItems().stream()
                    .filter(TransactionFeed::isOutbound)
                    .filter(TransactionFeed::isNotInternalTransfer)
                    .peek(transactionFeed -> log.info("TransactionFeed %s".formatted(transactionFeed)))
                    .collect(Collectors.toList())));
    }
}
