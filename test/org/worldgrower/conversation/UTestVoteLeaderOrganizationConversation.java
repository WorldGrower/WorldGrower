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

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestVoteLeaderOrganizationConversation {

	private final VoteLeaderOrganizationConversation conversation = Conversations.VOTE_LEADER_ORGANIZATION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.RELATIONSHIPS, new IdRelationshipMap());
		createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(1, replyPhrases.size());
		assertEquals("Let's put it to a vote. From now for " + VotingPropertyUtils.getNumberOfTurnsCandidatesMayBeProposed(world) + " turns anyone can become a candidate for leader, and after that voting starts.", replyPhrases.get(0).getResponsePhrase());
	}
	
	private WorldObject createVillagersOrganization(World world) {
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId(); 
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.RELATIONSHIPS, new IdRelationshipMap());
		createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(0));
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("I want to vote on leadership for the OrgName", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 1);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 2);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(4, world.getWorldObjects().size());		
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
		
		performer.getProperty(Constants.GROUP).add(0);
		target.getProperty(Constants.GROUP).add(0);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
	}
}
