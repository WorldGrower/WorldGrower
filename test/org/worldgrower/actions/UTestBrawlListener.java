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
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.BrawlPropertyUtils;

public class UTestBrawlListener {

	@Test
	public void testExecute() {
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		BrawlPropertyUtils.startBrawl(performer, target, 100);
		
		target.setProperty(Constants.HIT_POINTS, 1);
		
		new BrawlListener().actionPerformed(Actions.NON_LETHAL_MELEE_ATTACK_ACTION, performer, target, null, null);
		
		assertEquals(null, performer.getProperty(Constants.BRAWL_OPPONENT_ID));
		assertEquals(null, target.getProperty(Constants.BRAWL_OPPONENT_ID));
		assertEquals(1100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(900, target.getProperty(Constants.GOLD).intValue());
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.GOLD, 1000);
		return performer;
	}
}