package com.synisys.training.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import com.synisys.training.de.dao.DaoException;
import com.synisys.training.report.ReportItem;
import com.synisys.training.report.SectorReport;
import com.synisys.training.report.SectorReportItem;

public class ReportDao {
	private DataSource dataSource;
	
	private static final String LOAD_REPORTS_LIST = "exec USP_DE_S_Reports";
	
	private static final String LOAD_DONOR_REPORTS = "exec USP_RP_S_Donors";
	private static final String LOAD_REGIONS_REPORTS = "exec USP_RP_S_Regions";
	private static final String LOAD_PROJECTS_REPORTS = "exec USP_RP_S_Projects";
	private static final String LOAD_SECOTRS_REPORTS = "exec USP_RP_S_Sectors";
	
	public ReportDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<ReportItem> loadReports() throws DaoException {
		List<ReportItem> reportItems = new ArrayList<>();

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(LOAD_REPORTS_LIST)) {

			try (ResultSet resultSet = statement.executeQuery()) {
				
				ReportItem report;

				while (resultSet.next()) {
					report = new ReportItem();
					report.setReportId(resultSet.getInt("ReportID"));
					report.setName(resultSet.getString("Name"));
					report.setDescription(resultSet.getString("Description"));

					reportItems.add(report);
				}
				return reportItems;
			}
		} catch (SQLException e) {
			throw new DaoException("Exception occured while loading reports", e);
		}
	}
	
	public SectorReport loadSectorReport(ReportItem reportItem) throws DaoException {
		Objects.requireNonNull(reportItem, "Report Item can not be null");
		
		SectorReport sectorReport = new SectorReport(reportItem);

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(LOAD_SECOTRS_REPORTS)) {
			
			List<SectorReportItem> sectorItems = new ArrayList<>();
			
			try (ResultSet resultSet = statement.executeQuery()) {				
				SectorReportItem sectorItem;
				while (resultSet.next()) {
					sectorItem = new SectorReportItem(resultSet.getInt("SectorID"));
					sectorItem.setSectorName(resultSet.getString("Name"));
					sectorItem.setAmount(resultSet.getBigDecimal("Amount"));

					sectorItems.add(sectorItem);
				}				
			}
			
			sectorReport.setSectorItems(sectorItems);
			
		} catch (SQLException e) {
			throw new DaoException("Exception occured while loading reports", e);
		}
		
		return sectorReport;
	}	
}
