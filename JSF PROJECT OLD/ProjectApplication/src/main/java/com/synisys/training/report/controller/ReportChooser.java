package com.synisys.training.report.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.synisys.training.Initializer;
import com.synisys.training.de.dao.DaoException;
import com.synisys.training.report.*;
import com.synisys.training.report.dao.ReportDao;

public class ReportChooser extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public static final int DONORS_REPORT_ID = 1;
	public static final int PROJECTS_REPORT_ID = 2;
	public static final int REGIONS_REPORT_ID = 3;
	public static final int SECTORS_REPORT_ID = 4;
		
	private ReportDao dao;
	
	@Override
	public void init(){		
		dao = new ReportDao(Initializer.getDataSource());	
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ReportItem currentReport = null;
							
		try{
			//get current report item from request		
			Integer currentReportId = Integer.valueOf(request.getParameter("currentReportId"));
			Objects.requireNonNull(currentReportId, "Current report id is not found");
			
			//get reports list to find corresponding report
			List<ReportItem> reportList = (List<ReportItem>) request.getServletContext().getAttribute("reportsList");
			Objects.requireNonNull(reportList, "Can not find report list");
			
			for (ReportItem reportItem : reportList){
				if (reportItem.getReportId().equals(currentReportId)){
					currentReport = reportItem;
					break;
				}
			}
			 
		} catch (ClassCastException | NumberFormatException ex){			
			throw new RuntimeException("Current Report attribute is missing from session", ex);
		}
				
		
		if (currentReport == null) {
			throw new RuntimeException("Current Report attribute is missing from session");
		} 
						
		if (currentReport.getReportId() == null){
			throw new RuntimeException("Current Report doesn't have report id specified");
		} 
		
		String reportAddress;
		if (currentReport.getReportId() == DONORS_REPORT_ID) {
			reportAddress = "reports/DonorsReport.jsp";
			// load Donors report and set it in request
			//TODO:
		} else if (currentReport.getReportId() == PROJECTS_REPORT_ID) {
			reportAddress = "reports/ProjectsReport.jsp";
			// load Projects report and set it in request
			//TODO:
		} else if (currentReport.getReportId() == REGIONS_REPORT_ID) {
			reportAddress = "reports/RegionsReport.jsp";
			// load Regions report and set it in request
			//TODO:
		} else if (currentReport.getReportId() == SECTORS_REPORT_ID) {
			reportAddress = "reports/SectorsReport.jsp";
			
			// load Sectors report and set it in request
			SectorReport sectorReport;
			try {
				sectorReport = dao.loadSectorReport(currentReport);
			} catch (DaoException e) {
				throw new RuntimeException(e);
			}
			
			request.setAttribute("sectorReport", sectorReport);
			
		} else {			
			throw new RuntimeException(String.format("Unknown report Id: %s!", currentReport.getReportId()));
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(reportAddress);
		dispatcher.forward(request, response);
	}
}
