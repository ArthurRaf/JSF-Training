package com.synisys.training.de.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.joda.time.DateTime;

import com.synisys.training.de.classifier.location.Province;
import com.synisys.training.de.classifier.location.Region;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.model.Money;
import com.synisys.training.de.model.Project;
import com.synisys.training.de.model.functionals.FunctionalStatus;
import com.synisys.training.de.model.functionals.ProjectContact;
import com.synisys.training.de.model.functionals.ProjectDonor;
import com.synisys.training.de.model.functionals.ProjectLocation;

public class ProjectDao {
	private DataSource dataSource;
	private ClassifierDao classifierDao;

	private static final String LOAD_PROJECT = "exec USP_DE_S_Projects @ProjectID=?";
	private static final String INSERT_PROJECT = "exec USP_DE_I_Project @ProjectTitle=?, @Description=?, @StartDate=?, @EndDate=?, @Amount=?, @CurrencyId=?, @SubSectorID=?";
	private static final String UPDATE_PROJECT = "exec USP_DE_U_Project @ProjectTitle=?, @Description=?, @StartDate=?, @EndDate=?, @Amount=?, @CurrencyId=?, @SubSectorID=?, @ProjectID=?";
	private static final String DELETE_PROJECT = "exec USP_DE_D_Projects @ProjectID=?";

	private static final String LOAD_PROJECT_CONTACTS = "exec USP_DE_S_ProjectContacts @ProjectID=?";
	private static final String INSERT_PROJECT_CONTACT = "exec USP_DE_I_ProjectContacts @FirstName=?, @LastName=?, @OrganisationID=?, @Email=?, @Phone=?, @ProjectID=?";
	private static final String UPDATE_PROJECT_CONTACT = "exec USP_DE_U_ProjectContacts @FirstName=?, @LastName=?, @OrganisationID=?, @Email=?, @Phone=?, @ProjectContactId=?";
	private static final String DELETE_PROJECT_CONTACT = "exec USP_DE_D_ProjectContacts @ProjectContactId=?";

	private static final String LOAD_PROJECT_LOCATIONS = "exec USP_DE_S_ProjectLocations @ProjectID=?";
	private static final String INSERT_PROJECT_LOCATION = "exec USP_DE_I_ProjectLocations @ProvinceID=?, @DistrictID=?, @LPercent=?, @ProjectID=?";
	private static final String UPDATE_PROJECT_LOCATION = "exec USP_DE_U_ProjectLocations @ProvinceID=?, @DistrictID=?, @LPercent=?, @ProjectLocationId=?";
	private static final String DELETE_PROJECT_LOCATION = "exec USP_DE_D_ProjectLocations @ProjectLocationId=?";

	private static final String LOAD_PROJECT_DONORS = "exec USP_DE_S_ProjectDonors @ProjectID=?";
	private static final String INSERT_PROJECT_DONOR = "exec USP_DE_I_ProjectDonors @OrganisationID=?, @ProjectID=?";
	private static final String UPDATE_PROJECT_DONOR = "exec USP_DE_U_ProjectDonors @OrganisationID=?, @ProjectDonorId=?";
	private static final String DELETE_PROJECT_DONOR = "exec USP_DE_D_ProjectDonors @ProjectDonorId=?";

	public ProjectDao(DataSource dataSource, ClassifierDao classifierDao) {
		this.dataSource = dataSource;
		this.classifierDao = classifierDao;

	}
	
	public ClassifierDao getClassifierDao(){
		Objects.requireNonNull("Classifier Dao can not be null.");
		return classifierDao;
	}

