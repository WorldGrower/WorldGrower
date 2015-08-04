package org.worldgrower.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.worldgrower.World;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Profession;

public class OrganizationNamer {

	private static final String[] PREFIXES = { "Alliance", "Dominion", "Legion", "Society", "Union", "Federated", "Order", "Consolidation" };
	private static final String[] SUFFIXES = { "Organization", "Harvesters", "Syndicate", "Guild", "Association", "Companions", "Pact", "Congregation", "Coalition", "Confederation" };
	
	public List<String> getNames(Profession profession, World world) {
		List<String> result = new ArrayList<>();
		
		for(String prefix : PREFIXES) {
			result.add(prefix + " of " + profession.getDescription() + "s");
		}
		
		for(String suffix : SUFFIXES) {
			result.add(profession.getDescription() + " " + suffix);
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
