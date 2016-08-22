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

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.Background;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.ProfessionExplanation;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.ChildrenPropertyUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;
import org.worldgrower.util.SentenceUtils;

public class ChooseProfessionAction implements ManagedOperation {

	private static final List<ProfessionInfo> PROFESSION_INFOS = Arrays.asList(
			new ProfessionInfo(Professions.PRIEST_PROFESSION, 1.4, 1.2, 0.8, 0.8, 1.6, 1.0),
			new ProfessionInfo(Professions.BLACKSMITH_PROFESSION, 1.2, 1.1, 0.8, 0.8, 1.3, 1.0),
			new ProfessionInfo(Professions.THIEF_PROFESSION, 0.8, 1.2, 1.4, 0.8, 0.8, 1.4),
			new ProfessionInfo(Professions.FARMER_PROFESSION, 1.0, 1.2, 0.8, 1.0, 1.2, 1.0),
			new ProfessionInfo(Professions.LUMBERJACK_PROFESSION, 1.2, 1.2, 1.2, 0.8, 1.0, 1.0),
			new ProfessionInfo(Professions.MINER_PROFESSION, 1.4, 1.3, 1.1, 0.8, 1.0, 1.0),
			new ProfessionInfo(Professions.SHERIFF_PROFESSION, 1.5, 1.3, 0.8, 1.1, 0.8, 0.8),
			new ProfessionInfo(Professions.GRAVE_DIGGER_PROFESSION, 1.5, 1.1, 0.8, 1.0, 1.1, 1.1),
			new ProfessionInfo(Professions.TAX_COLLECTOR_PROFESSION, 1.0, 1.0, 1.0, 1.0, 1.2, 1.4),
			new ProfessionInfo(Professions.WEAVER_PROFESSION, 0.8, 1.0, 0.8, 1.0, 1.4, 1.1),
			new ProfessionInfo(Professions.CARPENTER_PROFESSION, 0.8, 1.0, 0.8, 1.0, 1.4, 1.1),
			new ProfessionInfo(Professions.WIZARD_PROFESSION, 0.8, 0.8, 1.0, 1.4, 1.1, 0.9),
			new ProfessionInfo(Professions.NECROMANCER_PROFESSION, 0.8, 0.8, 1.0, 1.4, 1.1, 0.9),
			new ProfessionInfo(Professions.FISHER_PROFESSION, 0.8, 1.0, 0.8, 1.0, 1.4, 1.1),
			new ProfessionInfo(Professions.ARENA_OWNER_PROFESSION, 0.8, 0.8, 0.8, 1.1, 1.1, 1.4),
			new ProfessionInfo(Professions.ARENA_FIGHTER_PROFESSION, 1.4, 1.4, 0.8, 0.8, 0.8, 0.8),
			new ProfessionInfo(Professions.JOURNALIST_PROFESSION, 0.8, 0.8, 0.8, 1.2, 1.2, 1.4),
			new ProfessionInfo(Professions.ASSASSIN_PROFESSION, 1.4, 1.2, 0.8, 0.8, 1.0, 1.4),
			new ProfessionInfo(Professions.ALCHEMIST_PROFESSION, 0.8, 1.0, 0.8, 1.0, 1.4, 1.1),
			new ProfessionInfo(Professions.BREWER_PROFESSION, 0.8, 1.0, 0.8, 1.0, 1.4, 1.1),
			new ProfessionInfo(Professions.TRICKSTER_PROFESSION, 0.8, 1.0, 0.8, 1.3, 0.8, 1.4),
			new ProfessionInfo(Professions.MERCHANT_PROFESSION, 1.1, 1.1, 0.8, 0.8, 1.3, 1.1)
			);
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		// 1) does performer have appropriate stats?
		// 2) how many competition is there already for this profession?
		// 3) is there a need for someone of this profession?
		// 4) what does the background of the performer indicate?
		
		ProfessionResult professionResult = getProfessionResult(performer, world);
		ProfessionEvaluation bestProfession = professionResult.getBestProfession();
		String reason = professionResult.getReason();
	
		Profession profession = bestProfession.getProfession();
		
