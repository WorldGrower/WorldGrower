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
import org.worldgrower.actions.*;
import org.worldgrower.actions.magic.UTestAnimateDeadAction;
import org.worldgrower.actions.magic.UTestAnimateSuitOfArmorAction;
import org.worldgrower.actions.magic.UTestBurdenAction;
import org.worldgrower.actions.magic.UTestCureDiseaseAction;
import org.worldgrower.actions.magic.UTestCurePoisonAction;
import org.worldgrower.actions.magic.UTestDarkVisionSpellAction;
import org.worldgrower.actions.magic.UTestDetectMagicAction;
import org.worldgrower.actions.magic.UTestDetectPoisonAndDiseaseAction;
import org.worldgrower.actions.magic.UTestDisguiseMagicSpellAction;
import org.worldgrower.actions.magic.UTestDisintegrateArmorAction;
import org.worldgrower.actions.magic.UTestDisintegrateWeaponAction;
import org.worldgrower.actions.magic.UTestDispelMagicAction;
import org.worldgrower.actions.magic.UTestEnlargeAction;
import org.worldgrower.actions.magic.UTestEntangleAction;
import org.worldgrower.actions.magic.UTestFearMagicSpellAction;
import org.worldgrower.actions.magic.UTestFeatherAction;
import org.worldgrower.actions.magic.UTestFireBoltAttackAction;
import org.worldgrower.actions.magic.UTestFireTrapAction;
import org.worldgrower.actions.magic.UTestInflictWoundsAction;
import org.worldgrower.actions.magic.UTestInvisibilityAction;
import org.worldgrower.actions.magic.UTestLichTransformationAction;
import org.worldgrower.actions.magic.UTestLightningBoltAttackAction;
import org.worldgrower.actions.magic.UTestLockMagicSpellAction;
import org.worldgrower.actions.magic.UTestMendAction;
import org.worldgrower.actions.magic.UTestMinorHealAction;
import org.worldgrower.actions.magic.UTestMinorIllusionAction;
import org.worldgrower.actions.magic.UTestParalyzeAction;
import org.worldgrower.actions.magic.UTestRayOfFrostAttackAction;
import org.worldgrower.actions.magic.UTestReduceAction;
import org.worldgrower.actions.magic.UTestResearchSpellAction;
import org.worldgrower.actions.magic.UTestScribeMagicSpellAction;
import org.worldgrower.actions.magic.UTestSilenceMagicAction;
import org.worldgrower.actions.magic.UTestSleepMagicAction;
import org.worldgrower.actions.magic.UTestSoulTrapAction;
import org.worldgrower.actions.magic.UTestTrapContainerMagicSpellAction;
import org.worldgrower.actions.magic.UTestUnlockMagicSpellAction;
import org.worldgrower.actions.magic.UTestWaterWalkAction;
import org.worldgrower.attribute.UTestIdList;
import org.worldgrower.attribute.UTestIdMap;
import org.worldgrower.attribute.UTestIntProperty;
import org.worldgrower.attribute.UTestItemCountMap;
import org.worldgrower.attribute.UTestKnowledgeMap;
import org.worldgrower.attribute.UTestPrices;
import org.worldgrower.attribute.UTestPropertyCountMap;
import org.worldgrower.attribute.UTestSkill;
import org.worldgrower.attribute.UTestSkillUtils;
import org.worldgrower.attribute.UTestWorldObjectProperties;
import org.worldgrower.condition.UTestConditions;
import org.worldgrower.condition.UTestFearCondition;
import org.worldgrower.condition.UTestGhoulUtils;
import org.worldgrower.condition.UTestIntoxicatedCondition;
import org.worldgrower.condition.UTestSoulTrappedCondition;
import org.worldgrower.condition.UTestVampireBiteCondition;
import org.worldgrower.condition.UTestVampireUtils;
import org.worldgrower.condition.UTestWerewolfUtils;
import org.worldgrower.conversation.UTestArenaFighterPayCheckConversation;
import org.worldgrower.conversation.UTestAskGoalConversation;
import org.worldgrower.conversation.UTestAssassinateTargetConversation;
import org.worldgrower.conversation.UTestBecomeArenaFighterConversation;
import org.worldgrower.conversation.UTestBrawlConversation;
import org.worldgrower.conversation.UTestBreakupWithMateConversation;
import org.worldgrower.conversation.UTestBuyHouseConversation;
import org.worldgrower.conversation.UTestCollectPayCheckConversation;
import org.worldgrower.conversation.UTestCollectTaxesConversation;
import org.worldgrower.conversation.UTestComplimentConversation;
import org.worldgrower.conversation.UTestConversations;
import org.worldgrower.conversation.UTestCureDiseaseConversation;
import org.worldgrower.conversation.UTestCurePoisonConversation;
import org.worldgrower.conversation.UTestDeityFollowersConversation;
import org.worldgrower.conversation.UTestDeityReasonConversation;
import org.worldgrower.conversation.UTestDemandMoneyConversation;
import org.worldgrower.conversation.UTestDemandsConversation;
import org.worldgrower.conversation.UTestDrinkingContestConversation;
import org.worldgrower.conversation.UTestFamilyConversation;
import org.worldgrower.conversation.UTestGiveFoodConversation;
import org.worldgrower.conversation.UTestGiveMoneyConversation;
import org.worldgrower.conversation.UTestImmediateGoalConversation;
import org.worldgrower.conversation.UTestIntimidateConversation;
import org.worldgrower.conversation.UTestJoinPerformerOrganizationConversation;
import org.worldgrower.conversation.UTestJoinTargetOrganizationConversation;
import org.worldgrower.conversation.UTestKissConversation;
import org.worldgrower.conversation.UTestKnowledgeSorter;
import org.worldgrower.conversation.UTestKnowledgeToDescriptionMapper;
import org.worldgrower.conversation.UTestLearnSkillUsingOrganizationConversation;
import org.worldgrower.conversation.UTestLocationConversation;
import org.worldgrower.conversation.UTestLookTheSameConversation;
import org.worldgrower.conversation.UTestMergeOrganizationsConversation;
import org.worldgrower.conversation.UTestMinorHealConversation;
import org.worldgrower.conversation.UTestNicerConversation;
import org.worldgrower.conversation.UTestNotNicerConversation;
import org.worldgrower.conversation.UTestOrganizationConversation;
import org.worldgrower.conversation.UTestProfessionConversation;
import org.worldgrower.conversation.UTestProfessionPractitionersConversation;
import org.worldgrower.conversation.UTestProfessionReasonConversation;
import org.worldgrower.conversation.UTestProposeMateConversation;
import org.worldgrower.conversation.UTestRelationshipConversation;
import org.worldgrower.conversation.UTestSawDisguisingConversation;
import org.worldgrower.conversation.UTestSellHouseConversation;
import org.worldgrower.conversation.UTestSetOrganizationProfitPercentageConversation;
import org.worldgrower.conversation.UTestShareKnowledgeConversation;
import org.worldgrower.conversation.UTestStartArenaFightConversation;
import org.worldgrower.conversation.UTestStopSellingConversation;
import org.worldgrower.conversation.UTestSwitchDeityConversation;
import org.worldgrower.conversation.UTestVoteLeaderOrganizationConversation;
import org.worldgrower.conversation.UTestWhoIsLeaderOrganizationConversation;
import org.worldgrower.conversation.UTestWhyAngryConversation;
import org.worldgrower.conversation.UTestWhyAngryOtherConversation;
import org.worldgrower.conversation.UTestWhyNotIntelligentConversation;
import org.worldgrower.conversation.leader.UTestCanAttackCriminalsConversation;
import org.worldgrower.conversation.leader.UTestCanCollectTaxesConversation;
import org.worldgrower.conversation.leader.UTestSetHouseTaxRateConversation;
import org.worldgrower.conversation.leader.UTestSetShackTaxRateConversation;
import org.worldgrower.curse.UTestVampireCurse;
import org.worldgrower.curse.UTestWerewolfCurse;
import org.worldgrower.deity.UTestAphrodite;
import org.worldgrower.deity.UTestApollo;
import org.worldgrower.deity.UTestAres;
import org.worldgrower.deity.UTestArtemis;
import org.worldgrower.deity.UTestAthena;
import org.worldgrower.deity.UTestDeity;
import org.worldgrower.deity.UTestDeityPropertyUtils;
import org.worldgrower.deity.UTestDemeter;
import org.worldgrower.deity.UTestDionysus;
import org.worldgrower.deity.UTestHades;
import org.worldgrower.deity.UTestHephaestus;
import org.worldgrower.deity.UTestHera;
import org.worldgrower.deity.UTestHermes;
import org.worldgrower.deity.UTestPoseidon;
import org.worldgrower.deity.UTestZeus;
import org.worldgrower.generator.UTestBeastOnTurn;
import org.worldgrower.generator.UTestBerryBushOnTurn;
import org.worldgrower.generator.UTestBuildingGenerator;
import org.worldgrower.generator.UTestCommonerGenerator;
import org.worldgrower.generator.UTestCommonerOnTurn;
import org.worldgrower.generator.UTestCowOnTurn;
import org.worldgrower.generator.UTestCreatureGenerator;
import org.worldgrower.generator.UTestFiretrapOnTurn;
import org.worldgrower.generator.UTestFishOnTurn;
import org.worldgrower.generator.UTestIllusionOnTurn;
import org.worldgrower.generator.UTestItem;
import org.worldgrower.generator.UTestNightShadeOnTurn;
import org.worldgrower.generator.UTestPlantOnTurn;
import org.worldgrower.generator.UTestSkeletonEvaluationFunction;
import org.worldgrower.generator.UTestVotingOnTurn;
import org.worldgrower.goal.*;
import org.worldgrower.gui.UTestImageInfoReader;
import org.worldgrower.history.UTestHistoryImpl;
import org.worldgrower.personality.UTestPersonality;
import org.worldgrower.personality.UTestPersonalityTraitValue;
import org.worldgrower.profession.UTestArenaFighterProfession;
import org.worldgrower.profession.UTestArenaOwnerProfession;
import org.worldgrower.profession.UTestAssassinProfession;
import org.worldgrower.profession.UTestBlacksmithProfession;
import org.worldgrower.profession.UTestCarpenterProfession;
import org.worldgrower.profession.UTestFarmerProfession;
import org.worldgrower.profession.UTestFisherProfession;
import org.worldgrower.profession.UTestGraveDiggerProfession;
import org.worldgrower.profession.UTestJournalistProfession;
import org.worldgrower.profession.UTestLumberjackProfession;
import org.worldgrower.profession.UTestMinerProfession;
import org.worldgrower.profession.UTestNecromancerProfession;
import org.worldgrower.profession.UTestPlayerCharacterProfession;
import org.worldgrower.profession.UTestPriestProfession;
import org.worldgrower.profession.UTestProfessions;
import org.worldgrower.profession.UTestSheriffProfession;
import org.worldgrower.profession.UTestTaxCollectorProfession;
import org.worldgrower.profession.UTestThiefProfession;
import org.worldgrower.profession.UTestWeaverProfession;
import org.worldgrower.profession.UTestWizardProfession;
import org.worldgrower.terrain.UTestTerrainImpl;
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
	UTestRecruitProfessionOrganizationMembersGoal.class,
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
	UTestCollectTaxesGoal.class,
	UTestCommonerOnTurn.class,
	UTestCowOnTurn.class,
	UTestFishOnTurn.class,
	UTestVotingOnTurn.class,
	UTestPlantOnTurn.class,
	UTestSkeletonEvaluationFunction.class,
	UTestIllusionOnTurn.class,
	UTestNightShadeOnTurn.class,
	UTestBerryBushOnTurn.class,
	UTestWaterPropertyUtils.class,
	UTestSellHouseGoal.class,
	UTestRevengeGoal.class,
	UTestChooseProfessionGoal.class,
	UTestCreatePaperGoal.class,
	UTestCreatePoisonGoal.class,
	UTestPregnancyPropertyUtils.class,
	UTestSwitchDeityConversation.class,
	UTestItemCountMap.class,
	UTestBeastOnTurn.class,
	UTestBuildingGenerator.class,
	UTestBuildHouseAction.class,
	UTestBuildShackAction.class,
	UTestDrinkAction.class,
	UTestDrinkFromInventoryAction.class,
	UTestBuyAction.class,
	UTestSellAction.class,
	UTestSleepAction.class,
	UTestCollectWaterAction.class,
	UTestStealAction.class,
	UTestEatNightShadeAction.class,
	UTestSetLegalActionsAction.class,
	UTestBuildWellAction.class,
	UTestBuildShrineAction.class,
	UTestBuildArenaAction.class,
	UTestBuildInnAction.class,
	UTestBuildSacrificialAltarAction.class,
	UTestBuildJailAction.class,
	UTestBuildPaperMillAction.class,
	UTestCreateGraveAction.class,
	UTestConstructTrainingDummyAction.class,
	UTestEatAction.class,
	UTestBuildSmithAction.class,
	UTestBuildLibraryAction.class,
	UTestPlantBerryBushAction.class,
	UTestMineOreAction.class,
	UTestMineStoneAction.class,
	UTestMineSoulGemsAction.class,
	UTestCutWoodAction.class,
	UTestHarvestFoodAction.class,
	UTestHarvestGrapesAction.class,
	UTestRepairEquipmentInInventoryAction.class,
	UTestMergeOrganizationsConversation.class,
	UTestAskGoalConversation.class,
	UTestStopSellingConversation.class,
	UTestEatFromInventoryAction.class,
	UTestPlantGrapeVineAction.class,
	UTestDonateMoneyAction.class,
	UTestHarvestNightShadeAction.class,
	UTestMineGoldAction.class,
	UTestCatchFishAction.class,
	UTestExtractOilAction.class,
	UTestPutItemInInventoryAction.class,
	UTestGetItemFromInventoryAction.class,
	UTestReduceAction.class,
	UTestEnlargeAction.class,
	UTestMendAction.class,
	UTestBurdenAction.class,
	UTestFeatherAction.class,
	UTestDisintegrateArmorAction.class,
	UTestDisintegrateWeaponAction.class,
	UTestDispelMagicAction.class,
	UTestFireBoltAttackAction.class,
	UTestInflictWoundsAction.class,
	UTestRayOfFrostAttackAction.class,
	UTestSilenceMagicAction.class,
	UTestSleepMagicAction.class,
	UTestWaterWalkAction.class,
	UTestInvisibilityAction.class,
	UTestSoulTrapAction.class,
	UTestCureDiseaseAction.class,
	UTestLockMagicSpellAction.class,
	UTestUnlockMagicSpellAction.class,
	UTestLightningBoltAttackAction.class,
	UTestAnimateSuitOfArmorAction.class,
	UTestResearchSpellAction.class,
	UTestAnimateDeadAction.class,
	UTestDetectPoisonAndDiseaseAction.class,
	UTestDetectMagicAction.class,
	UTestVoteForLeaderAction.class,
	UTestBecomeLeaderCandidateAction.class,
	UTestStartOrganizationVoteAction.class,
	UTestCraftIronAxeAction.class,
	UTestCraftIronCuirassAction.class,
	UTestCraftIronBootsAction.class,
	UTestCraftIronClaymoreAction.class,
	UTestCraftIronGauntletsAction.class,
	UTestCraftIronGreavesAction.class,
	UTestCraftIronShieldAction.class,
	UTestCraftIronGreataxeAction.class,
	UTestCraftIronGreatswordAction.class,
	UTestCraftLongBowAction.class,
	UTestCraftRepairHammerAction.class,
	UTestCreateHumanMeatAction.class,
	UTestStopSellingGoal.class,
	UTestCreateReligionOrganizationAction.class,
	UTestCreateProfessionOrganizationAction.class,
	UTestChooseDeityAction.class,
	UTestKnowledgeToDescriptionMapper.class,
	UTestMeleeAttackAction.class,
	UTestRangedAttackAction.class,
	UTestPoisonInventoryWaterAction.class,
	UTestArenaFightOnTurn.class,
	UTestThrowOilAction.class,
	UTestButcherAction.class,
	UTestSexAction.class,
	UTestBrawlListener.class,
	UTestMintGoldAction.class,
	UTestHandoverTaxesAction.class,
	UTestCapturePersonAction.class,
	UTestCapturePersonForSacrificeAction.class,
	UTestPoisonAction.class,
	UTestOperationStatistics.class,
	UTestPoisonAttackAction.class,
	UTestDrinkingContestListener.class,
	UTestDrinkingContestConversation.class,
	UTestParalyzeAction.class,
	UTestPlantTreeAction.class,
	UTestCraftIronHelmetAction.class,
	UTestCreatePaperAction.class,
	UTestEquipInventoryItemAction.class,
	UTestPlantNightShadeAction.class,
	UTestPlantCottonPlantAction.class,
	UTestBrewWineAction.class,
	UTestWeaveCottonBootsAction.class,
	UTestWeaveCottonShirtAction.class,
	UTestWeaveCottonPantsAction.class,
	UTestWeaveCottonHatAction.class,
	UTestWeaveCottonGlovesAction.class,
	UTestBrewPoisonAction.class,
	UTestRestAction.class,
	UTestHarvestCottonAction.class,
	UTestConstructBedAction.class,
	UTestConstructFishingPoleAction.class,
	UTestMarkInventoryItemAsSellableAction.class,
	UTestGatherRemainsAction.class,
	UTestCommandAction.class,
	UTestKissAction.class,
	UTestKnowledgeSorter.class,
	UTestMarkAsSellableAction.class,
	UTestItem.class,
	UTestCreateNewsPaperAction.class,
	UTestReadItemInInventoryAction.class,
	UTestDisguiseMagicSpellAction.class,
	UTestSoulTrappedCondition.class,
	UTestWerewolfUtils.class,
	UTestIntoxicatedCondition.class,
	UTestVampireBiteCondition.class,
	UTestSetShackTaxRateConversation.class,
	UTestSetHouseTaxRateConversation.class,
	UTestCanAttackCriminalsConversation.class,
	UTestCommonerNameGeneratorImpl.class,
	UTestMetaInformation.class,
	UTestProfessions.class,
	UTestSkill.class,
	UTestVampireUtils.class,
	UTestDeity.class,
	UTestDionysus.class,
	UTestAres.class,
	UTestAphrodite.class,
	UTestDemeter.class,
	UTestHades.class,
	UTestHermes.class,
	UTestAthena.class,
	UTestHephaestus.class,
	UTestArtemis.class,
	UTestApollo.class,
	UTestCanCollectTaxesConversation.class,
	UTestKnowledgePropertyUtils.class,
	UTestReadNewsPaperGoal.class,
	UTestCreateNewsPaperGoal.class,
	UTestWineGoal.class,
	UTestGoal.class,
	UTestVampireBiteAction.class,
	UTestGhoulUtils.class,
	UTestStartBrawlGoal.class,
	UTestTerrainImpl.class,
	UTestWorshipDeityAction.class,
	UTestDetermineDeathReasonAction.class,
	UTestObfuscateDeathReasonAction.class,
	UTestReadAction.class,
	UTestStealGoal.class,
	UTestSellStolenGoodsGoal.class,
	UTestEquipmentPropertyUtils.class,
	UTestDisguiseAction.class,
	UTestSetTaxRateAction.class,
	UTestUnpassableCreaturePositionCondition.class,
	UTestNonLethalMeleeAttackAction.class,
	UTestInvestigateAction.class,
	UTestPersonalityTraitValue.class,
	UTestPersonality.class,
	UTestFireTrapAction.class,
	UTestMinorHealAction.class,
	UTestRepairHammerGoal.class,
	UTestRepairEquipmentGoal.class,
	UTestCurePoisonAction.class,
	UTestStartDrinkingContestGoal.class,
	UTestBreakupWithMateConversation.class,
	UTestBreakupWithMateGoal.class,
	UTestLegalizeVampirismGoal.class,
	UTestMinorIllusionAction.class,
	UTestScribeMagicSpellAction.class,
	UTestImageInfoReader.class,
	UTestUnlockJailDoorAction.class,
	UTestCreateBloodAction.class,
	UTestLichTransformationAction.class,
	UTestFillSoulGemGoal.class,
	UTestBecomeLichGoal.class,
	UTestCollectPayCheckConversation.class,
	UTestCureDiseaseConversation.class,
	UTestGetDiseaseCuredGoal.class,
	UTestSwitchDeityGoal.class,
	UTestVampireCurse.class,
	UTestWerewolfCurse.class,
	UTestChildrenPropertyUtils.class,
	UTestProfessionConversation.class,
	UTestFiretrapOnTurn.class,
	UTestDefaultGoalChangedListener.class,
	UTestOreGoal.class,
	UTestAttackTargetGoal.class,
	UTestCraftIronMaceAction.class,
	UTestDoNothingAction.class,
	UTestMoveAction.class,
	UTestAssassinateTargetConversation.class,
	UTestAssassinateTargetGoal.class,
	UTestFindAssassinationGoal.class,
	UTestResearchEvocationSkillAction.class,
	UTestCraftIronKatarAction.class,
	UTestBrewSleepingPotionAction.class,
	UTestSleepingPoisonAction.class,
	UTestPoisonInventoryWaterWithSleepingPotionAction.class,
	UTestWorldObjectFacade.class,
	UTestCreateSleepingPotionGoal.class,
	UTestClaimBuildingAction.class,
	UTestPoisonWeaponAction.class,
	UTestStandStillToTalkGoal.class,
	UTestPrices.class,
	UTestPoisonWeaponGoal.class,
	UTestHera.class,
	UTestZeus.class,
	UTestPoseidon.class,
	UTestGiveMoneyConversation.class,
	UTestRedistributeGoldAmongFamilyGoal.class,
	UTestFearMagicSpellAction.class,
	UTestEnergyPropertyUtils.class,
	UTestArenaFighterProfession.class,
	UTestArenaOwnerProfession.class,
	UTestAssassinProfession.class,
	UTestBlacksmithProfession.class,
	UTestCarpenterProfession.class,
	UTestFarmerProfession.class,
	UTestFisherProfession.class,
	UTestGraveDiggerProfession.class,
	UTestJournalistProfession.class,
	UTestLumberjackProfession.class,
	UTestMinerProfession.class,
	UTestNecromancerProfession.class,
	UTestPlayerCharacterProfession.class,
	UTestPriestProfession.class,
	UTestSheriffProfession.class,
	UTestTaxCollectorProfession.class,
	UTestThiefProfession.class,
	UTestWeaverProfession.class,
	UTestWizardProfession.class,
	UTestWeaveryGoal.class,
	UTestBuildWeaveryAction.class,
	UTestEntangleAction.class,
	UTestWorkbenchGoal.class,
	UTestBuildWorkbenchAction.class,
	UTestPerceptionPropertyUtils.class,
	UTestBreweryGoal.class,
	UTestStealLifeAction.class,
	UTestLocationWorldObjectsCache.class,
	UTestBuildBreweryAction.class,
	UTestStealGoldAction.class,
	UTestDarkVisionSpellAction.class,
	UTestWorldOnTurnImpl.class,
	UTestFearCondition.class,
	UTestTrapContainerMagicSpellAction.class,
	UTestContainerUtils.class,
	UTestLeashAction.class,
	UTestUnleashAction.class,
	UTestClaimCattleAction.class,
	UTestClaimBuildingAction.class
})
public class CompleteTestSuite {
}
