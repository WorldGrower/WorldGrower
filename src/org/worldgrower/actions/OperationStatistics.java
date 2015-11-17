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

import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
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
}
