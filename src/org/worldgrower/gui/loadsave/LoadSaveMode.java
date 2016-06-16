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
package org.worldgrower.gui.loadsave;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LoadSaveMode {
	LOAD("Load") {
		@Override
		public void handleFile(SaveGameHandler saveGameHandler, SaveGame saveGame) {
			saveGameHandler.loadGame(saveGame.getFile());
		}

		@Override
		public SaveGame[] getSaveFiles() {
			return SaveFileUtils.getSaveFiles();
		}
	},
	SAVE("Save") {
		@Override
		public void handleFile(SaveGameHandler saveGameHandler, SaveGame saveGame) {
			final File file;
			if (saveGame.isCreateNewFile()) {
				file = SaveFileUtils.createNewSaveFile();
			} else {
				file = saveGame.getFile();
			}
			saveGameHandler.saveGame(file);
		}

		@Override
		public SaveGame[] getSaveFiles() {
			List<SaveGame> saveGamesList = new ArrayList<>(Arrays.asList(SaveFileUtils.getSaveFiles()));
			saveGamesList.add(0, new SaveGame(null, true));
			
			return saveGamesList.toArray(new SaveGame[0]);
			
		}
	};
	
	private final String description;

	private LoadSaveMode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public abstract void handleFile(SaveGameHandler saveGameHandler, SaveGame saveGame);

	public abstract SaveGame[] getSaveFiles();
}
