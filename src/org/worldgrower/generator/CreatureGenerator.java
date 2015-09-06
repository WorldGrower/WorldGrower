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
package org.worldgrower.generator;

import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.DoNothingOnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.Skill;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.gui.ImageIds;

public class CreatureGenerator {

	private final WorldObject organization;
	
	public CreatureGenerator(WorldObject organization) {
		this.organization = organization;
	}

	public int generateRat(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		final String gender;
		if (Math.random() > 0.5f) {
			gender = "female";
		} else {
			gender = "male";
		}
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 2);
		properties.put(Constants.HIT_POINTS_MAX, 3);
		properties.put(Constants.NAME, "Rat");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.RAT);
		properties.put(Constants.FOOD, 200);
		properties.put(Constants.WATER, 200);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.GOLD, 0);
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 500);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.RAT_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		
		properties.put(Constants.ARMOR, 10);
		
		properties.put(Constants.STRENGTH, 6);
		properties.put(Constants.DEXTERITY, 12);
		properties.put(Constants.CONSTITUTION, 8);
		properties.put(Constants.INTELLIGENCE, 6);
		properties.put(Constants.WISDOM, 12);
		properties.put(Constants.CHARISMA, 6);
		
		properties.put(Constants.HAND_TO_HAND_SKILL, new Skill(10));
		
		properties.put(Constants.DAMAGE, 2);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject rat = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new BeastOnTurn(this::generateRat), new RatWorldEvaluationFunction());
		world.addWorldObject(rat);
		
		return id;
	}
	
	public int generateSpider(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		final String gender;
		if (Math.random() > 0.5f) {
			gender = "female";
		} else {
			gender = "male";
		}
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 9);
		properties.put(Constants.HIT_POINTS_MAX, 10);
		properties.put(Constants.NAME, "Spider");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.SPIDER);
		properties.put(Constants.FOOD, 200);
		properties.put(Constants.WATER, 200);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.GOLD, 0);
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 500);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.SPIDER_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		
		properties.put(Constants.ARMOR, 10);
		
		properties.put(Constants.STRENGTH, 6);
		properties.put(Constants.DEXTERITY, 12);
		properties.put(Constants.CONSTITUTION, 8);
		properties.put(Constants.INTELLIGENCE, 6);
		properties.put(Constants.WISDOM, 12);
		properties.put(Constants.CHARISMA, 6);
		
		properties.put(Constants.HAND_TO_HAND_SKILL, new Skill(10));
		
		properties.put(Constants.DAMAGE, 5);
		properties.put(Constants.DAMAGE_RESIST, 10);
		
		WorldObject spider = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new BeastOnTurn(this::generateSpider), new SpiderWorldEvaluationFunction());
		world.addWorldObject(spider);
		
		return id;
	}
	
	public int generateSlime(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		final String gender;
		if (Math.random() > 0.5f) {
			gender = "female";
		} else {
			gender = "male";
		}
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 15);
		properties.put(Constants.HIT_POINTS_MAX, 20);
		properties.put(Constants.NAME, "Slime");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.SLIME);
		properties.put(Constants.FOOD, 500);
		properties.put(Constants.WATER, 500);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.GOLD, 0);
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 500);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.SLIME_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		
		properties.put(Constants.ARMOR, 10);
		
		properties.put(Constants.STRENGTH, 12);
		properties.put(Constants.DEXTERITY, 8);
		properties.put(Constants.CONSTITUTION, 16);
		properties.put(Constants.INTELLIGENCE, 6);
		properties.put(Constants.WISDOM, 12);
		properties.put(Constants.CHARISMA, 6);
		
		properties.put(Constants.HAND_TO_HAND_SKILL, new Skill(10));
		
		properties.put(Constants.DAMAGE, 3);
		properties.put(Constants.DAMAGE_RESIST, 8);
		
		WorldObject slime = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new BeastOnTurn(this::generateSlime), new SlimeWorldEvaluationFunction());
		world.addWorldObject(slime);
		
		return id;
	}
	
	public int generateSkeleton(int x, int y, World world, WorldObject performer) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 15);
		properties.put(Constants.HIT_POINTS_MAX, 15);
		properties.put(Constants.NAME, "Skeleton");
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SKELETON);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.GOLD, 0);
		properties.put(Constants.CREATURE_TYPE, CreatureType.UNDEAD_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.GOLD, 0);
		properties.put(Constants.CREATOR_ID, performer.getProperty(Constants.ID));
		properties.put(Constants.GIVEN_ORDER, null);
		
		properties.put(Constants.ARMOR, 10);
		
		properties.put(Constants.STRENGTH, 6);
		properties.put(Constants.DEXTERITY, 12);
		properties.put(Constants.CONSTITUTION, 8);
		properties.put(Constants.INTELLIGENCE, 6);
		properties.put(Constants.WISDOM, 12);
		properties.put(Constants.CHARISMA, 6);
		SkillUtils.addAllSkills(properties);
		
		properties.put(Constants.DAMAGE, 5);
		properties.put(Constants.DAMAGE_RESIST, 10);
		
		WorldObject skeleton = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new SkeletonOnTurn(), new SkeletonWorldEvaluationFunction());
		world.addWorldObject(skeleton);
		
		return id;
	}
	
	public int generateFish(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		final String gender;
		if (Math.random() > 0.5f) {
			gender = "female";
		} else {
			gender = "male";
		}
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 2);
		properties.put(Constants.HIT_POINTS_MAX, 3);
		properties.put(Constants.NAME, "Fish");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.FISH);
		properties.put(Constants.FOOD, 1000);
		properties.put(Constants.FOOD_SOURCE, 1);
		properties.put(Constants.WATER, 1000);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.GOLD, 0);
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 500);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.FISH_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		
		properties.put(Constants.ARMOR, 10);
		
		properties.put(Constants.DAMAGE, 2);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject fish = new WorldObjectImpl(properties, new FishOnTurn(this::generateFish));
		world.addWorldObject(fish);
		
		return id;
	}
}
