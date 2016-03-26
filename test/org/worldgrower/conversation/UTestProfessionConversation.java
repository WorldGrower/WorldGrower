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
import org.worldgrower.actions.ChooseProfessionAction;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.profession.Professions;

public class UTestProfessionConversation {

	private final ProfessionConversation conversation = Conversations.PROFESSION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(4, replyPhrases.size());
		assertEquals("I'm a farmer", replyPhrases.get(0).getResponsePhrase());
		assertEquals("I don't have a profession", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	
		target.setProperty(Constants.PROFESSION, null);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("What is your profession?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testGetReplyPhrasesForThief() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.THIEF_PROFESSION);

		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		ChooseProfessionAction.createThiefFacade(target);
		target.getProperty(Constants.BLUFF_SKILL).use(100, target, Constants.BLUFF_SKILL, new WorldStateChangedListeners());
		target = FacadeUtils.createFacade(target, target, performer, world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I'm a farmer", replyPhrases.get(0).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrasesForNecromancer() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.NECROMANCER_PROFESSION);

		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		ChooseProfessionAction.createNecromancerFacade(target);
		target.getProperty(Constants.BLUFF_SKILL).use(100, target, Constants.BLUFF_SKILL, new WorldStateChangedListeners());
		target = FacadeUtils.createFacade(target, target, performer, world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I'm a wizard", replyPhrases.get(0).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrasesForAssassin() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.NECROMANCER_PROFESSION);

		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		ChooseProfessionAction.createAssassinFacade(target);
		target.getProperty(Constants.BLUFF_SKILL).use(100, target, Constants.BLUFF_SKILL, new WorldStateChangedListeners());
		target = FacadeUtils.createFacade(target, target, performer, world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I'm a carpenter", replyPhrases.get(0).getResponsePhrase());
	}
}
