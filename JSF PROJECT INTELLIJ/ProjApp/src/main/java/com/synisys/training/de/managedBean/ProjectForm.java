package com.synisys.training.de.managedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import com.synisys.training.Initializer;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.classifier.location.District;
import com.synisys.training.de.classifier.location.Province;
import com.synisys.training.de.classifier.location.Region;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.classifier.sector.SubSector;
import com.synisys.training.de.dao.ClassifierDao;
import com.synisys.training.de.dao.DaoException;
import com.synisys.training.de.dao.ProjectDao;
import com.synisys.training.de.model.Project;
import com.synisys.training.de.model.functionals.FunctionalStatus;
import com.synisys.training.de.model.functionals.ProjectContact;

@ManagedBean
@SessionScoped
public class ProjectForm {

	private Project currentProject;

	private ProjectDao dao;
	
	private Sector sector;

	private Region region;

	private Province province;

	private Province provinces;

	private Organisation organisation;

	private District districts;

	private District district;



	public ProjectForm(){
		ClassifierDao classifierDao = new ClassifierDao(Initializer.getDataSource());
		dao = new ProjectDao(Initializer.getDataSource(), classifierDao);
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public void loadCurrentProject(Integer currentProjectId){
		Objects.requireNonNull(currentProjectId, "Current Project id can not be null.");
		
		try {
			this.currentProject = dao.loadProject(currentProjectId);
		} catch (DaoException e) {
			throw new RuntimeException(String.format("Can not load project by %s id.", currentProjectId), e);
		}
		
		//initialize sector property
		if (this.currentProject.getSubSector() != null) {
			this.sector = this.currentProject.getSubSector().getParent(); 
		}		
	}

	public void  addNewProject(Integer id) {
		this.currentProject = new Project(id);
	}


	
	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Province getProvinces() {
		return provinces;
	}

	public void setProvinces(Province provinces) {
		this.provinces = provinces;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public District getDistricts() {
		return districts;
	}

	public void setDistricts(District districts) {
		this.districts = districts;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public String resetSubSector(ValueChangeEvent event){
		this.currentProject.setSubSector(null);
		return null;
	}

	public String resetLocation(ValueChangeEvent event){
		this.currentProject.setLocations(null);
		return null;
	}
	
	public List<SubSector> getSubSectors() {
		Sector parent = this.sector;

		List<SubSector> subSectors = new ArrayList<>();
		if (parent != null) {
			try {
				subSectors.addAll(dao.getClassifierDao().loadSubSectors(parent));
			} catch (DaoException e) {
				throw new RuntimeException("Can not load sub-sectors", e);
			}
		}
		return subSectors;
	}

	public String saveProjects(){
		Objects.requireNonNull(currentProject,
				"Project not saved properly.");

		Objects.requireNonNull(currentProject,
				"Project is not specified for deletion.");



				// save project in DB
				try {
					dao.saveProject(currentProject);
				} catch (DaoException e) {
					throw new RuntimeException(String.format(
							"Can not save project by %s id", currentProject.getId()),
							e);
				}

				return "ProjectsAndReportsList";
			}
	public String cancelAction(){
		//reset current project contact
		cleanFormProperties();

		return "ProjectAddEditForm";
	}

	private void cleanFormProperties(){
		this.currentProject = null;
		this.dao = null;
		this.sector = null;
		this.region = null;
		this.province = null;
		this.provinces = null;
		this.organisation = null;
		this.districts = null;
		this.district = null;

	}
}
