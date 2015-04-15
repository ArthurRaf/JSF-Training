package com.synisys.training.de.model.functionals;

import java.math.BigDecimal;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.synisys.training.de.classifier.location.District;
import com.synisys.training.de.classifier.location.Province;
import com.synisys.training.de.classifier.location.Region;
import com.synisys.training.de.model.Project;
import com.synisys.training.de.utility.BigDecimalHelper;

public class ProjectLocation extends AbstractFunctional<Project> {

	private Province province;
	private District district;
	private BigDecimal percent;

	public ProjectLocation(Project project, FunctionalStatus functionalStatus) {
		super(project, functionalStatus);
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public Region getRegion() {
		return province == null ? null : province.getParent();
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	protected ToStringHelper getToStringHelper() {
		return super.getToStringHelper().add("province", province).add("district", district).add("percent", percent);
	}

	@Override
	public int hashCode() {
		return (super.hashCode() + 31 * Objects.hashCode(province, district)) * 31 + BigDecimalHelper.hash(percent);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this){
			return true;
		}
		if (obj != null && getClass().equals(obj.getClass())) {
			ProjectLocation that = (ProjectLocation) obj;
			return super.equals(obj) && Objects.equal(this.province, that.province)
					&& Objects.equal(this.district, that.district)
					&& BigDecimalHelper.equals(this.percent, that.percent);
			//Objects.equal(this.percent, that.percent);
		}
		return false;
	}

	@Override
	public ProjectLocation cloneModel() {
		ProjectLocation projectLocation = new ProjectLocation(getParent(), getFunctionalStatus());
		copyTo(projectLocation);
		return projectLocation;
	}

	public void copyTo(ProjectLocation projectLocation) {
		super.copyTo(projectLocation);
		projectLocation.setProvince(province);
		projectLocation.setDistrict(district);
		projectLocation.setPercent(percent);
		
	}
	
	public void delete(){
		this.setFunctionalStatus(FunctionalStatus.Deleted);
	}
}
