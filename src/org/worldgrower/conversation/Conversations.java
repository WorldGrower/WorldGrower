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

import static org.worldgrower.goal.FacadeUtils.createFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.leader.CanCollectTaxesConversation;
import org.worldgrower.conversation.leader.SetHouseTaxRateConversation;
import org.worldgrower.conversation.leader.SetShackTaxRateConversation;
import org.worldgrower.history.HistoryItem;

public class Conversations implements Serializable {

	public static final Conversation NAME_CONVERSATION = new NameConversation();
	public static final Conversation GOAL_CONVERSATION = new GoalConversation();
	public static final Conversation IMMEDIATE_GOAL_CONVERSATION = new ImmediateGoalConversation();
	public static final Conversation RELATIONSHIP_CONVERSATION = new RelationshipConversation();
	public static final Conversation PROFESSION_CONVERSATION = new ProfessionConversation();
	public static final Conversation DEITY_CONVERSATION = new DeityConversation();
	public static final Conversation DEITY_EXPLANATION_CONVERSATION = new DeityExplanationConversation();
	public static final Conversation DEITY_REASON_CONVERSATION = new DeityReasonConversation();
	public static final Conversation PROFESSION_REASON_CONVERSATION = new ProfessionReasonConversation();
	public static final Conversation DEMANDS_CONVERSATION = new DemandsConversation();
	public static final Conversation BROKEN_LAW_CONVERSATION = new BrokenLawConversation();
	public static final Conversation COMPLIMENT_CONVERSATION = new ComplimentConversation();
	public static final Conversation EXPLAIN_CURSE_CONVERSATION = new ExplainCurseConversation();
	public static final Conversation LOCATION_CONVERSATION = new LocationConversation();
	public static final Conversation DEMAND_MONEY_CONVERSATION = new DemandMoneyConversation();
	public static final Conversation PROPOSE_MATE_CONVERSATION = new ProposeMateConversation();
	public static final Conversation FAMILY_CONVERSATION = new FamilyConversation();
	public static final Conversation KISS_CONVERSATION = new KissConversation();
	public static final Conversation WHY_ANGRY_CONVERSATION = new WhyAngryConversation();
	public static final Conversation WHY_ANGRY_OTHER_CONVERSATION = new WhyAngryOtherConversation();
	public static final Conversation NICER_CONVERSATION = new NicerConversation();
	public static final Conversation NOT_NICER_CONVERSATION = new NotNicerConversation();
	public static final Conversation ORGANIZATION_CONVERSATION = new OrganizationConversation();
	public static final Conversation JOIN_PERFORMER_ORGANIZATION_CONVERSATION = new JoinPerformerOrganizationConversation();
	public static final Conversation JOIN_TARGET_ORGANIZATION_CONVERSATION = new JoinTargetOrganizationConversation();
	public static final Conversation LEARN_SKILLS_USING_ORGANIZATION = new LearnSkillUsingOrganizationConversation();
	public static final Conversation SET_ORGANIZATION_PROFIT_PERCENTAGE = new SetOrganizationProfitPercentageConversation();
	public static final Conversation CURE_POISON_CONVERSATION = new CurePoisonConversation();
	public static final Conversation WHO_IS_LEADER_ORGANIZATION_CONVERSATION = new WhoIsLeaderOrganizationConversation();
	public static final Conversation VOTE_LEADER_ORGANIZATION_CONVERSATION = new VoteLeaderOrganizationConversation();
	public static final SetShackTaxRateConversation SET_SHACK_TAX_RATE_CONVERSATION = new SetShackTaxRateConversation();
	public static final SetHouseTaxRateConversation SET_HOUSE_TAX_RATE_CONVERSATION = new SetHouseTaxRateConversation();
	public static final CollectTaxesConversation COLLECT_TAXES_CONVERSATION = new CollectTaxesConversation();
	public static final CollectPayCheckConversation COLLECT_PAY_CHECK_CONVERSATION = new CollectPayCheckConversation();
	public static final CanCollectTaxesConversation CAN_COLLECT_TAXES_CONVERSATION = new CanCollectTaxesConversation();
	public static final SellHouseConversation SELL_HOUSE_CONVERSATION = new SellHouseConversation();
	public static final MinorHealConversation MINOR_HEAL_CONVERSATION = new MinorHealConversation();
	public static final BuyHouseConversation BUY_HOUSE_CONVERSATION = new BuyHouseConversation();
	public static final ShareKnowledgeConversation SHARE_KNOWLEDGE_CONVERSATION = new ShareKnowledgeConversation();
	
	private static final List<Conversation> CONVERSATIONS = new ArrayList<>();
	private static final Map<Conversation, ConversationCategory> CONVERSATION_CATEGORIES = new HashMap<>();
	
	private static final List<InterceptedConversation> INTERCEPTED_CONVERSATIONS = Arrays.asList(new WhyNotIntelligentConversation());
	
