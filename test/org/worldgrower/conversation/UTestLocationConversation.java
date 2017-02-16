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
import static org.worldgrower.TestUtils.createWorldObject;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.conversation.LocationConversation.Direction;
import org.worldgrower.goal.Goals;

public class UTestLocationConversation {

	private LocationConversation conversation = Conversations.LOCATION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		subject.setProperty(Constants.NAME, "subject");
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 8);
		assertEquals("subject is north of here", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("subject is northeast of here", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 1);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Goals.COLLECT_WATER_GOAL);
		WorldObject subject = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 3);
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, null, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		WorldObject subject = TestUtils.createWorldObject(3, "subject");
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, subject, null);
		assertEquals(1, questions.size());
		assertEquals("Where is subject?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testGetPossibleSubjects() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(3));
		
		WorldObject subject = TestUtils.createWorldObject(3, "subject");
		world.addWorldObject(subject);
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(subject, 1000);
		
		List<WorldObject> subjects = conversation.getPossibleSubjects(performer, target, null, world);
		assertEquals(1, subjects.size());
		assertEquals(subject, subjects.get(0));
	}
	
	@Test
	public void testGetDirectionSouth() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(2, 10, 1, 1);
		
		assertEquals(Direction.SOUTH, LocationConversation.getDirection(performer, subject));
	}
	
	@Test
	public void testGetDirectionNorth() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(2, 0, 1, 1);
		
		assertEquals(Direction.NORTH, LocationConversation.getDirection(performer, subject));
	}
	
	@Test
	public void testGetDirectionWest() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(0, 2, 1, 1);
		
		assertEquals(Direction.WEST, LocationConversation.getDirection(performer, subject));
	}
	
	@Test
	public void testGetDirectionEast() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		WorldObject subject = createWorldObject(10, 2, 1, 1);
		
		assertEquals(Direction.EAST, LocationConversation.getDirection(performer, subject));
	}
}
