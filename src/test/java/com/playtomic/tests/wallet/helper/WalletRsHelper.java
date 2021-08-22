package com.playtomic.tests.wallet.helper;

import com.playtomic.tests.wallet.model.response.WalletRs;

import java.math.BigDecimal;

public class WalletRsHelper {

    public static WalletRs walletRs() {
        return WalletRs.builder()
                .id(1L)
                .firstName("first name")
                .lastName("last name")
                .amount(BigDecimal.valueOf(1000))
                .build();
    }
}
