package com.javiertorres.StarlingAPIChallenge.infrastructure.controller;

import com.javiertorres.StarlingAPIChallenge.application.SaveRoundUpTransactionsUseCase;
import com.javiertorres.StarlingAPIChallenge.infrastructure.dto.RoundUpTriggerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class RoundUpController {

    @Autowired
    SaveRoundUpTransactionsUseCase saveRoundUpTransactionsUseCase;

    @PostMapping(value = "/round-up")
    public Mono<Boolean> roundUp(
        @RequestBody RoundUpTriggerDTO roundUpTriggerDTO) {
        log.info("started");

        return saveRoundUpTransactionsUseCase
            .run(roundUpTriggerDTO.minTransactionTimestamp(), roundUpTriggerDTO.maxTransactionTimestamp());
    }
}
