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
package org.worldgrower.gui.conversation;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;

import org.worldgrower.conversation.ConversationFormatter;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.ImageSubstituter;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JProgressBarFactory;

public class RespondToQuestionDialog extends AbstractDialog {

	private final JComboBox<Response> comboBoxResponse;
	private final JLabel label;
	private int selectedResponse = -1;
	private final JProgressBar relationshipProgresBar;
	private final ImageInfoReader imageInfoReader;
	private final ConversationFormatter conversationFormatter;
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			RespondToQuestionDialog.this.dispose();
		}
	}

	public RespondToQuestionDialog(int id, int conversationId, int historyItemId, int additionalValue, int additionalValue2, Questioner questioner, Conversations conversations, ImageIds imageIdPerformer, ImageIds imageIdTarget, String performerName, String targetName, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, JFrame parentFrame) {
		super(600, 300, imageInfoReader);
		this.imageInfoReader = imageInfoReader;
		ImageSubstituter imageSubstituter = new ImageSubstituter(imageInfoReader);
		conversationFormatter = new ConversationFormatterImpl(imageSubstituter);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(470, 205, 112, 48);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setOpaque(false);
		addComponent(buttonPane);

		JButton cancelButton = JButtonFactory.createButton("Cancel", imageInfoReader, soundIdReader);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		JButton okButton = JButtonFactory.createButton("OK", imageInfoReader, soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JLabel targetLabel = JLabelFactory.createJLabel(imageInfoReader.getImage(imageIdTarget, null));
		targetLabel.setToolTipText(targetName);
		targetLabel.setBounds(6, 90, 32, 48);
		addComponent(targetLabel);
		
		comboBoxResponse = createResponseComboBox(id, conversationId, historyItemId, conversations, questioner, additionalValue, additionalValue2);
		comboBoxResponse.setBounds(44, 86, 515, 55);
		comboBoxResponse.setForeground(ColorPalette.FOREGROUND_COLOR);
		SwingUtils.setComboBoxSelectionColor(comboBoxResponse, ColorPalette.FOREGROUND_COLOR);
		
		addComponent(comboBoxResponse);
		
		JLabel performerLabel = JLabelFactory.createJLabel(imageInfoReader.getImage(imageIdPerformer, null));
		performerLabel.setToolTipText(performerName);
		performerLabel.setBounds(6, 17, 32, 48);
		addComponent(performerLabel);
		
		String questionPhrase = questioner.getQuestionPhrase(conversationFormatter);
		label = JLabelFactory.createJLabel("<html>"+ questionPhrase +"</html>");
		label.setBounds(44, 27, 515, 46);
		addComponent(label);
		
		JLabel relationshipLabel = JLabelFactory.createJLabel("Relationship:");
		relationshipLabel.setToolTipText("Relationship with " + performerName);
		relationshipLabel.setBounds(6, 210, 100, 30);
		addComponent(relationshipLabel);
		
		relationshipProgresBar = JProgressBarFactory.createHorizontalJProgressBar(-1000, 1000, imageInfoReader);
		relationshipProgresBar.setBounds(109, 210, 340, 30);
		relationshipProgresBar.setValue(questioner.getRelationshipValue());
		relationshipProgresBar.setToolTipText("Relationship with " + performerName);
		addComponent(relationshipProgresBar);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				selectedResponse = comboBoxResponse.getSelectedIndex();
			}
		});
		
		okButton.addActionListener(new CloseDialogAction());
		cancelButton.addActionListener(new CloseDialogAction());	
		
		DialogUtils.createDialogBackPanel(this, parentFrame.getContentPane());
	}
	
	private JComboBox<Response> createResponseComboBox(int id, int subjectId, int historyItemId, Conversations conversations, Questioner questioner, int additionalValue, int additionalValue2) {
		JComboBox<Response> responseComboBox = JComboBoxFactory.createJComboBox(imageInfoReader);
		responseComboBox.setRenderer(new ResponseListRenderer(imageInfoReader));
		List<Response> responses = questioner.getResponsePhrases(id, subjectId, historyItemId, additionalValue, additionalValue2);
		for(Response response : responses) {
			responseComboBox.addItem(response);
		}
		return responseComboBox;
	}

	public int showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		return selectedResponse;
	}
}
