package com.synisys.training.report.controller;

import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.synisys.training.Initializer;
import com.synisys.training.de.dao.DaoException;
import com.synisys.training.report.dao.ReportDao;

public class ReportsListLoader extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private ReportDao dao;
	
	@Override
	public void init(){		
		dao = new ReportDao(Initializer.getDataSource());	
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		//check if reportsList attribute exists in application scope
		Object reportsList = request.getServletContext().getAttribute("reportsList");
		if (reportsList == null){
			// load reports list
			try {
				reportsList = dao.loadReports();
			} catch(DaoException e) {
				throw new RuntimeException(e);
			}
					
			// put reportsList in session
			if (reportsList == null){
				throw new RuntimeException("ReportList is not loaded!");
			} else {
				request.getServletContext().setAttribute("reportsList", reportsList);
			}			
		}
		
		response.sendRedirect("ProjectsAndReportsList.xhtml");
	}	
}
