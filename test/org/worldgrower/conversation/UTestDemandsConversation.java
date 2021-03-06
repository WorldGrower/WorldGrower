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
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Demands;

public class UTestDemandsConversation {

	private DemandsConversation conversation = new DemandsConversation();
	
	@Test
	public void testGetReplyPhrase() {
		Demands demands = new Demands();
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.DEMANDS, demands);
		ConversationContext conversationContext = new ConversationContext(null, target, null, null, null, 0);
		Response response = conversation.getReplyPhrase(conversationContext);
		assertEquals(1, response.getId());
		
		demands.add(Constants.FOOD, 1);
		response = conversation.getReplyPhrase(conversationContext);
		assertEquals(0, response.getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		Demands demands = new Demands();
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.DEMANDS, demands);
		List<Question> questions = conversation.getQuestionPhrases(null, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("What would you like to buy?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesNoDemands() {
		List<Response> replyPhrases = getReplyPhrases(new Demands());
		assertEquals(2, replyPhrases.size());
		assertEquals("I'd like to buy ", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I'm not looking for anything to buy right now", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesOneDemand() {
		Demands demands = new Demands();
		demands.add(Constants.FOOD, 1);
		List<Response> replyPhrases = getReplyPhrases(demands);
		assertEquals("I'd like to buy food", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesTwoDemands() {
		Demands demands = new Demands();
		demands.add(Constants.FOOD, 1);
		demands.add(Constants.WATER, 1);
		List<Response> replyPhrases = getReplyPhrases(demands);
		assertEquals("I'd like to buy food and water", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrasesThreeDemands() {
		Demands demands = new Demands();
		demands.add(Constants.FOOD, 1);
		demands.add(Constants.WATER, 1);
		demands.add(Constants.WINE, 1);
		List<Response> replyPhrases = getReplyPhrases(demands);
		assertEquals("I'd like to buy food, water and wine", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	private List<Response> getReplyPhrases(Demands demands) {
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.DEMANDS, demands);
		ConversationContext conversationContext = new ConversationContext(null, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(conversationContext);
		return replyPhrases;
	}
}
