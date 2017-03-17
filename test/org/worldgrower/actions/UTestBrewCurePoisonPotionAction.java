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
import org.worldgrower.generator.Item;

public class UTestBrewCurePoisonPotionAction {

	private BrewCurePoisonPotionAction action = Actions.BREW_CURE_POISON_POTION_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createApothecary(world, performer);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.OIL.generate(1f), 10);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(7, performerInventory.getQuantityFor(Constants.OIL));
		assertEquals(1, performerInventory.getQuantityFor(Constants.CURE_POISON));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);

		WorldObject target = createApothecary(world, performer);
		
		assertEquals(true, action.isValidTarget(performer, target, world));
		assertEquals(false, action.isValidTarget(performer, performer, world));
	}

	WorldObject createApothecary(World world, WorldObject performer) {
		int apothecaryId = BuildingGenerator.generateApothecary(0, 0, world, performer);
		WorldObject apothecary = world.findWorldObjectById(apothecaryId);
		return apothecary;
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.OIL.generate(1f), 10);
		
		assertEquals(true, action.isActionPossible(performer, performer, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, action.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}