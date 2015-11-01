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
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.Skill;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestLearnSkillUsingOrganizationConversation {

	private final LearnSkillUsingOrganizationConversation conversation = Conversations.LEARN_SKILLS_USING_ORGANIZATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes, I'll teach you about farmer skills", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.FARMING_SKILL, new Skill(20));
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "farmers", Professions.FARMER_PROFESSION, world);
		
		performer.setProperty(Constants.GROUP, new IdList().add(organization));
		target.setProperty(Constants.GROUP, new IdList().add(organization));
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("Can you teach me to improve my farmer skills as a fellow member of the farmers?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.setProperty(Constants.FARMING_SKILL, new Skill(20));
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		conversation.handleResponse(0, context);
		conversation.handleResponse(0, context);
		assertEquals(1, performer.getProperty(Constants.FARMING_SKILL).getLevel());
	}
	
	@Test
	public void testHandleResponse1() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		conversation.handleResponse(1, context);
		assertEquals(0, performer.getProperty(Constants.FARMING_SKILL).getLevel());
		assertEquals(-10, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-10, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.setProperty(Constants.FARMING_SKILL, new Skill(20));
		world.addWorldObject(performer);
		world.addWorldObject(target);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "farmers", Professions.FARMER_PROFESSION, world);
		
		performer.setProperty(Constants.GROUP, new IdList().add(organization));
		target.setProperty(Constants.GROUP, new IdList().add(organization));
		
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
		
		target.setProperty(Constants.FARMING_SKILL, new Skill(0));
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
	}
}
