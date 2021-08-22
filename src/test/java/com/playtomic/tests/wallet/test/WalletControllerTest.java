package com.playtomic.tests.wallet.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.exception.BadRequestException;
import com.playtomic.tests.wallet.helper.WalletRsHelper;
import com.playtomic.tests.wallet.model.request.SubtractRq;
import com.playtomic.tests.wallet.model.response.RechargeRs;
import com.playtomic.tests.wallet.model.response.SubtractRs;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getWalletById() throws Exception {
        Mockito.when(walletService.getWalletById(1L)).thenReturn(WalletRsHelper.walletRs());

        final String contentAsString = mockMvc.perform(get("/api/wallet/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final String expected = FileUtils.readFileAsString("json/WalletRs.json");
        JSONAssert.assertEquals(expected, contentAsString, true);
    }

    @Test
    public void getWalletById_ExceptionOccured() throws Exception {
        Mockito.when(walletService.getWalletById(1L)).thenThrow(BadRequestException.class);

        mockMvc.perform(get("/api/wallet/1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void subtractAmount() throws Exception {
        final SubtractRq subtractRq = SubtractRq.builder().id(1L).amount(BigDecimal.valueOf(1000)).cardNumber("4382").build();
        Mockito.when(walletService.subtractAmount(subtractRq))
                .thenReturn(SubtractRs.builder().accepted(Boolean.TRUE).build());

        final String contentAsString = mockMvc.perform(put("/api/wallet/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subtractRq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final String expected = FileUtils.readFileAsString("json/SubtractRs.json");
        JSONAssert.assertEquals(expected, contentAsString, true);
    }

    @Test
    public void subtractAmount_InvalidRq() throws Exception {
        final SubtractRq subtractRq = SubtractRq.builder().id(1L).cardNumber("4382").build();

        mockMvc.perform(put("/api/wallet/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subtractRq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void rechargeWallet() throws Exception {
        final Long id = 1L;
        final BigDecimal rechargeAmount = BigDecimal.valueOf(2000);

        Mockito.when(walletService.rechargeWallet(id, rechargeAmount))
                .thenReturn(RechargeRs.builder().accepted(Boolean.TRUE).build());

        final String contentAsString = mockMvc.perform(post("/api/wallet/recharge")
                .param("id", String.valueOf(id))
                .param("amount", String.valueOf(rechargeAmount)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final String expected = FileUtils.readFileAsString("json/RechargeRs.json");
        JSONAssert.assertEquals(expected, contentAsString, true);
    }
}