package com.synisys.training.de.managedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.synisys.training.Initializer;
import com.synisys.training.de.classifier.ClassifiersHolder;
import com.synisys.training.de.classifier.Currency;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.dao.ClassifierDao;
import com.synisys.training.de.dao.DaoException;
import com.synisys.training.de.dao.ProjectDao;
import com.synisys.training.de.model.Project;

@ManagedBean
@SessionScoped
public class ProjectsForm {
	private List<Project> projects;

	private ProjectDao dao;

	public ProjectsForm() {
		ClassifierDao classifierDao = new ClassifierDao(
				Initializer.getDataSource());
		dao = new ProjectDao(Initializer.getDataSource(), classifierDao);
	}

	public ProjectDao getProjectDao() {
		Objects.requireNonNull(this.dao,
				"Project Dao has not been initialized normally!");
		return this.dao;
	}

	public List<Project> getProjects() {
		// load projects list
		try {
			this.projects = dao.loadAllProjects();
		} catch (DaoException e) {
			throw new RuntimeException("Can not load Projects List.", e);
		}

		Objects.requireNonNull(projects,
				"Projects List is not loaded properly.");
		return projects;
	}



	public void deleteProject(Project project) {
		Objects.requireNonNull(projects,
				"Projects List is not loaded properly.");

		Objects.requireNonNull(project,
				"Project is not specified for deletion.");

		for (Project proj : projects) {
			if (proj.equals(project)) {
				// remove project object from projects list
				projects.remove(proj);

				// remove project from DB
				try {
					dao.deleteProject(project);
				} catch (DaoException e) {
					throw new RuntimeException(String.format(
							"Can not delet project by %s id", project.getId()),
							e);
				}
				break;
			}
		}
	}
}
