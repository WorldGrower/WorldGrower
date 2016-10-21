package org.worldgrower.actions;

import org.worldgrower.attribute.ManagedProperty;

public class GovernanceOption {
	private final ManagedProperty<?> managedProperty;
	private final int newValue;
	
	public GovernanceOption(ManagedProperty<?> managedProperty, int newValue) {
		this.managedProperty = managedProperty;
		this.newValue = newValue;
	}

	public ManagedProperty<?> getProperty() {
		return managedProperty;
	}

	public int getNewValue() {
		return newValue;
	}
}
