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
import java.io.FileFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class SaveFileUtils {

	private static final String FILE_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
	private static final File SAVE_DIRECTORY = new File(System.getProperty("user.home"));
	
	public static File createNewSaveFile() {
		return new File(SAVE_DIRECTORY, getDefaultFilename());
	}
	
	private static String getDefaultFilename() {
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat(FILE_DATE_TIME_FORMAT);
		return format.format(currentTime) + ".sav";
	}
	
	public static Date getSaveTime(File file) {
		try {
			String saveTime = file.getName().substring(0, 14);
			SimpleDateFormat format = new SimpleDateFormat(FILE_DATE_TIME_FORMAT);
			return format.parse(saveTime);
		} catch (ParseException e) {
			throw new IllegalStateException("Problem parsing filename " + file, e);
		}
	}
	
	public static SaveGame[] getSaveFiles() {
		File[] files = SAVE_DIRECTORY.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".sav");
			}
		});
		
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return -Long.compare(o1.lastModified(), o2.lastModified());
			}
		});
		
		SaveGame[] saveGames = new SaveGame[files.length];
		for(int i=0; i<saveGames.length; i++) {
			saveGames[i] = new SaveGame(files[i], false);
		}
		return saveGames;
	}
}
