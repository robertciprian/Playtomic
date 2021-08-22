package com.playtomic.tests.wallet.test;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.BadRequestException;
import com.playtomic.tests.wallet.helper.SubtractRqHelper;
import com.playtomic.tests.wallet.helper.WalletHelper;
import com.playtomic.tests.wallet.helper.WalletRsHelper;
import com.playtomic.tests.wallet.model.request.RechargeRq;
import com.playtomic.tests.wallet.model.request.SubtractRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.model.response.SubtractRs;
import com.playtomic.tests.wallet.model.response.WalletRs;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private WalletService underTest;

    @Test
    public void getWalletById() {
        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(WalletHelper.walletById(1L)));

        final WalletRs actual = underTest.getWalletById(1L);

        final WalletRs expected = WalletRsHelper.walletRs();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getWalletById_NoWalletFound() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.getWalletById(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No wallet found! Please try with different id.");
    }

    @Test
    public void subtractAmount_NoWalletFound() {
        final SubtractRq subtractRq = SubtractRqHelper.subtractRq();
        when(walletRepository.findByIdAndCardNumber(subtractRq.getId(), subtractRq.getCardNumber()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.subtractAmount(subtractRq))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No wallet found!");
    }

    @Test
    public void subtractAmount_AmountLessThanSubtractAmount() {
        final SubtractRq subtractRq = SubtractRqHelper.subtractRq();
        when(walletRepository.findByIdAndCardNumber(subtractRq.getId(), subtractRq.getCardNumber()))
                .thenReturn(Optional.of(WalletHelper.walletWithSmallAmount(subtractRq.getId(), subtractRq.getCardNumber())));

        Assertions.assertThatThrownBy(() -> underTest.subtractAmount(subtractRq))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Client has insufficient funds!");
    }

    @Test
    public void subtractAmount() {
        final SubtractRq subtractRq = SubtractRqHelper.subtractRq();
        final Wallet wallet = WalletHelper.walletByIdAndCardNumber(subtractRq.getId(), subtractRq.getCardNumber());
        final Wallet withAmountSubtracted = WalletHelper.walletWithAmountSubtracted();
        when(walletRepository.findByIdAndCardNumber(subtractRq.getId(), subtractRq.getCardNumber()))
                .thenReturn(Optional.of(wallet));

        final SubtractRs actual = underTest.subtractAmount(subtractRq);

        Assertions.assertThat(actual).isEqualTo(SubtractRs.builder().accepted(Boolean.TRUE).build());
        Mockito.verify(walletRepository).save(withAmountSubtracted);
    }

    @Test
    public void rechargeWallet_NoWalletFound() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> underTest.rechargeWallet(1L, BigDecimal.valueOf(1000)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No wallet found! Please try with different id.");
    }

    @Test
    public void rechargeWallet_RechargeFailed() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(WalletHelper.walletById(1L)));
        when(stripeService.rechargeWallet(any(RechargeRq.class))).thenReturn(RechargeRs.builder().accepted(Boolean.FALSE).build());

        final RechargeRs actual = underTest.rechargeWallet(1L, BigDecimal.valueOf(1000));

        Assertions.assertThat(actual.isAccepted()).isFalse();
    }

    @Test
    public void rechargeWallet() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(WalletHelper.walletById(1L)));
        when(stripeService.rechargeWallet(any(RechargeRq.class))).thenReturn(RechargeRs.builder().accepted(Boolean.TRUE).build());
        final Wallet withAmountRecharged = WalletHelper.walletWithAmountRecharged(1L);

        final RechargeRs actual = underTest.rechargeWallet(1L, BigDecimal.valueOf(1000));

        Assertions.assertThat(actual.isAccepted()).isTrue();
        verify(walletRepository).save(withAmountRecharged);
    }
}