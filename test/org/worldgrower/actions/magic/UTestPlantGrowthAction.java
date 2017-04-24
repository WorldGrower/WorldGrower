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
package org.worldgrower.actions.magic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.PlantGenerator;

public class UTestPlantGrowthAction {

	private PlantGrowthAction action = Actions.PLANT_GROWTH_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 10, 10);
		
		int treeId = PlantGenerator.generateOldTree(0, 0, world);
		WorldObject tree = world.findWorldObjectById(treeId);
		assertEquals(200, tree.getProperty(Constants.WOOD_SOURCE).intValue());
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(500, tree.getProperty(Constants.WOOD_SOURCE).intValue());
		
		assertEquals(5, world.getWorldObjects().size());
		assertEquals(500, world.getWorldObjects().get(0).getProperty(Constants.WOOD_SOURCE).intValue());
		assertEquals(50, world.getWorldObjects().get(1).getProperty(Constants.WOOD_SOURCE).intValue());
		assertEquals(50, world.getWorldObjects().get(2).getProperty(Constants.WOOD_SOURCE).intValue());
		assertEquals(50, world.getWorldObjects().get(3).getProperty(Constants.WOOD_SOURCE).intValue());
		assertEquals(50, world.getWorldObjects().get(4).getProperty(Constants.WOOD_SOURCE).intValue());
	}
	
	@Test
	public void testExecuteForSources() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 10, 10);
		
		int treeId = PlantGenerator.generateOldTree(0, 0, world);
		WorldObject tree = world.findWorldObjectById(treeId);
		assertEquals(200, tree.getProperty(Constants.WOOD_SOURCE).intValue());
		
		int berryBushId = PlantGenerator.generateBerryBush(0, 0, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		assertEquals(1, berryBush.getProperty(Constants.FOOD_SOURCE).intValue());
		
		int nightshadeId = PlantGenerator.generateNightShade(0, 0, world);
		WorldObject nightshade = world.findWorldObjectById(nightshadeId);
		assertEquals(1, nightshade.getProperty(Constants.NIGHT_SHADE_SOURCE).intValue());
		
		int cottonPlantId = PlantGenerator.generateCottonPlant(0, 0, world);
		WorldObject cottonPlant = world.findWorldObjectById(cottonPlantId);
		assertEquals(1, cottonPlant.getProperty(Constants.COTTON_SOURCE).intValue());
		
		int grapevineId = PlantGenerator.generateGrapeVine(0, 0, world);
		WorldObject grapeVine = world.findWorldObjectById(grapevineId);
		assertEquals(1, grapeVine.getProperty(Constants.GRAPE_SOURCE).intValue());
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(500, tree.getProperty(Constants.WOOD_SOURCE).intValue());
		assertEquals(500, berryBush.getProperty(Constants.FOOD_SOURCE).intValue());
		assertEquals(500, nightshade.getProperty(Constants.NIGHT_SHADE_SOURCE).intValue());
		assertEquals(500, cottonPlant.getProperty(Constants.COTTON_SOURCE).intValue());
		assertEquals(500, grapeVine.getProperty(Constants.GRAPE_SOURCE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(1, 1, 4, 4);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(action));
		assertEquals(true, action.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, action.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, action.distance(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(1, 1, 4, 4);
		
		assertEquals(true, action.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}