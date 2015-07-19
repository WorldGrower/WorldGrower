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
import org.worldgrower.actions.UTestChooseProfessionAction;
import org.worldgrower.actions.UTestTalkAction;
import org.worldgrower.attribute.UTestIdList;
import org.worldgrower.attribute.UTestIdMap;
import org.worldgrower.attribute.UTestWorldObjectProperties;
import org.worldgrower.conversation.UTestConversations;
import org.worldgrower.conversation.UTestLocationConversation;
import org.worldgrower.goal.UTestBuildLocationUtils;
import org.worldgrower.goal.UTestGoalUtils;
import org.worldgrower.goal.UTestZone;
import org.worldgrower.history.UTestHistoryImpl;

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
	UTestLocationConversation.class
})
public class CompleteTestSuite {
}
