package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.BadRequestException;
import com.playtomic.tests.wallet.model.request.RechargeRq;
import com.playtomic.tests.wallet.model.request.SubtractRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.model.response.SubtractRs;
import com.playtomic.tests.wallet.model.response.WalletRs;
import com.playtomic.tests.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final StripeService stripeService;

    public WalletRs getWalletById(final Long id) {
        final Wallet walletInDb = walletRepository.findById(id).orElseThrow(() -> new BadRequestException("No wallet found! Please try with different id."));
        return buildWalletRs(walletInDb);
    }

    private WalletRs buildWalletRs(final Wallet walletInDb) {
        return WalletRs.builder()
                .id(walletInDb.getId())
                .firstName(walletInDb.getFirstName())
                .lastName(walletInDb.getLastName())
                .amount(walletInDb.getAmount())
                .build();
    }

    public SubtractRs subtractAmount(final SubtractRq subtractRq) {
        final Wallet walletInDb = walletRepository.findByIdAndCardNumber(subtractRq.getId(), subtractRq.getCardNumber())
                .orElseThrow(() -> new BadRequestException("No wallet found!"));

        if (walletInDb.getAmount().compareTo(subtractRq.getAmount()) < 0) {
            throw new BadRequestException("Client has insufficient funds!");
        }

        walletInDb.setAmount(walletInDb.getAmount().subtract(subtractRq.getAmount()));
        walletRepository.save(walletInDb);

        return SubtractRs.builder()
                .accepted(Boolean.TRUE)
                .build();
    }

    public RechargeRs rechargeWallet(final Long id,
                                     final BigDecimal rechargeAmount) {
        final Wallet walletInDb = walletRepository.findById(id).orElseThrow(() -> new BadRequestException("No wallet found! Please try with different id."));
        final RechargeRq rechargeRq = buildRechargeRq(rechargeAmount, walletInDb);

        final RechargeRs rechargeRs = stripeService.rechargeWallet(rechargeRq);

        if (rechargeRs.isAccepted()) {
            walletInDb.setAmount(walletInDb.getAmount().add(rechargeAmount));
            walletRepository.save(walletInDb);
        }
        return rechargeRs;
    }

    private RechargeRq buildRechargeRq(final BigDecimal rechargeAmount,
                                       final Wallet wallet) {
        return RechargeRq.builder()
                .firstName(wallet.getFirstName())
                .lastName(wallet.getLastName())
                .amount(rechargeAmount)
                .cardNumber(wallet.getCardNumber())
                .cvc(wallet.getCvc())
                .validUntil(wallet.getValidUntil())
                .build();
    }
}
