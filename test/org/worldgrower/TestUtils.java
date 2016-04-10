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
package org.worldgrower;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.Skill;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class TestUtils {

	public static WorldObject createWorldObject(int x, int y, int width, int height) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.LUMBERING_SKILL, new Skill(10));
		WorldObject w1 = new WorldObjectImpl(properties);
		return w1;
	}
	
	public static WorldObject createWorldObject(int x, int y, int width, int height, ManagedProperty<?> property, Object value) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties);
		return w1;
	}
	
	public static WorldObject createIntelligentWorldObject(int x, int y, int width, int height, ManagedProperty<?> property, Object value) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, null);
		return w1;
	}
	
	public static WorldObject createWorldObject(int id, String name) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, name);
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList().add(6));
		properties.put(Constants.SOCIAL, 0);
		properties.put(Constants.RELATIONSHIPS, new IdRelationshipMap());
		properties.put(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		properties.put(Constants.CREATURE_TYPE,CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.BACKGROUND, new BackgroundImpl());
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, null);
		return w1;
	}
	
	public static WorldObject createIntelligentWorldObject(int id, ManagedProperty<?> property, Object value) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList().add(6));
		properties.put(Constants.SOCIAL, 0);
		properties.put(Constants.RELATIONSHIPS, new IdRelationshipMap());
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.STRENGTH, 10);
		properties.put(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.PRICES, new Prices());
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, null);
		return w1;
	}
	
	public static WorldObject createIntelligentWorldObject(int id, Goal goal) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList().add(6));
		properties.put(Constants.SOCIAL, 0);
		properties.put(Constants.RELATIONSHIPS, new IdRelationshipMap());
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.PRICES, new Prices());
		properties.put(Constants.STRENGTH, 10);
		properties.put(Constants.WATER, 10);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.STRENGTH, 10);
		properties.put(Constants.DEXTERITY, 10);
		properties.put(Constants.CONSTITUTION, 10);
		properties.put(Constants.INTELLIGENCE, 10);
		properties.put(Constants.WISDOM, 10);
		properties.put(Constants.CHARISMA, 10);
		SkillUtils.addAllSkills(properties);
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new WorldObjectPriorities() {

			@Override
			public List<Goal> getPriorities(WorldObject performer, World world) {
				return Arrays.asList(goal);
			}
		});
		return w1;
	}
	
	public static WorldObject createIntelligentWorldObject(int id, String name) {
		WorldObject worldObject = createIntelligentWorldObject(id, Goals.IDLE_GOAL);
		worldObject.setProperty(Constants.NAME, name);
		return worldObject;
	}
	
	private static class WorldObjectPrioritiesImpl implements WorldObjectPriorities {

		@Override
		public List<Goal> getPriorities(WorldObject performer, World world) {
			return null;
		}
	}
	
	public static WorldObject createIntelligentWorldObject(int id, ManagedProperty<?> property, Object value, WorldObjectPriorities worldObjectPriorities) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList());
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, worldObjectPriorities);
		return w1;
	}

	public static WorldObject createWorldObject(int id, Map<ManagedProperty<?>, Object> properties) {
		properties.put(Constants.ID, id);
		return new WorldObjectImpl(properties, Actions.ALL_ACTIONS, null);
	}

	public static WorldObject createSkilledWorldObject(int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.STRENGTH, 10);
		properties.put(Constants.CONSTITUTION, 10);
		properties.put(Constants.DEXTERITY, 10);
		properties.put(Constants.INTELLIGENCE, 10);
		properties.put(Constants.WISDOM, 10);
		properties.put(Constants.CHARISMA, 10);
		properties.put(Constants.PRICES, new Prices());
		SkillUtils.addAllSkills(properties);
		WorldObject worldObject = createWorldObject(id, properties);
		return worldObject;
	}
	
	public static<T> WorldObject createSkilledWorldObject(int id, ManagedProperty<T> propertyKey, T value) {
		WorldObject worldObject = createSkilledWorldObject(id);
		worldObject.setProperty(propertyKey, value);
		return worldObject;
	}
}