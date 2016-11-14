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

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.worldgrower.SaveGameStatistics;
import org.worldgrower.WorldImpl;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.util.JLabelFactory;

class SaveGameRenderer extends JLabel implements ListCellRenderer<SaveGame> {
	private final JLabel rendererLabel = JLabelFactory.createJLabel("");

	public SaveGameRenderer() {
		this.rendererLabel.setOpaque(false);
	}
	
	@Override
    public Component getListCellRendererComponent(JList<? extends SaveGame> list,
    											SaveGame value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {

		if (value.isCreateNewFile()) {
			rendererLabel.setText("Create new save");
		} else {
			SaveGameStatistics saveGameStatistics = WorldImpl.getSaveGameStatistics(value.getFile());
			String saveDateDescription = getSaveDateDescription(value);
			rendererLabel.setText(saveGameStatistics.getPlayerCharacterName() + ", level " + saveGameStatistics.getPlayerCharacterLevel() + " at turn " + saveGameStatistics.getTurn() + ", saved on " + saveDateDescription);
		}
		
		if (isSelected) {
			rendererLabel.setBackground(ColorPalette.LIGHT_BACKGROUND_COLOR);
		} else {
			rendererLabel.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		}
		
        return rendererLabel;
    }

	private String getSaveDateDescription(SaveGame value) {
		Date saveDate = SaveFileUtils.getSaveTime(value.getFile());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd HH:mm:ss");
		String saveDateDescription = simpleDateFormat.format(saveDate);
		return saveDateDescription;
	}
}