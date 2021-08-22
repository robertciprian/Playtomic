package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.request.SubtractRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.model.response.SubtractRs;
import com.playtomic.tests.wallet.model.response.WalletRs;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }


    @GetMapping("/{id}")
    public WalletRs getWalletById (@PathVariable final Long id) {
        log.info("Starting request for retrieving a wallet by id...");
        return walletService.getWalletById(id);
    }

    @PutMapping("/charge")
    public SubtractRs subtractAmount(@Valid @RequestBody final SubtractRq subtractRq) {
        log.info("Starting request for subtracting amount from wallet...");
        return walletService.subtractAmount(subtractRq);
    }

    @PostMapping("/recharge")
    public RechargeRs rechargeWallet(@RequestParam(name = "id") final Long id,
                                     @RequestParam(name = "amount") final BigDecimal rechargeAmount) {
        log.info("Starting request for recharging wallet...");
        return walletService.rechargeWallet(id, rechargeAmount);
    }
}
