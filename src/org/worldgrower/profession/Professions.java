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
import java.util.List;
import java.util.stream.Collectors;

public class Professions {
	
	private static final List<Profession> ALL_PROFESSIONS = new ArrayList<>();
	
	public static final FarmerProfession FARMER_PROFESSION = new FarmerProfession(ALL_PROFESSIONS);
	public static final BlacksmithProfession BLACKSMITH_PROFESSION = new BlacksmithProfession(ALL_PROFESSIONS);
	public static final PriestProfession PRIEST_PROFESSION = new PriestProfession(ALL_PROFESSIONS);
	public static final ThiefProfession THIEF_PROFESSION = new ThiefProfession(ALL_PROFESSIONS);
	public static final LumberjackProfession LUMBERJACK_PROFESSION = new LumberjackProfession(ALL_PROFESSIONS);
	public static final MinerProfession MINER_PROFESSION = new MinerProfession(ALL_PROFESSIONS);
	public static final SheriffProfession SHERIFF_PROFESSION = new SheriffProfession(ALL_PROFESSIONS);
	public static final GraveDiggerProfession GRAVE_DIGGER_PROFESSION = new GraveDiggerProfession(ALL_PROFESSIONS);
	public static final TaxCollectorProfession TAX_COLLECTOR_PROFESSION = new TaxCollectorProfession(ALL_PROFESSIONS);
	public static final WeaverProfession WEAVER_PROFESSION = new WeaverProfession(ALL_PROFESSIONS);
	public static final CarpenterProfession CARPENTER_PROFESSION = new CarpenterProfession(ALL_PROFESSIONS);
	public static final WizardProfession WIZARD_PROFESSION = new WizardProfession(ALL_PROFESSIONS);
	public static final NecromancerProfession NECROMANCER_PROFESSION = new NecromancerProfession(ALL_PROFESSIONS);
	public static final FisherProfession FISHER_PROFESSION = new FisherProfession(ALL_PROFESSIONS);
	public static final ArenaOwnerProfession ARENA_OWNER_PROFESSION = new ArenaOwnerProfession(ALL_PROFESSIONS);
	public static final ArenaFighterProfession ARENA_FIGHTER_PROFESSION = new ArenaFighterProfession(ALL_PROFESSIONS);
	public static final JournalistProfession JOURNALIST_PROFESSION = new JournalistProfession(ALL_PROFESSIONS);
	public static final AssassinProfession ASSASSIN_PROFESSION = new AssassinProfession(ALL_PROFESSIONS);
	
	public static List<String> getDescriptions() {
		return ALL_PROFESSIONS.stream().map(p -> p.getDescription()).collect(Collectors.toList());
	}
	
	public static List<Profession> getAllProfessions() {
		return ALL_PROFESSIONS;
	}

	public static Profession getProfessionByDescription(String professionName) {
		for(Profession profession : ALL_PROFESSIONS) {
			if (profession.getDescription().equals(professionName)) {
				return profession;
			}
		}
		throw new IllegalStateException("profession " + professionName + " not found in " + ALL_PROFESSIONS);
	}

	public static int indexOf(Profession profession) {
		return ALL_PROFESSIONS.indexOf(profession);
	}
}
