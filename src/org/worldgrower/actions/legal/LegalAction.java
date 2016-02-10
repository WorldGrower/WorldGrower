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

public final class LegalAction implements Serializable {

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

	public ActionLegalHandler getActionLegalHandler() {
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
}
