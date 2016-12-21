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

public class UTestDropItemAction {

	private DropItemAction action = Actions.DROP_ITEM_ACTION;
	
	@Test
	public void testExecuteRemoveAll() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CLAYMORE.generate(1f));
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.DAMAGE));
		
		action.execute(performer, performer, new int[] { 0, 1 }, world);
		
		assertEquals(0, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.DAMAGE));
	}
	
	@Test
	public void testExecuteRemoveSome() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CLAYMORE.generate(1f), 10);
		assertEquals(10, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.DAMAGE));
		
		action.execute(performer, performer, new int[] { 0, 1 }, world);
		
		assertEquals(9, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.DAMAGE));
	}
}