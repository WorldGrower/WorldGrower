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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.worldgrower.World;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Profession;

public class OrganizationNamer {

	private static final String[] PROFESSION_PREFIXES = { "Alliance", "Dominion", "Legion", "Society", "Union", "Federated", "Order", "Consolidation", "Council", "Academy", "Agency" };
	private static final String[] PROFESSION_SUFFIXES = { "Organization", "Harvesters", "Syndicate", "Guild", "Association", "Companions", "Pact", "Congregation", "Coalition", "Confederation", "Convention", "Authority", "Forum" };
	
	private static final String[] DEITY_PREFIXES = { "Friends", "Church", "Sect", "Cult", "Faith", "Religion"};
	private static final String[] DEITY_SUFFIXES = { };
	
	
	public List<String> getProfessionOrganizationNames(Profession profession, World world) {
		return getOrganizationNames(profession.getDescription(), world, PROFESSION_PREFIXES, PROFESSION_SUFFIXES);
	}
	
	public List<String> getDeityOrganizationNames(Deity deity, World world) {
		return getOrganizationNames(deity.getName(), world, DEITY_PREFIXES, DEITY_SUFFIXES);
	}
	
	private List<String> getOrganizationNames(String description, World world, String[] prefixes, String[] suffixes) {
		List<String> result = new ArrayList<>();
		
		for(String prefix : prefixes) {
			result.add(prefix + " of " + description);
		}
		
		for(String suffix : suffixes) {
			result.add(description + " " + suffix);
		}
		
		
		Iterator<String> resultIterator = result.iterator();
		while (resultIterator.hasNext()) {
			String organizationName = resultIterator.next();
			if (GroupPropertyUtils.isOrganizationNameInUse(organizationName, world)) {
				resultIterator.remove();
			}
		}
		
		return result;
	}
}
