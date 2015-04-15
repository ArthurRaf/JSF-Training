package com.synisys.training.de.dao;

import com.synisys.training.de.classifier.Entity;

public abstract class AbstractClassifierFactory<T extends Entity> implements ClassifierFactory<T> {

	private ClassifierCache<T> classifierCache = new ClassifierCache<>();

	public ClassifierCache<T> getClassifierCache() {
		return classifierCache;
	}

}
