package com.javiertorres.StarlingAPIChallenge.domain.feature;

import com.javiertorres.StarlingAPIChallenge.domain.Amount;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionFeed;
import com.javiertorres.StarlingAPIChallenge.domain.TransactionsFeed;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionsFeedFeature {

    public static final TransactionsFeed INSTANCE = create();

    public static final String GET_TRANSACTIONS_FEED_JSON_RESPONSE = """
        {
           "feedItems": [
             {
               "amount": {
                 "currency": "GBP",
                 "minorUnits": 4242
               },
               "direction": "IN",
               "source": "FASTER_PAYMENTS_IN"
             },
             {
               "amount": {
                 "currency": "GBP",
                 "minorUnits": 170
               },
               "direction": "OUT",
               "source": "FASTER_PAYMENTS_OUT"
             },
             {
               "amount": {
                 "currency": "GBP",
                 "minorUnits": 1234
               },
               "direction": "IN",
               "source": "INTERNAL_TRANSFER"
             },
             {
               "amount": {
                 "currency": "GBP",
                 "minorUnits": 4321
               },
               "direction": "OUT",
               "source": "INTERNAL_TRANSFER"
             }
           ]
         }
        """
        .stripIndent();

    public static TransactionsFeed create() {
        var gbpCurrency = Currency.getInstance("GBP");
        var transactionFeed1 = new TransactionFeed(new Amount(gbpCurrency, new BigDecimal(4242)), "IN", "FASTER_PAYMENTS_IN");
        var transactionFeed2 = new TransactionFeed(new Amount(gbpCurrency, new BigDecimal(170)), "OUT", "FASTER_PAYMENTS_OUT");
        var transactionFeed3 = new TransactionFeed(new Amount(gbpCurrency, new BigDecimal(1234)), "IN", "INTERNAL_TRANSFER");
        var transactionFeed4 = new TransactionFeed(new Amount(gbpCurrency, new BigDecimal(4321)), "OUT", "INTERNAL_TRANSFER");

        return new TransactionsFeed(List.of(transactionFeed1, transactionFeed2, transactionFeed3, transactionFeed4));
    }
}