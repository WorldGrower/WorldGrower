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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.GhostImageIds;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.gui.ImageIds;

public class UTestRemainsOnTurn {

	private final RemainsOnTurn onTurn = new RemainsOnTurn();
	
	@Test
	public void testOnTurnSpawnGhost() {
		WorldImpl world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		GhostImageIds ghostImageIds = new GhostImageIds();
		ghostImageIds.addGhostImageId(ImageIds.KNIGHT, ImageIds.KNIGHT_GHOST);
		world.addUserData(ghostImageIds);
		WorldObject remains = createSkeleton(world);
		
		assertEquals(ImageIds.KNIGHT, remains.getProperty(Constants.DEATH_INFORMATION).getOriginalImageId());
		assertEquals("worldObject", remains.getProperty(Constants.DEATH_INFORMATION).getOriginalName());
		
		onTurn.onTurn(remains, world, new WorldStateChangedListeners());
		assertEquals(1, world.getWorldObjects().size());

		for(int i=0; i<200; i++) { world.nextTurn(); }
		
		onTurn.onTurn(remains, world, new WorldStateChangedListeners());
		assertEquals(2, world.getWorldObjects().size());
		
		WorldObject ghost = world.getWorldObjects().get(1);
		assertEquals(CreatureType.GHOST_CREATURE_TYPE, ghost.getProperty(Constants.CREATURE_TYPE));
		assertEquals(0, ghost.getProperty(Constants.X).intValue());
		assertEquals(0, ghost.getProperty(Constants.Y).intValue());
	}
	
	private WorldObject createSkeleton(World world) {
		WorldObject originalWorldObject = TestUtils.createSkilledWorldObject(2);
		originalWorldObject.setProperty(Constants.X, 0);
		originalWorldObject.setProperty(Constants.Y, 0);
		originalWorldObject.setProperty(Constants.GOLD, 0);
		originalWorldObject.setProperty(Constants.ORGANIZATION_GOLD, 0);
		originalWorldObject.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		originalWorldObject.setProperty(Constants.DEATH_REASON, "drowning");
		originalWorldObject.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		int skeletonId = CommonerGenerator.generateSkeletalRemains(originalWorldObject, world);
		WorldObject skeleton = world.findWorldObjectById(skeletonId);
		return skeleton;
	}
	

}