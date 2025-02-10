package com.bank_inc.card.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank_inc.card.Transaction;
import com.bank_inc.card.dto.AnulationRequest;
import com.bank_inc.card.dto.PurchaseRequest;
import com.bank_inc.card.service.TransactionService;

@RestController
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/transaction/purchase")
	public ResponseEntity<Transaction> purchase(@RequestBody PurchaseRequest purchaseRequest) {
		Transaction transaction = transactionService.processPurchase(purchaseRequest);
		return ResponseEntity.ok(transaction);
	}
	
	@GetMapping("/transaction/{transactionId}")
	public ResponseEntity<?> getTransaccion(@PathVariable String transactionId) {
		Transaction transaction = transactionService.getTransaction(transactionId);
		return new ResponseEntity<>(transaction, HttpStatus.OK);

	}
	
	@PostMapping("/transaction/anulation")
    public ResponseEntity<?> annulateTransaction(@RequestBody AnulationRequest anulationRequest) {
        transactionService.annulateTransaction(anulationRequest);
        return ResponseEntity.ok("Transacción anulada exitosamente");
    }

}
