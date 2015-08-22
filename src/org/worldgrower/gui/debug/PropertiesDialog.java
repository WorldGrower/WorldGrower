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
package org.worldgrower.gui.debug;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import org.worldgrower.WorldObject;


public class PropertiesDialog extends JDialog {

	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			PropertiesDialog.this.dispose();
		}
	}
	
	public PropertiesDialog(WorldObject worldObject) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 400, 800);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
        JTable propertiesTable = new JTable(new PropertiesModel(worldObject));
        JScrollPane tableScrollPane = new JScrollPane(propertiesTable);
		getContentPane().add(tableScrollPane);
		propertiesTable.addMouseListener(new PropertiesMouseAdapter());
	}
	
	public void showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private static class PropertiesMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			JTable table = (JTable) mouseEvent.getSource();
	        Point p = mouseEvent.getPoint();
	        int row = table.rowAtPoint(p);
	        if (mouseEvent.getClickCount() == 2) {
	        	JOptionPane.showMessageDialog(table, table.getValueAt(row, 1));
	        }
		}
	}
}
