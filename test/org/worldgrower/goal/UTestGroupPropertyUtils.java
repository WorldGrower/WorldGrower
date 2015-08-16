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
import static org.worldgrower.TestUtils.createIntelligentWorldObject;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.profession.Professions;

public class UTestGroupPropertyUtils {

	@Test
	public void testIsWorldObjectPotentialEnemy() {
		WorldObject performer = createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(3));
		WorldObject w1 = createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject w2 = createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(4));
		WorldObject w3 = createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(3).add(4));
		
		assertEquals(true, GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w1));
		assertEquals(true, GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w2));
		assertEquals(false, GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w3));
	}
	
	@Test
	public void testIsOrganizationNameInUse() {
		World world = new WorldImpl(0, 0, null, null);
		GroupPropertyUtils.create(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(true, GroupPropertyUtils.isOrganizationNameInUse("TestOrg", world));
		assertEquals(false, GroupPropertyUtils.isOrganizationNameInUse("TestOrg2", world));
	}
	
	@Test
	public void testFindOrganizationsUsingLeader() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject leader = createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		world.addWorldObject(leader);
		
		IdList leaderGroup = leader.getProperty(Constants.GROUP);
		leaderGroup.add(GroupPropertyUtils.create(1, "TestOrg", Professions.FARMER_PROFESSION, world));
		leaderGroup.add(GroupPropertyUtils.create(2, "TestOrg2", Professions.FARMER_PROFESSION, world));
		
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(leader, world);
		assertEquals(1, organizations.size());
		assertEquals("TestOrg", organizations.get(0).getProperty(Constants.NAME));
	}
}
