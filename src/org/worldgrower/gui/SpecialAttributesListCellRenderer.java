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
package org.worldgrower.gui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;

import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JProgressBarFactory;

public class SpecialAttributesListCellRenderer implements ListCellRenderer<SpecialAttribute> {

	private final ImageInfoReader imageInfoReader;

	public SpecialAttributesListCellRenderer(ImageInfoReader imageInfoReader) {
		super();
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends SpecialAttribute> list, SpecialAttribute item, int index, boolean isSelected, boolean cellHasFocus) {
		JPanel panel = JPanelFactory.createBorderlessPanel();
		panel.setToolTipText(item.getLongDescription());
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblItem = JLabelFactory.createJLabel(item.getDescription());
		lblItem.setBounds(3, 13, 80, 20);
		lblItem.setToolTipText(item.getLongDescription());
		panel.add(lblItem);
		
		JProgressBar itemProgressBar = JProgressBarFactory.createHorizontalJProgressBar(0, item.getMaxValue(), imageInfoReader);
		itemProgressBar.setBounds(120, 13, 110, 20);
		itemProgressBar.setValue(item.getCurrentValue());
		itemProgressBar.setToolTipText(item.getLongDescription());
		panel.add(itemProgressBar);
		
		return panel;
	}
}