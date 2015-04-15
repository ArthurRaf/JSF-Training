package com.synisys.training.de.managedBean;
import com.synisys.training.Initializer;
import com.synisys.training.de.classifier.Organisation;
import com.synisys.training.de.dao.ClassifierDao;
import com.synisys.training.de.dao.DaoException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Objects;

/**
 * Created by arthur.rafayelyan on 4/13/2015.
 */
@ManagedBean(name = "organisationSubForm")
@SessionScoped
public class OrganisationSubForm {

    private ClassifierDao dao;

    private Organisation currentOrganisation;

    public Organisation getCurrentOrganisation() {
        return currentOrganisation;
    }

    public void setCurrentOrganisation(Organisation currentOrganisation) {
        this.currentOrganisation = currentOrganisation;
    }

    public void addNewOrganisation() {
        this.currentOrganisation = new Organisation();
    }

     public OrganisationSubForm(){
        ClassifierDao classifierDao = new ClassifierDao(Initializer.getDataSource());
        dao = new ClassifierDao(Initializer.getDataSource(), classifierDao);
    }

    public String saveOrganisation(){
        Objects.requireNonNull(currentOrganisation,
                "Project not saved properly.");

        Objects.requireNonNull(currentOrganisation,
                "Project is not specified for deletion.");



        // save project in DB
        try {
            dao.saveOrganisation(currentOrganisation);
        } catch (DaoException e) {
            throw new RuntimeException(String.format(
                    "Can not save project by %s id", currentOrganisation.getId()),
                    e);
        }
        return "ProjectAddEditForm";
    }

    public String cancelAction(){
        //reset current project contact
        cleanFormProperties();

        return "ProjectAddEditForm";
    }

    private void cleanFormProperties(){
        this.currentOrganisation = null;
    }

}
