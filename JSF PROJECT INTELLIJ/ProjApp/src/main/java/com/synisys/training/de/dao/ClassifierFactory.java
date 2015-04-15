package com.synisys.training.de.dao;

import com.synisys.training.de.classifier.Entity;

public interface ClassifierFactory<T extends Entity> {

	public T newInstance(int id, String name);
	
	public ClassifierCache<T> getClassifierCache();
	
}
