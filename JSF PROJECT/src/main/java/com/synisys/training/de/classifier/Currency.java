package com.synisys.training.de.classifier;

public class Currency extends AbstractEntity{

	// Default constructor for Currency is needed for JSF while restoring view for the Currency combo UI component. 
	public Currency() {	 
	}
	
	public Currency(int id, String name) {
		super(id, name);
	}
}
