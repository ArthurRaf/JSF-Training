package com.synisys.training.de.classifier;

import com.google.common.base.Objects;

public class AbstractEntity implements Entity {
	private int id;
	private String name;

	// Default constructor for AbstractEntity is needed for JSF while restoring view for combo UI components.
	public AbstractEntity() {
	}

	public AbstractEntity(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("id", id).add("name", name).toString();
	}

	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public boolean equals(Object obj) {
		return obj==this || obj != null && getClass().equals(obj.getClass()) && id==((Entity)obj).getId();
	}
}
