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

public class UTestLookTheSameConversation {

	private final LookTheSameConversation conversation = new LookTheSameConversation();
	
	@Test
	public void testGetReplyPhrases() {
		World world = createWorld();
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject performer = createPerformerFacade(target, world);
		
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(1, replyPhrases.size());
		assertEquals("You look exactly like me. Who or what are you?", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}

	private WorldImpl createWorld() {
		int worldDimension = 1;
		return new WorldImpl(worldDimension, worldDimension, new DungeonMaster(worldDimension, worldDimension), null);
	}
	
	@Test
	public void testGetReplyPhrasesNull() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(0, replyPhrases.size());
	}

	private WorldObject createPerformerFacade(WorldObject target, World world) {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject facade = target;
		performer.setProperty(Constants.FACADE, facade);
		performer = FacadeUtils.createFacadeForSelf(performer);
		return performer;
	}
	
	@Test
	public void testGetReplyPhraseNull() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(null, conversation.getReplyPhrase(context));
	}
	
	@Test
	public void testGetReplyPhraseMinusOne() {
		World world = createWorld();
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject performer = createPerformerFacade(target, world);
		
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(-1, conversation.getReplyPhrase(context).getId());
	}
}
