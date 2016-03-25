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
import org.worldgrower.DungeonMaster;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.KnowledgeMap;

public class UTestSawDisguisingConversation {

	private final SawDisguisingConversation conversation = new SawDisguisingConversation();
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = createPerformerWithFacade(world);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		WorldObject facade = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, Constants.FACADE, facade);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(1, replyPhrases.size());
		assertEquals("A good try, performer, but I saw you disguise yourself earlier", replyPhrases.get(0).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrasesNull() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(0, replyPhrases.size());
	}

	private WorldObject createPerformerWithFacade(World world) {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject facade = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		performer.setProperty(Constants.NAME, "performer");
		facade.setProperty(Constants.NAME, "berry bush");
		world.addWorldObject(facade);
		performer.setProperty(Constants.FACADE, facade);
		return performer;
	}
	
	@Test
	public void testGetReplyPhraseNull() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(null, conversation.getReplyPhrase(context));
	}
	
	@Test
	public void testGetReplyPhraseMinusThree() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = createPerformerWithFacade(world);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		WorldObject facade = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, Constants.FACADE, facade);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(-3, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testHandleResponseMinusThree() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = createPerformerWithFacade(world);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		WorldObject facade = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(performer, Constants.FACADE, facade);

		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);		
		conversation.handleResponse(-3, context, Conversations.DEITY_CONVERSATION);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
