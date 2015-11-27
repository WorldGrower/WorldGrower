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
package org.worldgrower.actions.magic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class UTestDetectPoisonAndDiseaseAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		target.setProperty(Constants.NAME, "target");
		
		Conditions.add(target, Condition.VAMPIRE_BITE_CONDITION, 8, world);
		WorldListener listener = new WorldListener();
		world.addListener(listener);
		
		Actions.DETECT_POISON_AND_DISEASE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals("target has the following conditions: bitten by a vampire", listener.getMessage());
	}
	
	@Test
	public void testExecutePoisoned() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		target.setProperty(Constants.NAME, "target");
		
		Conditions.add(target, Condition.POISONED_CONDITION, 8, world);
		WorldListener listener = new WorldListener();
		world.addListener(listener);
		
		Actions.DETECT_POISON_AND_DISEASE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals("target has the following conditions: poisoned", listener.getMessage());
	}
	
	@Test
	public void testExecuteMultipleConditions() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		target.setProperty(Constants.NAME, "target");
		
		Conditions.add(target, Condition.POISONED_CONDITION, 8, world);
		Conditions.add(target, Condition.VAMPIRE_BITE_CONDITION, 8, world);
		WorldListener listener = new WorldListener();
		world.addListener(listener);
		
		Actions.DETECT_POISON_AND_DISEASE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals("target has the following conditions: poisoned, bitten by a vampire", listener.getMessage());
	}
	
	private static class WorldListener implements ManagedOperationListener {
		private String message = null;

		@Override
		public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
			this.message = (String) value;
		}

		public String getMessage() {
			return message;
		}
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}