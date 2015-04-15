package com.synisys.training.de.model.functionals;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.model.Project;

public class ProjectDonor extends AbstractFunctional<Project> {

	private Organisation donor;

	public ProjectDonor(Project project, FunctionalStatus functionalStatus) {
		super(project, functionalStatus);
	}

	protected ToStringHelper getToStringHelper() {
		return super.getToStringHelper().add("donor", donor);
	}

	public Organisation getDonor() {
		return donor;
	}

	public void setDonor(Organisation donor) {
		this.donor = donor;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + 31 * Objects.hashCode(donor);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this){
			return true;
		}
		if (obj != null && getClass().equals(obj.getClass())) {
			ProjectDonor that = (ProjectDonor) obj;
			return super.equals(obj) && Objects.equal(this.donor, that.donor);
		}
		return false;
	}

	@Override
	public ProjectDonor cloneModel() {
		ProjectDonor projectDonor = new ProjectDonor(getParent(), getFunctionalStatus());
		copyTo(projectDonor);
		return projectDonor;
	}

	public void copyTo(ProjectDonor projectDonor) {
		super.copyTo(projectDonor);
		projectDonor.setDonor(donor);
	}

}
