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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestCreatePaperAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.WOOD.generate(1f), 10);
		performerInventory.addQuantity(Item.WATER.generate(1f), 10);

		Actions.CREATE_PAPER_ACTION.execute(performer, performer, new int[0], world);
		
		assertEquals(9, performerInventory.getQuantityFor(Constants.WOOD));
		assertEquals(9, performerInventory.getQuantityFor(Constants.WATER));
		assertEquals(1, performerInventory.getQuantityFor(Constants.PAPER));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);

		assertEquals(true, Actions.CREATE_PAPER_ACTION.isValidTarget(performer, performer, world));
	}

	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Item.WOOD.generate(1f), 10);
		performerInventory.addQuantity(Item.WATER.generate(1f), 10);
		
		assertEquals(0, Actions.CREATE_PAPER_ACTION.distance(performer, performer, new int[0], world));
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