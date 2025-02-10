package com.bank_inc.card.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.bank_inc.card.Card;
import com.bank_inc.card.dto.CardBalance;
import com.bank_inc.card.dto.CardResponseGenerated;
import com.bank_inc.card.repository.CardRepository;

@Service
public class CardService {

	private final CardRepository cardRepository;

	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public CardResponseGenerated generateCardNumber(String productId) {
		CardResponseGenerated response = new CardResponseGenerated();

		if (productId.length() != 6) {
			response.setError("El ID del producto debe tener solo 6 dígitos");
			return response;
		}

		if (!productId.matches("\\d{6}")) {
			response.setError("El productId solo debe ser numérico.");
			return response;
		}

		Random random = new Random();
		StringBuilder cardNumber = new StringBuilder(productId);
		while (cardNumber.length() < 16) {
			cardNumber.append(random.nextInt(10));
		}

		response.setCardNumber(cardNumber.toString());
		return response;
	}

	public Card activateCard(String cardId) {
		LocalDate expirationDate = LocalDate.now().plusYears(3);
		String formattedExpirationDate = expirationDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));

		Card card = new Card();
		card.setCardId(Long.parseLong(cardId));
		card.setExpirationDate(formattedExpirationDate);
		card.setActive(true);

		return cardRepository.save(card);

	}

	public void blockCard(Long cardId) {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));
		card.setBlocked(true);
		cardRepository.save(card);
	}

	public Card balance(CardBalance cardBalance) {
		Card card = cardRepository.findById(Long.parseLong(cardBalance.getCardId()))
				.orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));
		BigDecimal number = new BigDecimal(cardBalance.getBalance());
		card.setBalance(number);
		return cardRepository.save(card);
	}

	public Card getBalance(String cardId) {
		Card card = cardRepository.findById(Long.parseLong(cardId))
				.orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));
		return card;

	}
	

}
