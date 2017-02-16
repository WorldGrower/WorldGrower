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
import org.worldgrower.DungeonMaster;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.FacadeUtils;

public class UTestWhyNotIntelligentConversation {

	private final WhyNotIntelligentConversation conversation = new WhyNotIntelligentConversation();
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = createPerformerFacade(world);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("What are you? Why am I talking with a berry bush?", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("A good try, berry bush, but I see through your disguise", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}

	private WorldObject createPerformerFacade(World world) {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject facade = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		facade.setProperty(Constants.NAME, "berry bush");
		world.addWorldObject(facade);
		performer.setProperty(Constants.FACADE, facade);
		performer = FacadeUtils.createFacadeForSelf(performer);
		return performer;
	}
	
	@Test
	public void testGetReplyPhraseNull() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(null, conversation.getReplyPhrase(context));
	}
	
	@Test
	public void testGetReplyPhraseMinusOne() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = createPerformerFacade(world);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(-1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhraseMinusTwo() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject facade = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		world.addWorldObject(facade);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.FACADE, facade);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(-2, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testHandleResponseMinusTwo() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = createPerformerFacade(world);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());

		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);		
		conversation.handleResponse(-2, context, Conversations.DEITY_CONVERSATION);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
