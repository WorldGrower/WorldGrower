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
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestSexAction {

	@Test
	public void testExecuteFemalePerformer() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.GENDER, "female");
		target.setProperty(Constants.GENDER, "male");
		
		assertEquals(null, performer.getProperty(Constants.PREGNANCY));
		Actions.SEX_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(0, performer.getProperty(Constants.PREGNANCY).intValue());
	}
	
	@Test
	public void testExecuteFemaleTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.GENDER, "male");
		target.setProperty(Constants.GENDER, "female");
		
		assertEquals(null, target.getProperty(Constants.PREGNANCY));
		Actions.SEX_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(0, target.getProperty(Constants.PREGNANCY).intValue());
	}
	
	@Test
	public void testIsValidTargetNoRelationships() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(true, Actions.SEX_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsValidTargetRelationships() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		assertEquals(false, Actions.SEX_ACTION.isValidTarget(performer, target, world));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		
		performer.setProperty(Constants.GROUP, new IdList().add(1));
		target.setProperty(Constants.GROUP, new IdList().add(1));
		
		assertEquals(true, Actions.SEX_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, Actions.SEX_ACTION.distance(performer, target, Args.EMPTY, world));
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