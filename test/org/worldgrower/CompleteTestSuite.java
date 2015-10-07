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
package org.worldgrower;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.worldgrower.actions.UTestAttackUtils;
import org.worldgrower.actions.UTestChooseProfessionAction;
import org.worldgrower.actions.UTestCraftUtils;
import org.worldgrower.actions.UTestOrganizationNamer;
import org.worldgrower.actions.UTestTalkAction;
import org.worldgrower.actions.UTestVotingPropertyUtils;
import org.worldgrower.attribute.UTestIdList;
import org.worldgrower.attribute.UTestIdMap;
import org.worldgrower.attribute.UTestIntProperty;
import org.worldgrower.attribute.UTestKnowledgeMap;
import org.worldgrower.attribute.UTestPropertyCountMap;
import org.worldgrower.attribute.UTestSkillUtils;
import org.worldgrower.attribute.UTestWorldObjectProperties;
import org.worldgrower.condition.UTestConditions;
import org.worldgrower.conversation.UTestArenaFighterPayCheckConversation;
import org.worldgrower.conversation.UTestBrawlConversation;
import org.worldgrower.conversation.UTestComplimentConversation;
import org.worldgrower.conversation.UTestConversations;
import org.worldgrower.conversation.UTestCurePoisonConversation;
import org.worldgrower.conversation.UTestDeityFollowersConversation;
import org.worldgrower.conversation.UTestDeityReasonConversation;
import org.worldgrower.conversation.UTestDemandMoneyConversation;
import org.worldgrower.conversation.UTestDemandsConversation;
import org.worldgrower.conversation.UTestFamilyConversation;
import org.worldgrower.conversation.UTestGiveFoodConversation;
import org.worldgrower.conversation.UTestImmediateGoalConversation;
import org.worldgrower.conversation.UTestIntimidateConversation;
import org.worldgrower.conversation.UTestJoinPerformerOrganizationConversation;
import org.worldgrower.conversation.UTestJoinTargetOrganizationConversation;
import org.worldgrower.conversation.UTestLocationConversation;
import org.worldgrower.conversation.UTestMinorHealConversation;
import org.worldgrower.conversation.UTestNicerConversation;
import org.worldgrower.conversation.UTestOrganizationConversation;
import org.worldgrower.conversation.UTestProfessionPractitionersConversation;
import org.worldgrower.conversation.UTestSellHouseConversation;
import org.worldgrower.conversation.UTestSetOrganizationProfitPercentageConversation;
import org.worldgrower.conversation.UTestShareKnowledgeConversation;
import org.worldgrower.conversation.UTestStartArenaFightConversation;
import org.worldgrower.conversation.UTestWhoIsLeaderOrganizationConversation;
import org.worldgrower.conversation.UTestWhyAngryConversation;
import org.worldgrower.conversation.UTestWhyAngryOtherConversation;
import org.worldgrower.generator.UTestCreatureGenerator;
import org.worldgrower.goal.UTestAbstractScribeSpellsGoal;
import org.worldgrower.goal.UTestArenaPropertyUtils;
import org.worldgrower.goal.UTestArmorPropertyUtils;
import org.worldgrower.goal.UTestBrawlPropertyUtils;
import org.worldgrower.goal.UTestBuildLocationUtils;
import org.worldgrower.goal.UTestBuySellUtils;
import org.worldgrower.goal.UTestDrownUtils;
import org.worldgrower.goal.UTestFacadeUtils;
import org.worldgrower.goal.UTestGoalUtils;
import org.worldgrower.goal.UTestGroupPropertyUtils;
import org.worldgrower.goal.UTestHousePropertyUtils;
import org.worldgrower.goal.UTestInventoryPropertyUtils;
import org.worldgrower.goal.UTestLegalActionsPropertyUtils;
import org.worldgrower.goal.UTestLocationUtils;
import org.worldgrower.goal.UTestLockUtils;
import org.worldgrower.goal.UTestMeleeDamagePropertyUtils;
import org.worldgrower.goal.UTestRacePropertyUtils;
import org.worldgrower.goal.UTestRelationshipPropertyUtils;
import org.worldgrower.goal.UTestSocializeGoal;
import org.worldgrower.goal.UTestWeightPropertyUtils;
import org.worldgrower.goal.UTestZone;
import org.worldgrower.history.UTestHistoryImpl;
import org.worldgrower.util.UTestNumberUtils;

