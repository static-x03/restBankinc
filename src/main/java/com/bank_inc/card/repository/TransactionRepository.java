package com.bank_inc.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank_inc.card.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
