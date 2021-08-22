package com.playtomic.tests.wallet.helper;

import com.playtomic.tests.wallet.model.request.SubtractRq;

import java.math.BigDecimal;

public class SubtractRqHelper {

    public static SubtractRq subtractRq() {
        return SubtractRq.builder()
                .id(1L)
                .cardNumber("4389437054429297")
                .amount(BigDecimal.valueOf(1000))
                .build();
    }
}
