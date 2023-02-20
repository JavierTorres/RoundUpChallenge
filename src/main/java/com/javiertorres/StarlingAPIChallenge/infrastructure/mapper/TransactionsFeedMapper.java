package com.javiertorres.StarlingAPIChallenge.infrastructure.mapper;

import com.javiertorres.StarlingAPIChallenge.domain.Amount;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionFeed;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.GetTransactionsFeedResponseV2;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.TransactionFeedDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionsFeedMapper {
    public static List<TransactionFeed> toListTransactionFeed(GetTransactionsFeedResponseV2 transactionsFeed) {
        if (transactionsFeed.feedItems() == null) {
            return Collections.emptyList();
        }

        return transactionsFeed.feedItems().stream()
            .map(TransactionsFeedMapper::mapToTransactionsFeed)
            .collect(Collectors.toList());
    }

    private static TransactionFeed mapToTransactionsFeed(TransactionFeedDTO transactionFeedDTO) {
        var amountDTO = transactionFeedDTO.amount();
        var amount = new Amount(amountDTO.currency(), amountDTO.minorUnits());
        return new TransactionFeed(amount, transactionFeedDTO.direction(), transactionFeedDTO.source());
    }
}
