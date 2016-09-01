/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.actions;

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.ConditionUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class AbstractResearchKnowledgeSkillAction implements ResearchKnowledgeSkillAction {

	private final SkillProperty skillProperty;
	
	public AbstractResearchKnowledgeSkillAction(SkillProperty skillProperty) {
		this.skillProperty = skillProperty;
	}

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		SkillUtils.useSkill(performer, skillProperty, world.getWorldStateChangedListeners());
		if (ConditionUtils.performerHasCondition(performer, Condition.ATHENA_BOON_CONDITION)) {
			SkillUtils.useSkill(performer, skillProperty, world.getWorldStateChangedListeners());	
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.LIBRARY_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "studying " + skillProperty.getName();
	}

	@Override
	public String getSimpleDescription() {
		return "study " + skillProperty.getName();
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public SkillProperty getSkillProperty() {
		return skillProperty;
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.SPELL_BOOK;
	}

	@Override
	public final SoundIds getSoundId() {
		return SoundIds.PAPER;
	}
}