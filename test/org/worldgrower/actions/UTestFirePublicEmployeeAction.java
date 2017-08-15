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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.PlayerCharacterProfession;
import org.worldgrower.profession.Professions;

public class UTestFirePublicEmployeeAction {

	private FirePublicEmployeeAction action = Actions.FIRE_PUBLIC_EMPLOYEE_ACTION;
	
	@Test
	public void testExecuteSheriff() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.CAN_ATTACK_CRIMINALS, true);
		target.setProperty(Constants.PROFESSION, Professions.SHERIFF_PROFESSION);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).incrementValue(target, 500);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(null, target.getProperty(Constants.CAN_ATTACK_CRIMINALS));
		assertEquals(null, target.getProperty(Constants.PROFESSION));
		assertEquals(false, villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).contains(target));
	}
	
	@Test
	public void testExecuteTaxCollector() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.CAN_COLLECT_TAXES, true);
		target.setProperty(Constants.PROFESSION, Professions.TAX_COLLECTOR_PROFESSION);
		target.setProperty(Constants.PROFESSION_START_TURN, 34);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).incrementValue(target, 500);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(null, target.getProperty(Constants.CAN_COLLECT_TAXES));
		assertEquals(null, target.getProperty(Constants.PROFESSION));
		assertEquals(null, target.getProperty(Constants.PROFESSION_START_TURN));
		assertEquals(false, villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).contains(target));
	}
	
	@Test
	public void testExecutePlayerCharacterTaxCollector() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.CAN_COLLECT_TAXES, true);
		PlayerCharacterProfession profession = new PlayerCharacterProfession("description");
		target.setProperty(Constants.PROFESSION, profession);
		target.setProperty(Constants.PROFESSION_START_TURN, 34);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).incrementValue(target, 500);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(null, target.getProperty(Constants.CAN_COLLECT_TAXES));
		assertEquals(profession, target.getProperty(Constants.PROFESSION));
		assertEquals(null, target.getProperty(Constants.PROFESSION_START_TURN));
		assertEquals(false, villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).contains(target));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, 2);
		target.setProperty(Constants.CAN_ATTACK_CRIMINALS, true);
		
		assertEquals(false, action.isValidTarget(performer, performer, world));
		assertEquals(true, action.isValidTarget(performer, target, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}

	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(false, action.isActionPossible(performer, target, Args.EMPTY, world));
		
		target.setProperty(Constants.CAN_ATTACK_CRIMINALS, true);
		assertEquals(true, action.isActionPossible(performer, target, Args.EMPTY, world));
	}

	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, action.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}