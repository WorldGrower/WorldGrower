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
package org.worldgrower.curse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestCurseListener {

	@Test
	public void testActionPerformedPerformedCursed() {
		World world = new WorldImpl(1, 1, null, null);
		CurseListener curseListener = new CurseListener(world);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		performer.setProperty(Constants.CURSE, Curse.POX_CURSE);

		curseListener.actionPerformed(Actions.TALK_ACTION, performer, target, Args.EMPTY, null);
		assertEquals(Curse.POX_CURSE, performer.getProperty(Constants.CURSE));
		assertEquals(null, target.getProperty(Constants.CURSE));
		
		curseListener.actionPerformed(Actions.SEX_ACTION, performer, target, Args.EMPTY, null);
		assertEquals(Curse.POX_CURSE, performer.getProperty(Constants.CURSE));
		assertEquals(Curse.POX_CURSE, target.getProperty(Constants.CURSE));
	}
	
	@Test
	public void testActionPerformedTargetCursed() {
		World world = new WorldImpl(1, 1, null, null);
		CurseListener curseListener = new CurseListener(world);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.CURSE, Curse.POX_CURSE);

		curseListener.actionPerformed(Actions.TALK_ACTION, performer, target, Args.EMPTY, null);
		assertEquals(null, performer.getProperty(Constants.CURSE));
		assertEquals(Curse.POX_CURSE, target.getProperty(Constants.CURSE));
		
		curseListener.actionPerformed(Actions.SEX_ACTION, performer, target, Args.EMPTY, null);
		assertEquals(Curse.POX_CURSE, performer.getProperty(Constants.CURSE));
		assertEquals(Curse.POX_CURSE, target.getProperty(Constants.CURSE));
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
