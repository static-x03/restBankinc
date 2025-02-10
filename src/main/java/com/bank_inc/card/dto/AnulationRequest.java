package com.bank_inc.card.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnulationRequest {

	private String cardId;
    private Long transactionId;
}
