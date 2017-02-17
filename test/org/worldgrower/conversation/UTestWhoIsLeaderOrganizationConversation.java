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
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.Turn;

public class UTestWhoIsLeaderOrganizationConversation {

	private final WhoIsLeaderOrganizationConversation conversation = Conversations.WHO_IS_LEADER_ORGANIZATION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(3, replyPhrases.size());
		assertEquals("OrgName has no leader at the moment", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("That's none of your business", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesPerformerLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(performer.getProperty(Constants.ID), "OrgName", world);
		world.addWorldObject(performer);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("You are the leader of the OrgName", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesTargetLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(target.getProperty(Constants.ID), "OrgName", world);
		world.addWorldObject(target);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I'm the leader of the OrgName", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject leader = TestUtils.createIntelligentWorldObject(3, Constants.RELATIONSHIPS, new IdRelationshipMap());
		leader.setProperty(Constants.NAME, "leader");
		WorldObject organization = GroupPropertyUtils.create(leader.getProperty(Constants.ID), "OrgName", world);
		world.addWorldObject(leader);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("leader is the leader of the OrgName", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 4000);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Conversations.createArgs(conversation, organization), Actions.TALK_ACTION), new Turn());
		assertEquals(2, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("Who leads the OrgName ?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(false, performer.getProperty(Constants.RELATIONSHIPS).contains(target));
		
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, 2);
		conversation.handleResponse(0, context);
		assertEquals(true, performer.getProperty(Constants.RELATIONSHIPS).contains(target));
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-10, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
