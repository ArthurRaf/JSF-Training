package com.synisys.training.de.classifier.sector;

import com.synisys.training.de.classifier.AbstractEntity;

public class Sector extends AbstractEntity{
	// Default constructor for Sector is needed for JSF while restoring view for the Sector combo UI component.
	public Sector() {	
		super();
	}
	
	public Sector(int id, String name) {
		super(id, name);
	}
}
