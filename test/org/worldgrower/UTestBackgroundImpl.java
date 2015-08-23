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
package org.worldgrower;

import static org.junit.Assert.assertEquals;
import static org.worldgrower.TestUtils.createWorldObject;

import java.util.List;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Background;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.RevengeGoal;
import org.worldgrower.history.Turn;

public class UTestBackgroundImpl {
	
	@Test
	public void testPerformerWasAttacked() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(10, 10, null, null);
		
		
		WorldObject performer = createWorldObject(0, "Tom");
		WorldObject attacker = createWorldObject(1, "attacker");
		world.getHistory().actionPerformed(new OperationInfo(attacker, performer, new int[0], Actions.MELEE_ATTACK_ACTION), new Turn());
		
		background.checkForNewGoals(performer, world);
		
		List<Goal> personalGoals = background.getPersonalGoals(performer, world);
		assertEquals(1, personalGoals.size());
		assertEquals(RevengeGoal.class, personalGoals.get(0).getClass());
	}
}
