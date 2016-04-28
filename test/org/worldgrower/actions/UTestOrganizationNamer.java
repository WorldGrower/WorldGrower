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

import java.util.List;

import org.junit.Test;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

public class UTestOrganizationNamer {

	private final OrganizationNamer organizationNamer = new OrganizationNamer();
	
	@Test
	public void testGetProfessionOrganizationNames() {
		World world = new WorldImpl(1, 1, null, null);
		
		List<String> organizationNames = organizationNamer.getProfessionOrganizationNames(Professions.FARMER_PROFESSION, world);
		assertEquals(true, organizationNames.size() > 0);
		assertEquals("Alliance of farmers", organizationNames.get(0));
	}
	
	@Test
	public void testgetDeityOrganizationNames() {
		World world = new WorldImpl(1, 1, null, null);
		
		List<String> organizationNames = organizationNamer.getDeityOrganizationNames(Deity.HADES, world);
		assertEquals(true, organizationNames.size() > 0);
		assertEquals("Friends of Hades", organizationNames.get(0));
	}
}