@RunWith(Suite.class)
@SuiteClasses({ 
	UTestBackgroundImpl.class, 
	UTestCreaturePositionCondition.class, 
	UTestTaskCalculator.class, 
	UTestTalkAction.class,
	UTestChooseProfessionAction.class,
	UTestGoalUtils.class,
	UTestGoalCalculator.class,
	UTestZone.class,
	UTestReach.class,
	UTestBuildLocationUtils.class,
	UTestWorldGenerator.class,
	UTestWorldObjectContainer.class,
	UTestConversations.class,
	UTestDungeonMaster.class,
	UTestGoalChangedCalculator.class,
	UTestWorldImpl.class,
	UTestIdMap.class,
	UTestWorldObjectProperties.class,
	UTestIdList.class,
	UTestHistoryImpl.class,
	UTestLocationConversation.class,
	UTestPropertyCountMap.class,
	UTestIntProperty.class,
	UTestNumberUtils.class,
	UTestAttackUtils.class,
	UTestGroupPropertyUtils.class,
	UTestSkillUtils.class,
	UTestAbstractScribeSpellsGoal.class,
	UTestKnowledgeMap.class,
	UTestDemandsConversation.class,
	UTestWorldObjectImpl.class,
	UTestConditions.class,
	UTestDefaultGoalObstructedHandler.class,
	UTestWorldFacade.class,
	UTestBuySellUtils.class,
	UTestHousePropertyUtils.class,
	UTestArenaPropertyUtils.class,
	UTestSocializeGoal.class,
	UTestLocationUtils.class,
	UTestArmorPropertyUtils.class,
	UTestMeleeDamagePropertyUtils.class,
	UTestCraftUtils.class,
	UTestVotingPropertyUtils.class,
	UTestFacadeUtils.class,
	UTestBrawlPropertyUtils.class,
	UTestRelationshipPropertyUtils.class,
	UTestDrownUtils.class,
	UTestWeightPropertyUtils.class,
	UTestInventoryPropertyUtils.class,
	UTestRacePropertyUtils.class,
	UTestLegalActionsPropertyUtils.class,
	UTestLockUtils.class,
	UTestOrganizationNamer.class,
	UTestWhoIsLeaderOrganizationConversation.class,
	UTestDemandMoneyConversation.class,
	UTestComplimentConversation.class,
	UTestProfessionPractitionersConversation.class,
	UTestDeityFollowersConversation.class,
	UTestShareKnowledgeConversation.class,
	UTestFamilyConversation.class,
	UTestWhyAngryOtherConversation.class,
	UTestWhyAngryConversation.class,
	UTestBrawlConversation.class,
	UTestMinorHealConversation.class,
	UTestSetOrganizationProfitPercentageConversation.class,
	UTestJoinPerformerOrganizationConversation.class,
	UTestCreatureGenerator.class,
	UTestArenaFighterPayCheckConversation.class,
	UTestJoinTargetOrganizationConversation.class,
	UTestGiveFoodConversation.class,
	UTestSellHouseConversation.class,
	UTestNicerConversation.class,
	UTestCurePoisonConversation.class,
	UTestIntimidateConversation.class,
	UTestStartArenaFightConversation.class,
	UTestDeityReasonConversation.class,
	UTestOrganizationConversation.class,
	UTestImmediateGoalConversation.class
})
public class CompleteTestSuite {
}
