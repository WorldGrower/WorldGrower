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
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;

public class UTestStartArenaFightConversation {

	private final StartArenaFightConversation conversation = Conversations.START_ARENA_FIGHT_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 3);
		assertEquals("Yes, you can start right away", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No, there is already another fight", replyPhrases.get(1).getResponsePhrase());
		assertEquals("Yes, but you'll have to wait until an opponent comes forth", replyPhrases.get(2).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_IDS, new IdList());
		
		target.setProperty(Constants.ARENA_FIGHTER_IDS, new IdList());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(2, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.ARENA_FIGHTER_IDS).add(3);
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.GOLD, 0);
		world.addWorldObject(subject);
		subject.setProperty(Constants.ARENA_OPPONENT_ID, -1);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("I would like to fight in the arena. Can I fight?", questions.get(0).getQuestionPhrase());
	}
}
