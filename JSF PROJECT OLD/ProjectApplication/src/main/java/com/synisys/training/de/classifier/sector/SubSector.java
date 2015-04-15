package com.synisys.training.de.classifier.sector;

import com.synisys.training.de.classifier.AbstractChildEntity;


public class SubSector extends AbstractChildEntity<Sector>{

	// Default constructor for SubSector is needed for JSF while restoring view for the SubSector combo UI component.
	public SubSector() {
		super();
	}
	
	public SubSector(int id, String name, Sector parent) {
		super(id, name, parent);
	}

}