	static {
		addNormalAndIntimidate(NAME_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		addNormalAndIntimidate(GOAL_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		addNormalAndIntimidate(IMMEDIATE_GOAL_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		add(RELATIONSHIP_CONVERSATION, ConversationCategory.RELATIONSHIP_OTHERS);
		addNormalAndIntimidate(PROFESSION_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		addNormalAndIntimidate(DEITY_CONVERSATION, ConversationCategory.DEITY);
		addNormalAndIntimidate(DEITY_EXPLANATION_CONVERSATION, ConversationCategory.DEITY);
		add(DEITY_REASON_CONVERSATION, ConversationCategory.DEITY);
		addNormalAndIntimidate(PROFESSION_REASON_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		addNormalAndIntimidate(DEMANDS_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		add(BROKEN_LAW_CONVERSATION, ConversationCategory.DEMAND);
		add(COMPLIMENT_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(EXPLAIN_CURSE_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		addNormalAndIntimidate(LOCATION_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(DEMAND_MONEY_CONVERSATION, ConversationCategory.DEMAND);
		add(PROPOSE_MATE_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(FAMILY_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		add(KISS_CONVERSATION, ConversationCategory.DEMAND);
		add(WHY_ANGRY_CONVERSATION, ConversationCategory.PERSONAL_INFORMATION);
		add(WHY_ANGRY_OTHER_CONVERSATION, ConversationCategory.RELATIONSHIP_OTHERS);
		add(NICER_CONVERSATION, ConversationCategory.RELATIONSHIP_OTHERS);
		add(NOT_NICER_CONVERSATION, ConversationCategory.RELATIONSHIP_OTHERS);
		addNormalAndIntimidate(ORGANIZATION_CONVERSATION, ConversationCategory.GROUP);
		addNormalAndIntimidate(JOIN_PERFORMER_ORGANIZATION_CONVERSATION, ConversationCategory.GROUP);
		addNormalAndIntimidate(JOIN_TARGET_ORGANIZATION_CONVERSATION, ConversationCategory.GROUP);
		add(LEARN_SKILLS_USING_ORGANIZATION, ConversationCategory.GROUP);
		add(SET_ORGANIZATION_PROFIT_PERCENTAGE, ConversationCategory.GROUP);
		addNormalAndIntimidate(CURE_POISON_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(WHO_IS_LEADER_ORGANIZATION_CONVERSATION, ConversationCategory.GROUP);
		addNormalAndIntimidate(VOTE_LEADER_ORGANIZATION_CONVERSATION, ConversationCategory.GROUP);
		add(SET_SHACK_TAX_RATE_CONVERSATION, ConversationCategory.LEADER);
		add(SET_HOUSE_TAX_RATE_CONVERSATION, ConversationCategory.LEADER);
		add(COLLECT_TAXES_CONVERSATION, ConversationCategory.GROUP);
		add(COLLECT_PAY_CHECK_CONVERSATION, ConversationCategory.LEADER);
		addNormalAndIntimidate(CAN_COLLECT_TAXES_CONVERSATION, ConversationCategory.LEADER);
		add(SELL_HOUSE_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(MINOR_HEAL_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_HOUSE_CONVERSATION, ConversationCategory.DEMAND);
		add(SHARE_KNOWLEDGE_CONVERSATION, ConversationCategory.SHARE_KNOWLEDGE);
	}
	
	public static int[] createArgs(Conversation conversation) {
		int id = indexOf(conversation);
		return new int[] { id, -1, -1, 0 };
	}

	private static int indexOf(Conversation conversation) {
		int id = CONVERSATIONS.indexOf(conversation);
		if (id == -1) {
			throw new IllegalStateException("Conversation " + conversation + " not found in list of conversations");
		}
		return id;
	}
	
	public static int[] createArgs(Conversation conversation, WorldObject subject) {
		int id = indexOf(conversation);
		if (subject != null) {
			return new int[] { id, subject.getProperty(Constants.ID), -1, 0 };
		} else {
			return new int[] { id, -1, -1, 0 };
		}
	}
	
	public static int[] createArgs(Conversation conversation, WorldObject subject, int additionalValue) {
		int id = indexOf(conversation);
		if (subject != null) {
			return new int[] { id, subject.getProperty(Constants.ID), -1, additionalValue };
		} else {
			return new int[] { id, -1, -1, additionalValue };
		}
	}
	
	public static int[] createArgs(Conversation conversation, HistoryItem historyItem) {
		int id = indexOf(conversation);
		if (historyItem != null) {
			return new int[] { id, -1, historyItem.getHistoryId(), 0 };
		} else {
			return new int[] { id, -1, -1, 0 };
		}
	}

	private static void add(Conversation conversation, ConversationCategory conversationCategory) {
		CONVERSATIONS.add(conversation);
		CONVERSATION_CATEGORIES.put(conversation, conversationCategory);
	}
	
	private static void addNormalAndIntimidate(Conversation conversation, ConversationCategory conversationCategory) {
		add(conversation, conversationCategory);
		add(new IntimidateConversation(conversation), ConversationCategory.INTIMIDATE_TARGET);
	}
	
	public Map<ConversationCategory, List<Question>> getQuestionPhrases(WorldObject performer, WorldObject target, World world) {
		Map<ConversationCategory, List<Question>> questions = new HashMap<>();
		for(int index = 0; index < CONVERSATIONS.size(); index++) {
			Conversation conversation = CONVERSATIONS.get(index);
			if (conversation.isConversationAvailable(performer, target, world)) {
				List<Question> questionsToBeAdded = conversation.getQuestionPhrases(performer, target, null, world);
				for(Question question : questionsToBeAdded) {
					question.setId(index);
					ConversationCategory conversationCategory = CONVERSATION_CATEGORIES.get(conversation);
					List<Question> questionList = questions.get(conversationCategory);
					if (questionList == null) {
						questionList = new ArrayList<>();
						questions.put(conversationCategory, questionList);
					}
					questionList.add(question);
				}
			}
		}
		return questions;
	}
	
	public String getQuestionPhrase(int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world) {
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		List<Question> questions = CONVERSATIONS.get(index).getQuestionPhrases(performer, target, questionHistoryItem, world);
		return questions.stream().filter(q -> q.getSubjectId() == subjectId).collect(Collectors.toList()).get(0).getQuestionPhrase();
	}
	
	private HistoryItem getQuestionHistoryItem(int historyItemId, World world) {
		if (historyItemId >= 0) {
			return world.getHistory().getHistoryItem(historyItemId);
		} else {
			return null;
		}
	}

	public Response getReplyPhrase(int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world, int additionalValue) {
		WorldObject subject = getSubject(subjectId, world);
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		WorldObject performerFacade = createFacade(performer, performer, target);
		WorldObject targetFacade = createFacade(target, performer, target);
		ConversationContext conversationContext = new ConversationContext(performerFacade, targetFacade, subject, questionHistoryItem, world, additionalValue);
		Response response = getReplyPhrase(index, conversationContext);
		return response;
	}

	private Response getReplyPhrase(int index, ConversationContext conversationContext) {
		for(InterceptedConversation interceptedConversation : INTERCEPTED_CONVERSATIONS) {
			if (interceptedConversation.getReplyPhrase(conversationContext) != null) {
				return interceptedConversation.getReplyPhrase(conversationContext);
			}
		}
		
		return CONVERSATIONS.get(index).getReplyPhrase(conversationContext);
	}
	
	private WorldObject getSubject(int subjectId, World world) {
		if (subjectId >= 0) {
			return world.findWorldObject(Constants.ID, subjectId);
		} else {
			return null;
		}
	}

	public List<Response> getReplyPhrases(int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world, int additionalValue) {
		WorldObject subject = getSubject(subjectId, world);
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		WorldObject performerFacade = createFacade(performer, performer, target);
		WorldObject targetFacade = createFacade(target, performer, target);
		ConversationContext conversationContext = new ConversationContext(performerFacade, targetFacade, subject, questionHistoryItem, world, additionalValue);
		List<Response> responses = getReplyPhrases(index, conversationContext);
		
		return responses;
	}

	private List<Response> getReplyPhrases(int index, ConversationContext conversationContext) {
		List<Response> responses = CONVERSATIONS.get(index).getReplyPhrases(conversationContext);
		
		for(InterceptedConversation interceptedConversation : INTERCEPTED_CONVERSATIONS) {
			responses.addAll(interceptedConversation.getReplyPhrases(conversationContext));
		}
		
		return responses;
	}
	
	public void handleResponse(int replyIndex, int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world, int additionalValue) {
		WorldObject subject = getSubject(subjectId, world);
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		ConversationContext conversationContext = new ConversationContext(performer, target, subject, questionHistoryItem, world, additionalValue);
		handleResponse(replyIndex, index, conversationContext);
	}

	private void handleResponse(int replyIndex, int index, ConversationContext conversationContext) {
		for(InterceptedConversation interceptedConversation : INTERCEPTED_CONVERSATIONS) {
			interceptedConversation.handleResponse(replyIndex, conversationContext, CONVERSATIONS.get(index));
		}
		
		CONVERSATIONS.get(index).handleResponse(replyIndex, conversationContext);
	}

	public int size() {
		return CONVERSATIONS.size();
	}

	public Conversation getConversation(int index) {
		return CONVERSATIONS.get(index);
	}

	public int distance(int index, WorldObject performer, WorldObject target, World world) {
		return CONVERSATIONS.get(index).isConversationAvailable(performer, target, world) ? 0 : 1;
	}
}
