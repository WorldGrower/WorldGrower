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
package org.worldgrower.conversation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.EventKnowledge;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.history.Turn;
import org.worldgrower.profession.Professions;

public class UTestKnowledgeToDescriptionMapper {

	private final KnowledgeToDescriptionMapper mapper = new KnowledgeToDescriptionMapper();
	
	@Test
	public void testMapPropertyKnowledgeVampire() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		world.addWorldObject(performer);
		
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		PropertyKnowledge knowledge = new PropertyKnowledge(performer.getProperty(Constants.ID), Constants.CREATURE_TYPE, CreatureType.VAMPIRE_CREATURE_TYPE);
		
		assertEquals("Did you know that performer is a vampire?", mapper.getQuestionDescription(knowledge, world));
	}
	
	//TODO: better description
	@Test
	public void testMapEventKnowledgeMateConversation() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		world.addWorldObject(performer);
		
		EventKnowledge knowledge = new EventKnowledge(1, world);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROPOSE_MATE_CONVERSATION), Actions.TALK_ACTION), new Turn());
		
		assertEquals("Did you know that performer was talking about becoming a mate for someone?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgePoisonedWell() {
		World world = new WorldImpl(10, 10, null, null);
		int id = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject subject = world.findWorldObjectById(id);
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.POISON_DAMAGE, 10);
		assertEquals("Did you know that the well is poisoned?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeSleepingPotionInWell() {
		World world = new WorldImpl(10, 10, null, null);
		int id = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject subject = world.findWorldObjectById(id);
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.SLEEP_INDUCING_DRUG_STRENGTH, 10);
		assertEquals("Did you know that the well contains sleeping potion?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeChildBirth() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		world.addWorldObject(target);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.CHILD_BIRTH_ID, 2);
		assertEquals("Did you know that subject gave birth to target?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeDeathReason() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		String deathReason = "subject died by drowning";
		subject.setProperty(Constants.DEATH_REASON, deathReason);
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.DEATH_REASON, deathReason);
		assertEquals("Did you know that subject died by drowning?", mapper.getQuestionDescription(knowledge, world));
	}

	@Test
	public void testMapEventKnowledgeDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.DEITY, Deity.HADES);
		assertEquals("Did you know that subject worships Hades?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeNoDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.DEITY, null);
		assertEquals("Did you know that subject doesn't worship a deity?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeProfession() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.PROFESSION, Professions.FARMER_PROFESSION);
		assertEquals("Did you know that subject is a farmer?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeNoProfession() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.PROFESSION, null);
		assertEquals("Did you know that subject doesn't have a profession?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeStealing() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		world.addWorldObject(performer);
		
		EventKnowledge knowledge = new EventKnowledge(1, world);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.STEAL_ACTION), new Turn());
		
		assertEquals("Did you know that performer was stealing from target?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapOrganizationLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		WorldObject leader = TestUtils.createIntelligentWorldObject(4, Constants.NAME, "leader");
		world.addWorldObject(subject);
		world.addWorldObject(leader);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		assertEquals("Did you know that leader is the leader of the subject?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapOrganizationNoLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		WorldObject leader = TestUtils.createIntelligentWorldObject(4, Constants.NAME, "leader");
		world.addWorldObject(subject);
		world.addWorldObject(leader);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.ORGANIZATION_LEADER_ID, null);
		assertEquals("Did you know that subject doesn't have a leader?", mapper.getQuestionDescription(knowledge, world));
	}
	
	@Test
	public void testMapPropertyKnowledgeTrappedContainer() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		world.addWorldObject(performer);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(performer.getProperty(Constants.ID), Constants.TRAPPED_CONTAINER_DAMAGE, 5);
		
		assertEquals("Did you know that performer is magically trapped?", mapper.getQuestionDescription(knowledge, world));
	}
}
