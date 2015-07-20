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
package org.worldgrower.profession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.World;
import org.worldgrower.goal.GroupPropertyUtils;

public class Professions {
	public static final FarmerProfession FARMER_PROFESSION = new FarmerProfession();
	public static final BlacksmithProfession BLACKSMITH_PROFESSION = new BlacksmithProfession();
	public static final PriestProfession PRIEST_PROFESSION = new PriestProfession();
	public static final ThiefProfession THIEF_PROFESSION = new ThiefProfession();
	public static final LumberjackProfession LUMBERJACK_PROFESSION = new LumberjackProfession();
	public static final MinerProfession MINER_PROFESSION = new MinerProfession();
	public static final SheriffProfession SHERIFF_PROFESSION = new SheriffProfession();
	
	private static final List<Profession> ALL_PROFESSIONS = new ArrayList<>();
	
	static {
		add(FARMER_PROFESSION);
		add(BLACKSMITH_PROFESSION);
		add(PRIEST_PROFESSION);
		add(THIEF_PROFESSION);
		add(LUMBERJACK_PROFESSION);
		add(MINER_PROFESSION);
		add(SHERIFF_PROFESSION);
	}
	
	private static void add(Profession profession) {
		ALL_PROFESSIONS.add(profession);
	}
	
	public static List<String> getDescriptions() {
		return ALL_PROFESSIONS.stream().map(p -> p.getDescription()).collect(Collectors.toList());
	}

	public static Profession getProfessionByDescription(String professionName) {
		for(Profession profession : ALL_PROFESSIONS) {
			if (profession.getDescription().equals(professionName)) {
				return profession;
			}
		}
		throw new IllegalStateException("profession " + professionName + " not found in " + ALL_PROFESSIONS);
	}
}
