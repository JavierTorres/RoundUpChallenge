package com.javiertorres.StarlingAPIChallenge.infrastructure.mapper;

import com.javiertorres.StarlingAPIChallenge.domain.Account;
import com.javiertorres.StarlingAPIChallenge.domain.Accounts;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.GetAccountsResponseV2;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountsMapper {
    public static Accounts mapToAccounts(GetAccountsResponseV2 getAccountsResponseV2) {
        if (getAccountsResponseV2.accounts() == null) {
            return new Accounts(Collections.emptyList());
        }

        return new Accounts(getAccountsResponseV2.accounts()
            .stream().map(accountDTO ->
                new Account(accountDTO.accountUid(), accountDTO.defaultCategory(), accountDTO.accountType(), accountDTO.currency()))
            .collect(Collectors.toList()));
    }
}