	public Project loadProject(int projectId) throws DaoException {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(LOAD_PROJECT)) {
			statement.setInt(1, projectId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					throw new DaoException(String.format("Project with id %d not found", projectId));
				}
				if (resultSet.getInt("ProjectID") != projectId) {
					throw new IllegalStateException("Wrong data loaded from db");
				}

				Project project = new Project(projectId);
				project.setTitle(resultSet.getString("ProjectTitle"));
				project.setDescription(resultSet.getString("Description"));
				project.setStartDate(new DateTime(resultSet.getTimestamp("StartDate")));
				project.setEndDate(new DateTime(resultSet.getTimestamp("EndDate")));
				Money totalAmount = new Money(resultSet.getBigDecimal("Amount"), classifierDao.loadCurrency(resultSet.getInt("CurrencyID")));
				project.setTotalAmount(totalAmount);

				project.setContacts(loadContacts(connection, project));
				Sector sector = classifierDao.loadSector(resultSet.getInt("SectorID"));
				project.setSubSector(classifierDao.loadSubSector(sector, resultSet.getInt("SubSectorID")));

				project.setLocations(loadLocations(connection, project));
				project.setDonors(loadDonors(connection, project));
				if (resultSet.next()) {
					throw new DaoException(String.format("Multiple records found for project with id %d ", projectId));
				}
				return project;
			}

		} catch (SQLException e) {
			throw new DaoException(String.format("Exception occured loading project with id %d", projectId), e);
		}
	}
	
	public List<Project> loadAllProjects() throws DaoException {		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(LOAD_PROJECT)) {
			statement.setObject(1, null);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Project> projects = new ArrayList<>();
				
				Project project= null;
				while (resultSet.next()){
					project = new Project(resultSet.getInt("ProjectID"));
					project.setTitle(resultSet.getString("ProjectTitle"));
					projects.add(project);
				}
				return projects;
			}
		} catch (SQLException e) {
			throw new DaoException("Exception occured while loading all projects list", e);
		}
	}	

	public void saveProject(Project project) throws DaoException {
		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setAutoCommit(false);
				if (project.getId() == null) {
					try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT)) {
						setStatementParameters(project, preparedStatement);
						try (ResultSet resultSet = preparedStatement.executeQuery()) {
							if(!resultSet.next()){
								throw new DaoException("Insert project statement should return identity row ");
							}
							int id = resultSet.getInt("ProjectID");
							project.setId(id);
						}
					}

				}
				else {
					try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT)) {
						setStatementParameters(project, preparedStatement);
						preparedStatement.setInt(8, project.getId());
						preparedStatement.executeUpdate();
					}
				}
				saveContacts(connection, project);
				saveLocations(connection, project);
				saveDonors(connection, project);
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException sqlException) {
					sqlException.printStackTrace();
				}

				throw new DaoException(String.format("Exception occured when trying to save project %s",
						project.toString()), e);
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

	public void deleteProject(Project project) throws DaoException {

		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setAutoCommit(false);

				try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT)) {
					preparedStatement.setInt(1, project.getId());
					preparedStatement.executeUpdate();
				}
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException sqlException) {
					sqlException.printStackTrace();
				}

				throw new DaoException(String.format("Exception occured when trying to delete project %s",
						project.toString()), e);
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

	private void saveContacts(Connection connection, Project project) throws SQLException, DaoException {
		for (ProjectContact projectContact : project.getContacts()) {
			if (projectContact.getFunctionalStatus() == FunctionalStatus.New) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT_CONTACT)) {
					setStatementParameters(projectContact, preparedStatement);
					preparedStatement.setInt(6, project.getId());
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						if(!resultSet.next()){
							throw new DaoException("Insert Contact statement should return identity row ");
						}
						int id = resultSet.getInt("projectContactID");
						projectContact.setId(id);
						projectContact.setFunctionalStatus(FunctionalStatus.Regular);
					}
				}
			}
			else if (projectContact.getFunctionalStatus() == FunctionalStatus.Regular) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT_CONTACT)) {
					setStatementParameters(projectContact, preparedStatement);
					preparedStatement.setInt(6, projectContact.getId());
					preparedStatement.executeUpdate();
				}
			}
			else if (projectContact.getFunctionalStatus() == FunctionalStatus.Deleted) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT_CONTACT)) {
					preparedStatement.setInt(1, projectContact.getId());
					preparedStatement.executeUpdate();
				}
			}
			else {
				throw new IllegalStateException();
			}
		}
	}

	private void saveLocations(Connection connection, Project project) throws SQLException, DaoException {
		for (ProjectLocation projectLocation : project.getLocations()) {
			if (projectLocation.getFunctionalStatus() == FunctionalStatus.New) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT_LOCATION)) {
					setStatementParameters(projectLocation, preparedStatement);
					preparedStatement.setInt(4, project.getId());
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						if(!resultSet.next()){
							throw new DaoException("Insert Location statement should return identity row ");
						}
						int id = resultSet.getInt("projectLocationID");
						projectLocation.setId(id);
						projectLocation.setFunctionalStatus(FunctionalStatus.Regular);
					}
				}
			}
			else if (projectLocation.getFunctionalStatus() == FunctionalStatus.Regular) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT_LOCATION)) {
					setStatementParameters(projectLocation, preparedStatement);
					preparedStatement.setInt(4, projectLocation.getId());
					preparedStatement.executeUpdate();
				}
			}
			else if (projectLocation.getFunctionalStatus() == FunctionalStatus.Deleted) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT_LOCATION)) {
					preparedStatement.setInt(1, projectLocation.getId());
					preparedStatement.executeUpdate();
				}
			}
			else {
				throw new IllegalStateException();
			}
		}

	}

	private void saveDonors(Connection connection, Project project) throws SQLException, DaoException {
		for (ProjectDonor projectDonor : project.getDonors()) {
			if (projectDonor.getFunctionalStatus() == FunctionalStatus.New) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT_DONOR)) {
					setStatementParameters(projectDonor, preparedStatement);
					preparedStatement.setInt(2, project.getId());
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						if(!resultSet.next()){
							throw new DaoException("Insert Donor statement should return identity row ");
						}
						int id = resultSet.getInt("projectDonorID");
						projectDonor.setId(id);
						projectDonor.setFunctionalStatus(FunctionalStatus.Regular);
					}
				}
			}
			else if (projectDonor.getFunctionalStatus() == FunctionalStatus.Regular) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT_DONOR)) {
					setStatementParameters(projectDonor, preparedStatement);
					preparedStatement.setInt(2, projectDonor.getId());
					preparedStatement.executeUpdate();
				}
			}
			else if (projectDonor.getFunctionalStatus() == FunctionalStatus.Deleted) {
				try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT_DONOR)) {
					preparedStatement.setInt(1, projectDonor.getId());
					preparedStatement.executeUpdate();
				}
			}
			else {
				throw new IllegalStateException();
			}
		}

	}

	private void setStatementParameters(Project project, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, project.getTitle());
		preparedStatement.setString(2, project.getDescription());
		preparedStatement.setTimestamp(3, new Timestamp(project.getStartDate().getMillis()));
		preparedStatement.setTimestamp(4, new Timestamp(project.getEndDate().getMillis()));
		preparedStatement.setBigDecimal(5, project.getTotalAmount().getAmount());
		preparedStatement.setInt(6, project.getTotalAmount().getCurrency().getId());
		preparedStatement.setInt(7, project.getSubSector().getId());

	}

	private void setStatementParameters(ProjectContact projectContact, PreparedStatement preparedStatement)
			throws SQLException {
		preparedStatement.setString(1, projectContact.getFirstName());
		preparedStatement.setString(2, projectContact.getLastName());
		preparedStatement.setInt(3, projectContact.getOrganisation().getId());
		preparedStatement.setString(4, projectContact.getEmail());
		preparedStatement.setString(5, projectContact.getPhone());

	}

	private void setStatementParameters(ProjectLocation projectLocation, PreparedStatement preparedStatement)
			throws SQLException {
		preparedStatement.setInt(1, projectLocation.getProvince().getId());
		preparedStatement.setInt(2, projectLocation.getDistrict() != null ? projectLocation.getDistrict().getId()
				: null);
		preparedStatement.setBigDecimal(3, projectLocation.getPercent());

	}

	private void setStatementParameters(ProjectDonor projectDonor, PreparedStatement preparedStatement)
			throws SQLException {
		preparedStatement.setInt(1, projectDonor.getDonor().getId());

	}

	private List<ProjectContact> loadContacts(Connection connection, Project project) throws SQLException, DaoException {

		try (PreparedStatement statement = connection.prepareStatement(LOAD_PROJECT_CONTACTS)) {
			List<ProjectContact> contacts = new ArrayList<>();
			statement.setInt(1, project.getId());
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					ProjectContact projectContact = new ProjectContact(project, FunctionalStatus.Regular);
					projectContact.setId(resultSet.getInt("ProjectContactID"));
					projectContact.setFirstName(resultSet.getString("FirstName"));
					projectContact.setLastName(resultSet.getString("LastName"));
					projectContact.setOrganisation(classifierDao.loadOrganisation(resultSet.getInt("OrganisationId")));
					projectContact.setEmail(resultSet.getString("Email"));
					projectContact.setPhone(resultSet.getString("Phone"));
					contacts.add(projectContact);
				}
				return contacts;
			}
		}
	}

	private List<ProjectLocation> loadLocations(Connection connection, Project project) throws SQLException,
			DaoException {

		try (PreparedStatement statement = connection.prepareStatement(LOAD_PROJECT_LOCATIONS)) {
			List<ProjectLocation> locations = new ArrayList<>();
			statement.setInt(1, project.getId());
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					ProjectLocation projectLocation = new ProjectLocation(project, FunctionalStatus.Regular);
					projectLocation.setId(resultSet.getInt("ProjectLocationID"));
					Region region = classifierDao.loadRegion(resultSet.getInt("RegionID"));
					Province province = classifierDao.loadProvince(region, resultSet.getInt("ProvinceID"));
					projectLocation.setProvince(province);
					projectLocation.setDistrict(classifierDao.loadDistrict(province, resultSet.getInt("DistrictID")));
					projectLocation.setPercent(resultSet.getBigDecimal("LPercent"));
					locations.add(projectLocation);
				}
				return locations;
			}
		}
	}

	private List<ProjectDonor> loadDonors(Connection connection, Project project) throws SQLException, DaoException {

		try (PreparedStatement statement = connection.prepareStatement(LOAD_PROJECT_DONORS)) {
			List<ProjectDonor> donors = new ArrayList<>();
			statement.setInt(1, project.getId());
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					ProjectDonor projectDonor = new ProjectDonor(project, FunctionalStatus.Regular);
					projectDonor.setId(resultSet.getInt("ProjectDonorId"));
					projectDonor.setDonor(classifierDao.loadOrganisation(resultSet.getInt("OrganisationID")));
					donors.add(projectDonor);
				}
				return donors;
			}
		}
	}
}
