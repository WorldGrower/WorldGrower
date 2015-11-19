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
import org.worldgrower.attribute.ItemCountMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestSellAction {

	@Test
	public void testExecuteSell() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WATER.generate(1f));
		
		assertEquals(100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(100, target.getProperty(Constants.GOLD).intValue());
		int indexOfWater = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER);
		Actions.SELL_ACTION.execute(performer, target, new int[] { indexOfWater, 10 }, world);
		
		assertEquals(110, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(90, target.getProperty(Constants.GOLD).intValue());
		assertEquals(-1, performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER));
		assertEquals(0, target.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.GOLD, 100);
		performer.setProperty(Constants.ITEMS_SOLD, new ItemCountMap());
		return performer;
	}
}