package com.bank_inc.card.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {

	private String cardId;
	private BigDecimal price;
}
