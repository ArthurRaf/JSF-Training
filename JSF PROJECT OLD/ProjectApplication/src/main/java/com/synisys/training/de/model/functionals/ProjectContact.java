package com.synisys.training.de.model.functionals;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.model.Project;

public class ProjectContact extends AbstractFunctional<Project> {

	private String firstName;
	private String lastName;
	private Organisation organisation;
	private String email;
	private String phone;

	public ProjectContact(Project project, FunctionalStatus functionalStatus) {
		super(project, functionalStatus);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	protected ToStringHelper getToStringHelper() {
		return super.getToStringHelper().add("firstName", firstName).add("lastName", lastName)
				.add("organisation", organisation).add("email", email).add("phone", phone);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + 31 * Objects.hashCode(firstName, lastName, organisation, email, phone);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this){
			return true;
		}
		if (obj != null && getClass().equals(obj.getClass())) {
			ProjectContact that = (ProjectContact) obj;
			return super.equals(obj) && Objects.equal(this.firstName, that.firstName)
					&& Objects.equal(this.lastName, that.lastName)
					&& Objects.equal(this.organisation, that.organisation) && Objects.equal(this.email, that.email)
					&& Objects.equal(this.phone, that.phone);
		}
		return false;
	}

	@Override
	public ProjectContact cloneModel() {
		ProjectContact projectContact= new ProjectContact(getParent(), getFunctionalStatus());
		copyTo(projectContact);
		return projectContact;
	}
	public void copyTo(ProjectContact projectContact){
		super.copyTo(projectContact);
		projectContact.setFirstName(firstName);
		projectContact.setLastName(lastName);
		projectContact.setOrganisation(organisation);
		projectContact.setEmail(email);
		projectContact.setPhone(phone);
	}
	
	public void delete(){
		this.setFunctionalStatus(FunctionalStatus.Deleted);
	}
}
