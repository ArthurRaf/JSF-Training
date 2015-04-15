package com.synisys.training.report;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SectorReport implements Serializable {

	private static final long serialVersionUID = 1L;

	private ReportItem reportItem;

	private List<SectorReportItem> sectorItems;

	public SectorReport(ReportItem reportItem) {
		Objects.requireNonNull(reportItem);
		this.reportItem = reportItem;
	}

	public ReportItem getReportItem() {
		return reportItem;
	}

	public void setReportItem(ReportItem reportItem) {
		this.reportItem = reportItem;
	}

	public List<SectorReportItem> getSectorItems() {
		return sectorItems;
	}

	public void setSectorItems(List<SectorReportItem> sectorItems) {
		this.sectorItems = sectorItems;
	}		
}
