package com.playtomic.tests.wallet.helper;

import com.playtomic.tests.wallet.entity.Wallet;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WalletHelper {

    public static Wallet walletById(final Long id) {
        return Wallet.builder()
                .id(id)
                .firstName("first name")
                .lastName("last name")
                .amount(BigDecimal.valueOf(1000))
                .cardNumber("4389437054429297")
                .cvc("400")
                .validUntil(LocalDate.ofYearDay(2025, 30))
                .build();
    }

    public static Wallet walletWithSmallAmount(final Long id,
                                               final String cardNumber) {
        return Wallet.builder()
                .id(id)
                .firstName("first name")
                .lastName("last name")
                .amount(BigDecimal.valueOf(900))
                .cardNumber(cardNumber)
                .cvc("400")
                .validUntil(LocalDate.ofYearDay(2025, 30))
                .build();
    }

    public static Wallet walletByIdAndCardNumber(final Long id,
                                                 final String cardNumber) {
        return Wallet.builder()
                .id(id)
                .firstName("first name")
                .lastName("last name")
                .amount(BigDecimal.valueOf(2000))
                .cardNumber(cardNumber)
                .cvc("400")
                .validUntil(LocalDate.ofYearDay(2025, 30))
                .build();
    }

    public static Wallet walletWithAmountSubtracted() {
        return Wallet.builder()
                .id(1L)
                .firstName("first name")
                .lastName("last name")
                .amount(BigDecimal.valueOf(1000))
                .cardNumber("4389437054429297")
                .cvc("400")
                .validUntil(LocalDate.ofYearDay(2025, 30))
                .build();
    }

    public static Wallet walletWithAmountRecharged(final Long id) {
        return Wallet.builder()
                .id(id)
                .firstName("first name")
                .lastName("last name")
                .amount(BigDecimal.valueOf(2000))
                .cardNumber("4389437054429297")
                .cvc("400")
                .validUntil(LocalDate.ofYearDay(2025, 30))
                .build();
    }
}
