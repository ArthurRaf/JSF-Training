package com.synisys.training.de.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.synisys.training.de.classifier.Currency;
import com.synisys.training.de.utility.BigDecimalHelper;

public final class Money {
	private BigDecimal amount;
	private Currency currency;

	public Money(BigDecimal amount, Currency currency){
		this.amount = amount;
		this.currency = currency;
	}
	
	public Money(Money money){
		this.amount = money.amount;
		this.currency = money.currency;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currency) *31+BigDecimalHelper.hash(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Money) {
			Money that = (Money) obj;
			return Objects.equals(this.currency, that.currency) && BigDecimalHelper.equals(this.amount, that.amount);
		}
		else {
			return false;
		}

	}
}
