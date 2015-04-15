package com.synisys.training.de.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.synisys.training.de.classifier.Currency;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.classifier.sector.SubSector;
import com.synisys.training.de.model.functionals.FunctionalStatus;
import com.synisys.training.de.model.functionals.ProjectContact;
import com.synisys.training.de.model.functionals.ProjectDonor;
import com.synisys.training.de.model.functionals.ProjectLocation;

@ManagedBean
public class Project implements Serializable {
	private static final long serialVersionUID = -4049305035883111784L;

	private Integer id;
	private String title;
	private String description;
	private DateTime startDate;
	private DateTime endDate;
	private Money totalAmount = new Money(null, new Currency());
	private List<ProjectContact> contacts = new ArrayList<>();
	private SubSector subSector;
	private List<ProjectLocation> locations = new ArrayList<>();
	private List<ProjectDonor> donors = new ArrayList<>();

	public Project() {
	}

	public Project(Integer id) {
		Objects.requireNonNull(id);
		this.id = id;
	}

	public Period getDuration() {
		return new Period(startDate, endDate);
	}

	public String getDurationStr() {
		PeriodFormatter formatter = new PeriodFormatterBuilder().appendYears()
				.appendSuffix(" year ", " years ").appendMonths()
				.appendSuffix(" month ", " months ").appendWeeks()
				.appendSuffix(" week ", " weeks ").appendDays()
				.appendSuffix(" day ", " days ").toFormatter();
		return new Period(startDate, endDate).toString(formatter);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		Objects.requireNonNull(id);
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public Date getUtilStartDate() {
		if (this.startDate != null) {
			Date date = new Date(this.startDate.getMillis());
			return date;
		}
		return null;
	}

	public void setUtilStartDate(Date startDate) {
		if (startDate != null) {
			this.startDate = new DateTime(startDate.getTime());
		} else {
			this.startDate = null;
		}
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public Date getUtilEndDate() {
		if (this.endDate != null) {
			Date date = new Date(this.endDate.getMillis());
			return date;
		}
		return null;
	}

	public void setUtilEndDate(Date endDate) {
		if (endDate != null) {
			this.endDate = new DateTime(endDate.getTime());
		} else {
			this.endDate = null;
		}
	}

	public Money getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Money totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<ProjectContact> getContacts() {
		return contacts;
	}

	public List<ProjectContact> getNotDeletedContacts() {
		List<ProjectContact> contacts = new ArrayList<>();
			for (ProjectContact contact : this.contacts) {
				if (contact.getFunctionalStatus() != FunctionalStatus.Deleted) {
					contacts.add(contact);
				}
			}
		return contacts;
	}

	public void setContacts(List<ProjectContact> contacts) {
		this.contacts = contacts;
	}

	public Sector getSector() {
		return subSector == null ? null : subSector.getParent();
	}

	public SubSector getSubSector() {
		return subSector;
	}

	public void setSubSector(SubSector subSector) {
		this.subSector = subSector;
	}

	public List<ProjectLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<ProjectLocation> locations) {
		this.locations = locations;
	}

	public List<ProjectDonor> getDonors() {
		return donors;
	}

	public void setDonors(List<ProjectDonor> donors) {
		this.donors = donors;
	}

	@Override
	public String toString() {
		return com.google.common.base.Objects.toStringHelper(getClass())
				.add("title", title).add("id", id).toString();
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id;
		} else {
			return com.google.common.base.Objects.hashCode(title, description,
					startDate, endDate, totalAmount);
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass().equals(obj.getClass())) {
			Project that = (Project) obj;
			return Objects.equals(this.id, that.id)
					&& Objects.equals(this.title, that.title)
					&& Objects.equals(this.description, that.description)
					&& Objects.equals(this.startDate, that.startDate)
					&& Objects.equals(this.endDate, that.endDate)
					&& Objects.equals(this.totalAmount, that.totalAmount)
					&& Objects.equals(this.contacts, that.contacts)
					&& Objects.equals(this.locations, that.locations)
					&& Objects.equals(this.donors, that.donors);
		}
		return false;
	}

	public Project cloneProject() {
		Project clone = new Project();
		copyTo(clone);
		return clone;
	}

	public void copyTo(Project clone) {
		clone.id = id;
		clone.setTitle(title);
		clone.setDescription(description);
		clone.setStartDate(startDate);
		clone.setEndDate(endDate);
		clone.setTotalAmount(new Money(totalAmount));
		List<ProjectContact> clonedContacts = new ArrayList<>();
		for (ProjectContact projectContact : this.contacts) {
			clonedContacts.add(projectContact.cloneModel());
		}
		clone.setContacts(clonedContacts);

		List<ProjectLocation> clonedLocations = new ArrayList<>();
		for (ProjectLocation projectLocation : this.locations) {
			clonedLocations.add(projectLocation.cloneModel());
		}
		clone.setLocations(clonedLocations);

		List<ProjectDonor> clonedDonors = new ArrayList<>();
		for (ProjectDonor projectDonor : this.donors) {
			clonedDonors.add(projectDonor.cloneModel());
		}
		clone.setDonors(clonedDonors);
	}

}
