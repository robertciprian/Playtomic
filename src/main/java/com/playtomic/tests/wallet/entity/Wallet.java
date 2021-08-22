package com.playtomic.tests.wallet.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "WALLET")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    @NotEmpty
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotEmpty
    private String lastName;

    @Column(name = "AMOUNT")
    @NotEmpty
    private BigDecimal amount;

    @Column(name = "CARD_NUMBER", unique = true)
    @NotEmpty
    private String cardNumber;

    @Column(name = "CVC")
    @NotEmpty
    private String cvc;

    @Column(name = "VALID_UNTIL")
    @NotEmpty
    private LocalDate validUntil;

    @Column(name = "CREATED_BY")
    @CreatedBy
    public String createdBy;

    @Column(name = "CREATED_AT")
    @CreatedDate
    public LocalDateTime createdAt;

    @Column(name = "UPDATED_BY")
    @LastModifiedBy
    public String editedBy;

    @Column(name = "UPDATED_AT")
    @LastModifiedDate
    public LocalDateTime editedAt;
}
