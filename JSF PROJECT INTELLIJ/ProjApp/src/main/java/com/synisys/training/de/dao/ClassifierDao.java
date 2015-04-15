package com.synisys.training.de.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import com.synisys.training.de.classifier.ChildEntity;
import com.synisys.training.de.classifier.Currency;
import com.synisys.training.de.classifier.Entity;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.classifier.location.District;
import com.synisys.training.de.classifier.location.Province;
import com.synisys.training.de.classifier.location.Region;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.classifier.sector.SubSector;

public class ClassifierDao {

	private DataSource dataSource;
	private ClassifierDao classifierDao;
	private static final String LOAD_CURRENCIES = "exec USP_DE_S_Currencies";
	private static final String LOAD_SECTORS = "exec USP_DE_S_Sectors";
	private static final String LOAD_SUB_SECTORS = "exec USP_DE_S_SubSectors @SectorID=?";
	private static final String LOAD_ORGANISATIONS = "exec USP_DE_S_Organisations";
	private static final String INSERT_ORGANISATIONS = "exec USP_DE_I_Organisation @Name=?,@Description=?";
	private static final String LOAD_REGIONS = "exec USP_DE_S_Regions";
	private static final String LOAD_PROVINCES = "exec USP_DE_S_Provinces @RegionID=?";
	private static final String LOAD_DISTRICTS = "exec USP_DE_S_Districts @ProvinceID=?";


	private ClassifierFactory<Sector> sectorFactory = new AbstractClassifierFactory<Sector>() {

		@Override
		public Sector newInstance(int id, String name) {
			return new Sector(id, name);
		}
	};


	private ChildClassifierFactory<Sector, SubSector> subSectorFactory = new AbstractChildClassifierFactory<Sector, SubSector>() {

		@Override
		public SubSector newInstance(int id, String name, Sector sector) {
			return new SubSector(id, name, sector);
		}
	};
	private ClassifierFactory<Region> regionFactory = new AbstractClassifierFactory<Region>() {

		@Override
		public Region newInstance(int id, String name) {
			return new Region(id, name);
		}
	};
	private ChildClassifierFactory<Region, Province> provinceFactory = new AbstractChildClassifierFactory<Region, Province>() {

		@Override
		public Province newInstance(int id, String name, Region region) {
			return new Province(id, name, region);
		}
	};
	private ChildClassifierFactory<Province, District> districtFactory = new AbstractChildClassifierFactory<Province, District>() {

		@Override
		public District newInstance(int id, String name, Province region) {
			return new District(id, name, region);
		}
	};

	private ClassifierFactory<Currency> currencyFactory = new AbstractClassifierFactory<Currency>() {

		@Override
		public Currency newInstance(int id, String name) {
			return new Currency(id, name);
		}
	};

//	private ClassifierFactory<Donor> donorFactory = new AbstractClassifierFactory<Donor>() {
//
//		@Override
//		public Donor newInstance(int id, String name) {
//			return new Donor(id, name);
//		}
//	};
	private ClassifierFactory<Organisation> organisationFactory = new AbstractClassifierFactory<Organisation>() {

		@Override
		public Organisation newInstance(int id, String name) {
			return new Organisation();
		}
	};


	public ClassifierDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ClassifierDao(DataSource dataSource, ClassifierDao classifierDao) {
		this.dataSource = dataSource;
		this.classifierDao = classifierDao;

	}

	public Sector loadSector(int sectorId) throws DaoException {
		if (!sectorFactory.getClassifierCache().isCached()) {
			loadSectors();
		}
		return sectorFactory.getClassifierCache().getInstance(sectorId);
	}

	public List<Sector> loadSectors() throws DaoException {
		return loadClassifiers(sectorFactory, LOAD_SECTORS);
	}

	public SubSector loadSubSector(Sector sector, int subSectorId) throws DaoException {
		if (!subSectorFactory.getChildClassifierCache().isCached(sector)) {
			loadSubSectors(sector);
		}
		return subSectorFactory.getChildClassifierCache().getInstance(subSectorId);
	}

	public List<SubSector> loadSubSectors(Sector sector) throws DaoException {
		Objects.requireNonNull(sector);
		return loadChildClassifiers(subSectorFactory, LOAD_SUB_SECTORS, sector);
	}

	public Currency loadCurrency(int currencyId) throws DaoException {
		if (!currencyFactory.getClassifierCache().isCached()) {
			loadCurrencies();
		}
		return currencyFactory.getClassifierCache().getInstance(currencyId);
	}

