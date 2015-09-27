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

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.worldgrower.ManagedOperation;

public class GuiAssignActionToLeftMouseAction extends AbstractAction {

	private List<ManagedOperation> actions;
	private WorldPanel parent;
	private GuiMouseListener guiMouseListener;
	
	public GuiAssignActionToLeftMouseAction(List<ManagedOperation> actions, WorldPanel parent, GuiMouseListener guiMouseListener) {
		super();
		this.actions = actions;
		this.parent = parent;
		this.guiMouseListener = guiMouseListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String[] actionDescriptions = getActionDescriptions().toArray(new String[0]);
		//String actionDescription = (String) JOptionPane.showInputDialog(parent, "Choose Action", "Choose Action", JOptionPane.QUESTION_MESSAGE, null, actionDescriptions, actionDescriptions[0]);
		
		AssignActionLeftMouseDialog assignActionLeftMouseDialog = new AssignActionLeftMouseDialog(actionDescriptions);
		String actionDescription = assignActionLeftMouseDialog.showMe();
		
		if (actionDescription != null) {
			int indexOfAction = Arrays.asList(actionDescriptions).indexOf(actionDescription);
			ManagedOperation action = actions.get(indexOfAction);
			guiMouseListener.setLeftMouseClickAction(action);
		}
	}
	
	private List<String> getActionDescriptions() {
		return actions.stream().map(a -> a.getSimpleDescription()).collect(Collectors.toList());
	}
}