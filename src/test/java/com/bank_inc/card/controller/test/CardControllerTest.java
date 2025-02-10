package com.bank_inc.card.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bank_inc.card.Card;
import com.bank_inc.card.controller.CardController;
import com.bank_inc.card.dto.CardBalance;
import com.bank_inc.card.dto.CardResponseGenerated;
import com.bank_inc.card.service.CardService;

@WebMvcTest(CardController.class)
public class CardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private CardService cardService;

	@Test
	public void testGenerateCard_Success() throws Exception {
		CardResponseGenerated response = new CardResponseGenerated();
		response.setCardNumber("1234567890123456");

		when(cardService.generateCardNumber("123456")).thenReturn(response);

		mockMvc.perform(get("/card/123456/number")).andExpect(status().isOk())
				.andExpect(jsonPath("$.cardNumber").value("1234567890123456"));
	}

	@Test
	public void testGenerateCard_InvalidProductId() throws Exception {
		CardResponseGenerated response = new CardResponseGenerated();
		response.setError("El ID del producto debe tener solo 6 dígitos");

		when(cardService.generateCardNumber("12345")).thenReturn(response);

		mockMvc.perform(get("/card/12345/number")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("El ID del producto debe tener solo 6 dígitos"));
	}

	@Test
	public void testActivateCard_Success() throws Exception {
		Card card = new Card();
		card.setCardId(1L);
		card.setActive(true);
		card.setExpirationDate("02/2028");

		when(cardService.activateCard("1234567890123456")).thenReturn(card);

		mockMvc.perform(post("/card/enroll").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cardId\": \"1234567890123456\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.cardId").value(1)).andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.expirationDate").value("02/2028"));
	}

	
	@Test
	public void testBlockCard_Success() throws Exception {
		doNothing().when(cardService).blockCard(1L);

		mockMvc.perform(delete("/card/1")).andExpect(status().isNoContent());
	}

	@Test
	public void testBalance_Success() throws Exception {
		Card card = new Card();
		card.setCardId(1L);
		card.setBalance(new BigDecimal("1000"));

		when(cardService.balance(any(CardBalance.class))).thenReturn(card);

		mockMvc.perform(post("/card/balance").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cardId\": \"1234567890123456\", \"balance\": \"1000\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.cardId").value(1)).andExpect(jsonPath("$.balance").value(1000));
	}

	

	@Test
	public void testGetBalance_Success() throws Exception {
		Card card = new Card();
		card.setCardId(1L);
		card.setBalance(new BigDecimal("1000"));

		when(cardService.getBalance("1")).thenReturn(card);

		mockMvc.perform(get("/card/balance/1")).andExpect(status().isOk()).andExpect(jsonPath("$.cardId").value(1))
				.andExpect(jsonPath("$.balance").value(1000));
	}

}
