package com.playtomic.tests.wallet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RechargeRq {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String cardNumber;

    @NotNull
    private String cvc;

    @NotNull
    private LocalDate validUntil;
}
