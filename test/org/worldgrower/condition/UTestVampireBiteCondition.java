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
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;

public class UTestVampireBiteCondition {

	private final VampireBiteCondition condition = Condition.VAMPIRE_BITE_CONDITION;
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		Conditions.add(performer, Condition.VAMPIRE_BITE_CONDITION, 8, world);
		
		condition.onTurn(performer, world, 100, new WorldStateChangedListeners());
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.VAMPIRE_BITE_CONDITION));
		
		for(int i=0; i<2000; i++) { world.nextTurn(); }
		condition.onTurn(performer, world, 100, new WorldStateChangedListeners());
		assertEquals(false, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.VAMPIRE_BITE_CONDITION));
		assertEquals(CreatureType.UNDEAD_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
}
