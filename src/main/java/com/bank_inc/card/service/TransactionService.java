package com.bank_inc.card.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.bank_inc.card.Card;
import com.bank_inc.card.Transaction;
import com.bank_inc.card.dto.AnulationRequest;
import com.bank_inc.card.dto.PurchaseRequest;
import com.bank_inc.card.repository.CardRepository;
import com.bank_inc.card.repository.TransactionRepository;

@Service
public class TransactionService {

	private final CardRepository cardRepository;
	private final TransactionRepository transactionRepository;

	public TransactionService(CardRepository cardRepository, TransactionRepository transactionRepository) {
		this.cardRepository = cardRepository;
		this.transactionRepository = transactionRepository;
	}

	public Transaction processPurchase(PurchaseRequest purchaseRequest) {
		Card card = cardRepository.findById(Long.parseLong(purchaseRequest.getCardId()))
				.orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

		if (card.getBalance().compareTo(purchaseRequest.getPrice()) < 0) {
			throw new IllegalArgumentException("Saldo insuficiente");
		}

		card.setBalance(card.getBalance().subtract(purchaseRequest.getPrice()));
		cardRepository.save(card);

		Transaction transaction = new Transaction();
		transaction.setCard(card);
		transaction.setPrice(purchaseRequest.getPrice());
		transaction.setTimestamp(LocalDateTime.now());
		return transactionRepository.save(transaction);
	}

	public void annulateTransaction(AnulationRequest anulationRequest) {
		Card card = cardRepository.findById(Long.parseLong(anulationRequest.getCardId()))
				.orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

		Transaction transaction = transactionRepository.findById(anulationRequest.getTransactionId())
				.orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));

		if (ChronoUnit.HOURS.between(transaction.getTimestamp(), LocalDateTime.now()) > 24) {
			throw new IllegalArgumentException("No se puede anular una transacción mayor a 24 horas");
		}

		transaction.setAnnulled(true);
		card.setBalance(card.getBalance().add(transaction.getPrice()));
		transactionRepository.save(transaction);
		cardRepository.save(card);
	}

	public Transaction getTransaction(String transactionId) {
		return transactionRepository.findById(Long.parseLong(transactionId))
				.orElseThrow(() -> new IllegalArgumentException("Tarjeta no encontrada"));

	}

}
