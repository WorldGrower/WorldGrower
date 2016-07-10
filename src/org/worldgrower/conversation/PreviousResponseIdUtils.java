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
package org.worldgrower.conversation;

import java.util.List;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.history.HistoryItem;

public class PreviousResponseIdUtils {

	public static boolean previousResponseIdsContains(Conversation conversation, int id, WorldObject performer, WorldObject target, World world) {
		List<HistoryItem> historyItems = getHistoryItems(conversation, performer, target, world);
		for(HistoryItem historyItem : historyItems) {
			int responseId = (Integer)historyItem.getAdditionalValue();
			if (responseId == id) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean previousResponseIdsContains(Conversation conversation, int id1, int id2, WorldObject performer, WorldObject target, World world) {
		List<HistoryItem> historyItems = getHistoryItems(conversation, performer, target, world);
		for(HistoryItem historyItem : historyItems) {
			int responseId = (Integer)historyItem.getAdditionalValue();
			if (responseId == id1 || responseId == id2) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> T getLastAdditionalValue(Conversation conversation, WorldObject performer, WorldObject target, World world) {
		List<HistoryItem> historyItems = getHistoryItems(conversation, performer, target, world);
		if (historyItems.size() > 0) {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			return (T) lastHistoryItem.getAdditionalValue();
		}
		return null;
	}

	private static List<HistoryItem> getHistoryItems(Conversation conversation, WorldObject performer, WorldObject target, World world) {
		return world.getHistory().findHistoryItemsForAnyPerformer(performer, target, Conversations.createArgs(conversation), Actions.TALK_ACTION);
	}
}
