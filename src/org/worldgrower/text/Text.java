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
package org.worldgrower.text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.IntimidateConversation;

public enum Text {
	QUESTION_ARENA_PAY_CHECK(Conversations.ARENA_FIGHTER_PAY_CHECK_CONVERSATION),
	ANSWER_ARENA_PAY_CHECK_YES(Conversations.ARENA_FIGHTER_PAY_CHECK_CONVERSATION),
	ANSWER_ARENA_PAY_CHECK_NO(Conversations.ARENA_FIGHTER_PAY_CHECK_CONVERSATION),
	QUESTION_ASK_GOAL(Conversations.ASK_GOAL_CONVERSATION), 
	ANSWER_ASK_GOAL_YES(Conversations.ASK_GOAL_CONVERSATION), 
	ANSWER_ASK_GOAL_EXPLAIN(Conversations.ASK_GOAL_CONVERSATION),
	ANSWER_ASK_GOAL_NO(Conversations.ASK_GOAL_CONVERSATION),
	QUESTION_ASSASSINATE_TARGET(Conversations.ASSASSINATE_TARGET_CONVERSATION),
	ANSWER_ASSASSINATE_TARGET_YES(Conversations.ASSASSINATE_TARGET_CONVERSATION),
	ANSWER_ASSASSINATE_TARGET_NO(Conversations.ASSASSINATE_TARGET_CONVERSATION),
	QUESTION_ARENA_FIGHTER(Conversations.BECOME_ARENA_FIGHTER_CONVERSATION),
	ANSWER_ARENA_FIGHTER_YES(Conversations.BECOME_ARENA_FIGHTER_CONVERSATION),
	ANSWER_ARENA_FIGHTER_NO(Conversations.BECOME_ARENA_FIGHTER_CONVERSATION),
	QUESTION_BRAWL_GOLD(Conversations.BRAWL_CONVERSATION),
	QUESTION_BRAWL(Conversations.BRAWL_CONVERSATION),
	ANSWER_BRAWL_YES(Conversations.BRAWL_CONVERSATION),
	ANSWER_BRAWL_NO(Conversations.BRAWL_CONVERSATION),
	ANSWER_BRAWL_LATER(Conversations.BRAWL_CONVERSATION),
	ANSWER_BRAWL_NOT_ENOUGH_GOLD(Conversations.BRAWL_CONVERSATION),
	ANSWER_BRAWL_GET_LOST(Conversations.BRAWL_CONVERSATION),
	QUESTION_BREAKUP(Conversations.BREAKUP_WITH_MATE_CONVERSATION),
	ANSWER_BREAKUP_YES(Conversations.BREAKUP_WITH_MATE_CONVERSATION),
	QUESTION_BUY_BUILDING(Conversations.BUY_HOUSE_CONVERSATION),
	ANSWER_BUY_BUILDING_YES(Conversations.BUY_HOUSE_CONVERSATION),
	ANSWER_BUY_BUILDING_NO(Conversations.BUY_HOUSE_CONVERSATION),
	QUESTION_COLLECT_BOUNTY(Conversations.COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION),
	ANSWER_COLLECT_BOUNTY_PAY(Conversations.COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION),
	ANSWER_COLLECT_BOUNTY_JAIL(Conversations.COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION),
	ANSWER_COLLECT_BOUNTY_RESIST(Conversations.COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION),
	QUESTION_COLLECT_PAYCHECK(Conversations.COLLECT_PAY_CHECK_CONVERSATION),
	ANSWER_COLLECT_PAYCHECK_YES(Conversations.COLLECT_PAY_CHECK_CONVERSATION),
	ANSWER_COLLECT_PAYCHECK_NO(Conversations.COLLECT_PAY_CHECK_CONVERSATION),
	QUESTION_COLLECT_TAXES(Conversations.COLLECT_TAXES_CONVERSATION),
	ANSWER_COLLECT_TAXES_YES(Conversations.COLLECT_TAXES_CONVERSATION),
	ANSWER_COLLECT_TAXES_NO(Conversations.COLLECT_TAXES_CONVERSATION),
	QUESTION_COMPLIMENT_STRONG(Conversations.COMPLIMENT_CONVERSATION),
	QUESTION_COMPLIMENT_HANDSOME(Conversations.COMPLIMENT_CONVERSATION),
	ANSWER_COMPLIMENT_THANKS(Conversations.COMPLIMENT_CONVERSATION),
	ANSWER_COMPLIMENT_STOP(Conversations.COMPLIMENT_CONVERSATION),
	ANSWER_COMPLIMENT_GETLOST(Conversations.COMPLIMENT_CONVERSATION),
	QUESTION_CURE_DISEASE(Conversations.CURE_DISEASE_CONVERSATION),
	ANSWER_CURE_DISEASE_YES(Conversations.CURE_DISEASE_CONVERSATION),
	ANSWER_CURE_DISEASE_NO(Conversations.CURE_DISEASE_CONVERSATION),
	ANSWER_CURE_DISEASE_GETLOST(Conversations.CURE_DISEASE_CONVERSATION),
	QUESTION_CURE_POISON(Conversations.CURE_POISON_CONVERSATION),
	ANSWER_CURE_POISON_YES(Conversations.CURE_POISON_CONVERSATION),
	ANSWER_CURE_POISON_NO(Conversations.CURE_POISON_CONVERSATION),
	ANSWER_CURE_POISON_GETLOST(Conversations.CURE_POISON_CONVERSATION),
	QUESTION_DEITY(Conversations.DEITY_CONVERSATION),
	ANSWER_DEITY_WORSHIP(Conversations.DEITY_CONVERSATION),
	ANSWER_DEITY_DONT_WORSHIP(Conversations.DEITY_CONVERSATION),
	ANSWER_DEITY_ALREADY(Conversations.DEITY_CONVERSATION),
	ANSWER_DEITY_CHANGED(Conversations.DEITY_CONVERSATION),
	QUESTION_DEITY_EXPLANATION(Conversations.DEITY_EXPLANATION_CONVERSATION),
	ANSWER_DEITY_EXPLANATION_YES(Conversations.DEITY_EXPLANATION_CONVERSATION),
	ANSWER_DEITY_EXPLANATION_NO(Conversations.DEITY_EXPLANATION_CONVERSATION),
	ANSWER_DEITY_EXPLANATION_GETLOST(Conversations.DEITY_EXPLANATION_CONVERSATION),
	QUESTION_DEITY_FOLLOWERS(Conversations.DEITY_FOLLOWERS_CONVERSATION),
	ANSWER_DEITY_FOLLOWERS_YES(Conversations.DEITY_FOLLOWERS_CONVERSATION),
	ANSWER_DEITY_FOLLOWERS_NO(Conversations.DEITY_FOLLOWERS_CONVERSATION),
	QUESTION_DEITY_REASON(Conversations.DEITY_REASON_CONVERSATION),
	ANSWER_DEITY_REASON(Conversations.DEITY_REASON_CONVERSATION),
	ANSWER_DEITY_REASON_DONT_CARE(Conversations.DEITY_REASON_CONVERSATION),
	QUESTION_DEMAND_MONEY(Conversations.DEMAND_MONEY_CONVERSATION),
	ANSWER_DEMAND_MONEY_GETLOST(Conversations.DEMAND_MONEY_CONVERSATION),
	ANSWER_DEMAND_MONEY_SURE(Conversations.DEMAND_MONEY_CONVERSATION),
	ANSWER_DEMAND_MONEY_NO(Conversations.DEMAND_MONEY_CONVERSATION),
	ANSWER_DEMAND_MONEY_CAN_ONLY(Conversations.DEMAND_MONEY_CONVERSATION),
	QUESTION_DRINKING_CONTEST_GOLD(Conversations.DRINKING_CONTEST_CONVERSATION),
	QUESTION_DRINKING_CONTEST(Conversations.DRINKING_CONTEST_CONVERSATION),
	ANSWER_DRINKING_CONTEST_YES(Conversations.DRINKING_CONTEST_CONVERSATION),
	ANSWER_DRINKING_CONTEST_NO(Conversations.DRINKING_CONTEST_CONVERSATION),
	ANSWER_DRINKING_CONTEST_LATER(Conversations.DRINKING_CONTEST_CONVERSATION),
	ANSWER_DRINKING_CONTEST_NOGOLD(Conversations.DRINKING_CONTEST_CONVERSATION),
	QUESTION_CURSE(Conversations.EXPLAIN_CURSE_CONVERSATION),
	ANSWER_CURSE_YES(Conversations.EXPLAIN_CURSE_CONVERSATION),
	ANSWER_CURSE_NO(Conversations.EXPLAIN_CURSE_CONVERSATION),
	QUESTION_FAMILY(Conversations.FAMILY_CONVERSATION),
	ANSWER_FAMILY_YES(Conversations.FAMILY_CONVERSATION),
	ANSWER_FAMILY_NO(Conversations.FAMILY_CONVERSATION),
	QUESTION_GIVE_ITEM(Conversations.GIVE_COTTON_CONVERSATION),
	ANSWER_GIVE_ITEM_THANKS(Conversations.GIVE_COTTON_CONVERSATION),
	ANSWER_GIVE_ITEM_GETLOST(Conversations.GIVE_COTTON_CONVERSATION),
	ANSWER_GIVE_ITEM_AGAIN(Conversations.GIVE_COTTON_CONVERSATION),
	QUESTION_GIVE_MONEY(Conversations.GIVE_MONEY_CONVERSATION),
	ANSWER_GIVE_MONEY_THANKS(Conversations.GIVE_MONEY_CONVERSATION),
	ANSWER_GIVE_MONEY_GETLOST(Conversations.GIVE_MONEY_CONVERSATION),
	ANSWER_GIVE_MONEY_AGAIN(Conversations.GIVE_MONEY_CONVERSATION),
	ANSWER_GIVE_MONEY_BRIBE(Conversations.GIVE_MONEY_CONVERSATION),
	QUESTION_GOAL(Conversations.GOAL_CONVERSATION),
	ANSWER_GOAL_YES(Conversations.GOAL_CONVERSATION),
	ANSWER_GOAL_NO(Conversations.GOAL_CONVERSATION),
	QUESTION_IMMEDIATE_GOAL(Conversations.IMMEDIATE_GOAL_CONVERSATION),
	ANSWER_IMMEDIATE_GOAL_YES(Conversations.IMMEDIATE_GOAL_CONVERSATION),
	ANSWER_IMMEDIATE_GOAL_NO(Conversations.IMMEDIATE_GOAL_CONVERSATION),
	QUESTION_INTIMIDATE(Conversations.getIntimidateConversation()),
	ANSWER_INTIMIDATE_GETLOST(Conversations.getIntimidateConversation()),
	ANSWER_INTIMIDATE_COMPLY(Conversations.getIntimidateConversation()),
	QUESTION_JOIN_PERFORMER_ORG(Conversations.JOIN_PERFORMER_ORGANIZATION_CONVERSATION),
	ANSWER_JOIN_PERFORMER_ORG_YES(Conversations.JOIN_PERFORMER_ORGANIZATION_CONVERSATION),
	ANSWER_JOIN_PERFORMER_ORG_NO(Conversations.JOIN_PERFORMER_ORGANIZATION_CONVERSATION),
	QUESTION_JOIN_TARGET_ORG(Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION),
	ANSWER_JOIN_TARGET_ORG_YES(Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION),
	ANSWER_JOIN_TARGET_ORG_NO(Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION),
	QUESTION_KISS(Conversations.KISS_CONVERSATION),
	ANSWER_KISS_YES(Conversations.KISS_CONVERSATION),
	ANSWER_KISS_NO(Conversations.KISS_CONVERSATION),
	ANSWER_KISS_SAME(Conversations.KISS_CONVERSATION),
	ANSWER_KISS_DIFFERENT(Conversations.KISS_CONVERSATION),
	QUESTION_LEARN_SKILL(Conversations.LEARN_SKILLS_USING_ORGANIZATION),
	ANSWER_LEARN_SKILL_YES(Conversations.LEARN_SKILLS_USING_ORGANIZATION),
	ANSWER_LEARN_SKILL_NO(Conversations.LEARN_SKILLS_USING_ORGANIZATION),
	QUESTION_LOCATION(Conversations.LOCATION_CONVERSATION),
	ANSWER_LOCATION(Conversations.LOCATION_CONVERSATION),
	QUESTION_MERGE_ORG(Conversations.MERGE_ORGANIZATIONS_CONVERSATION),
	ANSWER_MERGE_ORG_YES(Conversations.MERGE_ORGANIZATIONS_CONVERSATION),
	ANSWER_MERGE_ORG_NO(Conversations.MERGE_ORGANIZATIONS_CONVERSATION),
	QUESTION_MINOR_HEAL(Conversations.MINOR_HEAL_CONVERSATION),
	ANSWER_MINOR_HEAL_YES(Conversations.MINOR_HEAL_CONVERSATION),
	ANSWER_MINOR_HEAL_NO(Conversations.MINOR_HEAL_CONVERSATION),
	ANSWER_MINOR_HEAL_GETLOST(Conversations.MINOR_HEAL_CONVERSATION),
	QUESTION_NAME(Conversations.NAME_CONVERSATION),
	ANSWER_NAME(Conversations.NAME_CONVERSATION),
	ANSWER_NAME_GETLOST(Conversations.NAME_CONVERSATION),
	ANSWER_NAME_TOLD_WHILE(Conversations.NAME_CONVERSATION),
	ANSWER_NAME_LIKE(Conversations.NAME_CONVERSATION),
	ANSWER_NAME_DIDNT(Conversations.NAME_CONVERSATION),
	QUESTION_NICE(Conversations.NICER_CONVERSATION),
	QUESTION_NOT_NICE(Conversations.NICER_CONVERSATION),
	ANSWER_NICE_YES(Conversations.NICER_CONVERSATION),
	ANSWER_NICE_NO(Conversations.NICER_CONVERSATION),
	ANSWER_NICE_SAME(Conversations.NICER_CONVERSATION),
	ANSWER_NICE_DIFFERENT(Conversations.NICER_CONVERSATION),
	QUESTION_ORG(Conversations.ORGANIZATION_CONVERSATION),
	ANSWER_ORG_GROUP(Conversations.ORGANIZATION_CONVERSATION),
	ANSWER_ORG_NO_GROUP(Conversations.ORGANIZATION_CONVERSATION),
	ANSWER_ORG_SAME(Conversations.ORGANIZATION_CONVERSATION),
	ANSWER_ORG_DIFFERENT(Conversations.ORGANIZATION_CONVERSATION),
	QUESTION_PAY_BOUNTY(Conversations.PAY_BOUNTY_CONVERSATION),
	ANSWER_PAY_BOUNTY_YES(Conversations.PAY_BOUNTY_CONVERSATION),
	ANSWER_PAY_BOUNTY_NO(Conversations.PAY_BOUNTY_CONVERSATION),
	QUESTION_PROFESSION(Conversations.PROFESSION_CONVERSATION),
	ANSWER_PROFESSION_MY(Conversations.PROFESSION_CONVERSATION),
	ANSWER_PROFESSION_NO(Conversations.PROFESSION_CONVERSATION),
	ANSWER_PROFESSION_SAME(Conversations.PROFESSION_CONVERSATION),
	ANSWER_PROFESSION_NEW(Conversations.PROFESSION_CONVERSATION),
	QUESTION_PROFESSION_USERS(Conversations.PROFESSION_PRACTITIONERS_CONVERSATION),
	ANSWER_PROFESSION_USERS_YES(Conversations.PROFESSION_PRACTITIONERS_CONVERSATION),
	ANSWER_PROFESSION_USERS_NO(Conversations.PROFESSION_PRACTITIONERS_CONVERSATION),
	QUESTION_PROFESSION_REASON(Conversations.PROFESSION_REASON_CONVERSATION),
	ANSWER_PROFESSION_REASON_YES(Conversations.PROFESSION_REASON_CONVERSATION),
	ANSWER_PROFESSION_REASON_NO(Conversations.PROFESSION_REASON_CONVERSATION),
	ANSWER_PROFESSION_REASON_SAME(Conversations.PROFESSION_REASON_CONVERSATION),
	ANSWER_PROFESSION_REASON_DIFFERENT(Conversations.PROFESSION_REASON_CONVERSATION),
	QUESTION_PROPOSE_MATE(Conversations.PROPOSE_MATE_CONVERSATION),
	ANSWER_PROPOSE_MATE_YES(Conversations.PROPOSE_MATE_CONVERSATION),
	ANSWER_PROPOSE_MATE_NO(Conversations.PROPOSE_MATE_CONVERSATION),
	ANSWER_PROPOSE_MATE_SAME(Conversations.PROPOSE_MATE_CONVERSATION),
	ANSWER_PROPOSE_MATE_DIFFERENT(Conversations.PROPOSE_MATE_CONVERSATION),
	QUESTION_RELATIONSHIP(Conversations.RELATIONSHIP_CONVERSATION),
	ANSWER_RELATIONSHIP_DONT(Conversations.RELATIONSHIP_CONVERSATION),
	ANSWER_RELATIONSHIP_LIKE(Conversations.RELATIONSHIP_CONVERSATION),
	ANSWER_RELATIONSHIP_REALLY_LIKE(Conversations.RELATIONSHIP_CONVERSATION),
	ANSWER_RELATIONSHIP_DISLIKE(Conversations.RELATIONSHIP_CONVERSATION),
	QUESTION_SELL_BUILDING(Conversations.SELL_APOTHECARY_CONVERSATION),
	ANSWER_SELL_BUILDING_YES(Conversations.SELL_APOTHECARY_CONVERSATION),
	ANSWER_SELL_BUILDING_NO(Conversations.SELL_APOTHECARY_CONVERSATION),
	QUESTION_SET_PRICE(Conversations.SET_ORGANIZATION_PROFIT_PERCENTAGE),
	ANSWER_SET_PRICE_YES(Conversations.SET_ORGANIZATION_PROFIT_PERCENTAGE),
	ANSWER_SET_PRICE_NO(Conversations.SET_ORGANIZATION_PROFIT_PERCENTAGE),
	QUESTION_SHARE_KNOWLEDGE(Conversations.SHARE_KNOWLEDGE_CONVERSATION),
	ANSWER_SHARE_KNOWLEDGE_THANKS(Conversations.SHARE_KNOWLEDGE_CONVERSATION),
	ANSWER_SHARE_KNOWLEDGE_GETLOST(Conversations.SHARE_KNOWLEDGE_CONVERSATION),
	QUESTION_START_ARENA_FIGHT(Conversations.START_ARENA_FIGHT_CONVERSATION),
	ANSWER_START_ARENA_FIGHT_YES(Conversations.START_ARENA_FIGHT_CONVERSATION),
	ANSWER_START_ARENA_FIGHT_NO(Conversations.START_ARENA_FIGHT_CONVERSATION),
	ANSWER_START_ARENA_FIGHT_WAIT(Conversations.START_ARENA_FIGHT_CONVERSATION)
	;
	
