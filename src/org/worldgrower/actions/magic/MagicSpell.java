package org.worldgrower.actions.magic;

import org.worldgrower.ManagedOperation;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.gui.ImageIds;

public interface MagicSpell extends ManagedOperation {

	public int getResearchCost();
	public SkillProperty getSkill();
	public int getRequiredSkillLevel();
	public String getDescription();
	
	//default is temporary
	public default ImageIds getImageIds() {
		return ImageIds.SPELL_BOOK;
	}
}
