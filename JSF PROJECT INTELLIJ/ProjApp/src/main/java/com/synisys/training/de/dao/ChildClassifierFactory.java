package com.synisys.training.de.dao;

import com.synisys.training.de.classifier.ChildEntity;
import com.synisys.training.de.classifier.Entity;

public interface ChildClassifierFactory<Parent extends Entity, T extends ChildEntity<Parent>> {

	public T newInstance(int id, String name, Parent parent);
	
	public ChildClassifierCache<Parent, T> getChildClassifierCache();
}
