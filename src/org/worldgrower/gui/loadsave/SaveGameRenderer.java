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

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.worldgrower.gui.util.JLabelFactory;

class SaveGameRenderer extends JLabel implements ListCellRenderer<SaveGame> {
	private final JLabel rendererLabel = JLabelFactory.createJLabel("");

	@Override
    public Component getListCellRendererComponent(JList<? extends SaveGame> list,
    											SaveGame value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {

		if (value.isCreateNewFile()) {
			rendererLabel.setText("Create new save");
		} else {
			rendererLabel.setText(value.getFile().getName());
		}
		
        return rendererLabel;
    }
}