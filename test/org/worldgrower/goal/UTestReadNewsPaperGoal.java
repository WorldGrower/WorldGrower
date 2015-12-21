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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class UTestReadNewsPaperGoal {

	private ReadNewsPaperGoal goal = Goals.READ_NEWS_PAPER_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateGoalReadNewsPaper() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		performer.setProperty(Constants.NAME, "performer");
		world.addWorldObject(performer);
		
		List<Knowledge> knowledgeList = Arrays.asList(new PropertyKnowledge(performer.getProperty(Constants.ID), Constants.PROFESSION, Professions.FARMER_PROFESSION));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.generateNewsPaper(knowledgeList, new int[] {0}, world));
		
		assertEquals(Actions.READ_ITEM_IN_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateIsGoalMet() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.GOLD, 10);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.GOLD, 1000);
		performer.setProperty(Constants.NEWSPAPER_READ_TURN, 0);
		for(int i=0; i<1000; i++) { world.nextTurn(); }
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}