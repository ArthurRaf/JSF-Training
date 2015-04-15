package com.synisys.training.de.classifier;

import java.io.Serializable;

public class Organisation extends AbstractEntity implements Serializable{	
	private static final long serialVersionUID = 1L;

	public Organisation(int id, String name) {
		super(id, name);
	}
}
