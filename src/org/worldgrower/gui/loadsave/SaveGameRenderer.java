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
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.worldgrower.IncompatibleVersionException;
import org.worldgrower.SaveGameStatistics;
import org.worldgrower.WorldImpl;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.JLabelFactory;

class SaveGameRenderer extends JLabel implements ListCellRenderer<SaveGame> {
	private final ImageInfoReader imageInfoReader;
	private final JLabel rendererLabel = JLabelFactory.createJLabel("");

	public SaveGameRenderer(ImageInfoReader imageInfoReader) {
		this.imageInfoReader = imageInfoReader;
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
			Image image = imageInfoReader.getImage(ImageIds.PLUS, null);
			rendererLabel.setIcon(new ImageIcon(image));
		} else {
			SaveGameStatistics saveGameStatistics;
			try {
				saveGameStatistics = WorldImpl.getSaveGameStatistics(value.getFile());
				
				String saveDateDescription = getSaveDateDescription(value);
				rendererLabel.setText(saveGameStatistics.getPlayerCharacterName() + ", level " + saveGameStatistics.getPlayerCharacterLevel() + " at turn " + saveGameStatistics.getTurn() + ", saved on " + saveDateDescription);
				
				Image image = imageInfoReader.getImage(saveGameStatistics.getPlayerCharacterImageId(), null);
				rendererLabel.setIcon(new ImageIcon(image));
			} catch (IncompatibleVersionException e) {
				rendererLabel.setText("<incompatible save game>");
			}
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