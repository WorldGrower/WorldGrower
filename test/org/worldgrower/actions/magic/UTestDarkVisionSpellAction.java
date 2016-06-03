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

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class UTestDarkVisionSpellAction {

	private DarkVisionSpellAction action = Actions.DARK_VISION_SPELL_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		action.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.DARK_VISION_CONDITION));
	}

	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(false, action.isValidTarget(performer, performer, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.DARK_VISION_SPELL_ACTION));
		assertEquals(true, action.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(0, action.distance(performer, performer, Args.EMPTY, world));
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