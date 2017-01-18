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

public enum Text {
	QUESTION_ARENA_PAY_CHECK(ConversationKey.ARENA_PAY_CHECK),
	ANSWER_ARENA_PAY_CHECK_YES(ConversationKey.ARENA_PAY_CHECK),
	ANSWER_ARENA_PAY_CHECK_NO(ConversationKey.ARENA_PAY_CHECK),
	QUESTION_ASK_GOAL(ConversationKey.ASK_GOAL), 
	ANSWER_ASK_GOAL_YES(ConversationKey.ASK_GOAL), 
	ANSWER_ASK_GOAL_EXPLAIN(ConversationKey.ASK_GOAL),
	ANSWER_ASK_GOAL_NO(ConversationKey.ASK_GOAL),
	QUESTION_ASSASSINATE_TARGET(ConversationKey.ASSASSINATE_TARGET),
	ANSWER_ASSASSINATE_TARGET_YES(ConversationKey.ASSASSINATE_TARGET),
	ANSWER_ASSASSINATE_TARGET_NO(ConversationKey.ASSASSINATE_TARGET);
	
	private final ConversationKey conversationKey;
	
	public enum ConversationKey {
		ARENA_PAY_CHECK,
		ASK_GOAL,
		ASSASSINATE_TARGET
	}
	
	static ResourceBundle getMessages() {
		return MESSAGES;
	}

	private Text(ConversationKey conversationKey) {
		this.conversationKey = conversationKey;
	}

	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("MessagesBundle");
	
	public String get() {
        return MESSAGES.getString(this.toString());
	}
	
	public String get(String parm) {
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(get().replace("'", "''"));
        return formatter.format(new Object[]{ parm });
	}
	
	public String get(int parm) {
		return get(Integer.toString(parm));
	}
	
	private ConversationKey getConversationKey() {
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
		Map<ConversationKey, ConversationDescription> conversationDescriptions = new HashMap<>();
		for(Text text : Text.values()) {
			if (text.isConversation()) {
				ConversationKey conversationKey = text.getConversationKey();
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

	private static ConversationDescription getConversationDescription(Map<ConversationKey, ConversationDescription> conversationDescriptions, ConversationKey conversationKey) {
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
