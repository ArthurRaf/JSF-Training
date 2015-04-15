package com.synisys.training.de.classifier;

import java.io.Serializable;

public class Organisation extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private int id;
	private String description;
	private String name;

	public Organisation(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public Organisation(int id, String name) {
		super(id, name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
