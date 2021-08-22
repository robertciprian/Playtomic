package com.playtomic.tests.wallet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubtractRq {

    @NotNull
    private Long id;

    @NotNull
    private String cardNumber;

    @NotNull
    private BigDecimal amount;
}
