package com.synisys.training.de.managedBean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.synisys.training.de.model.functionals.FunctionalStatus;
import com.synisys.training.de.model.functionals.ProjectContact;

@ManagedBean
@SessionScoped
public class ContactSubForm {
	private ProjectContact currentProjectContact;	
	private boolean isNew = false;
			
	private ProjectForm getProjectForm(){
		ProjectForm projectForm = (ProjectForm) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("projectForm");
		return projectForm;
	}
	
	public ProjectContact getCurrentProjectContact() {		
		return currentProjectContact;
	}

	public void setCurrentProjectContact(ProjectContact currentProjectContact) {
		this.currentProjectContact = currentProjectContact;
	}
	
	public void addNewProjectContact() {		
		this.currentProjectContact = new ProjectContact(getProjectForm().getCurrentProject(), FunctionalStatus.New);
		this.isNew = true;
	}
	
	public String okAction() {		
		List<ProjectContact> projectContacts = getProjectForm().getCurrentProject()
				.getContacts();
		// if current contact is new, add it to the list
		if (this.isNew) {
			projectContacts.add(currentProjectContact);			
		} else {// contact is edited, find contact and replace it with this
				// modified clone		
			for (ProjectContact projectContact : projectContacts) {
				if (projectContact.getId() == currentProjectContact.getId()) {
					projectContacts.remove(projectContact);
					projectContacts.add(currentProjectContact);
					break;
				}
			}
		}
		//reset current project contact
		cleanFormProperties();
		
		return "ProjectAddEditForm";
	}
	
	public String cancelAction(){
		//reset current project contact
		cleanFormProperties();
		
		return "ProjectAddEditForm";
	}
	
	private void cleanFormProperties(){
		this.currentProjectContact = null;
		this.isNew = false;
	}
}
