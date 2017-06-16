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
package org.worldgrower.deity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.Turn;
import org.worldgrower.profession.Professions;

public class UTestDemeter {

	private Demeter deity = Deity.DEMETER;
	
	@Test
	public void testWorship() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(0, performer.getProperty(Constants.FARMING_SKILL).getLevel(performer));
		deity.worship(performer, target, 4, world);
		assertEquals(0, performer.getProperty(Constants.FARMING_SKILL).getLevel(performer));
		deity.worship(performer, target, 5, world);
		
		assertEquals(2, performer.getProperty(Constants.FARMING_SKILL).getLevel(performer));
	}
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		int berryBushId = PlantGenerator.generateBerryBush(0, 0, world);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		for(int i=0; i<20; i++) { world.addWorldObject(TestUtils.createIntelligentWorldObject(i+10, Constants.DEITY, Deity.ARES)); }
		
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		assertEquals(false, berryBush.getProperty(Constants.CONDITIONS).hasCondition(Condition.WILTING_CONDITION));
		
		for(int i=0; i<7800; i++) {
			villagersOrganization.getProperty(Constants.DEITY_ATTRIBUTES).onTurn(world);
			world.nextTurn();
		}
		
		for(int i=0; i<400; i++) {
			deity.onTurn(world, new WorldStateChangedListeners());
			world.nextTurn(); 
		}
		assertEquals(true, berryBush.getProperty(Constants.CONDITIONS).hasCondition(Condition.WILTING_CONDITION));
	}
	
	@Test
	public void testGetReasonIndex() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(-1, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		assertEquals(0, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		assertEquals(2, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, null);
		performer.setProperty(Constants.FOOD, 0);
		world.getHistory().actionPerformed(new OperationInfo(performer, performer, Args.EMPTY, Actions.EAT_ACTION), new Turn());
		assertEquals(1, deity.getReasonIndex(performer, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
