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

public class UTestBuyHouseConversation {

	private final BuyHouseConversation conversation = Conversations.BUY_HOUSE_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("Yes", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList().add(3));
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GOLD, 100);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("I'd like to buy a house", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.HOUSES, new IdList().add(3));
		
		performer.setProperty(Constants.GOLD, 200);
		target.setProperty(Constants.GOLD, 0);
		
		WorldObject house = TestUtils.createWorldObject(3, "house");
		house.setProperty(Constants.PRICE, 100);
		house.setProperty(Constants.SELLABLE, Boolean.TRUE);
		world.addWorldObject(house);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(100, target.getProperty(Constants.GOLD).intValue());
		assertEquals(true, performer.getProperty(Constants.HOUSES).contains(3));
		assertEquals(false, target.getProperty(Constants.HOUSES).contains(3));
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.HOUSES, new IdList().add(3));
		
		
		WorldObject house = TestUtils.createWorldObject(3, "house");
		house.setProperty(Constants.PRICE, 100);
		house.setProperty(Constants.SELLABLE, Boolean.TRUE);
		world.addWorldObject(house);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
		
		target.getProperty(Constants.HOUSES).removeAll();
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
	}
}
