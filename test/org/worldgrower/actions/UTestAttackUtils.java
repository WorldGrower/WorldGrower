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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;

public class UTestAttackUtils {

	@Test
	public void testAttack() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(20, target.getProperty(Constants.HIT_POINTS).intValue());
		AttackUtils.attack(Actions.MELEE_ATTACK_ACTION, performer, target, new int[0], world, 1.0f);
		assertEquals(18, target.getProperty(Constants.HIT_POINTS).intValue());
	}

	private WorldObject createCommoner(World world) {
		CommonerGenerator commonerGenerator = new CommonerGenerator(0, new CommonerImageIds(), new MockCommonerNameGenerator());
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(0, 0, world, villagersOrganization);
		WorldObject performer = world.findWorldObject(Constants.ID, performerId);
		return performer;
	}
	
}
