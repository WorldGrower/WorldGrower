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
package org.worldgrower.actions.legal;

import java.io.Serializable;

import org.worldgrower.ManagedOperation;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;
import org.worldgrower.gui.ImageIds;

/**
 * A LegalAction describes whether a certain action is legal
 */
public final class LegalAction implements Serializable {

	public static final LegalAction MELEE_ATTACK = new LegalAction(Actions.MELEE_ATTACK_ACTION, new AttackActionLegalHandler());
	public static final LegalAction FIRE_BOLT = new LegalAction(Actions.FIRE_BOLT_ATTACK_ACTION, new AttackActionLegalHandler());
	public static final LegalAction VAMPIRE_BITE = new LegalAction(Actions.VAMPIRE_BITE_ACTION, new AttackActionLegalHandler());
	public static final LegalAction BUTCHER = new LegalAction(Actions.BUTCHER_ACTION, new ButcherLegalHandler());
	
	private final ManagedOperation action;
	private final ActionLegalHandler actionLegalHandler;

	public LegalAction(ManagedOperation action, ActionLegalHandler actionLegalHandler) {
		super();
		this.action = action;
		this.actionLegalHandler = actionLegalHandler;
	}

	public ManagedOperation getAction() {
		return action;
	}

	ActionLegalHandler getActionLegalHandler() {
		return actionLegalHandler;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + action.hashCode();
		result = prime * result + actionLegalHandler.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LegalAction other = (LegalAction) obj;
		return (this.action == other.action) && (this.actionLegalHandler.equals(other.actionLegalHandler));
	}

	public String getDescription() {
		return action.getSimpleDescription() + " " + actionLegalHandler.getSimpleDescription();
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	public static LegalAction getWorshipLegalActionFor(Deity deity) {
		return new LegalAction(Actions.WORSHIP_DEITY_ACTION, new WorshipDeityLegalHandler(deity));
	}

	public ImageIds getImageId() {
		return action.getImageIds();
	}
}
