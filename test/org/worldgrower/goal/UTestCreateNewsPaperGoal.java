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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class UTestCreateNewsPaperGoal {

	private CreateNewsPaperGoal goal = Goals.CREATE_NEWS_PAPER_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateGoalCreateNewsPaper() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		performer.setProperty(Constants.NAME, "performer");
		world.addWorldObject(performer);
		
		Integer performerId = performer.getProperty(Constants.ID);
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, new PropertyKnowledge(performerId, Constants.PROFESSION, Professions.FARMER_PROFESSION));
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, new PropertyKnowledge(performerId, Constants.DEITY, Deity.ARES));
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, new PropertyKnowledge(performerId, Constants.DEATH_REASON, ""));
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, new PropertyKnowledge(performerId, Constants.CHILD_BIRTH_ID, performerId));
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, new PropertyKnowledge(performerId, Constants.ORGANIZATION_LEADER_ID, performerId));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.PAPER.generate(1f), 10);
		
		assertEquals(Actions.CREATE_NEWS_PAPER_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateIsGoalMet() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer();
		
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