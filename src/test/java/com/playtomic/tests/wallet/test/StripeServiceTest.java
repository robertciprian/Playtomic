package com.playtomic.tests.wallet.test;


import com.playtomic.tests.wallet.helper.RechargeRqHelper;
import com.playtomic.tests.wallet.model.request.RechargeRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.service.client.StripeServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StripeServiceTest {

    @Mock
    private StripeServiceClient client;

    @InjectMocks
    private StripeService underTest;

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeServiceException.class, () -> {
            underTest.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        underTest.charge("4242 4242 4242 4242", new BigDecimal(15));
    }

    @Test
    public void rechargeWallet() {
        final RechargeRq rechargeRq = RechargeRqHelper.rechargeRq();
        when(client.recharge(rechargeRq)).thenReturn(RechargeRs.builder().accepted(Boolean.TRUE).build());

        final RechargeRs actual = underTest.rechargeWallet(rechargeRq);

        Mockito.verify(client).recharge(rechargeRq);
        assertThat(actual.isAccepted()).isTrue();
    }
}
