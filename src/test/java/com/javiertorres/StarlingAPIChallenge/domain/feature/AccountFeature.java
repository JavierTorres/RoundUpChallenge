package com.javiertorres.StarlingAPIChallenge.domain.feature;

import com.javiertorres.StarlingAPIChallenge.domain.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Currency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountFeature {

    public static final Account INSTANCE = create("PRIMARY");

    public static final String GET_ACCOUNTS_JSON_RESPONSE = """
        {
            "accounts": [
                {
                    "accountUid": "31c1004b-4cd1-4b7a-83fc-80b0ab3adfd5",
                    "accountType": "PRIMARY",
                    "defaultCategory": "b9fcb48c-a50b-4662-97cb-41191c863677",
                    "currency": "GBP",
                    "createdAt": "2023-02-13T11:04:56.060Z",
                    "name": "Personal"
                }
            ]
        }
        """
        .stripIndent();

    public static Account create(String accountType) {
        return new Account("31c1004b-4cd1-4b7a-83fc-80b0ab3adfd5",
            "b9fcb48c-a50b-4662-97cb-41191c863677",
            accountType,
            Currency.getInstance("GBP"));
    }
}