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
package org.worldgrower.conversation;

import static org.worldgrower.TestUtils.createWorldObject;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.LocationConversation.Direction;

public class UTestLocationConversation {

	@Test
	public void testGetDirectionSouth() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(2, 10, 1, 1);
		
		assertEquals(Direction.SOUTH, LocationConversation.getDirection(performer, subject));
	}
	
	@Test
	public void testGetDirectionNorth() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(2, 0, 1, 1);
		
		assertEquals(Direction.NORTH, LocationConversation.getDirection(performer, subject));
	}
	
	@Test
	public void testGetDirectionWest() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(0, 2, 1, 1);
		
		assertEquals(Direction.WEST, LocationConversation.getDirection(performer, subject));
	}
	
	@Test
	public void testGetDirectionEast() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(10, 2, 1, 1);
		
		assertEquals(Direction.EAST, LocationConversation.getDirection(performer, subject));
	}
}
