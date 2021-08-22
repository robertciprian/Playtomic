package com.playtomic.tests.wallet.service.client;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.playtomic.tests.wallet.exception.ServiceUnavailableException;
import com.playtomic.tests.wallet.model.request.RechargeRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class StripeServiceClient {

    /*dummy path to a 3-party api which will do the stripe*/
    private static final String STRIPE_SERVICE_PATH = "/api/v1/stripe";

    @Value("${services.stripe-service.url}")
    private String url;

    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "rechargeAmount", threadPoolKey = "stripe-service")
    public RechargeRs recharge(final RechargeRq rechargeRq) {
        final UriComponents  uri = UriComponentsBuilder.fromHttpUrl(url)
                .path(STRIPE_SERVICE_PATH).build();
        try {
            log.info("Executing recharge using stripe 3-party system for rechargeRq {}", rechargeRq);
            restTemplate.exchange(uri.toUri(), HttpMethod.POST, new HttpEntity<>(rechargeRq), Void.class);
        } catch (HttpClientErrorException e) {
            log.error("Recharge failed! Business exception occured.");
            return RechargeRs.builder().accepted(Boolean.FALSE).build();
        } catch (Exception e) {
            log.error("Recharge failed!");
            throw new ServiceUnavailableException("Failed to reach 3-party system for recharge!");
        }
        return RechargeRs.builder().accepted(Boolean.TRUE).build();
    }
}
