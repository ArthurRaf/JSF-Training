package com.synisys.training.de.dao;

import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.synisys.training.de.classifier.location.District;
import com.synisys.training.de.classifier.location.Province;
import com.synisys.training.de.classifier.location.Region;
import com.synisys.training.de.model.Money;
import com.synisys.training.de.model.Project;
import com.synisys.training.de.model.functionals.FunctionalStatus;
import com.synisys.training.de.model.functionals.ProjectContact;
import com.synisys.training.de.model.functionals.ProjectDonor;
import com.synisys.training.de.model.functionals.ProjectLocation;

public class ProjectDaoTest {
	private ComboPooledDataSource dataSource;
	private ProjectDao dao;
	private ClassifierDao classifierDao;

	@Before
	public void init() throws PropertyVetoException {
		dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("net.sourceforge.jtds.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:jtds:sqlserver://sis2s093:1433;databaseName=cu_Training_Chili");
		dataSource.setUser("idmdaduser");
		dataSource.setPassword("idmdaduser");
		classifierDao = new ClassifierDao(dataSource);
		dao = new ProjectDao(dataSource, classifierDao);
	}
	
//	@Test
//	public void testDonorsEquality() throws DaoException {
//		
//		for(int i=1; i<100; i++){
//			Project project = new Project();
//			Set<ProjectDonor> donors = new HashSet<>();
//			ProjectDonor projectDonor;
//			
//			projectDonor = new ProjectDonor(project, FunctionalStatus.Regular);
//			projectDonor.setId(i);
//			projectDonor.setDonor(classifierDao.loadOrganisation(2));
//			donors.add(projectDonor);
//			
//			projectDonor = new ProjectDonor(project, FunctionalStatus.New);
//			donors.add(projectDonor);
//			projectDonor.setId(i + 1);
//			projectDonor.setFunctionalStatus(FunctionalStatus.Regular);
//			projectDonor.setDonor(classifierDao.loadOrganisation(1));
//
//			
//			Project project2 = new Project();
//			Set<ProjectDonor> donors2 = new HashSet<>();
//			ProjectDonor projectDonor2 = new ProjectDonor(project2, FunctionalStatus.Regular);
//			projectDonor2.setId(i);
//			projectDonor2.setDonor(classifierDao.loadOrganisation(2));
//			donors2.add(projectDonor2);
//
//			projectDonor2 = new ProjectDonor(project2, FunctionalStatus.Regular);
//			projectDonor2.setId(i + 1);
//			projectDonor2.setDonor(classifierDao.loadOrganisation(1));
//			donors2.add(projectDonor2);
//
//			Assert.assertEquals(donors, donors2);
//			
//		}
//		
//		
//		
//	}

	@Test
	public void testProjectClone() throws DaoException {
		Project project = createNewProject();
		Project clonedProject  = project.cloneProject();
		
		Assert.assertEquals(project, clonedProject);
	}
	
	@Test
	public void testProjectLoad() throws DaoException {

		Project project = dao.loadProject(1);
		Assert.assertEquals("Dangara Valley Irrigition project", project.getTitle());
		Assert.assertEquals(
				"Irrigation of new Dangara land needs in the completion of construction the VD-2 and VD-3 channels, construction of irrigation and drainage systems and provision of detailed projected works. The project will be carried out simultaneously in 3 districts, co",
				project.getDescription());
		Assert.assertEquals(new DateTime(2005, 10, 21, 0, 0, 0), project.getStartDate());
		Assert.assertEquals(new DateTime(2014, 10, 21, 0, 0, 0), project.getEndDate());
		Assert.assertEquals(9_100_000, project.getTotalAmount().getAmount().intValue());
		Assert.assertEquals(2, project.getTotalAmount().getCurrency().getId());
		Assert.assertEquals(1, project.getContacts().size());
		Assert.assertEquals("Johnny", project.getContacts().get(0).getFirstName());
		Assert.assertEquals("Crop/Pest control", project.getSubSector().getName());

		Assert.assertEquals(6, project.getLocations().size());
		Assert.assertEquals("Atacama", project.getLocations().get(0).getRegion().getName());
		Assert.assertEquals(10, project.getLocations().get(0).getPercent().intValue());
		Assert.assertEquals(1, project.getDonors().size());
		Assert.assertEquals(1, project.getDonors().iterator().next().getDonor().getId());
	}

