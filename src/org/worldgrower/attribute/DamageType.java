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
package org.worldgrower.attribute;


public enum DamageType {
	SLASHING("slashed to death"),
	PIERCING("having internal organs pierced to death"),
	BLUDGEONING("bludgeoned to death"),
	FIRE("burned to death"),
	ICE("frozen to death"),
	LIGHTNING("electrocuted to death"),
	NECROTIC("killed by necrotic damage");
	
	private final String deathDescription;

	private DamageType(String deathDescription) {
		this.deathDescription = deathDescription;
	}

	public String getDeathDescription() {
		return deathDescription;
	}
}
