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
import org.worldgrower.attribute.IdList;

public class UTestFamilyConversation {

	private final FamilyConversation conversation = Conversations.FAMILY_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.CHILDREN, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.CHILDREN, new IdList());

		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("Yes", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No, I don't have a mate and I don't have children", replyPhrases.get(1).getResponsePhrase());
	
		addChild(target, world, 3, "subject");
		
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("Yes, I don't have a mate and I have 1 child", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
		
		addMate(world, target, 4, "subject2");
		
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("Yes, I have a mate named subject2 and I have 1 child", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
		
		addChild(target, world, 5, "subject");
		
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("Yes, I have a mate named subject2 and I have 2 children", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
	}

	private void addMate(World world, WorldObject target, int id, String name) {
		WorldObject subject = TestUtils.createIntelligentWorldObject(id, Constants.NAME, name);
		world.addWorldObject(subject);
		target.setProperty(Constants.MATE_ID, subject.getProperty(Constants.ID));
	}

	private void addChild(WorldObject target, World world, int id, String name) {
		WorldObject subject = TestUtils.createIntelligentWorldObject(id, Constants.NAME, name);
		world.addWorldObject(subject);
		target.getProperty(Constants.CHILDREN).add(subject);
	}

	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.CHILDREN, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.CHILDREN, new IdList());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		target.getProperty(Constants.CHILDREN).add(subject);

		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.CHILDREN, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.CHILDREN, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Do you have a family?", questions.get(0).getQuestionPhrase());
	}
}
