package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {


    Optional<Wallet> findById(final Long id);

    Optional<Wallet> findByIdAndCardNumber(final Long id, final String iban);
}
