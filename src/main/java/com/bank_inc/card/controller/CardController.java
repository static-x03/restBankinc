package com.bank_inc.card.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank_inc.card.Card;
import com.bank_inc.card.dto.CardBalance;
import com.bank_inc.card.dto.CardRequest;
import com.bank_inc.card.dto.CardResponseGenerated;
import com.bank_inc.card.dto.ErrorResponse;
import com.bank_inc.card.service.CardService;

import org.springframework.http.HttpStatus;

@RestController
public class CardController {

	private final CardService cardService;

	public CardController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping("/card/{productId}/number")
	public ResponseEntity<?> generateCard(@PathVariable String productId) {
		CardResponseGenerated cardResponse = cardService.generateCardNumber(productId);
		HttpStatus status = cardResponse.getError() == null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(cardResponse, status);

	}

	@PostMapping("/card/enroll")
	public ResponseEntity<?> activateCard(@RequestBody CardRequest cardRequest) {
		if (cardRequest.getCardId().length() != 16) {
			ErrorResponse errorResponse = new ErrorResponse("El cardId debe tener 16 dígitos");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (!cardRequest.getCardId().matches("\\d{16}")) {
			ErrorResponse errorResponse = new ErrorResponse("El cardId debe ser solo numérico");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Card card = cardService.activateCard(cardRequest.getCardId());
		return ResponseEntity.ok(card);
	}

	@DeleteMapping("/card/{cardId}")
	public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
		cardService.blockCard(cardId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/card/balance")
	public ResponseEntity<?> balance(@RequestBody CardBalance cardBalance) {
		if (cardBalance.getCardId().length() != 16) {
			ErrorResponse errorResponse = new ErrorResponse("El cardId debe tener 16 dígitos");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (!cardBalance.getCardId().matches("\\d{16}")) {
			ErrorResponse errorResponse = new ErrorResponse("El cardId debe ser solo numérico");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (Integer.parseInt(cardBalance.getBalance()) <= 0) {
			ErrorResponse errorResponse = new ErrorResponse("El balance ser mayor a 0");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Card card = cardService.balance(cardBalance);
		return ResponseEntity.ok(card);
	}

	@GetMapping("/card/balance/{cardId}")
	public ResponseEntity<?> getBalance(@PathVariable String cardId) {
		Card card = cardService.getBalance(cardId);
		return new ResponseEntity<>(card, HttpStatus.OK);

	}
	

}
