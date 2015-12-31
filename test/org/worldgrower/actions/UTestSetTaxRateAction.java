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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestSetTaxRateAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(3);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		Actions.SET_TAX_RATE_ACTION.execute(performer, performer, new int[] {5, 6}, world);
		
		assertEquals(5, villagersOrganization.getProperty(Constants.SHACK_TAX_RATE).intValue());
		assertEquals(6, villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(3);
		WorldObject target = createPerformer(4);
		
		assertEquals(false, Actions.SET_TAX_RATE_ACTION.isValidTarget(performer, target, world));
		assertEquals(true, Actions.SET_TAX_RATE_ACTION.isValidTarget(performer, performer, world));
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
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