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
import org.worldgrower.deity.Deity;

public class UTestCapturePersonForSacrificeAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(12, 12, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.DEITY, Deity.HADES);
		
		Actions.BUILD_SACRIFICAL_ALTAR_ACTION.execute(performer, target, Args.EMPTY, world);
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		Actions.CAPTURE_PERSON_FOR_SACRIFICE_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(0, target.getProperty(Constants.X).intValue());
		assertEquals(0, target.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.CONDITIONS, new Conditions());
		
		assertEquals(true, Actions.CAPTURE_PERSON_FOR_SACRIFICE_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.DEITY, Deity.HADES);
		Actions.BUILD_SACRIFICAL_ALTAR_ACTION.execute(performer, target, Args.EMPTY, world);
		
		target.setProperty(Constants.CONDITIONS, new Conditions());
		Conditions.add(target, Condition.UNCONSCIOUS_CONDITION, 8, world);
		
		assertEquals(true, Actions.CAPTURE_PERSON_FOR_SACRIFICE_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
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