package com.bank_inc.card.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bank_inc.card.controller.TransactionController;
import com.bank_inc.card.dto.AnulationRequest;
import com.bank_inc.card.service.TransactionService;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private TransactionService transactionService;

	@Test
	public void testAnnulateTransaction_Success() throws Exception {
		doNothing().when(transactionService).annulateTransaction(any(AnulationRequest.class));

		mockMvc.perform(post("/transaction/anulation").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cardId\": \"1\", \"transactionId\": \"1\"}")).andExpect(status().isOk())
				.andExpect(content().string("Transacción anulada exitosamente"));
	}

}
