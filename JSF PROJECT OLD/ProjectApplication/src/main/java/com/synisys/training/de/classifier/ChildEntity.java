package com.synisys.training.de.classifier;

public interface ChildEntity<T extends Entity> extends Entity{

	public T getParent();
	
}
