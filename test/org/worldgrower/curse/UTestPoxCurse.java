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
package org.worldgrower.curse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestPoxCurse {

	private PoxCurse curse = Curse.POX_CURSE;
	
	@Test
	public void testPerform() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		performer.setProperty(Constants.CURSE, curse);

		curse.perform(performer, target, null, Actions.TALK_ACTION, world);
		assertEquals(curse, performer.getProperty(Constants.CURSE));
		assertEquals(null, target.getProperty(Constants.CURSE));
		
		curse.perform(performer, target, null, Actions.SEX_ACTION, world);
		assertEquals(curse, performer.getProperty(Constants.CURSE));
		assertEquals(curse, target.getProperty(Constants.CURSE));
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