		if (profession == Professions.THIEF_PROFESSION) {
			createThiefFacade(performer);
		}
		
		if (profession == Professions.NECROMANCER_PROFESSION) {
			createNecromancerFacade(performer);
		}
		
		if (profession == Professions.TRICKSTER_PROFESSION) {
			createTricksterFacade(performer);
		}
		
		if (profession == Professions.ASSASSIN_PROFESSION) {
			createAssassinFacade(performer);
		}
		
		if (profession == Professions.TAX_COLLECTOR_PROFESSION) {
			performer.setProperty(Constants.CAN_COLLECT_TAXES, Boolean.TRUE);
		}
		
		if (profession == Professions.SHERIFF_PROFESSION) {
			performer.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		}
		
		performer.setProperty(Constants.PROFESSION, profession);
		performer.getProperty(Constants.REASONS).addReason(Constants.PROFESSION, reason);
	}

	public static void createTricksterFacade(WorldObject performer) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.PROFESSION, Professions.WIZARD_PROFESSION);
		WorldObject facade = new WorldObjectImpl(properties);
		performer.setProperty(Constants.FACADE, facade);
	}
	
	public static void createNecromancerFacade(WorldObject performer) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.PROFESSION, Professions.WIZARD_PROFESSION);
		WorldObject facade = new WorldObjectImpl(properties);
		performer.setProperty(Constants.FACADE, facade);
	}

	public static void createThiefFacade(WorldObject performer) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject facade = new WorldObjectImpl(properties);
		performer.setProperty(Constants.FACADE, facade);
	}
	
	public static void createAssassinFacade(WorldObject performer) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.PROFESSION, Professions.CARPENTER_PROFESSION);
		WorldObject facade = new WorldObjectImpl(properties);
		performer.setProperty(Constants.FACADE, facade);
	}

	private ProfessionResult getProfessionResult(WorldObject performer, World world) {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = getProfessionEvaluationsByPerformer(performer);
		List<ProfessionEvaluation> professionEvaluationsByCompetition = getProfessionEvaluationsByCompetition(performer, world);
		List<ProfessionEvaluation> professionEvaluationsByDemand = getProfessionEvaluationsByDemand(performer, world);
		List<ProfessionEvaluation> professionEvaluationsByBackground = getProfessionEvaluationsByBackground(performer, world);
		List<ProfessionEvaluation> professionEvaluationsByParents = getProfessionEvaluationsByParents(performer, world);
		
		List<ProfessionEvaluation> mergedProfessionEvaluations = merge(professionEvaluationsByPerformer, professionEvaluationsByCompetition);
		mergedProfessionEvaluations = merge(mergedProfessionEvaluations, professionEvaluationsByDemand);
		mergedProfessionEvaluations = merge(mergedProfessionEvaluations, professionEvaluationsByBackground);
		mergedProfessionEvaluations = merge(mergedProfessionEvaluations, professionEvaluationsByParents);
		Collections.sort(mergedProfessionEvaluations);
		
		ProfessionEvaluation bestProfession = mergedProfessionEvaluations.get(0);	
		String reason = getReason(bestProfession.getProfession(), professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents);
		
		return new ProfessionResult(bestProfession, reason);
	}
	
	private static class ProfessionResult {
		private final ProfessionEvaluation bestProfession;
		private final String reason;
		
		public ProfessionResult(ProfessionEvaluation bestProfession, String reason) {
			this.bestProfession = bestProfession;
			this.reason = reason;
		}

		public ProfessionEvaluation getBestProfession() {
			return bestProfession;
		}

		public String getReason() {
			return reason;
		}
	}
	
	static String getReason(
			Profession bestProfession,
			List<ProfessionEvaluation> professionEvaluationsByPerformer,
			List<ProfessionEvaluation> professionEvaluationsByCompetition,
			List<ProfessionEvaluation> professionEvaluationsByDemand,
			List<ProfessionEvaluation> professionEvaluationsByBackground,
			List<ProfessionEvaluation> professionEvaluationsByParents) {
		
		int indexOfBestProfessionByPerformer = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByPerformer);
		int indexOfBestProfessionByCompetition = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByCompetition);
		int indexOfBestProfessionByDemand = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByDemand);
		int indexOfBestProfessionByBackground = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByBackground);
		int indexOfBestProfessionEvaluationsByParents = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByParents);
		
		indexOfBestProfessionByPerformer = normalizeIndex(indexOfBestProfessionByPerformer);
		indexOfBestProfessionByCompetition = normalizeIndex(indexOfBestProfessionByCompetition);
		indexOfBestProfessionByDemand = normalizeIndex(indexOfBestProfessionByDemand);
		indexOfBestProfessionByBackground = normalizeIndex(indexOfBestProfessionByBackground);
		
		String professionDescription = bestProfession.getDescription();
		String article = SentenceUtils.getArticle(professionDescription);
		
		//TODO: compare with professionEvaluationsByDemand
		if (indexOfBestProfessionByBackground == 0) {
			return "I choose to become " + article + " " + professionDescription + " because " + professionEvaluationsByBackground.get(0).getExplanation();
		} else if (indexOfBestProfessionByPerformer == 0 && indexOfBestProfessionByCompetition == 0) {
			return "I choose to become " + article + " " + professionDescription + " because there isn't much competition for that profession and I'm good at it";
		} else if (indexOfBestProfessionByPerformer < indexOfBestProfessionByCompetition || (indexOfBestProfessionByPerformer == 0)) {
			return "I choose to become " + article + " " + professionDescription + " because I'm good at it";
		} else if (indexOfBestProfessionByCompetition < indexOfBestProfessionByPerformer) {
			return "I choose to become " + article + " " + professionDescription + " because there isn't much competition for it";
		} else if (indexOfBestProfessionByDemand < indexOfBestProfessionByPerformer) {
			return "I choose to become " + article + " " + professionDescription + " because there is a demand for it";
		} else if (indexOfBestProfessionEvaluationsByParents == 0) {
			return "I choose to become " + article + " " + professionDescription + " because my parents are " + bestProfession.getDescription() + "s";
		}
		
		return "It just seemed like a good idea to become " + article + " " + professionDescription;
	}
	
	private static int normalizeIndex(int index) {
		return (index != -1 ? index : Integer.MAX_VALUE);
	}

	static List<ProfessionEvaluation> merge(List<ProfessionEvaluation> list1, List<ProfessionEvaluation> list2) {
		List<ProfessionEvaluation> result = new ArrayList<>();
		result.addAll(list1);
		
		for(ProfessionEvaluation professionEvaluation : list2) {
			int index = findIndexOfName(professionEvaluation.getProfession().getDescription(), result);
			if (index != -1) {
				result.set(index, result.get(index).add(professionEvaluation));
			} else {
				result.add(professionEvaluation);
			}
		}
		
		return result;
	}
	
	static int findIndexOfName(String name, List<ProfessionEvaluation> list) {
		int index = 0;
		for(ProfessionEvaluation professionEvaluation : list) {
			if (professionEvaluation.getProfession().getDescription().equals(name)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	static List<ProfessionEvaluation> getProfessionEvaluationsByPerformer(WorldObject performer) {
		List<ProfessionEvaluation> result = new ArrayList<>();
		for(ProfessionInfo professionInfo : PROFESSION_INFOS) {
			result.add(new ProfessionEvaluation(professionInfo.getProfession(), professionInfo.evaluate(performer)));
		}
		
		Collections.sort(result);
		return result;
	}
	
	static List<ProfessionEvaluation> getProfessionEvaluationsByCompetition(WorldObject performer, World world) {
		List<WorldObject> worldObjects = GroupPropertyUtils.findWorldObjectsInSameGroup(performer, world);
		Map<Profession, Integer> professionCounts = getProfessionCounts(worldObjects);
		
		List<ProfessionEvaluation> result = new ArrayList<>();
		for(ProfessionInfo professionInfo : PROFESSION_INFOS) {
			Integer professionCountInteger = professionCounts.get(professionInfo.getProfession());
			int professionCount = (professionCountInteger != null ? professionCountInteger.intValue() : 0);
			if (professionInfo.getProfession() == Professions.ARENA_OWNER_PROFESSION) {
				int evaluation = professionCount > 0 ? Integer.MIN_VALUE : (worldObjects.size() - professionCount) * 5;
				result.add(new ProfessionEvaluation(professionInfo.getProfession(), evaluation));
			} else {
				result.add(new ProfessionEvaluation(professionInfo.getProfession(), (worldObjects.size() - professionCount) * 5));
			}
		}
		Collections.sort(result);
		return result;
	}
	
	static List<ProfessionEvaluation> getProfessionEvaluationsByDemand(WorldObject performer, World world) {
		List<WorldObject> worldObjects = GroupPropertyUtils.findWorldObjectsInSameGroup(performer, world);
		
		PropertyCountMap<ManagedProperty<?>> mergedDemands = new PropertyCountMap<ManagedProperty<?>>();
		for(WorldObject worldObject : worldObjects) {
			if (worldObject.hasProperty(Constants.DEMANDS)) {
				mergedDemands.addAll(worldObject.getProperty(Constants.DEMANDS));
			}
		}
		
		return mapDemandsToProfessions(performer, mergedDemands, world);
	}

	// When demands are used, they should be divided by populationCount
	// Otherwise demands may result in 1 profession overshadowing the others
	static List<ProfessionEvaluation> mapDemandsToProfessions(WorldObject performer, PropertyCountMap<ManagedProperty<?>> demands, World world) {
		List<ProfessionEvaluation> result = new ArrayList<>();
		int populationCount = OperationStatistics.getPopulationCount(world);
		
		int foodDemand = demands.count(Constants.FOOD);
		foodDemand += OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.EAT_ACTION, Professions.FARMER_PROFESSION, world) / (populationCount / 2);
		result.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, foodDemand));
		
		//int waterDemand = demands.getQuantityFor(Constants.WATER);
		
		int woodDemand = demands.count(Constants.WOOD);
		woodDemand += OperationStatistics.getRecentOperationsByNonProfessionalsCount(Actions.CUT_WOOD_ACTION, Professions.LUMBERJACK_PROFESSION, world) / (4 * populationCount);
		result.add(new ProfessionEvaluation(Professions.LUMBERJACK_PROFESSION, woodDemand));
		
		int stoneDemand = demands.count(Constants.STONE);
		int oreDemand = demands.count(Constants.ORE);
		int goldDemand = demands.count(Constants.GOLD);
		result.add(new ProfessionEvaluation(Professions.MINER_PROFESSION, (stoneDemand + oreDemand + goldDemand) / populationCount));
		
		int houseDemand = demands.count(Constants.SLEEP_COMFORT);
		int pickaxeDemand = demands.count(Constants.PICKAXE_QUALITY);
		int scytheDemand = demands.count(Constants.SCYTHE_QUALITY);
		result.add(new ProfessionEvaluation(Professions.CARPENTER_PROFESSION, (houseDemand + pickaxeDemand + scytheDemand) / populationCount));
		
		List<WorldObject> remains = getRemains(world);
		if (remains.size() > 0) {
			result.add(new ProfessionEvaluation(Professions.GRAVE_DIGGER_PROFESSION, remains.size()));
		} else {
			result.add(new ProfessionEvaluation(Professions.GRAVE_DIGGER_PROFESSION, Integer.MIN_VALUE));
		}
		
		if (populationCount > 10) {
			result.add(new ProfessionEvaluation(Professions.NECROMANCER_PROFESSION, -1));
		} else {
			result.add(new ProfessionEvaluation(Professions.NECROMANCER_PROFESSION, Integer.MIN_VALUE));
		}
		
		if (populationCount > 15) {
			result.add(new ProfessionEvaluation(Professions.ASSASSIN_PROFESSION, -1));
		} else {
			result.add(new ProfessionEvaluation(Professions.ASSASSIN_PROFESSION, Integer.MIN_VALUE));
		}
		
		List<WorldObject> nearbyFish = world.findWorldObjectsByProperty(Constants.FOOD_SOURCE, w -> w.getProperty(Constants.CREATURE_TYPE) == CreatureType.FISH_CREATURE_TYPE && Reach.distance(performer, w) < 50);
		if (nearbyFish.size() > 10) {
			result.add(new ProfessionEvaluation(Professions.FISHER_PROFESSION, 1));
		} else {
			result.add(new ProfessionEvaluation(Professions.FISHER_PROFESSION, Integer.MIN_VALUE));
		}
		
		boolean canCollectTaxes = GroupPropertyUtils.canCollectTaxes(world);
		if (canCollectTaxes) {
			result.add(new ProfessionEvaluation(Professions.TAX_COLLECTOR_PROFESSION, -1));
			result.add(new ProfessionEvaluation(Professions.SHERIFF_PROFESSION, 0));
		} else {
			result.add(new ProfessionEvaluation(Professions.TAX_COLLECTOR_PROFESSION, Integer.MIN_VALUE));
			result.add(new ProfessionEvaluation(Professions.SHERIFF_PROFESSION, Integer.MIN_VALUE));
		}
		
		if (populationCount < 15) {
			result.add(new ProfessionEvaluation(Professions.THIEF_PROFESSION, Integer.MIN_VALUE));
		} else {
			result.add(new ProfessionEvaluation(Professions.THIEF_PROFESSION, 0));
		}
		
		if (populationCount < 15) {
			result.add(new ProfessionEvaluation(Professions.TRICKSTER_PROFESSION, Integer.MIN_VALUE));
		} else {
			result.add(new ProfessionEvaluation(Professions.TRICKSTER_PROFESSION, 0));
		}
		
		if (populationCount < 20) {
			result.add(new ProfessionEvaluation(Professions.JOURNALIST_PROFESSION, Integer.MIN_VALUE));
		} else {
			result.add(new ProfessionEvaluation(Professions.JOURNALIST_PROFESSION, -1));
		}
		
		if (populationCount < 10) {
			result.add(new ProfessionEvaluation(Professions.MERCHANT_PROFESSION, Integer.MIN_VALUE));
		} else {
			result.add(new ProfessionEvaluation(Professions.MERCHANT_PROFESSION, getNumberOfMatchingDemands(demands, world) / populationCount));
		}
		
		return result;
	}
	
	public static int getNumberOfMatchingDemands(PropertyCountMap<ManagedProperty<?>> demands, World world) {
		PropertyCountMap<ManagedProperty<?>> supply = calculateSupply(demands, world);
		int matchingDemands = 0;
		for(ManagedProperty<?> managedProperty : demands.keySet()) {
			int totalDemand = demands.count(managedProperty);
			int totalSupply = supply.count(managedProperty);
			
			matchingDemands += Math.min(totalDemand, totalSupply);
		}
		
		return matchingDemands;
	}

	static PropertyCountMap<ManagedProperty<?>> calculateSupply(PropertyCountMap<ManagedProperty<?>> demands, World world) {
		List<WorldObject> potentialSellers = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasProperty(Constants.INVENTORY));
		PropertyCountMap<ManagedProperty<?>> sellings = new PropertyCountMap<>();
		for(ManagedProperty<?> managedProperty : demands.keySet()) {
			for(WorldObject potentialSeller : potentialSellers) {
				WorldObjectContainer inventory = potentialSeller.getProperty(Constants.INVENTORY);
				int index = inventory.getIndexFor(w -> w.hasProperty(managedProperty) && w.hasProperty(Constants.SELLABLE) && w.getProperty(Constants.SELLABLE));
				if (index != -1) {
					sellings.add(managedProperty, inventory.get(index).getProperty(Constants.QUANTITY));
				}
			}
		}
		return sellings;
	}
	
	static List<WorldObject> getRemains(World world) {
		return world.findWorldObjects(w -> w.hasProperty(Constants.DECEASED_WORLD_OBJECT) && w.getProperty(Constants.DECEASED_WORLD_OBJECT));
	}

	static Map<Profession, Integer> getProfessionCounts(List<WorldObject> worldObjects) {
		Map<Profession, Integer> professionCounts = new HashMap<>();
		for(WorldObject worldObject : worldObjects) {
			Profession profession = worldObject.getProperty(Constants.PROFESSION);
			final int count;
			if (professionCounts.containsKey(profession)) {
				count = professionCounts.get(profession).intValue() + 1;
			} else {
				count = 1;
			}
			professionCounts.put(profession, count);
		}
		return professionCounts;
	}
	
	static List<ProfessionEvaluation> getProfessionEvaluationsByBackground(WorldObject performer, World world) {
		Background background = performer.getProperty(Constants.BACKGROUND);
		ProfessionExplanation professionExplanation = background.chooseProfession(performer, world);
		if (professionExplanation != null) {
			return Arrays.asList(new ProfessionEvaluation(professionExplanation.getProfession(), 10, professionExplanation.getExplanation()));
		} else {
			return new ArrayList<>();
		}
	}
	
	static List<ProfessionEvaluation> getProfessionEvaluationsByParents(WorldObject performer, World world) {
		List<ProfessionEvaluation> result = new ArrayList<>();
		List<WorldObject> parents = ChildrenPropertyUtils.getParents(performer, world);
		for(WorldObject parent : parents) {
			Profession parentProfession = parent.getProperty(Constants.PROFESSION);
			if (parentProfession != null) {
				result.add(new ProfessionEvaluation(parentProfession, 8));
			}
		}
		return result;
	}
	
	static class ProfessionEvaluation implements Comparable<ProfessionEvaluation> {
		private final Profession profession;
		private final int evaluation;
		private final String explanation;
		
		public ProfessionEvaluation(Profession profession, int evaluation) {
			this(profession, evaluation, null);
		}
		
		public ProfessionEvaluation(Profession profession, int evaluation, String explanation) {
			if (profession == null) { throw new IllegalStateException("profession is null"); }
			this.profession = profession;
			this.evaluation = evaluation;
			this.explanation = explanation;
		}

		public Profession getProfession() {
			return profession;
		}

		public int getEvaluation() {
			return evaluation;
		}
		
		public String getExplanation() {
			return explanation;
		}

		public ProfessionEvaluation add(ProfessionEvaluation other) {
			return new ProfessionEvaluation(profession, evaluation + other.evaluation);
		}

		@Override
		public int compareTo(ProfessionEvaluation other) {
			return -Integer.compare(this.getEvaluation(), other.getEvaluation());
		}
	}
	
	private static class ProfessionInfo {
		private final Profession profession;
		private final double strengthMultiplier;
		private final double constitutionMultiplier;
		private final double dexterityMultiplier;
		private final double intelligenceMultiplier;
		private final double wisdomMultiplier;
		private final double charismaMultiplier;
		
		public ProfessionInfo(Profession profession, double strengthMultiplier,
				double constitutionMultiplier, double dexterityMultiplier,
				double intelligenceMultiplier, double wisdomMultiplier,
				double charismaMultiplier) {
			this.profession = profession;
			this.strengthMultiplier = strengthMultiplier;
			this.constitutionMultiplier = constitutionMultiplier;
			this.dexterityMultiplier = dexterityMultiplier;
			this.intelligenceMultiplier = intelligenceMultiplier;
			this.wisdomMultiplier = wisdomMultiplier;
			this.charismaMultiplier = charismaMultiplier;
		}
		
		public Profession getProfession() {
			return profession;
		}

		public int evaluate(WorldObject performer) {
			int strength = performer.getProperty(Constants.STRENGTH);
			int constitution = performer.getProperty(Constants.CONSTITUTION);
			int dexterity = performer.getProperty(Constants.DEXTERITY);
			int intelligence = performer.getProperty(Constants.INTELLIGENCE);
			int wisdom = performer.getProperty(Constants.WISDOM);
			int charisma = performer.getProperty(Constants.CHARISMA);
			
			double evaluation = strength*strengthMultiplier + constitution*constitutionMultiplier
					+ dexterity*dexterityMultiplier + intelligence*intelligenceMultiplier
					+ wisdom*wisdomMultiplier + charisma* charismaMultiplier;
			return (int) evaluation;
		}
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "choosing a profession";
	}

	@Override
	public String getSimpleDescription() {
		return "choose profession";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.BLACK_CROSS;
	}
}
