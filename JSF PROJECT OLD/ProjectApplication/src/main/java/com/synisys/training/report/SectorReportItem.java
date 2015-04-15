package com.synisys.training.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class SectorReportItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer sectorId;
	private String sectorName;
	private BigDecimal amount;
	
	public SectorReportItem(Integer sectorId){
		Objects.requireNonNull(sectorId);
		this.sectorId = sectorId;
	}
	
	public Integer getSectorId() {
		return sectorId;
	}
	public void setSectorId(Integer sectorId) {
		Objects.requireNonNull(sectorId);
		this.sectorId = sectorId;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
