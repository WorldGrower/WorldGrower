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
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.EventKnowledge;
import org.worldgrower.attribute.PropertyKnowledge;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.history.Turn;
import org.worldgrower.profession.Professions;

public class UTestKnowledgeToDescriptionMapper {

	private final KnowledgeToDescriptionMapper mapper = new KnowledgeToDescriptionMapper();
	
	@Test
	public void testMapEventKnowledgeVampireBite() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		world.addWorldObject(performer);
		
		EventKnowledge knowledge = new EventKnowledge(1, world);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, new int[0], Actions.VAMPIRE_BITE_ACTION), new Turn());
		
		assertEquals("Did you know performer was biting target?", mapper.getDescription(knowledge, world));
	}
	
	//TODO: better description
	@Test
	public void testMapEventKnowledgeMateConversation() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		world.addWorldObject(performer);
		
		EventKnowledge knowledge = new EventKnowledge(1, world);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROPOSE_MATE_CONVERSATION), Actions.TALK_ACTION), new Turn());
		
		assertEquals("Did you know performer was talking about becoming a mate for someone?", mapper.getDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgePoisonedWell() {
		World world = new WorldImpl(0, 0, null, null);
		int id = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject subject = world.findWorldObject(Constants.ID, id);
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.POISON_DAMAGE, 10);
		assertEquals("Did you know the well is poisoned?", mapper.getDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeChildBirth() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		world.addWorldObject(target);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.CHILD_BIRTH_ID, 2);
		assertEquals("Did you know that subject gave birth to target?", mapper.getDescription(knowledge, world));
	}

	@Test
	public void testMapEventKnowledgeDeity() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.DEITY, Deity.HADES);
		assertEquals("Did you know subject worships Hades?", mapper.getDescription(knowledge, world));
	}
	
	@Test
	public void testMapEventKnowledgeProfession() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.PROFESSION, Professions.FARMER_PROFESSION);
		assertEquals("Did you know subject is a farmer?", mapper.getDescription(knowledge, world));
	}
	
	@Test
	public void testMapOrganizationLeader() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		WorldObject leader = TestUtils.createIntelligentWorldObject(4, Constants.NAME, "leader");
		world.addWorldObject(subject);
		world.addWorldObject(leader);
		
		PropertyKnowledge knowledge = new PropertyKnowledge(subject.getProperty(Constants.ID), Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		assertEquals("Did you know that leader is the leader of the subject?", mapper.getDescription(knowledge, world));
	}
}
