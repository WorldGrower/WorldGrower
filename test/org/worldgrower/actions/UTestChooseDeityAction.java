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
import org.worldgrower.attribute.IdList;
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

public class UTestChooseDeityAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.GROUP, new IdList());
		Actions.CHOOSE_DEITY_ACTION.execute(performer, performer, new int[] { 0, -1 }, world);
		
		assertEquals(Deity.DEMETER, performer.getProperty(Constants.DEITY));
	}
	
	@Test
	public void testExecuteThief() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.GROUP, new IdList());
		performer.setProperty(Constants.PROFESSION, Professions.THIEF_PROFESSION);
		performer.setProperty(Constants.FACADE, TestUtils.createSkilledWorldObject(3, Constants.GROUP, new IdList()));
		Actions.CHOOSE_DEITY_ACTION.execute(performer, performer, new int[] { 1, -1 }, world);
		
		assertEquals(Deity.HEPHAESTUS, performer.getProperty(Constants.DEITY));
		assertEquals(Deity.DEMETER, performer.getProperty(Constants.FACADE).getProperty(Constants.DEITY));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createSkilledWorldObject(3, Constants.GROUP, new IdList());
		
		assertEquals(true, Actions.CHOOSE_DEITY_ACTION.isValidTarget(performer, performer, world));
		assertEquals(false, Actions.CHOOSE_DEITY_ACTION.isValidTarget(performer, target, world));
	}
}