	private final Conversation conversationKey;
	
	private Text(Conversation conversationKey) {
		this.conversationKey = conversationKey;
	}

	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("MessagesBundle");
	
	public String get() {
        return MESSAGES.getString(this.toString());
	}
	
	public String get(String parm) {
        MessageFormat formatter = createFormatter();
        return formatter.format(new Object[]{ parm });
	}
	
	public String get(String parm1, String parm2) {
        MessageFormat formatter = createFormatter();
        return formatter.format(new Object[]{ parm1, parm2 });
	}
	
	public String get(String parm1, String parm2, String parm3) {
		 MessageFormat formatter = createFormatter();
	        return formatter.format(new Object[]{ parm1, parm2, parm3 });
	}

	private MessageFormat createFormatter() {
		MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(get().replace("'", "''"));
		return formatter;
	}
	
	public String get(int parm) {
		return get(Integer.toString(parm));
	}
	
	private Conversation getConversationKey() {
		return conversationKey;
	}

	private boolean isConversation() {
		return getConversationKey() != null;
	}
	
	private boolean isQuestion() {
		return name().startsWith("QUESTION_");
	}
	
	private boolean isAnswer() {
		return name().startsWith("ANSWER_");
	}
	
	public static List<ConversationDescription> getConversationDescriptions() {
		Map<Conversation, ConversationDescription> conversationDescriptions = new HashMap<>();
		for(Text text : Text.values()) {
			if (text.isConversation()) {
				Conversation conversationKey = text.getConversationKey();
				ConversationDescription conversationDescription = getConversationDescription(conversationDescriptions, conversationKey);
				if (text.isQuestion()) {
					conversationDescription.addQuestion(text.get());
				}
				if (text.isAnswer()) {
					conversationDescription.addAnswer(text.get());
				}
			}
		}
		
		
		List<ConversationDescription> conversationDescriptionList = new ArrayList<>(conversationDescriptions.values());
		validate(conversationDescriptionList);
		return conversationDescriptionList;
	}

	private static void validate(List<ConversationDescription> conversationDescriptionList) {
		for(ConversationDescription conversationDescription : conversationDescriptionList) {
			if (conversationDescription.getQuestions().size() == 0) {
				throw new IllegalStateException("conversationDescription " + conversationDescription.getConversationKey() + " doens't contain any questions");
			}
			if (conversationDescription.getAnswers().size() == 0) {
				throw new IllegalStateException("conversationDescription " + conversationDescription.getConversationKey() + " doens't contain any answers");
			}
		}
	}

	private static ConversationDescription getConversationDescription(Map<Conversation, ConversationDescription> conversationDescriptions, Conversation conversationKey) {
		final ConversationDescription conversationDescription;
		if (conversationDescriptions.containsKey(conversationKey)) {
			conversationDescription = conversationDescriptions.get(conversationKey);
		} else {
			conversationDescription = new ConversationDescription(conversationKey);
			conversationDescriptions.put(conversationKey, conversationDescription);
		}
		return conversationDescription;
	}
}
