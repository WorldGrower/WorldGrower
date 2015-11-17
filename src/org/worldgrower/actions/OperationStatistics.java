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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;

public class OperationStatistics {

	public static int getRecentOperationsCount(ManagedOperation managedOperation, World world) {
		List<HistoryItem> filteredHistoryItems = getRecentOperations(managedOperation, world);
		return filteredHistoryItems.size();
	}

	private static List<HistoryItem> getRecentOperations(ManagedOperation managedOperation, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> isRecent(h, world) ).collect(Collectors.toList());
		return filteredHistoryItems;
	}
	
	private static boolean isRecent(HistoryItem historyItem, World world) {
		return historyItem.getTurn().isWithin1000Turns(world.getCurrentTurn());
	}
	
	private static boolean isNonProfessional(HistoryItem historyItem, Profession profession) {
		Profession performerProfession = historyItem.getOperationInfo().getPerformer().getProperty(Constants.PROFESSION);
		return performerProfession == null || performerProfession != profession;
	}
	
	public static int getPopulationCount(World world) {
		return world.findWorldObjects(w -> w.hasIntelligence()).size();
	}
	
	public static int getRecentOperationsByNonProfessionalsCount(ManagedOperation managedOperation, Profession profession, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(managedOperation);
		List<HistoryItem> filteredHistoryItems = historyItems.stream().filter(h -> isNonProfessional(h, profession)).collect(Collectors.toList());
		return filteredHistoryItems.size();
	}
	
	public static Map<Item, Integer> getItemsSold(WorldObject performer, World world) {
		Map<Item, Integer> itemsSold = new HashMap<>();
		
		List<HistoryItem> buyActions = getRecentOperations(Actions.BUY_ACTION, world);
		buyActions = buyActions.stream().filter(h -> performer.equals(h.getOperationInfo().getTarget())).collect(Collectors.toList());
		
		addToItemsSold(itemsSold, buyActions, h -> h.getOperationInfo().getTarget());
		
		List<HistoryItem> sellActions = getRecentOperations(Actions.SELL_ACTION, world);
		sellActions = sellActions.stream().filter(h -> performer.equals(h.getOperationInfo().getPerformer())).collect(Collectors.toList());
		addToItemsSold(itemsSold, sellActions, h -> h.getOperationInfo().getPerformer());
		
		return itemsSold;
	}

	private static void addToItemsSold(Map<Item, Integer> itemsSold, List<HistoryItem> buyActions, Function<HistoryItem, WorldObject> inventoryTargetFunction) {
		for(HistoryItem buyAction : buyActions) {
			int index = buyAction.getOperationInfo().getArgs()[0];
			WorldObject inventoryTarget = inventoryTargetFunction.apply(buyAction);
			Item item = inventoryTarget.getProperty(Constants.INVENTORY).get(index).getProperty(Constants.ITEM_ID);
			if (!itemsSold.containsKey(item)) {
				itemsSold.put(item, 0);
			}
			itemsSold.put(item, itemsSold.get(item) + 1);
		}
	}
}
