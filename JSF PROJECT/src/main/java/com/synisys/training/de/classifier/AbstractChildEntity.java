package com.synisys.training.de.classifier;

import com.google.common.base.Objects;

public class AbstractChildEntity<T extends Entity> extends AbstractEntity implements ChildEntity<T> {

	private T parent;

	// Default constructor for AbstractChildEntity is needed for JSF while restoring view for combo UI components.
	public AbstractChildEntity() {	
	}
	
	public AbstractChildEntity(int id, String name, T parent) {
		super(id, name);
		java.util.Objects.requireNonNull(parent);
		this.parent = parent;
	}

	public T getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("id", getId()).add("name", getName())
				.add("parentId", parent.getId()).toString();
	}

	@SuppressWarnings("unchecked")//inheritance checked in super.equals class
	@Override
	public boolean equals(Object obj) {
		return obj == this || super.equals(obj)
				&& this.getParent().equals(((AbstractChildEntity<Entity>) obj).getParent());
	}

}
