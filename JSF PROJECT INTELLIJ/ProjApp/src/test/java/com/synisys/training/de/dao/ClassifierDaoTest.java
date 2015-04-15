package com.synisys.training.de.dao;

import java.beans.PropertyVetoException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.synisys.training.de.classifier.Currency;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.classifier.location.District;
import com.synisys.training.de.classifier.location.Province;
import com.synisys.training.de.classifier.location.Region;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.classifier.sector.SubSector;

public class ClassifierDaoTest {

	@Test
	public void testClassifiers() throws PropertyVetoException, DaoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("net.sourceforge.jtds.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:jtds:sqlserver://sis2s093:1433;databaseName=cu_Training_Chili");
		dataSource.setUser("idmdaduser");
		dataSource.setPassword("idmdaduser");
		try {

			ClassifierDao dao = new ClassifierDao(dataSource);
			

			List<Sector> sectors = dao.loadSectors();
			Assert.assertNotEquals(sectors.size(), 0);
			List<SubSector> subSectors = dao.loadSubSectors(sectors.get(0));
			Assert.assertNotEquals(subSectors.size(), 0);
			List<Currency> currencies =  dao.loadCurrencies();
			Assert.assertNotEquals(currencies.size(), 0);
			List<Organisation> organisations =  dao.loadOrganisations();
			Assert.assertNotEquals(organisations.size(), 0);
			
			List<Region> regions=  dao.loadRegions();
			Assert.assertNotEquals(regions.size(), 0);
			List<Province> provinces=  dao.loadProvinces(regions.get(0));
			Assert.assertNotEquals(provinces.size(), 0);
			List<District> districts=  dao.loadDistricts(provinces.get(0));
			Assert.assertNotEquals(districts.size(), 0);

		} finally {
			dataSource.close();
		}
	}
}
