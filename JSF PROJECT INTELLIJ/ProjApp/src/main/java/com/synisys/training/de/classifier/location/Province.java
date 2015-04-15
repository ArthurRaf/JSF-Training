package com.synisys.training.de.classifier.location;

import com.synisys.training.de.classifier.AbstractChildEntity;

public class Province extends AbstractChildEntity<Region>{
	
	public Province(int id, String name, Region parent) {
		super(id, name, parent);
	}

}
