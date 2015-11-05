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
import org.worldgrower.actions.UTestActions;
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
import org.worldgrower.conversation.UTestBecomeArenaFighterConversation;
import org.worldgrower.conversation.UTestBrawlConversation;
import org.worldgrower.conversation.UTestBuyHouseConversation;
import org.worldgrower.conversation.UTestCollectTaxesConversation;
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
import org.worldgrower.conversation.UTestKissConversation;
import org.worldgrower.conversation.UTestLearnSkillUsingOrganizationConversation;
import org.worldgrower.conversation.UTestLocationConversation;
import org.worldgrower.conversation.UTestLookTheSameConversation;
import org.worldgrower.conversation.UTestMinorHealConversation;
import org.worldgrower.conversation.UTestNicerConversation;
import org.worldgrower.conversation.UTestNotNicerConversation;
import org.worldgrower.conversation.UTestOrganizationConversation;
import org.worldgrower.conversation.UTestProfessionPractitionersConversation;
import org.worldgrower.conversation.UTestProfessionReasonConversation;
import org.worldgrower.conversation.UTestProposeMateConversation;
import org.worldgrower.conversation.UTestRelationshipConversation;
import org.worldgrower.conversation.UTestSawDisguisingConversation;
import org.worldgrower.conversation.UTestSellHouseConversation;
import org.worldgrower.conversation.UTestSetOrganizationProfitPercentageConversation;
import org.worldgrower.conversation.UTestShareKnowledgeConversation;
import org.worldgrower.conversation.UTestStartArenaFightConversation;
import org.worldgrower.conversation.UTestVoteLeaderOrganizationConversation;
import org.worldgrower.conversation.UTestWhoIsLeaderOrganizationConversation;
import org.worldgrower.conversation.UTestWhyAngryConversation;
import org.worldgrower.conversation.UTestWhyAngryOtherConversation;
import org.worldgrower.conversation.UTestWhyNotIntelligentConversation;
import org.worldgrower.deity.UTestDeityPropertyUtils;
import org.worldgrower.generator.UTestCommonerGenerator;
import org.worldgrower.generator.UTestCreatureGenerator;
import org.worldgrower.goal.UTestAbstractScribeSpellsGoal;
import org.worldgrower.goal.UTestArenaFightGoal;
import org.worldgrower.goal.UTestArenaGoal;
import org.worldgrower.goal.UTestArenaPropertyUtils;
import org.worldgrower.goal.UTestArmorPropertyUtils;
import org.worldgrower.goal.UTestBecomeProfessionsOrganizationMemberGoal;
import org.worldgrower.goal.UTestBecomeReligionOrganizationMemberGoal;
import org.worldgrower.goal.UTestBrawlGoal;
import org.worldgrower.goal.UTestBrawlPropertyUtils;
import org.worldgrower.goal.UTestBuildLocationUtils;
import org.worldgrower.goal.UTestBuyClothesGoal;
import org.worldgrower.goal.UTestBuySellUtils;
import org.worldgrower.goal.UTestCaptureCriminalsGoal;
import org.worldgrower.goal.UTestCatchFishGoal;
import org.worldgrower.goal.UTestChildrenGoal;
import org.worldgrower.goal.UTestChooseDeityGoal;
import org.worldgrower.goal.UTestCocoonOutsidersGoal;
import org.worldgrower.goal.UTestCollectArenaRewardGoal;
import org.worldgrower.goal.UTestCollectPayCheckGoal;
import org.worldgrower.goal.UTestCollectTaxesGoal;
import org.worldgrower.goal.UTestCollectWaterGoal;
import org.worldgrower.goal.UTestCottonGoal;
import org.worldgrower.goal.UTestCraftEquipmentGoal;
import org.worldgrower.goal.UTestCreateFoodSourcesGoal;
import org.worldgrower.goal.UTestCreateFurnitureGoal;
import org.worldgrower.goal.UTestCreateGraveGoal;
import org.worldgrower.goal.UTestCreateHouseGoal;
import org.worldgrower.goal.UTestCreateOrPlantWoodGoal;
import org.worldgrower.goal.UTestCreateWineGoal;
import org.worldgrower.goal.UTestDeathReasonPropertyUtils;
import org.worldgrower.goal.UTestDestroyShrinesToOtherDeitiesGoal;
import org.worldgrower.goal.UTestDonateMoneyToArenaGoal;
import org.worldgrower.goal.UTestDrinkWaterGoal;
import org.worldgrower.goal.UTestDrownUtils;
import org.worldgrower.goal.UTestEquipmentGoal;
import org.worldgrower.goal.UTestFacadeUtils;
import org.worldgrower.goal.UTestFeedOthersGoal;
import org.worldgrower.goal.UTestFightInArenaGoal;
import org.worldgrower.goal.UTestFishingPoleGoal;
import org.worldgrower.goal.UTestFoodGoal;
import org.worldgrower.goal.UTestFurnitureGoal;
import org.worldgrower.goal.UTestGatherFoodGoal;
import org.worldgrower.goal.UTestGatherRemainsGoal;
import org.worldgrower.goal.UTestGetHealedGoal;
import org.worldgrower.goal.UTestGetPoisonCuredGoal;
import org.worldgrower.goal.UTestGhoulGoal;
import org.worldgrower.goal.UTestGoalUtils;
import org.worldgrower.goal.UTestGroupPropertyUtils;
import org.worldgrower.goal.UTestHandoverTaxesGoal;
import org.worldgrower.goal.UTestHealOthersGoal;
import org.worldgrower.goal.UTestHouseGoal;
import org.worldgrower.goal.UTestHousePropertyUtils;
import org.worldgrower.goal.UTestHuntUndeadGoal;
import org.worldgrower.goal.UTestImproveOrganizationGoal;
import org.worldgrower.goal.UTestImproveRelationshipGoal;
import org.worldgrower.goal.UTestInventoryPropertyUtils;
import org.worldgrower.goal.UTestJailGoal;
import org.worldgrower.goal.UTestKillOutsidersGoal;
import org.worldgrower.goal.UTestKillVillagersGoal;
import org.worldgrower.goal.UTestLearnSkillGoal;
import org.worldgrower.goal.UTestLegalActionsPropertyUtils;
import org.worldgrower.goal.UTestLibraryGoal;
import org.worldgrower.goal.UTestLocationUtils;
import org.worldgrower.goal.UTestLockUtils;
import org.worldgrower.goal.UTestMarkFoodAsSellableGoal;
import org.worldgrower.goal.UTestMarkHouseAsSellableGoal;
import org.worldgrower.goal.UTestMateGoal;
import org.worldgrower.goal.UTestMeleeDamagePropertyUtils;
import org.worldgrower.goal.UTestMineGoldGoal;
import org.worldgrower.goal.UTestMineResourceGoal;
import org.worldgrower.goal.UTestMintGoldGoal;
import org.worldgrower.goal.UTestOffspringGoal;
import org.worldgrower.goal.UTestOrganizationCandidateGoal;
import org.worldgrower.goal.UTestOrganizationVoteGoal;
import org.worldgrower.goal.UTestPaperGoal;
import org.worldgrower.goal.UTestPaperMillGoal;
import org.worldgrower.goal.UTestProtectOneSelfGoal;
import org.worldgrower.goal.UTestRacePropertyUtils;
import org.worldgrower.goal.UTestRelationshipPropertyUtils;
import org.worldgrower.goal.UTestReleaseCapturedCriminalsGoal;
import org.worldgrower.goal.UTestResearchMagicSkillsKnowledgeGoal;
import org.worldgrower.goal.UTestRestGoal;
import org.worldgrower.goal.UTestSacrificePeopleToDeityGoal;
import org.worldgrower.goal.UTestScribeWizardSpellsGoal;
import org.worldgrower.goal.UTestSellWoodGoal;
import org.worldgrower.goal.UTestSetTaxesGoal;
import org.worldgrower.goal.UTestSexGoal;
import org.worldgrower.goal.UTestShackGoal;
import org.worldgrower.goal.UTestShrineToDeityGoal;
import org.worldgrower.goal.UTestSmithGoal;
import org.worldgrower.goal.UTestSocializeGoal;
import org.worldgrower.goal.UTestStartOrganizationVoteGoal;
import org.worldgrower.goal.UTestSubdueOutsidersGoal;
import org.worldgrower.goal.UTestTrainGoal;
import org.worldgrower.goal.UTestVampireBiteGoal;
import org.worldgrower.goal.UTestWeaveClothesGoal;
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
	UTestImmediateGoalConversation.class,
	UTestBecomeArenaFighterConversation.class,
	UTestProposeMateConversation.class,
	UTestBuyClothesGoal.class,
	UTestBuyHouseConversation.class,
	UTestCollectTaxesConversation.class,
	UTestVoteLeaderOrganizationConversation.class,
	UTestRelationshipConversation.class,
	UTestProfessionReasonConversation.class,
	UTestDeityPropertyUtils.class,
	UTestCommonerGenerator.class,
	UTestWhyNotIntelligentConversation.class,
	UTestActions.class,
	UTestKissConversation.class,
	UTestLearnSkillUsingOrganizationConversation.class,
	UTestLookTheSameConversation.class,
	UTestNotNicerConversation.class,
	UTestOperationInfo.class,
	UTestSawDisguisingConversation.class,
	UTestSacrificePeopleToDeityGoal.class,
	UTestProtectOneSelfGoal.class,
	UTestChildrenGoal.class,
	UTestMineResourceGoal.class,
	UTestCraftEquipmentGoal.class,
	UTestCottonGoal.class,
	UTestMateGoal.class,
	UTestEquipmentGoal.class,
	UTestWeaveClothesGoal.class,
	UTestGetPoisonCuredGoal.class,
	UTestShrineToDeityGoal.class,
	UTestDrinkWaterGoal.class,
	UTestCollectWaterGoal.class,
	UTestGhoulGoal.class,
	UTestImproveRelationshipGoal.class,
	UTestFishingPoleGoal.class,
	UTestFurnitureGoal.class,
	UTestGetHealedGoal.class,
	UTestFoodGoal.class,
	UTestGatherFoodGoal.class,
	UTestCreateFoodSourcesGoal.class,
	UTestImproveOrganizationGoal.class,
	UTestSubdueOutsidersGoal.class,
	UTestCaptureCriminalsGoal.class,
	UTestTrainGoal.class,
	UTestCreateWineGoal.class,
	UTestHealOthersGoal.class,
	UTestLibraryGoal.class,
	UTestPaperMillGoal.class,
	UTestSmithGoal.class,
	UTestFeedOthersGoal.class,
	UTestCreateHouseGoal.class,
	UTestSexGoal.class,
	UTestMarkFoodAsSellableGoal.class,
	UTestStartOrganizationVoteGoal.class,
	UTestOrganizationCandidateGoal.class,
	UTestShackGoal.class,
	UTestOrganizationVoteGoal.class,
	UTestKillVillagersGoal.class,
	UTestHuntUndeadGoal.class,
	UTestCreateFurnitureGoal.class,
	UTestPaperGoal.class,
	UTestMintGoldGoal.class,
	UTestCreateGraveGoal.class,
	UTestDestroyShrinesToOtherDeitiesGoal.class,
	UTestLearnSkillGoal.class,
	UTestCreateOrPlantWoodGoal.class,
	UTestDeathReasonPropertyUtils.class,
	UTestVampireBiteGoal.class,
	UTestRestGoal.class,
	UTestArenaGoal.class,
	UTestJailGoal.class,
	UTestGatherRemainsGoal.class,
	UTestMineGoldGoal.class,
	UTestScribeWizardSpellsGoal.class,
	UTestCatchFishGoal.class,
	UTestCollectArenaRewardGoal.class,
	UTestSetTaxesGoal.class,
	UTestFightInArenaGoal.class,
	UTestArenaFightGoal.class,
	UTestBrawlGoal.class,
	UTestSellWoodGoal.class,
	UTestDonateMoneyToArenaGoal.class,
	UTestBecomeReligionOrganizationMemberGoal.class,
	UTestBecomeProfessionsOrganizationMemberGoal.class,
	UTestResearchMagicSkillsKnowledgeGoal.class,
	UTestKillOutsidersGoal.class,
	UTestChooseDeityGoal.class,
	UTestCocoonOutsidersGoal.class,
	UTestReleaseCapturedCriminalsGoal.class,
	UTestHouseGoal.class,
	UTestMarkHouseAsSellableGoal.class,
	UTestOffspringGoal.class,
	UTestHandoverTaxesGoal.class,
	UTestCollectPayCheckGoal.class,
	UTestCollectTaxesGoal.class
})
public class CompleteTestSuite {
}
