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
package org.worldgrower.condition;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.generator.Item;

public class UTestSoulTrappedCondition {

	private final SoulTrappedCondition condition = Condition.SOUL_TRAPPED_CONDITION;
	
	@Test
	public void testFillSoulGem() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		performer.setProperty(Constants.DAMAGE, 100);
		target.setProperty(Constants.HIT_POINTS, 0);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SOUL_GEM.generate(1f));
		Conditions.add(target, Condition.SOUL_TRAPPED_CONDITION, 8, world);
		
		condition.perform(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.FILLED_SOUL_GEM));
	}
}
