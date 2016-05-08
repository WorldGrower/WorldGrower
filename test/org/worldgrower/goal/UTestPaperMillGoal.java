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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestPaperMillGoal {

	private PaperMillGoal goal = Goals.PAPER_MILL_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		PlantGenerator.generateTree(5, 5, world);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalNoRoomForPaperMill() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalBuildPaperMill() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		
		assertEquals(Actions.BUILD_PAPER_MILL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		addPaperMill(world, performer);
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private void addPaperMill(World world, WorldObject performer) {
		int paperMillId = BuildingGenerator.generatePaperMill(0, 0, world);
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(paperMillId, BuildingType.PAPERMILL));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		return commoner;
	}
}