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
package org.worldgrower.attribute;

import java.io.Serializable;

import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class EventKnowledge implements Knowledge, Serializable {
	private final int id;
	private final int subjectId;
	private final int historyId;
	
	public EventKnowledge(int subjectId, World world) {
		this.id = KnowledgeIdFactory.getNextId();
		this.subjectId = subjectId;
		this.historyId = world.getHistory().getNextHistoryId();
	}
	
	@Override
	public boolean refersToSameKnowledge(Knowledge knowledge) {
		if (knowledge instanceof EventKnowledge) {
			EventKnowledge eventKnowledge = (EventKnowledge) knowledge;
			return eventKnowledge.historyId == historyId;
		} else {
			return false;
		}
	}

	@Override
	public boolean knowledgeContainsId(int idToRemove) {
		return false;
	}

	@Override
	public Knowledge copy() {
		return this;
	}

	@Override
	public boolean hasProperty(ManagedProperty<?> managedProperty) {
		return false;
	}

	@Override
	public boolean hasPropertyValue(ManagedProperty<?> managedProperty, Object value) {
		return false;
	}

	public int getHistoryId() {
		return historyId;
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MIN_VALUE + historyId;
	}

	@Override
	public int getSubjectId() {
		return subjectId;
	}

	@Override
	public int getId() {
		return id;
	}
}
