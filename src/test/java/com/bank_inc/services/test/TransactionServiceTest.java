package com.bank_inc.services.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bank_inc.card.Card;
import com.bank_inc.card.Transaction;
import com.bank_inc.card.dto.AnulationRequest;
import com.bank_inc.card.dto.PurchaseRequest;
import com.bank_inc.card.repository.CardRepository;
import com.bank_inc.card.repository.TransactionRepository;
import com.bank_inc.card.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
	
	@Mock
    private CardRepository cardRepository;
	@Mock
    private TransactionRepository transactionRepository;
	@InjectMocks
    private TransactionService transactionService;
	
	@BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
    public void testProcessPurchase_Success() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCardId("1");
        purchaseRequest.setPrice(new BigDecimal("50"));

        Card card = new Card();
        card.setCardId(1L);
        card.setBalance(new BigDecimal("100"));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Transaction transaction = transactionService.processPurchase(purchaseRequest);

        assertNotNull(transaction);
        assertEquals(new BigDecimal("50"), card.getBalance());
    }
	
	@Test
    public void testAnnulateTransaction_Success() {
        AnulationRequest anulationRequest = new AnulationRequest();
        anulationRequest.setCardId("1");
        anulationRequest.setTransactionId(1L);

        Card card = new Card();
        card.setCardId(1L);
        card.setBalance(new BigDecimal("50"));

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setPrice(new BigDecimal("50"));
        transaction.setTimestamp(LocalDateTime.now().minusHours(1));
        
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        assertDoesNotThrow(() -> transactionService.annulateTransaction(anulationRequest));
        assertTrue(transaction.isAnnulled());
        assertEquals(new BigDecimal("100"), card.getBalance());
    }
	
	
	@Test
    public void testProcessPurchase_InsufficientFunds() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCardId("1");
        purchaseRequest.setPrice(new BigDecimal("200"));

        Card card = new Card();
        card.setCardId(1L);
        card.setBalance(new BigDecimal("100"));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.processPurchase(purchaseRequest);
        });

        String expectedMessage = "Saldo insuficiente";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