	public List<Currency> loadCurrencies() throws DaoException {
		return loadClassifiers(currencyFactory, LOAD_CURRENCIES);
	}

	private <T extends Entity> List<T> loadClassifiers(ClassifierFactory<T> classifierFactory, String sql)
			throws DaoException {
		Objects.requireNonNull(classifierFactory);
		Objects.requireNonNull(sql);

		if (classifierFactory.getClassifierCache().isCached()) {
			return classifierFactory.getClassifierCache().getInstances();
		}

		List<T> classifiers = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					T entity = classifierFactory.newInstance(resultSet.getInt(1), resultSet.getString(2));
					classifiers.add(entity);
				}
				classifierFactory.getClassifierCache().put(classifiers);
				return classifiers;
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	private <Parent extends Entity, T extends ChildEntity<Parent>> List<T> loadChildClassifiers(
			ChildClassifierFactory<Parent, T> childClassifierFactory, String sql, Parent parentEntity)
			throws DaoException {
		Objects.requireNonNull(childClassifierFactory);
		Objects.requireNonNull(sql);
		Objects.requireNonNull(parentEntity);

		if (childClassifierFactory.getChildClassifierCache().isCached(parentEntity)) {
			return childClassifierFactory.getChildClassifierCache().getInstances(parentEntity);
		}

		List<T> classifiers = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, parentEntity.getId());

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					T entity = childClassifierFactory.newInstance(resultSet.getInt(1), resultSet.getString(2),
							parentEntity);
					classifiers.add(entity);
				}
				childClassifierFactory.getChildClassifierCache().put(parentEntity, classifiers);
				return classifiers;
			}

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public List<Organisation> loadOrganisations() throws DaoException {
		return loadClassifiers(organisationFactory, LOAD_ORGANISATIONS);
	}
	
	public void resetOrganisationsCache(){
		organisationFactory.getClassifierCache().cleanCache();
	}

	public Organisation loadOrganisation(int organisationId) throws DaoException {
		if (!organisationFactory.getClassifierCache().isCached()) {
			loadOrganisations();
		}
		return organisationFactory.getClassifierCache().getInstance(organisationId);
	}

	/*  locations */

	public Region loadRegion(int regionId) throws DaoException {
		if (!regionFactory.getClassifierCache().isCached()) {
			loadRegions();
		}
		return regionFactory.getClassifierCache().getInstance(regionId);
	}

	public Province loadProvince(Region region, int provinceId) throws DaoException {
		if (!provinceFactory.getChildClassifierCache().isCached(region)) {
			loadProvinces(region);
		}
		return provinceFactory.getChildClassifierCache().getInstance(provinceId);
	}

	public District loadDistrict(Province province, int districtId) throws DaoException {
		if (!districtFactory.getChildClassifierCache().isCached(province)) {
			loadDistricts(province);
		}
		return districtFactory.getChildClassifierCache().getInstance(districtId);
	}

	public List<Region> loadRegions() throws DaoException {
		return loadClassifiers(regionFactory, LOAD_REGIONS);
	}

	public List<Province> loadProvinces(Region region) throws DaoException {
		return loadChildClassifiers(provinceFactory, LOAD_PROVINCES, region);
	}

	public List<District> loadDistricts(Province province) throws DaoException {
		return loadChildClassifiers(districtFactory, LOAD_DISTRICTS, province);
	}

	public void saveOrganisation(Organisation organisation) throws DaoException {
		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setAutoCommit(false);
				if (organisation.getId() == 0) {
					try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORGANISATIONS)) {
						setStatementParameters(organisation, preparedStatement);
						try (ResultSet resultSet = preparedStatement.executeQuery()) {
							if(!resultSet.next()){
								throw new DaoException("Insert project statement should return identity row ");
							}
						}
					}

				}
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException sqlException) {
					sqlException.printStackTrace();
				}

				throw new DaoException(String.format("Exception occured when trying to save project %s",
						organisation.toString()), e);
			} catch (Exception e) {
				try {
					connection.rollback();
				} catch (SQLException sqlException) {
					sqlException.printStackTrace();
				}
				throw e;
			}
		} catch (SQLException e) {//can be raised on connection close
			throw new DaoException(e);
		}

	}
	private void setStatementParameters(Organisation organisation, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, organisation.getName());
		preparedStatement.setString(2, organisation.getDescription());

	}
}
