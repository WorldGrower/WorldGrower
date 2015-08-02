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
import java.util.stream.Collectors;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.Background;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class ChooseProfessionAction implements ManagedOperation {

	private static final List<ProfessionInfo> PROFESSION_INFOS = Arrays.asList(
			new ProfessionInfo(Professions.PRIEST_PROFESSION, 1.4, 1.2, 0.8, 0.8, 1.6, 1.0),
			new ProfessionInfo(Professions.BLACKSMITH_PROFESSION, 1.2, 1.1, 0.8, 0.8, 1.2, 1.0),
			new ProfessionInfo(Professions.THIEF_PROFESSION, 0.8, 1.2, 1.4, 0.8, 0.8, 1.4),
			new ProfessionInfo(Professions.FARMER_PROFESSION, 1.0, 1.2, 0.8, 1.0, 1.2, 1.0),
			new ProfessionInfo(Professions.LUMBERJACK_PROFESSION, 1.2, 1.2, 1.2, 0.8, 1.0, 1.0),
			new ProfessionInfo(Professions.MINER_PROFESSION, 1.4, 1.3, 1.1, 0.8, 1.0, 1.0),
			new ProfessionInfo(Professions.SHERIFF_PROFESSION, 1.5, 1.3, 0.8, 1.1, 0.8, 0.8)
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
			
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.PROFESSION, Professions.FARMER_PROFESSION);
			WorldObject facade = new WorldObjectImpl(properties);
			performer.setProperty(Constants.FACADE, facade);
		}
		
		performer.setProperty(Constants.PROFESSION, profession);
		performer.getProperty(Constants.REASONS).addReason(Constants.PROFESSION, reason);
	}

	private ProfessionResult getProfessionResult(WorldObject performer, World world) {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = getProfessionEvaluationsByPerformer(performer);
		List<ProfessionEvaluation> professionEvaluationsByCompetition = getProfessionEvaluationsByCompetition(performer, world);
		List<ProfessionEvaluation> professionEvaluationsByDemand = getProfessionEvaluationsByDemand(performer, world);
		List<ProfessionEvaluation> professionEvaluationsByBackground = getProfessionEvaluationsByBackground(performer, world);
		
		List<ProfessionEvaluation> mergedProfessionEvaluations = merge(professionEvaluationsByPerformer, professionEvaluationsByCompetition);
		mergedProfessionEvaluations = merge(mergedProfessionEvaluations, professionEvaluationsByDemand);
		mergedProfessionEvaluations = merge(mergedProfessionEvaluations, professionEvaluationsByBackground);
		Collections.sort(mergedProfessionEvaluations);
		
		ProfessionEvaluation bestProfession = mergedProfessionEvaluations.get(0);	
		String reason = getReason(bestProfession.getProfession(), professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground);
		
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
	
	private String getReason(
			Profession bestProfession,
			List<ProfessionEvaluation> professionEvaluationsByPerformer,
			List<ProfessionEvaluation> professionEvaluationsByCompetition,
			List<ProfessionEvaluation> professionEvaluationsByDemand,
			List<ProfessionEvaluation> professionEvaluationsByBackground) {
		
		int indexOfBestProfessionByPerformer = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByPerformer);
		int indexOfBestProfessionByCompetition = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByCompetition);
		int indexOfBestProfessionByDemand = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByDemand);
		int indexOfBestProfessionByBackground = findIndexOfName(bestProfession.getDescription(), professionEvaluationsByBackground);
		
		//TODO: compare with professionEvaluationsByDemand
		if (indexOfBestProfessionByBackground == 0) {
			return "I choose to become a " + bestProfession.getDescription() + " because of my background";
		} else if (indexOfBestProfessionByPerformer == 0 && indexOfBestProfessionByCompetition == 0) {
			return "I choose to become a " + bestProfession.getDescription() + " because there isn't much competition for that profession and I'm good at it";
		} else if (indexOfBestProfessionByPerformer < indexOfBestProfessionByCompetition) {
			return "I choose to become a " + bestProfession.getDescription() + " because I'm good at it";
		} else if (indexOfBestProfessionByCompetition < indexOfBestProfessionByPerformer) {
			return "I choose to become a " + bestProfession.getDescription() + " because there isn't much competition for it";
		} else if (indexOfBestProfessionByDemand < indexOfBestProfessionByPerformer) {
			return "I choose to become a " + bestProfession.getDescription() + " because there is a demand for it";
		}
		
		return "It just seemed like a good idea to become a " + bestProfession.getDescription();
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
	
	private List<ProfessionEvaluation> getProfessionEvaluationsByPerformer(WorldObject performer) {
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
			result.add(new ProfessionEvaluation(professionInfo.getProfession(), (worldObjects.size() - professionCount) * 5));
		}
		Collections.sort(result);
		return result;
	}
	
	static List<ProfessionEvaluation> getProfessionEvaluationsByDemand(WorldObject performer, World world) {
		List<WorldObject> worldObjects = GroupPropertyUtils.findWorldObjectsInSameGroup(performer, world);
		
		PropertyCountMap mergedDemands = new PropertyCountMap();
		for(WorldObject worldObject : worldObjects) {
			if (worldObject.hasProperty(Constants.DEMANDS)) {
				mergedDemands.addAll(worldObject.getProperty(Constants.DEMANDS));
			}
		}
		
		return mapDemandsToProfessions(mergedDemands, world);
	}

	static List<ProfessionEvaluation> mapDemandsToProfessions(PropertyCountMap demands, World world) {
		List<ProfessionEvaluation> result = new ArrayList<>();
		int populationCount = getPopulationCount(world);
		
		int foodDemand = demands.count(Constants.FOOD);
		foodDemand += getRecentOperationsByNonProfessionalsCount(Actions.EAT_ACTION, Professions.FARMER_PROFESSION, world) / (5 * populationCount);
		result.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, foodDemand));
		
		//int waterDemand = demands.getQuantityFor(Constants.WATER);
		
		int woodDemand = demands.count(Constants.WOOD);
		woodDemand += getRecentOperationsByNonProfessionalsCount(Actions.CUT_WOOD_ACTION, Professions.LUMBERJACK_PROFESSION, world) / (5 * populationCount);
		result.add(new ProfessionEvaluation(Professions.LUMBERJACK_PROFESSION, woodDemand));
		
		int stoneDemand = demands.count(Constants.STONE);
		int oreDemand = demands.count(Constants.ORE);
		result.add(new ProfessionEvaluation(Professions.MINER_PROFESSION, (stoneDemand + oreDemand) / 2));
		
		return result;
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
	
	private List<ProfessionEvaluation> getProfessionEvaluationsByBackground(WorldObject performer, World world) {
		Background background = performer.getProperty(Constants.BACKGROUND);
		Profession backgroundProfession = background.chooseValue(performer, Constants.PROFESSION, world);
		if (backgroundProfession != null) {
			return Arrays.asList(new ProfessionEvaluation(backgroundProfession, 10));
		} else {
			return new ArrayList<>();
		}
	}
	
	static class ProfessionEvaluation implements Comparable<ProfessionEvaluation> {
		private final Profession profession;
		private final int evaluation;
		
		public ProfessionEvaluation(Profession profession, int evaluation) {
			if (profession == null) { throw new IllegalStateException("profession is null"); }
			this.profession = profession;
			this.evaluation = evaluation;
		}

		public Profession getProfession() {
			return profession;
		}

		public int getEvaluation() {
			return evaluation;
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
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		int performerId = performer.getProperty(Constants.ID);
		int targetId = target.getProperty(Constants.ID);
		return (performerId == targetId);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
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
	
	public static int getRecentOperationsCount(ManagedOperation managedOperation, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> isRecent(h, world) ).collect(Collectors.toList());
		return filteredHistoryItems.size();
	}
	
	private static boolean isRecent(HistoryItem historyItem, World world) {
		return historyItem.getTurn().isWithin1000Turns(world.getCurrentTurn());
	}
	
	private static boolean isNonProfessional(HistoryItem historyItem, Profession profession) {
		Profession performerProfession = historyItem.getOperationInfo().getPerformer().getProperty(Constants.PROFESSION);
		return performerProfession == null || performerProfession != profession;
	}
	
	private static int getPopulationCount(World world) {
		return world.findWorldObjects(w -> w.hasIntelligence()).size();
	}
	
	public static int getRecentOperationsByNonProfessionalsCount(ManagedOperation managedOperation, Profession profession, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> isNonProfessional(h, profession)).collect(Collectors.toList());
		return filteredHistoryItems.size();
	}
	
}
