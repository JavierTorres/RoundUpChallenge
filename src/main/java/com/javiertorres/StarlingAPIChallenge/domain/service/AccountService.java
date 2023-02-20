package com.javiertorres.StarlingAPIChallenge.domain.service;

import com.javiertorres.StarlingAPIChallenge.domain.Account;
import com.javiertorres.StarlingAPIChallenge.domain.StarlingClient;
import com.javiertorres.StarlingAPIChallenge.domain.exception.NotPrimaryAccountException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final StarlingClient starlingClient;

    public Mono<Account> getPrimaryAccount() {
        var accountsMono = starlingClient.getAccounts();

        if (Objects.isNull(accountsMono)) {
            throw new NotPrimaryAccountException();
        }

        return accountsMono.map(accounts -> accounts.accounts().stream()
                .filter(Account::isPrimaryAccountType)
                .peek(account -> log.info("The primary account is %s".formatted(account)))
                .findAny()
                .orElseThrow(() -> new NotPrimaryAccountException()));
    }
}
