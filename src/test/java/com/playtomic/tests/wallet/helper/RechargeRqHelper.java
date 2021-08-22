package com.playtomic.tests.wallet.helper;

import com.playtomic.tests.wallet.model.request.RechargeRq;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RechargeRqHelper {

    public static RechargeRq rechargeRq() {
        return RechargeRq.builder()
                .firstName("client first name")
                .lastName("client last name")
                .amount(BigDecimal.valueOf(300))
                .cardNumber("4389437054429297")
                .cvc("402")
                .validUntil(LocalDate.ofYearDay(2025, 25))
                .build();
    }

    public static RechargeRq rqWithoutCardNumber() {
        return RechargeRq.builder()
                .firstName("client first name")
                .lastName("client last name")
                .amount(BigDecimal.valueOf(300))
                .cvc("402")
                .validUntil(LocalDate.ofYearDay(2025, 25))
                .build();
    }
}
