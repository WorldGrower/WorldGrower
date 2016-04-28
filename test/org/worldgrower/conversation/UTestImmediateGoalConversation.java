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
import org.worldgrower.ManagedOperation;
import org.worldgrower.MockMetaInformation;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.profession.Professions;

public class UTestImmediateGoalConversation {

	private final ImmediateGoalConversation conversation = Conversations.IMMEDIATE_GOAL_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		MockMetaInformation.setMetaInformation(target, Goals.BRAWL_GOAL);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("I'm attacking targetName because I'm brawling", replyPhrases.get(0).getResponsePhrase());
		assertEquals("I'm not doing anything", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrasesForSpecificActions() {
		assertReplyPhrase("I'm attacking targetName because I'm brawling", Goals.BRAWL_GOAL, Actions.MELEE_ATTACK_ACTION);
		assertReplyPhrase("I'm resting because I'm tired", Goals.REST_GOAL, Actions.REST_ACTION);
		assertReplyPhrase("I'm biting targetName because I'm thirsty for blood", Goals.VAMPIRE_BLOOD_LEVEL_GOAL, Actions.VAMPIRE_BITE_ACTION);
		assertReplyPhrase("I'm eating targetName because I'm hungry and looking for food", Goals.FOOD_GOAL, Actions.EAT_ACTION);
		assertReplyPhrase("I'm cutting down the targetName because I'm looking to own a place to rest", Goals.SHACK_GOAL, Actions.CUT_WOOD_ACTION);
		assertReplyPhrase("I'm building a shack because I'm looking to own a place to rest", Goals.SHACK_GOAL, Actions.BUILD_SHACK_ACTION);
		assertReplyPhrase("I'm drinking from targetName because I'm thirsty and looking for water", Goals.DRINK_WATER_GOAL, Actions.DRINK_ACTION);
		assertReplyPhrase("I'm having sex with targetName because I'm looking to have children", Goals.CHILDREN_GOAL, Actions.SEX_ACTION);
		assertReplyPhrase("I'm moving because I'm avoiding danger", Goals.PROTECT_ONSE_SELF_GOAL, Actions.MOVE_ACTION);
		assertReplyPhrase("I'm attacking targetName because I'm getting revenge", Goals.REVENGE_GOAL, Actions.FIRE_BOLT_ATTACK_ACTION);
		assertReplyPhrase("I'm planting a grape vine because I'm planting grape vines", Goals.CREATE_WINE_GOAL, Actions.PLANT_GRAPE_VINE_ACTION);
		assertReplyPhrase("I'm animating targetName because I'm looking to have a mate", Goals.MATE_GOAL, Actions.ANIMATE_DEAD_ACTION);
		assertReplyPhrase("I'm mining targetName because I'm looking for gold", Goals.MINE_GOLD_GOAL, Actions.MINE_GOLD_ACTION);
		assertReplyPhrase("I'm constructing fishing pole because I'm looking to gather fish", Goals.CATCH_FISH_GOAL, Actions.CONSTRUCT_FISHING_POLE_ACTION);
		assertReplyPhrase("I'm studying necromancy because I'm becoming a lich", Goals.BECOME_LICH_GOAL, Actions.RESEARCH_NECROMANCY_SKILL_ACTION);
		assertReplyPhrase("I'm crafting iron boots because I'm looking to get equipment", Goals.CRAFT_EQUIPMENT_GOAL, Actions.CRAFT_IRON_BOOTS_ACTION);
		assertReplyPhrase("I'm brewing poison because I'm looking to breakup with a mate", Goals.BREAKUP_WITH_MATE_GOAL, Actions.BREW_POISON_ACTION);
		assertReplyPhrase("I'm attacking targetName in a non lethal manner because I'm brawling", Goals.BRAWL_GOAL, Actions.NON_LETHAL_MELEE_ATTACK_ACTION);
		assertReplyPhrase("I'm becoming a leader candidate because I'm collecting a pay check", Goals.COLLECT_PAY_CHECK_GOAL, Actions.BECOME_LEADER_CANDIDATE_ACTION);
		assertReplyPhrase("I'm voting for a leader because I'm collecting taxes", Goals.COLLECT_TAXES_GOAL, Actions.VOTE_FOR_LEADER_ACTION);
		assertReplyPhrase("I'm creating a religion organization because I'm looking for an religion organization to belong to", Goals.BECOME_RELIGION_ORGANIZATION_MEMBER_GOAL, Actions.CREATE_RELIGION_ORGANIZATION_ACTION);
		assertReplyPhrase("I'm creating a profession organization because I'm looking for an profession organization to belong to", Goals.BECOME_PROFESSION_ORGANIZATION_MEMBER_GOAL, Actions.CREATE_PROFESSION_ORGANIZATION_ACTION);
		assertReplyPhrase("I'm choosing a deity because I'm looking for a deity to worship", Goals.CHOOSE_DEITY_GOAL, Actions.CHOOSE_DEITY_ACTION);
		assertReplyPhrase("I'm choosing a profession because I'm looking for a profession", Goals.CHOOSE_PROFESSION_GOAL, Actions.CHOOSE_PROFESSION_ACTION);
		assertReplyPhrase("I'm planting cotton plant because I'm looking for cotton", Goals.COTTON_GOAL, Actions.PLANT_COTTON_PLANT_ACTION);
		assertReplyPhrase("I'm voting for a leader because I'm concerned about the organization leadership", Goals.ORGANIZATION_VOTE_GOAL, Actions.VOTE_FOR_LEADER_ACTION);
		assertReplyPhrase("I'm becoming a leader candidate because I'm concerned about the organization leadership", Goals.ORGANIZATION_CANDIDATE_GOAL, Actions.BECOME_LEADER_CANDIDATE_ACTION);
		assertReplyPhrase("I'm starting an organization vote because I'm looking for a new leader for an organization", Goals.START_ORGANIZATION_VOTE_GOAL, Actions.START_ORGANIZATION_VOTE_ACTION);
		assertReplyPhrase("I'm creating a newspaper because I'm recruiting new profession organization members", Goals.RECRUIT_PROFESSION_ORGANIZATION_MEMBERS_GOAL, Actions.CREATE_NEWS_PAPER_ACTION);
		assertReplyPhrase("I'm equiping an inventory item because I'm looking to brawl", Goals.START_BRAWL_GOAL, Actions.EQUIP_INVENTORY_ITEM_ACTION);
		assertReplyPhrase("I'm crafting a repair hammer because I'm repairing equipment", Goals.REPAIR_EQUIPMENT_GOAL, Actions.CRAFT_REPAIR_HAMMER_ACTION);
		assertReplyPhrase("I'm poisoning inventory water because I'm creating poison", Goals.CREATE_POISON_GOAL, Actions.POISON_INVENTORY_WATER_ACTION);
		assertReplyPhrase("I'm weaving cotton boots because I'm looking to weave clothes", Goals.WEAVE_CLOTHES_GOAL, Actions.WEAVE_COTTON_BOOTS_ACTION);
		assertReplyPhrase("I'm creating paper because I'm looking to have paper", Goals.CREATE_PAPER_GOAL, Actions.CREATE_PAPER_ACTION);
		assertReplyPhrase("I'm stealing from targetName because I'm in need of gold", Goals.STEAL_GOAL, Actions.STEAL_ACTION);
		assertReplyPhrase("I'm talking because I'm interested in hearing what you have to say", Goals.STAND_STILL_TO_TALK_GOAL, Actions.STAND_STILL_TO_TALK_ACTION);
	}
	
	private void assertReplyPhrase(String replyPhrase, Goal goal, ManagedOperation action) {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		MockMetaInformation.setMetaInformation(target, goal, action);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(replyPhrase, replyPhrases.get(0).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhraseThief() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.PROFESSION, Professions.THIEF_PROFESSION);

		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		target.setProperty(Constants.FACADE, TestUtils.createWorldObject(2, "facade"));
		MockMetaInformation.setMetaInformation(target, Goals.STEAL_GOAL, Actions.STEAL_ACTION);
		target.getProperty(Constants.BLUFF_SKILL).use(100, target, Constants.BLUFF_SKILL, new WorldStateChangedListeners());
		target = FacadeUtils.createFacade(target, target, performer, world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I'm eating null because I'm hungry and looking for food", replyPhrases.get(0).getResponsePhrase());
	}

	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		MockMetaInformation.setMetaInformation(target, Goals.BRAWL_GOAL);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		MockMetaInformation.setMetaInformation(target, null);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("What are you doing?", questions.get(0).getQuestionPhrase());
	}
}
