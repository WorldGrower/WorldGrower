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
	ANSWER_CURE_POISON_GETLOST(Conversations.CURE_POISON_CONVERSATION);
	
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
