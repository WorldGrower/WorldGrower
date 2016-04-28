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
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.history.Turn;
import org.worldgrower.profession.Professions;

public class UTestOperationStatistics {

	@Test
	public void testGetRecentOperationsCount() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		
		assertEquals(1, OperationStatistics.getRecentOperationsCount(Actions.MELEE_ATTACK_ACTION, world));
	}
	
	@Test
	public void testGetPrice() {
		World world = new WorldImpl(1, 1, null, null);
		
		assertEquals(1, OperationStatistics.getPrice(Item.BERRIES, world));
		
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.PRICES).setPrice(Item.BERRIES, 10);
		world.addWorldObject(performer);
		
		assertEquals(10, OperationStatistics.getPrice(Item.BERRIES, world));
		
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.getProperty(Constants.PRICES).setPrice(Item.BERRIES, 20);
		world.addWorldObject(target);
		
		assertEquals(15, OperationStatistics.getPrice(Item.BERRIES, world));
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