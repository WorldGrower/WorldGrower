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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.Item;

public class UTestReversePickpocketAction {

	private final ReversePickPocketAction action = Actions.REVERSE_PICK_POCKET_ACTION;
	
	@Test
	public void testExecuteSuccess() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		performer.getProperty(Constants.THIEVERY_SKILL).use(1000, performer, Constants.THIEVERY_SKILL, new WorldStateChangedListeners());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WATER.generate(1f));
		
		for(int i=0; i<80; i++) { world.nextTurn(); }
		
		int index = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER);
		action.execute(performer, target, new int[] { index }, world);
		
		assertEquals(-1, performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER));
		assertEquals(0, target.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER));
	}
	
	@Test
	public void testExecuteFailure() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		
		int index = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.EQUIPMENT_SLOT);
		action.execute(performer, target, new int[] { index }, world);
		
		assertEquals(0, performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.EQUIPMENT_SLOT));
		assertEquals(-1, target.getProperty(Constants.INVENTORY).getIndexFor(Constants.EQUIPMENT_SLOT));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);

		assertEquals(true, action.isValidTarget(performer, target, world));
		
		assertEquals(false, action.isValidTarget(performer, TestUtils.createWorldObject(4, "target"), world));
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
		performer.setProperty(Constants.NAME, "Test");
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		return performer;
	}
}