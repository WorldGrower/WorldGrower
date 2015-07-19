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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommonerNameGenerator implements Serializable {
	
	private List<String> maleCommonerNames = new ArrayList<>();
	private List<String> femaleCommonerNames = new ArrayList<>();

	private int currentMaleCommonerIndex = 0;
	private int currentFemaleCommonerIndex = 0;
	
	public CommonerNameGenerator() throws IOException {
		maleCommonerNames.addAll(readFile("resources/male_names.txt"));
		femaleCommonerNames.addAll(readFile("resources/female_names.txt"));
	}
	
	private List<String> readFile(String filename) throws IOException {
		return Files.readAllLines(Paths.get(filename));
	}
	
	public String getNextMaleCommonerName() {
		String name = maleCommonerNames.get(currentMaleCommonerIndex);
		currentMaleCommonerIndex = ((currentMaleCommonerIndex+1) % maleCommonerNames.size());
		return name;
	}

	public String getNextFemaleCommonerName() {
		String name = femaleCommonerNames.get(currentFemaleCommonerIndex);
		currentFemaleCommonerIndex = ((currentFemaleCommonerIndex+1) % femaleCommonerNames.size());
		return name;
	}
}