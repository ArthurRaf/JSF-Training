package com.synisys.training.de.classifier;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.synisys.training.Initializer;
import com.synisys.training.de.classifier.sector.Sector;
import com.synisys.training.de.dao.ClassifierDao;
import com.synisys.training.de.dao.DaoException;

/**
 * Holds all application scoped classifiers lists
 * @author tatevik.khachatryan
 *
 */
@ManagedBean(name = "classifiersHolder")
@ApplicationScoped
public class ClassifiersHolder {
	private ClassifierDao classifierDao;

	public ClassifiersHolder() {
		this.classifierDao = new ClassifierDao(Initializer.getDataSource());
	}

	public List<Sector> getSectors(){
		try {
			List<Sector> sectors = new ArrayList<>();
			sectors.addAll(classifierDao.loadSectors());
			return sectors;
		} catch (DaoException e) {
			throw new RuntimeException("Can not load sectors", e);
		}		
	}
}
