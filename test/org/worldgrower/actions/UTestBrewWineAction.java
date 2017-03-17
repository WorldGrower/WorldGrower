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
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestBrewWineAction {

	private BrewWineAction action = Actions.BREW_WINE_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createBrewery(world, performer);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.GRAPES.generate(1f), 10);

		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(7, performerInventory.getQuantityFor(Constants.GRAPE));
		assertEquals(1, performerInventory.getQuantityFor(Constants.WINE));
	}
	
	@Test
	public void testExecuteDionysusBoon() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createBrewery(world, performer);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.GRAPES.generate(1f), 10);

		Conditions.add(performer, Condition.DIONYSUS_BOON_CONDITION, 8, world);
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(7, performerInventory.getQuantityFor(Constants.GRAPE));
		assertEquals(2, performerInventory.getQuantityFor(Constants.WINE));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);

		WorldObject target = createBrewery(world, performer);
		
		assertEquals(false, action.isValidTarget(performer, performer, world));
		assertEquals(true, action.isValidTarget(performer, target, world));
	}

	WorldObject createBrewery(World world, WorldObject performer) {
		int breweryId = BuildingGenerator.generateBrewery(0, 0, world, performer);
		WorldObject target = world.findWorldObjectById(breweryId);
		return target;
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.GRAPES.generate(1f), 10);

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