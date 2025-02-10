package com.bank_inc.card;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

	@Id
	private Long cardId;
	@Column(nullable = false)
	private String expirationDate;
	@Column(nullable = false)
	private Boolean active = false;
	@Column(nullable = false)
	private BigDecimal balance = BigDecimal.ZERO;
	@Column(nullable = false)
	private boolean blocked = false;
	@Column(nullable = false)
	private String currency = "USD";
	
	@JsonIgnore
	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
	
	
}
