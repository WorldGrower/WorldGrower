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
package org.worldgrower.deity;

import java.io.ObjectStreamException;
import java.util.Arrays;
import java.util.List;

public class Demeter implements Deity {

	@Override
	public String getName() {
		return "Demeter";
	}

	@Override
	public String getExplanation() {
		return getName() + " is the God of harvest, sacred laws and life and death.";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public List<String> getReasons() {
		return Arrays.asList(
				"As a child, I was always scared of food running out. I worship " + getName() + " so that it never happens again.",
				"Our existance depends on nature. That is why I worship " + getName()
		);
	}
}
