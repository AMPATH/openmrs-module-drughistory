package org.openmrs.module.drughistory;

import org.openmrs.module.drughistory.api.db.hibernate.type.StringEnum;

/**
 * Enumeration for drug event types
 */
public enum DrugEventType implements StringEnum {

	START("START"),
	STOP("STOP"),
	CONTINUE("CONTINUE"),
	CONFIRM("CONFIRM"),
	DENY("DENY"),
	FILLED("FILLED");

	private final String value;

	private DrugEventType(final String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
