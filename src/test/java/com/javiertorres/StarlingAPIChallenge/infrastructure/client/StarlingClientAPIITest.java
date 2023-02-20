package com.javiertorres.StarlingAPIChallenge.infrastructure.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.javiertorres.StarlingAPIChallenge.domain.feature.AccountFeature;
import com.javiertorres.StarlingAPIChallenge.domain.feature.SavingsGoalFeature;
import com.javiertorres.StarlingAPIChallenge.domain.feature.TransactionsFeedFeature;
import com.javiertorres.StarlingAPIChallenge.infrastructure.config.StarlingWebClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.javiertorres.StarlingAPIChallenge.domain.feature.AccountFeature.GET_ACCOUNTS_JSON_RESPONSE;
import static com.javiertorres.StarlingAPIChallenge.domain.feature.SavingsGoalFeature.GET_SAVINGS_GOALS_JSON_RESPONSE;
import static com.javiertorres.StarlingAPIChallenge.domain.feature.SavingsGoalFeature.PUT_SAVINGS_GOAL_JSON_RESPONSE;
import static com.javiertorres.StarlingAPIChallenge.domain.feature.TopUpFeature.PUT_TOP_UP_REQUEST;
import static com.javiertorres.StarlingAPIChallenge.domain.feature.TopUpFeature.PUT_TOP_UP_RESPONSE;
import static com.javiertorres.StarlingAPIChallenge.domain.feature.TransactionsFeedFeature.GET_TRANSACTIONS_FEED_JSON_RESPONSE;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
class StarlingClientAPIITest {

    private WireMockServer wireMockServer;

    private StarlingClientAPI testObj;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        var baseUrl = format("http://localhost:%s", wireMockServer.port());
        var starlingWebClient = new StarlingWebClientConfig().starlingWebClient(baseUrl, "token");
        testObj = new StarlingClientAPI(starlingWebClient);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void givenStarlingClientAPI_whenCallingGetAccounts_returnsAnAccount() {
        // Given
        stubFor(get(urlEqualTo("/accounts")).willReturn(ok()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(GET_ACCOUNTS_JSON_RESPONSE)));

        // When
        var response = testObj.getAccounts().block();

        // Then
        assertThat(response.accounts().size()).isEqualTo(1);
        assertThat(response.accounts().get(0)).isEqualTo(AccountFeature.INSTANCE);
    }

    @Test
    void givenStarlingClientAPI_whenCallingGetAccounts_doesNotReturnAccounts() {
        // Given
        stubFor(get(urlEqualTo("/accounts")).willReturn(ok()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{}")));

        // When
        var response = testObj.getAccounts().block();

        // Then
        assertThat(response.accounts()).isEmpty();
    }

    @Test
    void givenStarlingClientAPI_whenCallingGetTransactionFeed_returnsATransactionsFeed() {
        // Given
        var account = AccountFeature.INSTANCE;
        var minTransactionStr = "2023-02-11T11:18:23Z";
        var maxTransactionStr = "2023-02-18T11:18:23Z";
        var minTransactionZoneDateTime = ZonedDateTime.parse(minTransactionStr);
        var maxTransactionZoneDateTime = ZonedDateTime.parse(maxTransactionStr);
        var url = ("/feed/account/{account}/category/{category}/transactions-between?" +
            "minTransactionTimestamp={minTransactionTimestamp}&maxTransactionTimestamp={maxTransactionTimestamp}")
            .replace("{account}", account.accountUid())
            .replace("{category}", account.defaultCategory())
            .replace("{minTransactionTimestamp}", minTransactionStr)
            .replace("{maxTransactionTimestamp}", maxTransactionStr);

        stubFor(get(urlEqualTo(url))
            .willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(GET_TRANSACTIONS_FEED_JSON_RESPONSE)
            ));

        // When
        var response = testObj.getTransactionFeed(Mono.just(account), minTransactionZoneDateTime, maxTransactionZoneDateTime)
            .block();

        // Then
        assertThat(response.feedItems()).containsAll(TransactionsFeedFeature.INSTANCE.feedItems());
    }

    @Test
    void givenStarlingClientAPI_whenCallingGetTransactionFeed_doesNotReturnTransactionsFeed() {
        // Given
        var account = AccountFeature.INSTANCE;
        var minTransactionStr = "2023-02-11T11:18:23Z";
        var maxTransactionStr = "2023-02-18T11:18:23Z";
        var minTransactionZoneDateTime = ZonedDateTime.parse(minTransactionStr);
        var maxTransactionZoneDateTime = ZonedDateTime.parse(maxTransactionStr);
        var url = ("/feed/account/{account}/category/{category}/transactions-between?" +
            "minTransactionTimestamp={minTransactionTimestamp}&maxTransactionTimestamp={maxTransactionTimestamp}")
            .replace("{account}", account.accountUid())
            .replace("{category}", account.defaultCategory())
            .replace("{minTransactionTimestamp}", minTransactionStr)
            .replace("{maxTransactionTimestamp}", maxTransactionStr);

        stubFor(get(urlEqualTo(url))
            .willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{}")
            ));

        // When
        var response = testObj.getTransactionFeed(Mono.just(account), minTransactionZoneDateTime, maxTransactionZoneDateTime)
            .block();

        // Then
        assertThat(response.feedItems()).isEmpty();
    }

