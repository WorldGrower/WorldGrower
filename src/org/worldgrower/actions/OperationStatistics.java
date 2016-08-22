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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.Item;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;

public class OperationStatistics {

	public static int getRecentOperationsCount(ManagedOperation managedOperation, World world) {
		List<HistoryItem> filteredHistoryItems = getRecentOperations(managedOperation, world);
		return filteredHistoryItems.size();
	}
	
	public static int getRecentOperationsCount(ManagedOperation managedOperation, int[] args, World world) {
		List<HistoryItem> filteredHistoryItems = getRecentOperations(managedOperation, args, world);
		return filteredHistoryItems.size();
	}

	private static List<HistoryItem> getRecentOperations(ManagedOperation managedOperation, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> isRecent(h, world) ).collect(Collectors.toList());
		return filteredHistoryItems;
	}
	
	private static List<HistoryItem> getRecentOperations(ManagedOperation managedOperation, int[] args, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> Arrays.equals(h.getArgs(),  args) && isRecent(h, world) ).collect(Collectors.toList());
		return filteredHistoryItems;
	}
	
	private static boolean isRecent(HistoryItem historyItem, World world) {
		return historyItem.getTurn().isWithin1000Turns(world.getCurrentTurn());
	}
	
	private static boolean isNonProfessional(HistoryItem historyItem, Profession profession) {
		Profession performerProfession = historyItem.getPerformer().getProperty(Constants.PROFESSION);
		return performerProfession == null || performerProfession != profession;
	}
	
	public static int getPopulationCount(World world) {
		return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isPartOfPopulation(w)).size();
	}

	private static boolean isPartOfPopulation(WorldObject w) {
		return w.hasIntelligence() && (w.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE);
	}
	
	public static int getRecentOperationsByNonProfessionalsCount(ManagedOperation managedOperation, Profession profession, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> isNonProfessional(h, profession)).collect(Collectors.toList());
		return filteredHistoryItems.size();
	}

	public static int getPrice(Item item, World world) {
		List<WorldObject> sellers = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.getProperty(Constants.PROFESSION) != null && w.getProperty(Constants.PROFESSION).getSellItems().contains(item));
		if (sellers.size() > 0) {
			int totalPrice = 0;
			for(WorldObject seller : sellers) {
				totalPrice += seller.getProperty(Constants.PRICES).getPrice(item);
			}
			int sellerCount = sellers.size();
			if (sellerCount == 0) {
				sellerCount = 1;
			}
			return totalPrice / sellerCount;
		} else {
			return item.getPrice();
		}
	}
}
