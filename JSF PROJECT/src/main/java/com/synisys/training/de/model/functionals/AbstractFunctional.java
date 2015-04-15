package com.synisys.training.de.model.functionals;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class AbstractFunctional<T> {

	private T parent;
	private int id;
	private FunctionalStatus functionalStatus;

	public AbstractFunctional(T parent, FunctionalStatus functionalStatus) {
		this.parent = parent;
		this.functionalStatus = functionalStatus;
	}

	public T getParent() {
		return parent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FunctionalStatus getFunctionalStatus() {
		return functionalStatus;
	}

	public void setFunctionalStatus(FunctionalStatus functionalStatus) {
		this.functionalStatus = functionalStatus;
	}

	@Override
	public String toString() {
		return getToStringHelper().toString();
	}

	protected ToStringHelper getToStringHelper() {
		return Objects.toStringHelper(getClass()).add("id", id).add("status", functionalStatus);//.add("parent", parent);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, functionalStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass().equals(obj.getClass())) {
			AbstractFunctional<T> that = (AbstractFunctional<T>) obj;
			return Objects.equal(this.id, that.id) && Objects.equal(this.functionalStatus, that.functionalStatus);
		}
		return false;
	}

	public abstract AbstractFunctional<T> cloneModel();

	public void copyTo(AbstractFunctional<T> clone) {
		clone.setId(id);
		clone.setFunctionalStatus(functionalStatus);
	}
}
