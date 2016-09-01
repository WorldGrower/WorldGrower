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
package org.worldgrower.deity;

import java.io.ObjectStreamException;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Professions;

public class Athena implements Deity {

	@Override
	public String getName() {
		return "Athena";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the Goddess of wisdom, reason, intelligent activity, literature, handicrafts and science, defense and strategic warfare.";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				getName() + " is the Goddess of wisdom and reason, I see worshipping her as a sign of progress"
		);
	}
	
	@Override
	public List<Goal> getOrganizationGoals() {
		return addDefaultOrganizationGoals(Goals.RESEARCH_MAGIC_SKILLS_KNOWLEDGE_GOAL);
	}

	@Override
	public int getReasonIndex(WorldObject performer, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			return 0;
		}
		
		return -1;
	}
	
	@Override
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners) {
	}
	

	@Override
	public ImageIds getStatueImageId() {
		return ImageIds.STATUE_OF_ATHENA;
	}
	
	@Override
	public SkillProperty getSkill() {
		return Constants.CARPENTRY_SKILL;
	}
	
	@Override
	public ImageIds getBoonImageId() {
		return ImageIds.ATHENA_SYMBOL;
	}
	
	@Override
	public Condition getBoon() {
		return Condition.ATHENA_BOON_CONDITION;
	}
}
