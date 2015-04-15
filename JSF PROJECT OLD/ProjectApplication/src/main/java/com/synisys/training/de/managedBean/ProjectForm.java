package com.synisys.training.de.managedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import com.synisys.training.Initializer;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.classifier.sector.SubSector;
import com.synisys.training.de.dao.ClassifierDao;
import com.synisys.training.de.dao.DaoException;
import com.synisys.training.de.dao.ProjectDao;
import com.synisys.training.de.model.Project;

@ManagedBean
@SessionScoped
public class ProjectForm {
	private Project currentProject;
	private ProjectDao dao;
	
	private Sector sector;
	
	public ProjectForm(){
		ClassifierDao classifierDao = new ClassifierDao(Initializer.getDataSource());
		dao = new ProjectDao(Initializer.getDataSource(), classifierDao);
	}

	public Project getCurrentProject() {
		return currentProject;
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
	
	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}
	
	public String resetSubSector(ValueChangeEvent event){
		this.currentProject.setSubSector(null);
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
	
	
	private void cleanFormState(){
		this.currentProject = null;				
		this.sector = null;		
	}

}
