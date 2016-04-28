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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;

public class UTestChildrenPropertyUtils {

	@Test
	public void testGetParents() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.CHILDREN, new IdList());
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), ChildrenPropertyUtils.getParents(performer, world));
		
		WorldObject parent = TestUtils.createSkilledWorldObject(3);
		parent.setProperty(Constants.CHILDREN, new IdList().add(performer));
		world.addWorldObject(parent);
		assertEquals(Arrays.asList(parent), ChildrenPropertyUtils.getParents(performer, world));
	}
}
