package com.synisys.training.report;

import java.io.Serializable;
import java.util.Objects;

public class ReportItem implements Serializable{
	private static final long serialVersionUID = 1L;	
	
	private Integer reportId;
	private String name;
	private String description;
	
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		Objects.requireNonNull(reportId);
		this.reportId = reportId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
