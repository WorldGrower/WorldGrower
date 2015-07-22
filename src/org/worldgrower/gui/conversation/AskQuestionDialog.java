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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.ConversationCategory;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Question;
import org.worldgrower.conversation.Response;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class AskQuestionDialog extends JDialog implements ManagedOperationListener {

	private final Answerer answerer;
	private final JButton askQuestion;
	private final JLabel label;
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AskQuestionDialog.this.dispose();
		}
	}
	
	public AskQuestionDialog(Answerer answerer, Conversations conversations, ImageIds imageIdPerformer, ImageIds imageIdTarget, Map<Integer, ImageIds> subjectImageIds, ImageInfoReader imageInfoReader) {
		this.answerer = answerer;
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 650, 300);
		getContentPane().setLayout(null);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 218, 632, 35);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		JLabel performerLabel = new JLabel(new ImageIcon(imageInfoReader.getImage(imageIdPerformer, null)));
		performerLabel.setBounds(6, 17, 32, 48);
		getContentPane().add(performerLabel);
		
		askQuestion = new JButton("Ask Question");
		askQuestion.setBounds(44, 27, 580, 22);
		askQuestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPopupMenu popupMenu = createQuestions(imageInfoReader, subjectImageIds, conversations, answerer);
				popupMenu.show(askQuestion, askQuestion.getX(), askQuestion.getY());
				
			}
			
		});
		getContentPane().add(askQuestion);
		
		JLabel targetLabel = new JLabel(new ImageIcon(imageInfoReader.getImage(imageIdTarget, null)));
		targetLabel.setBounds(6, 90, 32, 48);
		getContentPane().add(targetLabel);
		
		label = new JLabel(" ");
		label.setBounds(44, 70, 395, 96);
		getContentPane().add(label);
		
		okButton.addActionListener(new CloseDialogAction());
		cancelButton.addActionListener(new CloseDialogAction());
		
		this.addWindowListener(new WindowAdapter()
	    {
	      public void windowClosed(WindowEvent e)
	      {
	        answerer.doneTalking();
	      }
	    });
	}
	
	private JPopupMenu createQuestions(ImageInfoReader imageInfoReader, Map<Integer, ImageIds> subjectImageIds, Conversations conversations, Answerer answerer) {
		Map<ConversationCategory, List<Question>> questions = answerer.getQuestionPhrases();
		JPopupMenu popupMenu = new JPopupMenu();
		for(Entry<ConversationCategory, List<Question>> entry : questions.entrySet()) {
			JMenu menu = new JMenu(entry.getKey().getDescription());
			popupMenu.add(menu);
			
			for(Question question : entry.getValue()) {
				JMenuItem questionMenuItem = new JMenuItem(question.getQuestionPhrase());
				ImageIds subjectImageId = subjectImageIds.get(question.getSubjectId());
				if (subjectImageId != null) {
					questionMenuItem.setIcon(new ImageIcon(imageInfoReader.getImage(subjectImageId, null)));
				}
				questionMenuItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						int[] args = new int[] { question.getId(), question.getSubjectId(), question.getHistoryItemId(), question.getAdditionalValue() };
						answerer.askQuestion(args);
					}
				});
				menu.add(questionMenuItem);
			}
		}
		
		return popupMenu;
	}

	public void showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if (answerer.filterMessage(performer)) {
			Response response = (Response) value;
			label.setText(response.getResponsePhrase());
		}
	}
}
