package com.javiertorres.StarlingAPIChallenge.infrastructure.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.javiertorres.StarlingAPIChallenge.domain.exception.StarlingClientException;
import com.javiertorres.StarlingAPIChallenge.infrastructure.config.StarlingWebClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
class StarlingClientErrorsITest {

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

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 403, 404, 500, 502, 503, 504})
    void givenStarlingClientAPI_whenError_handlesError(Integer error) {
        // Given
        var errorResponse = """
            {"error":"this is an error","error_description":"this is the error description"}
            """.stripIndent();

        stubFor(get(urlEqualTo("/accounts")).willReturn(aResponse().withStatus(error)
            .withHeader("Content-Type", "application/json")
            .withBody(errorResponse)));

        // When
        var response = testObj.getAccounts();

        // Then
        assertThatThrownBy(() -> response.block()).isInstanceOf(StarlingClientException.class)
            .hasMessageContaining(errorResponse);
    }
}