package com.synisys.training;

import java.beans.PropertyVetoException;
import java.util.Objects;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Application Lifecycle Listener implementation class Initializer
 *
 */
public class Initializer implements ServletContextListener {
	private static ComboPooledDataSource dataSource = null; 

	public static ComboPooledDataSource getDataSource(){
		Objects.requireNonNull(dataSource);
		return dataSource;
	}
	
    /**
     * Default constructor. 
     */
    public Initializer() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	//Initialize data source for the application
    	
    	dataSource = new ComboPooledDataSource();
		
		try {
			dataSource.setDriverClass("net.sourceforge.jtds.jdbc.Driver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Cannot initialize dataSource", e);
		}
		
		dataSource.setJdbcUrl("jdbc:jtds:sqlserver://sis2s093:1433;databaseName=cu_Training_Chili");
		dataSource.setUser("idmdaduser");
		dataSource.setPassword("idmdaduser");
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	dataSource.close();
    }
	
}
