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

public class UTestCraftIronGreataxeAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		Actions.CRAFT_IRON_GREATAXE_ACTION.execute(performer, performer, new int[0], world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.DAMAGE));
	}
}