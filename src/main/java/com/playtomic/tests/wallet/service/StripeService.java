package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.model.request.RechargeRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.service.client.StripeServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;


/**
 * Handles the communication with Stripe.
 *
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeService {

    final private static BigDecimal THRESHOLD = new BigDecimal(10);
    private final StripeServiceClient stripeServiceClient;

    /**
     * Charges money in the credit card.
     *
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param creditCardNumber The number of the credit card
     * @param amount The amount that will be charged.
     *
     * @throws StripeServiceException
     */
    public void charge(String creditCardNumber, BigDecimal amount) throws StripeServiceException {
        Assert.notNull(creditCardNumber, "creditCardNumber == null");
        Assert.notNull(amount, "amount == null");

        if (amount.compareTo(THRESHOLD) < 0) {
            throw new StripeServiceException();
        }
    }

    public RechargeRs rechargeWallet(final RechargeRq rechargeRq) {
        return stripeServiceClient.recharge(rechargeRq);
    }
}
