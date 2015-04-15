package com.synisys.training.de.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.synisys.training.de.classifier.ChildEntity;
import com.synisys.training.de.classifier.Entity;

public class ChildClassifierCache<Parent extends Entity, T extends ChildEntity<Parent>> {

	private Map<Integer, Map<Integer, T>> cachedItemsByParent = new HashMap<>();
	private Map<Integer, T> allItems = new HashMap<>();

	public void put(Parent parent, Collection<T> entities) {
		Map<Integer, T> children = new LinkedHashMap<>();
		cachedItemsByParent.put(parent.getId(), children);

		for (T entity : entities) {
			children.put(entity.getId(), entity);
		}
		allItems.putAll(children);
	}

	public boolean isCached(Parent parent) {
		Objects.requireNonNull(parent);
		return cachedItemsByParent.get(parent.getId()) != null;
	}

	public T getInstance(int id) {
		return allItems.get(id);
	}

	public List<T> getInstances(Parent parent) {
		Map<Integer, T> children = cachedItemsByParent.get(parent.getId());
		return children == null ? null : new ArrayList<>(children.values());
	}

	/*private Map<Integer, T> cachedItems;

	public void put(Collection<T> entities) {
		cachedItems = new LinkedHashMap<>();
		for (T entity : entities) {
			cachedItems.put(entity.getId(), entity);
		}
	}

	public boolean isCached() {
		return cachedItems != null;
	}

	public T getInstance(int id) {
		return cachedItems == null ? null : cachedItems.get(id);
	}

	public List<T> getInstances() {
		return cachedItems == null ? null : new ArrayList<>(cachedItems.values());
	}
	
	
	
	private Map<Class<Entity>, EntityItems<Entity>> cache = new HashMap<>();
	private Map<Entity, EntityChildren<Entity>> childrenCache = new HashMap<>();

	private class EntityItems<T extends Entity>{
		private Map<Integer, T> items = new LinkedHashMap<>();
		public void put(Collection<T> entities){
			for (T entity : entities) {
				items.put(entity.getId(), entity);
			}
		}
		
		public List<T> get(){
			return new ArrayList<>(items.values());
		}
	}
	
	private class EntityChildren<T extends Entity>{
		private Map<Integer, ChildEntity<T>> children = new LinkedHashMap<>();
		
		public void put(Collection<ChildEntity<T>> entities){
			for (ChildEntity<T> entity : entities) {
				children.put(entity.getId(), entity);
			}
		}
		
		public List<ChildEntity<T>> get(){
			return new ArrayList<>(children.values());
		}
	}
	
	
	
	public void put(Class<Entity> entityClass, Collection<Entity> entities) {
		EntityItems<Entity> items = new EntityItems<>();
		cache.put(entityClass, items);
		items.put(entities);
	}

	public List<Entity> get(Class<Entity> entityClass) {
		EntityItems<Entity> items = cache.get(entityClass);
		if (items == null) {
			return null;
		}
		else {
			return items.get();
		}
	}

	public <T extends Entity> void addChildren(T parent, Collection<ChildEntity<T>> children) {
		//		if (childrenMap == null) {
		EntityChildren<? extends Entity> entityChildren = new EntityChildren<>();
		
	//		childrenCache.put(parent, entityChildren);
	//		entityChildren.put(children);
		//		}

	}

	public List<ChildEntity<Entity>> getChildren(Entity parent) {
		EntityChildren<Entity> items = childrenCache.get(parent);
		if (items == null) {
			return null;
		}
		else {
			return items.get();
		}
	}

	private void putToMap(Map<Integer, Entity> map, Collection<? extends Entity> entities) {
		for (Entity entity : entities) {
			map.put(entity.getId(), entity);
		}
	}

	private void putToChildrenMap(Map<Integer, ChildEntity<? extends Entity>> map, Collection<ChildEntity<? extends Entity>> entities) {
		for (ChildEntity<? extends Entity> entity : entities) {
			map.put(entity.getId(), entity);
		}
	}*/
}
