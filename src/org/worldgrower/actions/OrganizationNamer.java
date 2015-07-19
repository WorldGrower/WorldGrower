package org.worldgrower.actions;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.profession.Profession;

public class OrganizationNamer {

	private static final String[] PREFIXES = { "Alliance", "Dominion", "Legion"};
	private static final String[] SUFFIXES = { "Organization", "Harvesters", "Syndicate"};
	
	public List<String> getNames(Profession profession) {
		List<String> result = new ArrayList<>();
		
		for(String prefix : PREFIXES) {
			result.add(prefix + " of " + profession.getDescription() + "s");
		}
		
		for(String suffix : SUFFIXES) {
			result.add(profession.getDescription() + " " + suffix);
		}
		
		return result;
	}
}
