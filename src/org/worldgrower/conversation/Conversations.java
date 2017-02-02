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
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.conversation.leader.CanAttackCriminalsConversation;
import org.worldgrower.conversation.leader.CanCollectTaxesConversation;
import org.worldgrower.history.HistoryItem;

/**
 * This class holds all possible conversations and handles using Conversation objects inside
 * the TalkAction.
 */
public class Conversations implements Serializable {

	public static final Conversation NAME_CONVERSATION = new NameConversation();
	public static final GoalConversation GOAL_CONVERSATION = new GoalConversation();
	public static final ImmediateGoalConversation IMMEDIATE_GOAL_CONVERSATION = new ImmediateGoalConversation();
	public static final RelationshipConversation RELATIONSHIP_CONVERSATION = new RelationshipConversation();
	public static final ProfessionConversation PROFESSION_CONVERSATION = new ProfessionConversation();
	public static final DeityConversation DEITY_CONVERSATION = new DeityConversation();
	public static final DeityExplanationConversation DEITY_EXPLANATION_CONVERSATION = new DeityExplanationConversation();
	public static final DeityReasonConversation DEITY_REASON_CONVERSATION = new DeityReasonConversation();
	public static final ProfessionReasonConversation PROFESSION_REASON_CONVERSATION = new ProfessionReasonConversation();
	public static final Conversation DEMANDS_CONVERSATION = new DemandsConversation();
	public static final CollectBountyFromThievesConversation COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION = new CollectBountyFromThievesConversation();
	public static final ComplimentConversation COMPLIMENT_CONVERSATION = new ComplimentConversation();
	public static final ExplainCurseConversation EXPLAIN_CURSE_CONVERSATION = new ExplainCurseConversation();
	public static final LocationConversation LOCATION_CONVERSATION = new LocationConversation();
	public static final DemandMoneyConversation DEMAND_MONEY_CONVERSATION = new DemandMoneyConversation();
	public static final ProposeMateConversation PROPOSE_MATE_CONVERSATION = new ProposeMateConversation();
	public static final FamilyConversation FAMILY_CONVERSATION = new FamilyConversation();
	public static final KissConversation KISS_CONVERSATION = new KissConversation();
	public static final WhyAngryConversation WHY_ANGRY_CONVERSATION = new WhyAngryConversation();
	public static final WhyAngryOtherConversation WHY_ANGRY_OTHER_CONVERSATION = new WhyAngryOtherConversation();
	public static final NicerConversation NICER_CONVERSATION = new NicerConversation();
	public static final NotNicerConversation NOT_NICER_CONVERSATION = new NotNicerConversation();
	public static final OrganizationConversation ORGANIZATION_CONVERSATION = new OrganizationConversation();
	public static final JoinPerformerOrganizationConversation JOIN_PERFORMER_ORGANIZATION_CONVERSATION = new JoinPerformerOrganizationConversation();
	public static final JoinTargetOrganizationConversation JOIN_TARGET_ORGANIZATION_CONVERSATION = new JoinTargetOrganizationConversation();
	public static final LearnSkillUsingOrganizationConversation LEARN_SKILLS_USING_ORGANIZATION = new LearnSkillUsingOrganizationConversation();
	public static final SetOrganizationProfitPercentageConversation SET_ORGANIZATION_PROFIT_PERCENTAGE = new SetOrganizationProfitPercentageConversation();
	public static final CurePoisonConversation CURE_POISON_CONVERSATION = new CurePoisonConversation();
	public static final WhoIsLeaderOrganizationConversation WHO_IS_LEADER_ORGANIZATION_CONVERSATION = new WhoIsLeaderOrganizationConversation();
	public static final VoteLeaderOrganizationConversation VOTE_LEADER_ORGANIZATION_CONVERSATION = new VoteLeaderOrganizationConversation();
	public static final CollectTaxesConversation COLLECT_TAXES_CONVERSATION = new CollectTaxesConversation();
	public static final CollectPayCheckConversation COLLECT_PAY_CHECK_CONVERSATION = new CollectPayCheckConversation();
	public static final CanCollectTaxesConversation CAN_COLLECT_TAXES_CONVERSATION = new CanCollectTaxesConversation();
	public static final SellBuildingConversation SELL_HOUSE_CONVERSATION = new SellBuildingConversation(BuildingType.HOUSE);
	public static final SellBuildingConversation SELL_BREWERY_CONVERSATION = new SellBuildingConversation(BuildingType.BREWERY);
	public static final SellBuildingConversation SELL_SMITH_CONVERSATION = new SellBuildingConversation(BuildingType.SMITH);
	public static final SellBuildingConversation SELL_WORKBENCH_CONVERSATION = new SellBuildingConversation(BuildingType.WORKBENCH);
	public static final SellBuildingConversation SELL_PAPERMILL_CONVERSATION = new SellBuildingConversation(BuildingType.PAPERMILL);
	public static final SellBuildingConversation SELL_WEAVERY_CONVERSATION = new SellBuildingConversation(BuildingType.WEAVERY);
	public static final SellBuildingConversation SELL_APOTHECARY_CONVERSATION = new SellBuildingConversation(BuildingType.APOTHECARY);
	
