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
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.Location;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.gui.ImageIds;

public class InvestigateAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		double skillBonus = SkillUtils.useSkill(performer, Constants.PERCEPTION_SKILL, world.getWorldStateChangedListeners());
		int range = (int)(5 * skillBonus + 1);
		List<WorldObject> surroundingWorldObjects = world.findWorldObjects(w -> Reach.distance(performer, w) <= range);
		
		findInvisibleWorldObjects(performer, target, args, world, surroundingWorldObjects);
		findIllusionaryWorldObjects(performer, target, args, world, surroundingWorldObjects);
	}

	private void findIllusionaryWorldObjects(WorldObject performer, WorldObject target, int[] args, World world, List<WorldObject> surroundingWorldObjects) {
		for(WorldObject surroundingWorldObject : surroundingWorldObjects) {
			if (surroundingWorldObject.hasProperty(Constants.ILLUSION_CREATOR_ID)) {
				if (!performer.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(surroundingWorldObject, Constants.ILLUSION_CREATOR_ID)) {
					performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(surroundingWorldObject, Constants.ILLUSION_CREATOR_ID, surroundingWorldObject.getProperty(Constants.ILLUSION_CREATOR_ID));
				}
			}
		}
	}

	void findInvisibleWorldObjects(WorldObject performer, WorldObject target, int[] args, World world, List<WorldObject> surroundingWorldObjects) {
		List<WorldObject> newlyDiscoveredWorldObjects = getNewlyDiscoveredWorldObjects(performer, surroundingWorldObjects);
		for(WorldObject newlyDiscoveredWorldObject : newlyDiscoveredWorldObjects) {
			int subjectId = newlyDiscoveredWorldObject.getProperty(Constants.ID);
			int x = newlyDiscoveredWorldObject.getProperty(Constants.X);
			int y = newlyDiscoveredWorldObject.getProperty(Constants.Y);
			
			performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subjectId, Constants.LOCATION, new Location(x, y));
			world.logAction(this, performer, target, args, newlyDiscoveredWorldObject.getProperty(Constants.NAME) + " has been discovered");
		}
	}

	private List<WorldObject> getNewlyDiscoveredWorldObjects(WorldObject performer, List<WorldObject> surroundingWorldObjects) {
		KnowledgeMap performerKnowledgeMap = performer.getProperty(Constants.KNOWLEDGE_MAP);
		List<WorldObject> newlyDiscoveredWorldObjects = new ArrayList<>();
		for(WorldObject surroundingWorldObject : surroundingWorldObjects) {
			if (surroundingWorldObject.hasProperty(Constants.PASSABLE)) {
				if (!performerKnowledgeMap.hasKnowledge(surroundingWorldObject)) {
					newlyDiscoveredWorldObjects.add(surroundingWorldObject);
				}
			}
		}
		
		return newlyDiscoveredWorldObjects;
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "investigating surroundings";
	}
	
	@Override
	public String getSimpleDescription() {
		return "investigate";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.INVESTIGATE;
	}
	
	static boolean illusionIsBelievedBy(WorldObject personViewingWorld, WorldObject worldObject, World world) {
		final int insight;
		if (personViewingWorld.getProperty(Constants.INSIGHT_SKILL) == null) {
			insight = 0;
		} else {
			insight = Constants.INSIGHT_SKILL.getLevel(personViewingWorld);
		}
				
		int illusionCreatorId = worldObject.getProperty(Constants.ILLUSION_CREATOR_ID);
		WorldObject illusionCreator = world.findWorldObject(Constants.ID, illusionCreatorId);
		int illusion = Constants.ILLUSION_SKILL.getLevel(illusionCreator);
		
		return (illusion > insight);
	}
}