    @Test
    void givenStarlingClientAPI_whenPutNewSavingGoal_returnsSavingGoalUid() {
        // Given
        var account = AccountFeature.INSTANCE;
        var accountId = account.accountUid();
        var currency = account.currency();
        var name = "Savings for the account %s".formatted(accountId);
        var url = "/account/{account}/savings-goals"
            .replace("{account}", accountId);
        var minorUnits = 1234L;
        var expectedJsonBody = SavingsGoalFeature.getPutSavingsGoalJsonRequest(name, minorUnits);
        var expectedSavingsGoalUid = "23c1004b-4cd1-4b7a-83fc-80b0ab3adfd7";

        stubFor(put(urlEqualTo(url))
            .withRequestBody(equalToJson(expectedJsonBody))
            .willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(PUT_SAVINGS_GOAL_JSON_RESPONSE)
            ));

        // When
        var response = testObj.putNewSavingGoal(accountId, name, currency, minorUnits).block();

        // Then
        assertThat(response).isEqualTo(expectedSavingsGoalUid);
    }

    @Test
    void givenStarlingClientAPI_whenTopUpExistingSavingGoal_returnsTrue() {
        // Given
        var account = AccountFeature.INSTANCE;
        var accountId = account.accountUid();
        var currency = account.currency();
        var savingGoalUid = UUID.randomUUID().toString();
        var transferUid = UUID.randomUUID().toString();

        var url = "/account/{account}/savings-goals/{savingGoalUid}/add-money/{transferUid}"
            .replace("{account}", accountId)
            .replace("{savingGoalUid}", savingGoalUid)
            .replace("{transferUid}", transferUid);

        var minorUnits = 123456L;


        stubFor(put(urlEqualTo(url))
            .withRequestBody(equalToJson(PUT_TOP_UP_REQUEST))
            .willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(PUT_TOP_UP_RESPONSE)
            ));

        // When
        var response = testObj.topUpExistingSavingGoal(accountId, savingGoalUid, currency, minorUnits, transferUid).block();

        // Then
        assertThat(response).isTrue();
    }

    @Test
    void givenStarlingClientAPIAndExistingSavingsGoal_whenCallingGetSavingsGoalFeed_returnsSavingsGoalUid() {
        // Given
        var account = AccountFeature.INSTANCE;
        var accountId = account.accountUid();
        var url = ("/account/{account}/savings-goals")
            .replace("{account}", account.accountUid());
        var expectedSavingsGoalUid = "77887788-7788-7788-7788-778877887788";

        stubFor(get(urlEqualTo(url))
            .willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(GET_SAVINGS_GOALS_JSON_RESPONSE)
            ));

        // When
        var response = testObj.getSavingsGoal(accountId).block();

        // Then
        assertThat(response.get()).isEqualTo(expectedSavingsGoalUid);
    }

    @Test
    void givenStarlingClientAPIAndNotExistingSavingsGoal_whenCallingGetSavingsGoalFeed_returnsEmpty() {
        // Given
        var account = AccountFeature.INSTANCE;
        var accountId = account.accountUid();
        var url = ("/account/{account}/savings-goals")
            .replace("{account}", account.accountUid());

        stubFor(get(urlEqualTo(url))
            .willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{}")
            ));

        // When
        var response = testObj.getSavingsGoal(accountId).block();

        // Then
        assertThat(response.isEmpty()).isTrue();
    }
}