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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.util.FileUtils;

/**
 * This class provides unique names for commoners.
 */
public class CommonerNameGeneratorImpl implements CommonerNameGenerator, Serializable {
	
	private List<String> maleCommonerNames = new ArrayList<>();
	private List<String> femaleCommonerNames = new ArrayList<>();

	private int currentMaleCommonerIndex = 0;
	private int currentFemaleCommonerIndex = 0;
	
	private final String nameToSkip;
	
	public CommonerNameGeneratorImpl(String nameToSkip) throws IOException {
		maleCommonerNames.addAll(FileUtils.readFile("/male_names.txt"));
		femaleCommonerNames.addAll(FileUtils.readFile("/female_names.txt"));
		
		this.nameToSkip = nameToSkip;
	}
		
	@Override
	public String getNextMaleCommonerName() {
		String name = maleCommonerNames.get(currentMaleCommonerIndex);
		currentMaleCommonerIndex = ((currentMaleCommonerIndex+1) % maleCommonerNames.size());
		if (name.equals(nameToSkip)) {
			name = getNextMaleCommonerName();
		}
		
		return name;
	}

	@Override
	public String getNextFemaleCommonerName() {
		String name = femaleCommonerNames.get(currentFemaleCommonerIndex);
		currentFemaleCommonerIndex = ((currentFemaleCommonerIndex+1) % femaleCommonerNames.size());
		if (name.equals(nameToSkip)) {
			name = getNextFemaleCommonerName();
		}
		
		return name;
	}
}