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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class KnowledgeMap implements IdContainer, Serializable {

	private final Map<Integer, Knowledge> idsToKnowledge = new HashMap<>();
	
	public KnowledgeMap() {
	}
	
	private KnowledgeMap(Map<Integer, Knowledge> resultMap) {
		idsToKnowledge.putAll(resultMap);
	}

	public final void addKnowledge(WorldObject worldObject, ManagedProperty<?> managedProperty, Object value) {
		addKnowledge(worldObject.getProperty(Constants.ID), managedProperty, value);
	}
	
	public final void addKnowledge(int id, ManagedProperty<?> managedProperty, Object value) {
		idsToKnowledge.put(id, new Knowledge(managedProperty, value));
	}

	public void addKnowledge(WorldObject worldObject, Knowledge knowledge) {
		idsToKnowledge.put(worldObject.getProperty(Constants.ID), new Knowledge(knowledge));
	}
	
	public List<WorldObject> findWorldObjects(ManagedProperty<?> managedProperty, Object value, World world) {
		List<WorldObject> worldObjects = new ArrayList<>();
		for(Entry<Integer, Knowledge> entry : idsToKnowledge.entrySet()) {
			int id = entry.getKey();
			Knowledge knowledgeValue = entry.getValue();
			if (knowledgeValue.getManagedProperty() == managedProperty && knowledgeValue.getValue().equals(value)) {
				worldObjects.add(world.findWorldObject(Constants.ID, id));
			}
		}
		return worldObjects;
	}
	
	public final boolean hasProperty(WorldObject worldObject, ManagedProperty<?> managedProperty) {
		return hasProperty(worldObject.getProperty(Constants.ID), managedProperty);
	}
	
	public final boolean hasProperty(int id, ManagedProperty<?> managedProperty) {
		Knowledge knowledge = idsToKnowledge.get(id);
		return knowledge != null && knowledge.getManagedProperty() == managedProperty;
	}
	
	public final Knowledge getKnowledge(WorldObject worldObject) {
		return getKnowledge(worldObject.getProperty(Constants.ID));
	}
	
	public final Knowledge getKnowledge(int id) {
		return idsToKnowledge.get(id);
	}
	
	public final void remove(int id) {
		idsToKnowledge.remove(id);
	}
	
	@Override
	public final String toString() {
		return "[" + idsToKnowledge + "]";
	}

	@Override
	public final void remove(WorldObject worldObject, ManagedProperty<?> property, int idToRemove) {
		KnowledgeMapProperty knowledgeMapProperty = (KnowledgeMapProperty) property;
		worldObject.getProperty(knowledgeMapProperty).remove(idToRemove);
		
		Iterator<Entry<Integer, Knowledge>> entryIterator = idsToKnowledge.entrySet().iterator();
		while(entryIterator.hasNext()) {
			Entry<Integer, Knowledge> entry = entryIterator.next();
			Knowledge knowledgeValue = entry.getValue();
			if (knowledgeValue.getManagedProperty() instanceof IdContainer && knowledgeValue.getValue().equals(idToRemove)) {
				entryIterator.remove();
			}
		}
	}
	
	public KnowledgeMap copy() {
		KnowledgeMap copy = new KnowledgeMap(idsToKnowledge);
		return copy;
	}

	public KnowledgeMap subtract(KnowledgeMap knowledgeMapToSubtract) {
		Map<Integer, Knowledge> resultMap = new HashMap<>(idsToKnowledge);
		Iterator<Entry<Integer, Knowledge>> entrySetIterator = resultMap.entrySet().iterator();
		while(entrySetIterator.hasNext()) {
			Entry<Integer, Knowledge> entry = entrySetIterator.next();
			Integer id = entry.getKey();
			ManagedProperty<?> property = entry.getValue().getManagedProperty();
			
			Knowledge knowledgeToSubtract = knowledgeMapToSubtract.idsToKnowledge.get(id);
			if (knowledgeToSubtract != null && knowledgeToSubtract.getManagedProperty() == property) {
				entrySetIterator.remove();
			}
		}
		return new KnowledgeMap(resultMap);
	}

	public boolean hasKnowledge() {
		return !idsToKnowledge.isEmpty();
	}

	public List<Integer> getIds() {
		return new ArrayList<>(idsToKnowledge.keySet());
	}
}