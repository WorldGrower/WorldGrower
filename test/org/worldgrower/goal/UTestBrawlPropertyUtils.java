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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;

public class UTestBrawlPropertyUtils {

	@Test
	public void testStartBrawl() {
		WorldObject performer = TestUtils.createSkilledWorldObject(0);
		WorldObject target = TestUtils.createSkilledWorldObject(1);
		
		BrawlPropertyUtils.startBrawl(performer, target, 10);
		assertEquals(1, performer.getProperty(Constants.BRAWL_OPPONENT_ID).intValue());
		assertEquals(0, target.getProperty(Constants.BRAWL_OPPONENT_ID).intValue());
	}
}
