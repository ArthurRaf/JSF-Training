package com.synisys.training.de.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.synisys.training.de.classifier.Entity;

public class ClassifierCache<T extends Entity> {

	private Map<Integer, T> cachedItems;

	public void put(Collection<T> entities) {
		cachedItems = new LinkedHashMap<>();
		for (T entity : entities) {
			cachedItems.put(entity.getId(), entity);
		}
	}		

	public boolean isCached() {
		return cachedItems != null;
	}
	
	public void cleanCache() {						
		this.cachedItems = null;		
	}

	public T getInstance(int id) {
		return cachedItems == null ? null : cachedItems.get(id);
	}

	public List<T> getInstances() {
		return cachedItems == null ? null : new ArrayList<>(cachedItems.values());
	}

}
