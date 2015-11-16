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

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.history.HistoryItem;

public class ConversationContext {

	private final WorldObject performer;
	private final WorldObject target;
	private final WorldObject subject;
	private final HistoryItem questionHistoryItem;
	private final World world;
	private final int additionalValue;
	private final int additionalValue2;
	
	public ConversationContext(WorldObject performer, WorldObject target, WorldObject subject, HistoryItem questionHistoryItem, World world, int additionalValue) {
		this(performer, target, subject, questionHistoryItem, world, additionalValue, 0);
	}
	
	public ConversationContext(WorldObject performer, WorldObject target, WorldObject subject, HistoryItem questionHistoryItem, World world, int additionalValue, int additionalValue2) {
		super();
		this.performer = performer;
		this.target = target;
		this.subject = subject;
		this.questionHistoryItem = questionHistoryItem;
		this.world = world;
		this.additionalValue = additionalValue;
		this.additionalValue2 = additionalValue2;
	}

	public WorldObject getPerformer() {
		return performer;
	}

	public WorldObject getTarget() {
		return target;
	}

	public WorldObject getSubject() {
		return subject;
	}

	public HistoryItem getQuestionHistoryItem() {
		return questionHistoryItem;
	}

	public World getWorld() {
		return world;
	}

	public int getAdditionalValue() {
		return additionalValue;
	}

	public int getAdditionalValue2() {
		return additionalValue2;
	}
}
