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
package org.worldgrower.generator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.Goals;

public class UTestSkeletonEvaluationFunction {

	private final SkeletonWorldEvaluationFunction priorities = new SkeletonWorldEvaluationFunction();
	
	@Test
	public void testDefaultPriorities() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		
		WorldObject skeleton = createSkeleton(world);
		
		assertEquals(Arrays.asList(Goals.KILL_OUTSIDERS_GOAL, Goals.IDLE_GOAL), priorities.getPriorities(skeleton, world));
	}
	
	@Test
	public void testOrder() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		
		WorldObject skeleton = createSkeleton(world);
		skeleton.setProperty(Constants.GIVEN_ORDER, Goals.CREATE_WOOD_GOAL);
		
		assertEquals(Arrays.asList(Goals.CREATE_WOOD_GOAL, Goals.KILL_OUTSIDERS_GOAL, Goals.IDLE_GOAL), priorities.getPriorities(skeleton, world));
	}

	private WorldObject createSkeleton(World world) {
		WorldObject originalWorldObject = TestUtils.createSkilledWorldObject(2);
		originalWorldObject.setProperty(Constants.X, 0);
		originalWorldObject.setProperty(Constants.Y, 0);
		originalWorldObject.setProperty(Constants.GOLD, 0);
		originalWorldObject.setProperty(Constants.ORGANIZATION_GOLD, 0);
		originalWorldObject.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		originalWorldObject.setProperty(Constants.DEATH_REASON, "drowning");
		int skeletonId = CommonerGenerator.generateSkeletalRemains(originalWorldObject, world);
		WorldObject skeleton = world.findWorldObject(Constants.ID, skeletonId);
		return skeleton;
	}
}