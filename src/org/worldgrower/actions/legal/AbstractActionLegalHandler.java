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


public abstract class AbstractActionLegalHandler implements ActionLegalHandler {

	private boolean legalFlag;

	public AbstractActionLegalHandler(boolean legalFlag) {
		this.legalFlag = legalFlag;
	}

	@Override
	public final boolean getLegalFlag() {
		return legalFlag;
	}

	@Override
	public final ActionLegalHandler setLegalFlag(boolean legalFlag) {
		this.legalFlag = legalFlag;
		return this;
	}
	
	
}
