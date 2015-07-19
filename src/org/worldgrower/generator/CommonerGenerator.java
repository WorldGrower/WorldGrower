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

import org.worldgrower.CommonerNameGenerator;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectContainer;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.ReasonsImpl;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;

public class CommonerGenerator {

	public static int generateCursedCommoner(int x, int y, World world, CommonerImageIds commonerImageIds, CommonerNameGenerator commonerNameGenerator, WorldObject organization) {
		int id = generateCommoner(x, y, world, commonerImageIds, commonerNameGenerator, organization);
		WorldObject cursedCommoner = world.findWorldObject(Constants.ID, id);
		cursedCommoner.setProperty(Constants.CURSE, Curse.TOAD_CURSE);
		
		return id;
	}
	
	public static int generateCommoner(int x, int y, World world, CommonerImageIds commonerImageIds, CommonerNameGenerator commonerNameGenerator, WorldObject organization) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		final ImageIds imageId;
		final String gender;
		final String name;
		if (Math.random() > 0.5f) {
			imageId = commonerImageIds.getNextFemaleCommonerImageId();
			gender = "female";
			name = commonerNameGenerator.getNextFemaleCommonerName();
		} else {
			imageId = commonerImageIds.getNextMaleCommonerImageId();
			gender = "male";
			name = commonerNameGenerator.getNextMaleCommonerName();
		}
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 15);
		properties.put(Constants.HIT_POINTS_MAX, 15);
		properties.put(Constants.NAME, name);
		properties.put(Constants.EXPERIENCE, 0);
		properties.put(Constants.ARMOR, 10);
		properties.put(Constants.STRENGTH, 10);
		properties.put(Constants.DEXTERITY, 10);
		properties.put(Constants.CONSTITUTION, 10);
		properties.put(Constants.INTELLIGENCE, 10);
		properties.put(Constants.WISDOM, 10);
		properties.put(Constants.CHARISMA, 10);
		
		SkillUtils.addAllSkills(properties);
		
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, imageId);
		properties.put(Constants.LOOK_DIRECTION, LookDirection.SOUTH);
		properties.put(Constants.FOOD, 500);
		properties.put(Constants.WATER, 500);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.PROFESSION, null);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.DEMANDS, new WorldObjectContainer());
		properties.put(Constants.RELATIONSHIPS, new IdMap());
		properties.put(Constants.REASONS, new ReasonsImpl());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 500);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.BACKGROUND, new BackgroundImpl());
		properties.put(Constants.GOLD, 100);
		properties.put(Constants.HOUSE_ID, null);
		properties.put(Constants.GROUP, new IdList().add(organization));
		
		properties.put(Constants.DAMAGE, 8);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject creature = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new CommonerOnTurn(commonerImageIds, commonerNameGenerator, organization), new CommonerWorldEvaluationFunction());
		world.addWorldObject(creature);
		
		return id;
	}
	
	public static void generateGods(World world) {
		CommonerGenerator.generateGod("Demeter", world);
		CommonerGenerator.generateGod("Hephaestus", world);
		CommonerGenerator.generateGod("Hades", world);
	}
	
	private static int generateGod(String name, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, -100000);
		properties.put(Constants.Y, -100000);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.NAME, name);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.STATUE_OF_DEITY);
		properties.put(Constants.CREATURE_TYPE, CreatureType.DEITY_CREATURE_TYPE);
		WorldObject god = new WorldObjectImpl(properties);
		world.addWorldObject(god);
		
		return id;
	}
}
