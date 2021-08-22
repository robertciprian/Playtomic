package com.playtomic.tests.wallet.test;

import com.playtomic.tests.wallet.exception.ServiceUnavailableException;
import com.playtomic.tests.wallet.helper.RechargeRqHelper;
import com.playtomic.tests.wallet.model.request.RechargeRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.service.client.StripeServiceClient;
import com.playtomic.tests.wallet.utils.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.net.URI;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

@ExtendWith(SpringExtension.class)
@RestClientTest(StripeServiceClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class StripeServiceClientTest {

    private static final String HOST = "http://stripe-service:8080";
    private static final String STRIPE_PATH = "/api/v1/stripe";

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private StripeServiceClient client;

    @Test
    public void recharge() throws Exception {
        final RechargeRq rechargeRq = RechargeRqHelper.rechargeRq();

        mockServer.expect(requestTo(new URI(HOST + STRIPE_PATH)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(FileUtils.readFileAsString("json/RechargeRq.json")))
                .andRespond(MockRestResponseCreators.withSuccess());

        final RechargeRs actual = client.recharge(rechargeRq);

        final RechargeRs expected = RechargeRs.builder().accepted(Boolean.TRUE).build();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void recharge_HttpClientErrorException() throws Exception {
        final RechargeRq rechargeRq = RechargeRqHelper.rqWithoutCardNumber();

        mockServer.expect(requestTo(new URI(HOST + STRIPE_PATH)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(FileUtils.readFileAsString("json/RechargeRq_withoutCardNumber.json")))
                .andRespond(MockRestResponseCreators.withBadRequest());

        final RechargeRs actual = client.recharge(rechargeRq);

        final RechargeRs expected = RechargeRs.builder().accepted(Boolean.FALSE).build();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void recharge_ServerError() throws Exception {
        final RechargeRq rechargeRq = RechargeRqHelper.rechargeRq();

        mockServer.expect(requestTo(new URI(HOST + STRIPE_PATH)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(FileUtils.readFileAsString("json/RechargeRq.json")))
                .andRespond(MockRestResponseCreators.withServerError());

        Assertions.assertThatThrownBy(() -> client.recharge(rechargeRq))
            .isInstanceOf(ServiceUnavailableException.class)
            .hasMessage("Failed to reach 3-party system for recharge!");
    }
}