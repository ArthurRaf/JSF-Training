package com.synisys.training.de.dao;

import com.synisys.training.de.classifier.ChildEntity;
import com.synisys.training.de.classifier.Entity;

public abstract class AbstractChildClassifierFactory<Parent extends Entity, T extends ChildEntity<Parent>> implements ChildClassifierFactory<Parent, T> {

	private ChildClassifierCache<Parent, T> classifierCache = new ChildClassifierCache<>();

	public ChildClassifierCache<Parent, T> getChildClassifierCache() {
		return classifierCache;
	}

}
