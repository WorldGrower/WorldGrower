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

import java.util.ArrayList;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;

public class UTestScribeWizardSpellsGoal {

	private ScribeWizardSpellsGoal goal = Goals.SCRIBE_WIZARD_SPELLS_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalResearchSkill() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		Actions.BUILD_LIBRARY_ACTION.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(Actions.getResearchSpellActionFor(Actions.FIRE_BOLT_ATTACK_ACTION), goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		BuildingGenerator.generateLibrary(5, 5, world, performer);
		PlantGenerator.generateOldTree(5, 5, world);
		
		performer.getProperty(Constants.KNOWN_SPELLS).add(Actions.FIRE_BOLT_ATTACK_ACTION);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalScribeSpell() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		Actions.BUILD_LIBRARY_ACTION.execute(performer, performer, Args.EMPTY, world);
		PlantGenerator.generateTree(5, 5, world);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.PAPER.generate(1f), 20);
		performer.getProperty(Constants.KNOWN_SPELLS).add(Actions.FIRE_BOLT_ATTACK_ACTION);
		
		assertEquals(Actions.getScribeMagicSpellActionFor(Actions.FIRE_BOLT_ATTACK_ACTION), goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		int libraryId = BuildingGenerator.generateLibrary(5, 5, world, performer);
		WorldObject library = world.findWorldObjectById(libraryId);
		
		library.getProperty(Constants.INVENTORY).add(Item.generateSpellBook(Actions.FIRE_BOLT_ATTACK_ACTION));
		library.getProperty(Constants.INVENTORY).add(Item.generateSpellBook(Actions.RAY_OF_FROST_ATTACK_ACTION));
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		return performer;
	}
}