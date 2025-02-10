package com.bank_inc.services.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bank_inc.card.Card;
import com.bank_inc.card.dto.CardBalance;
import com.bank_inc.card.dto.CardResponseGenerated;
import com.bank_inc.card.repository.CardRepository;
import com.bank_inc.card.service.CardService;

public class CardServiceTest {

	@Mock
	private CardRepository cardRepository;

	@InjectMocks
	private CardService cardService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGenerateCardNumber_Success() {
		String productId = "123456";
		CardResponseGenerated response = cardService.generateCardNumber(productId);

		assertNotNull(response);
		assertNull(response.getError());
		assertNotNull(response.getCardNumber());
		assertEquals(16, response.getCardNumber().length());
		assertTrue(response.getCardNumber().startsWith(productId));
	}

	@Test
	public void testGenerateCardNumber_InvalidProductIdLength() {
		String productId = "12345";
		CardResponseGenerated response = cardService.generateCardNumber(productId);

		assertNotNull(response);
		assertEquals("El ID del producto debe tener solo 6 dígitos", response.getError());
	}

	@Test
	public void testGenerateCardNumber_InvalidProductIdCharacters() {
		String productId = "12345a";
		CardResponseGenerated response = cardService.generateCardNumber(productId);

		assertNotNull(response);
		assertEquals("El productId solo debe ser numérico.", response.getError());
	}

	@Test
	public void testBlockCard_Success() {
		Long cardId = 1L;
		Card card = new Card();
		card.setCardId(cardId);

		when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
		when(cardRepository.save(any(Card.class))).thenReturn(card);

		assertDoesNotThrow(() -> cardService.blockCard(cardId));
		assertTrue(card.isBlocked());
	}

	@Test
	public void testBalance_Success() {
		CardBalance cardBalance = new CardBalance();
		cardBalance.setCardId("1");
		cardBalance.setBalance("1000");

		Card card = new Card();
		card.setCardId(1L);

		when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
		when(cardRepository.save(any(Card.class))).thenReturn(card);

		Card updatedCard = cardService.balance(cardBalance);

		assertNotNull(updatedCard);
		assertEquals(new BigDecimal("1000"), updatedCard.getBalance());
	}

	@Test
	public void testGetBalance_Success() {
		Card card = new Card();
		card.setCardId(1L);

		when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

		Card retrievedCard = cardService.getBalance("1");

		assertNotNull(retrievedCard);
		assertEquals(1L, retrievedCard.getCardId());
	}

}