	@Test
	public void testProjectInsert() throws DaoException {
		Project project = createNewProject();
		try {

			dao.saveProject(project);

			Project loadedProject = dao.loadProject(project.getId());

			Assert.assertEquals(project.getContacts(), loadedProject.getContacts());
			Assert.assertEquals(project.getLocations(), loadedProject.getLocations());
//			project.getDonors().equals(loadedProject.getDonors());
//			
//			
//			ArrayList<?> d1 = new ArrayList<>(project.getDonors());
//			ArrayList<?> d2 = new ArrayList<>(loadedProject.getDonors());			
//			Assert.assertEquals(d1.get(0), d2.get(1));
//			Assert.assertEquals(d1.get(1), d2.get(0));
//			
//			Assert.assertEquals(d1.get(0).hashCode(), d2.get(0).hashCode());
//			Assert.assertEquals(d1.get(1).hashCode(), d2.get(1).hashCode());
			
			Assert.assertEquals(project.getDonors(), loadedProject.getDonors());
			Assert.assertEquals(project, loadedProject);
			dao.deleteProject(project);
			boolean isDeleted = false;
			try {
				project = dao.loadProject(project.getId());
			} catch (DaoException e) {
				isDeleted = true;
			}
			Assert.assertTrue("Project should be deleted", isDeleted);

		} finally {
			if (project.getId() != null) {
				dao.deleteProject(project);
			}
		}

	}

	public void testProjectUpdate() throws DaoException {
		Project project = createNewProject();
		Project clonedProject = null;
		try {
			dao.saveProject(project);
			clonedProject = project.cloneProject();
			for (ProjectContact projectContact : clonedProject.getContacts()) {
				projectContact.setFunctionalStatus(FunctionalStatus.Deleted);
			}

			for (ProjectLocation projectLocation : clonedProject.getLocations()) {
				projectLocation.setFunctionalStatus(FunctionalStatus.Deleted);
			}

			for (ProjectDonor projectDonor : clonedProject.getDonors()) {
				projectDonor.setFunctionalStatus(FunctionalStatus.Deleted);
			}

			clonedProject.setTitle("Test2");
			clonedProject.setDescription("Description2");
			clonedProject.setStartDate(new DateTime(2015, 6, 21, 0, 0));
			clonedProject.setEndDate(new DateTime(2016, 6, 21, 0, 0));
			Money cost = new Money(new BigDecimal(1_500_000), classifierDao.loadCurrency(1));
			clonedProject.setTotalAmount(cost);
			//contacts
			List<ProjectContact> contacts = new ArrayList<>();
			ProjectContact projectContact = new ProjectContact(project, FunctionalStatus.New);
			projectContact.setFirstName("firstName");
			projectContact.setLastName("LastName");
			projectContact.setOrganisation(classifierDao.loadOrganisation(2));
			projectContact.setPhone("phone");
			projectContact.setEmail("email@email.email");

			contacts.add(projectContact);
			clonedProject.setContacts(contacts);

			clonedProject.setSubSector(classifierDao.loadSubSector(classifierDao.loadSector(2), 2));

			List<ProjectLocation> locations = new ArrayList<>();
			Region region;
			Province province;
			District district;
			ProjectLocation projectLocation = new ProjectLocation(project, FunctionalStatus.New);

			region = classifierDao.loadRegion(3);
			province = classifierDao.loadProvince(region, 3);
			district = classifierDao.loadDistrict(province, 3);
			projectLocation.setProvince(province);
			projectLocation.setDistrict(district);
			projectLocation.setPercent(new BigDecimal(100));
			locations.add(projectLocation);

			clonedProject.setLocations(locations);

			List<ProjectDonor> donors = new ArrayList<>();
			ProjectDonor projectDonor = new ProjectDonor(project, FunctionalStatus.New);
			projectDonor.setDonor(classifierDao.loadOrganisation(3));
			donors.add(projectDonor);

			clonedProject.setDonors(donors);

			dao.saveProject(clonedProject);
			Project loadedProject = dao.loadProject(project.getId());

			Assert.assertEquals(1, loadedProject.getContacts().size());
			Assert.assertEquals(1, loadedProject.getLocations().size());
			Assert.assertEquals(1, loadedProject.getDonors().size());

			Assert.assertEquals(clonedProject, loadedProject);

			//test clone/copyTo

		} finally {
			if (clonedProject != null && clonedProject.getId() != null) {
				dao.deleteProject(project);
			}
		}
	}

	

