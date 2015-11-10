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
package org.worldgrower.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Skill;
import org.worldgrower.condition.WorldStateChangedListeners;

public class UTestIllusionOnTurn {

	@Test
	public void testOnTurn() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 2);
		performer.setProperty(Constants.ILLUSION_SKILL, new Skill(20));
		performer.setProperty(Constants.ENERGY, 1000);
		world.addWorldObject(performer);
		Actions.MINOR_ILLUSION_ACTION.execute(performer, performer, new int[]{2}, world);
		
		WorldObject illusion = world.findWorldObject(Constants.ID, 0);
		assertEquals(11, illusion.getProperty(Constants.TURNS_TO_LIVE).intValue());
		
		illusion.onTurn(world, new WorldStateChangedListeners());
		assertEquals(10, illusion.getProperty(Constants.TURNS_TO_LIVE).intValue());
	}
	
	
}