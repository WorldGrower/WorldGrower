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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestDismissSecretChestAction {

	private DismissSecretChestAction action = Actions.DISMISS_SECRET_CHEST_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		int chestId = BuildingGenerator.generateChest(0, 0, world, 1f);
		WorldObject chest = world.findWorldObject(Constants.ID, chestId);
		
		assertEquals(0, chest.getProperty(Constants.X).intValue());
		assertEquals(0, chest.getProperty(Constants.Y).intValue());

		action.execute(performer, chest, new int[] {0}, world);
		
		assertEquals(-10, chest.getProperty(Constants.X).intValue());
		assertEquals(-10, chest.getProperty(Constants.Y).intValue());		
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		int chestId = BuildingGenerator.generateChest(0, 0, world, 1f);
		WorldObject chest = world.findWorldObject(Constants.ID, chestId);
		Actions.SECRET_CHEST_ACTION.execute(performer, chest, Args.EMPTY, world);
		
		assertEquals(true, action.isValidTarget(performer, chest, world));
		
		assertEquals(false, action.isValidTarget(performer, performer, world));
	}

	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		int chestId = BuildingGenerator.generateChest(0, 0, world, 1f);
		WorldObject chest = world.findWorldObject(Constants.ID, chestId);
		
		assertEquals(0, action.distance(performer, chest, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}