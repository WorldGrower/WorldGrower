package org.worldgrower.actions;

import org.worldgrower.attribute.IntProperty;

public class GovernanceOption {
	private final IntProperty intProperty;
	private final int newValue;
	
	public GovernanceOption(IntProperty intProperty, int newValue) {
		this.intProperty = intProperty;
		this.newValue = newValue;
	}

	public IntProperty getIntProperty() {
		return intProperty;
	}

	public int getNewValue() {
		return newValue;
	}
}
