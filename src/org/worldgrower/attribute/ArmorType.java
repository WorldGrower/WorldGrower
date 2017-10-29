package org.worldgrower.attribute;

import org.worldgrower.Constants;

public enum ArmorType {
	LIGHT(Constants.LIGHT_ARMOR_SKILL),
	HEAVY( Constants.HEAVY_ARMOR_SKILL);
	
	private final SkillProperty skillProperty;
	
	private ArmorType(SkillProperty skillProperty) {
		this.skillProperty = skillProperty;
	}

	public SkillProperty getSkillProperty() {
		return skillProperty;
	}
}
