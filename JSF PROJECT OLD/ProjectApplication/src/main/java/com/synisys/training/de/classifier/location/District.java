package com.synisys.training.de.classifier.location;

import com.synisys.training.de.classifier.AbstractChildEntity;

public class District extends AbstractChildEntity<Province>{

	public District(int id, String name, Province parent) {
		super(id, name, parent);
	}

}
