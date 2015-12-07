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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.EventKnowledge;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;

public class KnowledgeToDescriptionMapper {

	public String getDescription(Knowledge knowledge, World world) {
		WorldObject subject = world.findWorldObject(Constants.ID, knowledge.getSubjectId());
		if (knowledge instanceof PropertyKnowledge) {
			PropertyKnowledge propertyKnowledge = (PropertyKnowledge) knowledge;
			ManagedProperty<?> property = propertyKnowledge.getManagedProperty();
			Object value = propertyKnowledge.getValue();
			if (BuildingGenerator.isWell(subject) && property == Constants.POISON_DAMAGE) {
				return "Did you know the well is poisoned?";
			} else if (property == Constants.CHILD_BIRTH_ID) {
				WorldObject child = world.findWorldObject(Constants.ID, (Integer) value);
				return "Did you know that " + subject.getProperty(Constants.NAME) + " gave birth to " + child.getProperty(Constants.NAME) + "?";
			} else if (property == Constants.DEITY) {
				if (value != null) {
					return "Did you know " + subject.getProperty(Constants.NAME) + " worships " + ((Deity) value).getName() + "?";
				} else {
					return "Did you know " + subject.getProperty(Constants.NAME) + " doesn't worship a deity?";
				}
			} else if (property == Constants.PROFESSION) {
				if (value != null) {
					return "Did you know " + subject.getProperty(Constants.NAME) + " is a " + ((Profession) value).getDescription() + "?";
				} else {
					return "Did you know " + subject.getProperty(Constants.NAME) + " doesn't have a profession?";
				}
			} else if (property == Constants.ORGANIZATION_LEADER_ID) {
				if (value != null) {
					int leaderId = (Integer) value;
					WorldObject leader = world.findWorldObject(Constants.ID, leaderId);
					return "Did you know that " + leader.getProperty(Constants.NAME) + " is the leader of the " + subject.getProperty(Constants.NAME) + "?";
				} else {
					return "Did you know the " + subject.getProperty(Constants.NAME) + " doesn't have a leader?";
				}
			} else {
				throw new IllegalStateException("No mapping found for property " + property + " and value " + value);
			}
		} else if (knowledge instanceof EventKnowledge) {
			EventKnowledge eventKnowledge = (EventKnowledge) knowledge;
			int historyId = eventKnowledge.getHistoryId();
			HistoryItem historyItem = world.getHistory().getHistoryItem(historyId);
			return "Did you know " + historyItem.getOperationInfo().getThirdPersonDescription(world) + "?";
		} else {
			throw new IllegalStateException("No mapping found for knowledge " + knowledge);
		}
	}
}
