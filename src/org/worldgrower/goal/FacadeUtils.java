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
package org.worldgrower.goal;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectFacade;
import org.worldgrower.attribute.SkillUtils;

public class FacadeUtils {

	public static WorldObject createFacade(WorldObject worldObject, WorldObject performer, WorldObject target, World world) {
		if (worldObject != null) {
			WorldObject facade = worldObject.getProperty(Constants.FACADE);
						
			if ((facade != null) && (facadeIsBelieved(performer, target))) {
				SkillUtils.useSkill(performer, Constants.BLUFF_SKILL, world.getWorldStateChangedListeners());
				return new WorldObjectFacade(worldObject, facade);
			} else {
				return worldObject;
			}
		} else {
			return null;
		}
	}
	
	public static WorldObject createFacadeForSelf(WorldObject performer) {
		final WorldObject performerFacade;
		if (performer.getProperty(Constants.FACADE) != null) {
			performerFacade = new WorldObjectFacade(performer, performer.getProperty(Constants.FACADE));
		} else {
			performerFacade = performer;
		}
		return performerFacade;
	}

	private static boolean facadeIsBelieved(WorldObject performer, WorldObject target) {
		int bluffSkill = Constants.BLUFF_SKILL.getLevel(performer);
		int insightSkill = Constants.INSIGHT_SKILL.getLevel(target);
		
		if (target.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(performer, Constants.FACADE)) {
			insightSkill += 5;
		}
		
		return bluffSkill > insightSkill;
	}
	
	public static boolean performerIsSuccessFullyDisguised(WorldObject performer) {
		return performer instanceof WorldObjectFacade;
	}
 	
	public static void disguise(WorldObject performer, int selectedPersonId, World world) {
		final WorldObject facade;
		if (selectedPersonId < 0) {
			facade = null;
		} else {
			WorldObject selectedPerson = world.findWorldObject(Constants.ID, selectedPersonId);
			facade = selectedPerson.deepCopy();
		}
		
		performer.setProperty(Constants.FACADE, facade);
		if (facade != null) {
			everyoneInVicinityKnowsOfFacade(performer, world);
		}
	}
	
	private static void everyoneInVicinityKnowsOfFacade(WorldObject performer, World world) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(performer, performer, Constants.FACADE, performer.getProperty(Constants.FACADE), world);
	}
}
