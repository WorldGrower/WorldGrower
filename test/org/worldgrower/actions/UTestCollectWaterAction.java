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
import org.worldgrower.generator.BuildingGenerator;

public class UTestCollectWaterAction {

	@Test
	public void testExecuteCollectWater() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		
		Actions.COLLECT_WATER_ACTION.execute(performer, well, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WATER));
	}
	
	//TODO: implement alcohol level
	/*
	@Test
	public void testExecuteCollectAlcohol() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		well.setProperty(Constants.ALCOHOL_LEVEL, 9000);
		
		Actions.COLLECT_WATER_ACTION.execute(performer, well, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.ALCOHOL_LEVEL).intValue());
	}
	*/
	@Test
	public void testExecuteCollectPoison() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		well.setProperty(Constants.POISON_DAMAGE, 10);
		
		Actions.COLLECT_WATER_ACTION.execute(performer, well, Args.EMPTY, world);
		
		assertEquals(10, performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.POISON_DAMAGE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		
		assertEquals(true, Actions.COLLECT_WATER_ACTION.isValidTarget(performer, well, world));
		assertEquals(false, Actions.COLLECT_WATER_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		
		assertEquals(0, Actions.COLLECT_WATER_ACTION.distance(performer, well, Args.EMPTY,  world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}