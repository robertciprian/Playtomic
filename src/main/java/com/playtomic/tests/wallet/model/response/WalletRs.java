package com.playtomic.tests.wallet.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletRs {

    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal amount;
}