	public static final MinorHealConversation MINOR_HEAL_CONVERSATION = new MinorHealConversation();
	public static final BuyBuildingConversation BUY_HOUSE_CONVERSATION = new BuyBuildingConversation(BuildingType.HOUSE);
	public static final BuyBuildingConversation BUY_BREWERY_CONVERSATION = new BuyBuildingConversation(BuildingType.BREWERY);
	public static final BuyBuildingConversation BUY_SMITH_CONVERSATION = new BuyBuildingConversation(BuildingType.SMITH);
	public static final BuyBuildingConversation BUY_WORKBENCH_CONVERSATION = new BuyBuildingConversation(BuildingType.WORKBENCH);
	public static final BuyBuildingConversation BUY_PAPERMILL_CONVERSATION = new BuyBuildingConversation(BuildingType.PAPERMILL);
	public static final BuyBuildingConversation BUY_WEAVERY_CONVERSATION = new BuyBuildingConversation(BuildingType.WEAVERY);
	public static final BuyBuildingConversation BUY_APOTHECARY_CONVERSATION = new BuyBuildingConversation(BuildingType.APOTHECARY);
	
	public static final ShareKnowledgeConversation SHARE_KNOWLEDGE_CONVERSATION = new ShareKnowledgeConversation();
	public static final CanAttackCriminalsConversation CAN_ATTACK_CRIMINALS_CONVERSATION = new CanAttackCriminalsConversation();
	public static final DeityFollowersConversation DEITY_FOLLOWERS_CONVERSATION = new DeityFollowersConversation();
	public static final ProfessionPractitionersConversation PROFESSION_PRACTITIONERS_CONVERSATION = new ProfessionPractitionersConversation();
	public static final BrawlConversation BRAWL_CONVERSATION = new BrawlConversation();
	public static final BecomeArenaFighterConversation BECOME_ARENA_FIGHTER_CONVERSATION = new BecomeArenaFighterConversation();
	public static final StartArenaFightConversation START_ARENA_FIGHT_CONVERSATION = new StartArenaFightConversation();
	public static final ArenaFighterPayCheckConversation ARENA_FIGHTER_PAY_CHECK_CONVERSATION = new ArenaFighterPayCheckConversation();
	public static final GiveItemConversation GIVE_FOOD_CONVERSATION = new GiveItemConversation(Constants.FOOD, 1);
	public static final MergeOrganizationsConversation MERGE_ORGANIZATIONS_CONVERSATION = new MergeOrganizationsConversation();
	public static final SwitchDeityConversation SWITCH_DEITY_CONVERSATION = new SwitchDeityConversation();
	public static final StopSellingConversation STOP_SELLING_CONVERSATION = new StopSellingConversation();
	public static final AskGoalConversation ASK_GOAL_CONVERSATION = new AskGoalConversation();
	public static final DrinkingContestConversation DRINKING_CONTEST_CONVERSATION = new DrinkingContestConversation();
	public static final BreakupWithMateConversation BREAKUP_WITH_MATE_CONVERSATION = new BreakupWithMateConversation();
	public static final CureDiseaseConversation CURE_DISEASE_CONVERSATION = new CureDiseaseConversation();
	public static final AssassinateTargetConversation ASSASSINATE_TARGET_CONVERSATION = new AssassinateTargetConversation();
	public static final GiveMoneyConversation GIVE_MONEY_CONVERSATION = new GiveMoneyConversation();
	public static final GiveItemConversation GIVE_WINE_CONVERSATION = new GiveItemConversation(Constants.WINE, 5);
	public static final GiveItemConversation GIVE_WATER_CONVERSATION = new GiveItemConversation(Constants.WATER, 5);
	public static final GiveItemConversation GIVE_WOOD_CONVERSATION = new GiveItemConversation(Constants.WOOD, 1);
	public static final GiveItemConversation GIVE_STONE_CONVERSATION = new GiveItemConversation(Constants.STONE, 1);
	public static final GiveItemConversation GIVE_ORE_CONVERSATION = new GiveItemConversation(Constants.ORE, 1);
	public static final GiveItemConversation GIVE_PAPER_CONVERSATION = new GiveItemConversation(Constants.PAPER, 1);
	public static final GiveItemConversation GIVE_COTTON_CONVERSATION = new GiveItemConversation(Constants.COTTON, 1);
	