	@After
	public void cleanup() {
		dataSource.close();
	}

	private Project createNewProject() throws DaoException {
		Project project = new Project();
		project.setTitle("Test1");
		project.setDescription("Description1");
		project.setStartDate(new DateTime(2013, 6, 21, 0, 0));
		project.setEndDate(new DateTime(2014, 6, 21, 0, 0));
		Money cost = new Money(new BigDecimal(500_000), classifierDao.loadCurrency(2));
		project.setTotalAmount(cost);
		//contacts
		List<ProjectContact> contacts = new ArrayList<>();
		ProjectContact projectContact = new ProjectContact(project, FunctionalStatus.New);
		projectContact.setFirstName("firstName1");
		projectContact.setLastName("LastName1");
		projectContact.setOrganisation(classifierDao.loadOrganisation(1));
		projectContact.setPhone("phone1");
		projectContact.setEmail("email@email.email1");

		projectContact = new ProjectContact(project, FunctionalStatus.New);
		projectContact.setFirstName("firstName2");
		projectContact.setLastName("LastName2");
		projectContact.setOrganisation(classifierDao.loadOrganisation(2));
		projectContact.setPhone("phone2");
		projectContact.setEmail("email@email.email2");

		contacts.add(projectContact);
		project.setContacts(contacts);

		project.setSubSector(classifierDao.loadSubSector(classifierDao.loadSector(1), 1));

		List<ProjectLocation> locations = new ArrayList<>();
		Region region;
		Province province;
		District district;
		ProjectLocation projectLocation = new ProjectLocation(project, FunctionalStatus.New);

		region = classifierDao.loadRegion(1);
		province = classifierDao.loadProvince(region, 1);
		district = classifierDao.loadDistrict(province, 1);
		projectLocation.setProvince(province);
		projectLocation.setDistrict(district);
		projectLocation.setPercent(new BigDecimal(50));
		locations.add(projectLocation);

		projectLocation = new ProjectLocation(project, FunctionalStatus.New);
		region = classifierDao.loadRegion(2);
		province = classifierDao.loadProvince(region, 2);
		district = classifierDao.loadDistrict(province, 2);
		projectLocation.setProvince(province);
		projectLocation.setDistrict(district);
		projectLocation.setPercent(new BigDecimal(50));
		locations.add(projectLocation);

		project.setLocations(locations);

		List<ProjectDonor> donors = new ArrayList<>();
		ProjectDonor projectDonor = new ProjectDonor(project, FunctionalStatus.New);
		projectDonor.setDonor(classifierDao.loadOrganisation(1));
		donors.add(projectDonor);

		projectDonor = new ProjectDonor(project, FunctionalStatus.New);
		projectDonor.setDonor(classifierDao.loadOrganisation(2));
		donors.add(projectDonor);

		project.setDonors(donors);
		return project;

	}
}
