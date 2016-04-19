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
package org.worldgrower.actions;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.BrawlPropertyUtils;

public class BrawlListener implements ManagedOperationListener {

	public final List<BrawlFinishedListener> brawlFinishedListeners = new ArrayList<>(); 
	
	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if (BrawlPropertyUtils.isBrawling(performer) && BrawlPropertyUtils.isBrawling(target) && managedOperation == Actions.NON_LETHAL_MELEE_ATTACK_ACTION) {
			if (target.getProperty(Constants.HIT_POINTS) == 1) {
				int goldWon = BrawlPropertyUtils.endBrawlWithPerformerVictory(performer, target);
				
				for(BrawlFinishedListener brawlFinishedListener : brawlFinishedListeners) {
					brawlFinishedListener.brawlFinished(performer, target, goldWon);
				}
			}
		}
	}

	public void addBrawlFinishedListener(BrawlFinishedListener brawlFinishedListener) {
		brawlFinishedListeners.add(brawlFinishedListener);
	}
}
