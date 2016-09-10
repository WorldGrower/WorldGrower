package org.worldgrower.actions.magic;

import org.worldgrower.ManagedOperation;
import org.worldgrower.attribute.SkillProperty;

public interface MagicSpell extends ManagedOperation {

	public int getResearchCost();
	public SkillProperty getSkill();
	public int getRequiredSkillLevel();
}
