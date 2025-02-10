package com.bank_inc.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank_inc.card.Card;

public interface CardRepository extends JpaRepository<Card, Long>{

}
