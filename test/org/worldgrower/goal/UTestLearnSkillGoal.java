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
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.Skill;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.profession.Professions;

public class UTestLearnSkillGoal {

	private LearnSkillGoal goal = Goals.LEARN_SKILL_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalTalkAboutLearning() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		WorldObject target = createPerformer();
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		performer.getProperty(Constants.GROUP).add(organization);
		target.getProperty(Constants.GROUP).add(organization);
		
		target.setProperty(target.getProperty(Constants.PROFESSION).getSkillProperty(), new Skill(20));
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.LEARN_SKILLS_USING_ORGANIZATION);
	}
	
	@Test
	public void testCalculateGoalMemberOfOtherOrganization() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		WorldObject target = createPerformer();
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject organization2 = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		performer.getProperty(Constants.GROUP).add(organization);
		target.getProperty(Constants.GROUP).add(organization2);
		
		target.setProperty(target.getProperty(Constants.PROFESSION).getSkillProperty(), new Skill(20));
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalNotAMember() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		WorldObject target = createPerformer();
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		target.setProperty(target.getProperty(Constants.PROFESSION).getSkillProperty(), new Skill(20));
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(performer.getProperty(Constants.PROFESSION).getSkillProperty(), new Skill(20));
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.GROUP, new IdList());
		return performer;
	}
}