	public static final PayBountyConversation PAY_BOUNTY_CONVERSATION = new PayBountyConversation();
	public static final RemoveCurseConversation REMOVE_CURSE_CONVERSATION = new RemoveCurseConversation();
	
	private static final List<Conversation> CONVERSATIONS = new ArrayList<>();
	private static final Map<Conversation, ConversationCategory> CONVERSATION_CATEGORIES = new HashMap<>();
	
	private static final List<InterceptedConversation> INTERCEPTED_CONVERSATIONS = Arrays.asList(
			new WhyNotIntelligentConversation(), 
			new SawDisguisingConversation(),
			new LookTheSameConversation()
			);
	
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
		add(COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION, ConversationCategory.DEMAND);
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
		add(COLLECT_TAXES_CONVERSATION, ConversationCategory.GROUP);
		add(COLLECT_PAY_CHECK_CONVERSATION, ConversationCategory.LEADER);
		addNormalAndIntimidate(CAN_COLLECT_TAXES_CONVERSATION, ConversationCategory.LEADER);
		add(SELL_HOUSE_CONVERSATION, ConversationCategory.DEMAND);
		add(SELL_BREWERY_CONVERSATION, ConversationCategory.DEMAND);
		add(SELL_SMITH_CONVERSATION, ConversationCategory.DEMAND);
		add(SELL_WORKBENCH_CONVERSATION, ConversationCategory.DEMAND);
		add(SELL_PAPERMILL_CONVERSATION, ConversationCategory.DEMAND);
		add(SELL_WEAVERY_CONVERSATION, ConversationCategory.DEMAND);
		add(SELL_APOTHECARY_CONVERSATION, ConversationCategory.DEMAND);		
		addNormalAndIntimidate(MINOR_HEAL_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_HOUSE_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_BREWERY_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_SMITH_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_WORKBENCH_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_PAPERMILL_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_WEAVERY_CONVERSATION, ConversationCategory.DEMAND);
		add(BUY_APOTHECARY_CONVERSATION, ConversationCategory.DEMAND);
		add(SHARE_KNOWLEDGE_CONVERSATION, ConversationCategory.SHARE_KNOWLEDGE);
		add(CAN_ATTACK_CRIMINALS_CONVERSATION, ConversationCategory.LEADER);
		add(DEITY_FOLLOWERS_CONVERSATION, ConversationCategory.SHARE_KNOWLEDGE);
		add(PROFESSION_PRACTITIONERS_CONVERSATION, ConversationCategory.SHARE_KNOWLEDGE);
		add(BRAWL_CONVERSATION, ConversationCategory.DEMAND);
		add(BECOME_ARENA_FIGHTER_CONVERSATION, ConversationCategory.ARENA);
		add(START_ARENA_FIGHT_CONVERSATION, ConversationCategory.ARENA);
		add(ARENA_FIGHTER_PAY_CHECK_CONVERSATION, ConversationCategory.ARENA);
		add(GIVE_FOOD_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_MONEY_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_WINE_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_WATER_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_WOOD_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_STONE_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_ORE_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_PAPER_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		add(GIVE_COTTON_CONVERSATION, ConversationCategory.DIPLOMACY_TARGET);
		
		addNormalAndIntimidate(MERGE_ORGANIZATIONS_CONVERSATION, ConversationCategory.LEADER);
		addNormalAndIntimidate(SWITCH_DEITY_CONVERSATION, ConversationCategory.DEITY);
		addNormalAndIntimidate(STOP_SELLING_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(ASK_GOAL_CONVERSATION, ConversationCategory.REQUEST_ACTION);
		add(DRINKING_CONTEST_CONVERSATION, ConversationCategory.DEMAND);
		add(BREAKUP_WITH_MATE_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(CURE_DISEASE_CONVERSATION, ConversationCategory.DEMAND);
		add(ASSASSINATE_TARGET_CONVERSATION, ConversationCategory.DEMAND);
		add(PAY_BOUNTY_CONVERSATION, ConversationCategory.DEMAND);
		addNormalAndIntimidate(REMOVE_CURSE_CONVERSATION, ConversationCategory.DEMAND);
	}
	
	public static int[] createArgs(Conversation conversation) {
		int id = indexOf(conversation);
		return new int[] { id, -1, -1, 0, 0 };
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
			return new int[] { id, subject.getProperty(Constants.ID), -1, 0, 0 };
		} else {
			return new int[] { id, -1, -1, 0, 0 };
		}
	}
	
	public static int[] createArgs(Conversation conversation, WorldObject subject, int additionalValue) {
		int id = indexOf(conversation);
		if (subject != null) {
			return new int[] { id, subject.getProperty(Constants.ID), -1, additionalValue, 0 };
		} else {
			return new int[] { id, -1, -1, additionalValue, 0 };
		}
	}
	
	public static int[] createArgs(Conversation conversation, HistoryItem historyItem) {
		int id = indexOf(conversation);
		if (historyItem != null) {
			return new int[] { id, -1, historyItem.getHistoryId(), 0, 0 };
		} else {
			return new int[] { id, -1, -1, 0, 0 };
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
			if (conversation.isConversationAvailable(performer, target, null, world)) {
				WorldObject dummySubject = createDummySubject();
				List<Question> questionsToBeAdded = conversation.getQuestionPhrases(performer, target, null, dummySubject, world);
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

	private WorldObject createDummySubject() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "[...]");
		properties.put(Constants.ID, -1);
		WorldObject dummySubject = new WorldObjectImpl(properties);
		return dummySubject;
	}
	
	public String getQuestionPhrase(int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world) {
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		WorldObject subject = getSubject(subjectId, world);
		List<Question> questions = CONVERSATIONS.get(index).getQuestionPhrases(performer, target, questionHistoryItem, subject, world);
		List<Question> questionsBySubjectId = questions.stream().filter(q -> q.getSubjectId() == subjectId).collect(Collectors.toList());
		if (questionsBySubjectId.size() == 0) {
			throw new IllegalStateException("No question found for conversation " + CONVERSATIONS.get(index) + " and subjectId " + subjectId + " in " + questions);
		}
		
		return questionsBySubjectId.get(0).getQuestionPhrase();
	}
	
	private HistoryItem getQuestionHistoryItem(int historyItemId, World world) {
		if (historyItemId >= 0) {
			return world.getHistory().getHistoryItem(historyItemId);
		} else {
			return null;
		}
	}

	public Response getReplyPhrase(int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world, int additionalValue, int additionalValue2) {
		WorldObject subject = getSubject(subjectId, world);
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		WorldObject performerFacade = createFacade(performer, performer, target, world);
		WorldObject targetFacade = createFacade(target, performer, target, world);
		ConversationContext conversationContext = new ConversationContext(performerFacade, targetFacade, subject, questionHistoryItem, world, additionalValue, additionalValue2);
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
			return world.findWorldObjectById(subjectId);
		} else {
			return null;
		}
	}

	public List<Response> getReplyPhrases(int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world, int additionalValue, int additionalValue2) {
		WorldObject subject = getSubject(subjectId, world);
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		WorldObject performerFacade = createFacade(performer, performer, target, world);
		WorldObject targetFacade = createFacade(target, performer, target, world);
		ConversationContext conversationContext = new ConversationContext(performerFacade, targetFacade, subject, questionHistoryItem, world, additionalValue, additionalValue2);
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
	
	public void handleResponse(int replyIndex, int index, int subjectId, int historyItemId, WorldObject performer, WorldObject target, World world, int additionalValue, int additionalValue2) {
		WorldObject subject = getSubject(subjectId, world);
		HistoryItem questionHistoryItem = getQuestionHistoryItem(historyItemId, world);
		ConversationContext conversationContext = new ConversationContext(performer, target, subject, questionHistoryItem, world, additionalValue, additionalValue2);
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

	public static Conversation getConversation(int index) {
		return CONVERSATIONS.get(index);
	}

	public boolean isConversationAvailable(int index, WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return CONVERSATIONS.get(index).isConversationAvailable(performer, target, subject, world);
	}
	
	public boolean additionalValuesValid(int index, WorldObject performer, WorldObject target, int additionalValue, int additionalValue2, World world) {
		return CONVERSATIONS.get(index).additionalValuesValid(performer, target, additionalValue, additionalValue2, world);
	}
	
	public List<WorldObject> getPossibleSubjects(int index, int historyItemId, WorldObject performer, WorldObject target, World world) {
		return CONVERSATIONS.get(index).getPossibleSubjects(performer, target, getQuestionHistoryItem(historyItemId, world), world);
	}
	
	public static Conversation getIntimidateConversation() {
		for(Conversation conversation : CONVERSATIONS) {
			if (conversation instanceof IntimidateConversation) {
				return conversation;
			}
		}
		throw new IllegalStateException("No IntimidateConversation found in " + CONVERSATIONS);
	}

	public static Conversation getBuyBuildingConversation(BuildingType buildingType) {
		for(Conversation conversation : CONVERSATIONS) {
			if (conversation instanceof BuyBuildingConversation) {
				BuyBuildingConversation buyBuildingConversation = (BuyBuildingConversation) conversation;
				if (buyBuildingConversation.getBuildingType() == buildingType) {
					return buyBuildingConversation;
				}
			}
		}
		throw new IllegalStateException("No BuyBuildingConversation found for " + buildingType);
	}
	
	public static ConversationCategory getConversationCategory(Conversation conversation) {
		return CONVERSATION_CATEGORIES.get(conversation);
	}
}
