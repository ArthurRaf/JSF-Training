package com.synisys.training.de.converter;

import java.util.List;
import java.util.Objects;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.synisys.training.de.classifier.AbstractEntity;
import com.synisys.training.de.classifier.ClassifiersHolder;
import com.synisys.training.de.classifier.Entity;
import com.synisys.training.de.managedBean.ProjectForm;

@FacesConverter("com.synisys.training.de.converter.ClassifierConverter")
public class AbstractClassifierConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
                              String newValue) {
        Entity entity = null;
        String componentId = component.getClientId();

        if (newValue != null) {

            ClassifiersHolder classifiersHelper = (ClassifiersHolder) context
                    .getExternalContext().getApplicationMap()
                    .get("classifiersHolder");

            ProjectForm projectForm = (ProjectForm) context
                    .getExternalContext().getSessionMap()
                    .get("projectForm");

            List<? extends AbstractEntity> list = null;

            switch (componentId) {
                case "projectForm:sector":
                    list = classifiersHelper.getSectors();
                    break;
                case "projectForm:subSector":
                    list = projectForm.getSubSectors();
                    break;
                case "contactSubForm:organisation":
                    list = classifiersHelper.getOrganisations();
                    break;
                case "projectForm:currency":
                    list = classifiersHelper.getCurrencies();
                    break;
                default:
                    throw new RuntimeException(String.format("Converter called for unknown component %s", componentId));
            }

            Objects.requireNonNull(list, String.format(
                    "Can not load list for component %s", componentId));

            for (AbstractEntity currentEntity : list) {
                if (currentEntity.getId() == Integer.parseInt(newValue)) {
                    entity = currentEntity;
                    break;
                }
            }

        }
        return entity;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {
        if (value != null) {
            return String.valueOf(((Entity) value).getId());
        } else {
            return null;
        }
    }
}
