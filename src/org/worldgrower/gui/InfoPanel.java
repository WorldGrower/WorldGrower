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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.worldgrower.Constants;
import org.worldgrower.LogMessage;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.TargetKnowsAction;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.EnergyPropertyUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.status.StatusMessage;
import org.worldgrower.gui.status.StatusMessageDialog;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JTextPaneFactory;

public final class InfoPanel extends JPanel {

	private static final String ENERGY_TOOL_TIP = "energy";
	private static final String WATER_TOOL_TIP = "water";
	private static final String FOOD_TOOL_TIP = "food";
	private static final String HIT_POINTS_TOOL_TIP = "hit points";
	
	private final WorldObject playerCharacter;
	private final World world;
	private final JFrame parentFrame;
	private final SoundIdReader soundIdReader;
	private final ImageFactory imageFactory;

	private final JTextPane messageTextPane;
	private final JProgressBar hitPointsProgressBar;
	private final JProgressBar foodTextProgressBar;
	private final JProgressBar waterProgressBar;
	private final JProgressBar energyProgressBar;
	
	private final List<StatusMessage> statusMessages = new ArrayList<>();
	private int lastMessageTurn = -1;
	
    public InfoPanel(WorldObject playerCharacter, World world, SoundIdReader soundIdReader, String initialStatusMessage, JFrame parentFrame, ImageFactory imageFactory) throws IOException {
        super(new BorderLayout());
        setBackground(Color.RED);
        SpringLayout layout = new SpringLayout();
		setLayout(layout);
		makeUnfocussable(this);
        
		this.imageFactory = imageFactory;
		this.soundIdReader = soundIdReader;
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.parentFrame = parentFrame;
		
        messageTextPane = JTextPaneFactory.createJTextPane();
        messageTextPane.setEditable(false);
        
        int messageWidth = 600;
        int messageHeight = 70;
        messageTextPane.setMinimumSize(new Dimension(messageWidth, messageHeight));
        messageTextPane.setPreferredSize(new Dimension(messageWidth, messageHeight));
        setStatusMessage(initialStatusMessage);
        messageTextPane.setToolTipText("This area displays messages like combat or dialogues. Click to show previous messages.");
        makeUnfocussable(messageTextPane);
        
        messageTextPane.addMouseListener(new MessageTextAreaMouseListener());
        world.addListener(new MessageManagedOperationListener());
        
        hitPointsProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, playerCharacter.getProperty(Constants.HIT_POINTS_MAX));
        hitPointsProgressBar.setBackground(Color.BLACK);
        hitPointsProgressBar.setForeground(Color.RED);
        hitPointsProgressBar.setToolTipText(HIT_POINTS_TOOL_TIP);
        makeUnfocussable(hitPointsProgressBar);
        
        add(hitPointsProgressBar);
        layout.putConstraint(SpringLayout.WEST, hitPointsProgressBar, 0, SpringLayout.EAST, messageTextPane);
        layout.putConstraint(SpringLayout.NORTH, hitPointsProgressBar, 0, SpringLayout.NORTH, messageTextPane);
        layout.putConstraint(SpringLayout.SOUTH, hitPointsProgressBar, 0, SpringLayout.SOUTH, messageTextPane);
        
        foodTextProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 1000);
        foodTextProgressBar.setBackground(Color.BLACK);
        foodTextProgressBar.setForeground(Color.YELLOW);
        foodTextProgressBar.setToolTipText(FOOD_TOOL_TIP);
        makeUnfocussable(foodTextProgressBar);
        
        add(foodTextProgressBar);
        layout.putConstraint(SpringLayout.WEST, foodTextProgressBar, 0, SpringLayout.EAST, hitPointsProgressBar);
        layout.putConstraint(SpringLayout.NORTH, foodTextProgressBar, 0, SpringLayout.NORTH, hitPointsProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, foodTextProgressBar, 0, SpringLayout.SOUTH, messageTextPane);
        
        waterProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 1000);
        waterProgressBar.setBackground(Color.BLACK);
        waterProgressBar.setForeground(Color.BLUE);
        waterProgressBar.setToolTipText(WATER_TOOL_TIP);
        makeUnfocussable(waterProgressBar);
        
        add(waterProgressBar);
        layout.putConstraint(SpringLayout.WEST, waterProgressBar, 0, SpringLayout.EAST, foodTextProgressBar);
        layout.putConstraint(SpringLayout.NORTH, waterProgressBar, 0, SpringLayout.NORTH, foodTextProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, waterProgressBar, 0, SpringLayout.SOUTH, messageTextPane);

        energyProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, EnergyPropertyUtils.calculateEnergyMax(playerCharacter));
        energyProgressBar.setBackground(Color.BLACK);
        energyProgressBar.setForeground(Color.GREEN);
        energyProgressBar.setToolTipText(ENERGY_TOOL_TIP);
        makeUnfocussable(energyProgressBar);
        
        add(energyProgressBar);
        layout.putConstraint(SpringLayout.WEST, energyProgressBar, 0, SpringLayout.EAST, waterProgressBar);
        layout.putConstraint(SpringLayout.NORTH, energyProgressBar, 0, SpringLayout.NORTH, waterProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, energyProgressBar, 0, SpringLayout.SOUTH, messageTextPane);

        add(messageTextPane);
        layout.putConstraint(SpringLayout.WEST, messageTextPane, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, messageTextPane, 0, SpringLayout.NORTH, this);
        
        layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, energyProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, messageTextPane);        
    }

    private class MessageManagedOperationListener implements ManagedOperationListener {

		@Override
		public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object message) {
			if (performer.equals(playerCharacter) || target.equals(playerCharacter)) {
				if (message instanceof String) {
					if (target.equals(playerCharacter)) {
						setStatusMessage(imageFactory.getImage(performer), (String) message);
					} else {
						setStatusMessage(imageFactory.getImage(target), (String) message);
					}
				} else if (message instanceof LogMessage) {
					LogMessage logMessage = (LogMessage) message;
					if (target.equals(playerCharacter)) {
						if (logMessage.getTargetKnowsAction() == TargetKnowsAction.TRUE) {
							setStatusMessage(imageFactory.getImage(performer), logMessage.getMessage());
						}
					} else {
						setStatusMessage(imageFactory.getImage(target), logMessage.getMessage());
					}
				}
			}
		}
    }
    
    public void setStatusMessage(String message) {
    	int currentTurn = world.getCurrentTurn().getValue();
    	if (lastMessageTurn == currentTurn) {
    		appendIconAndText(imageFactory.getMoreMessagesImage(), "More messages...");
    	} else {
    		messageTextPane.setText("");
    		statusMessages.add(new StatusMessage(null, message));
    		appendText(message);
    	}
    	lastMessageTurn = currentTurn;
    }

	void appendText(String message) {
		StyledDocument document = (StyledDocument)messageTextPane.getDocument();
    	try {
			document.insertString(document.getLength(), message, null);
		} catch (BadLocationException e) {
			throw new IllegalStateException(e);
		}
	}
    
    public void setStatusMessage(Image image, String message) {
    	int currentTurn = world.getCurrentTurn().getValue();
    	if (lastMessageTurn == currentTurn) {
    		appendIconAndText(imageFactory.getMoreMessagesImage(), "More messages...");
    	} else {
    		messageTextPane.setText("");
	    	statusMessages.add(new StatusMessage(image, message));
	    	appendIconAndText(image, message);
    	}
    	lastMessageTurn = currentTurn;
    }

	void appendIconAndText(Image image, String message) {
		StyledDocument document = (StyledDocument)messageTextPane.getDocument();
    	
        try {
			JLabel jl  = JLabelFactory.createJLabel(message, image);
			String styleName = "style";
			Style textStyle = document.addStyle(styleName, null);
		    StyleConstants.setComponent(textStyle, jl);

		    document.insertString(document.getLength(), " ", document.getStyle(styleName));
		} catch (BadLocationException e) {
			throw new IllegalStateException(e);
		}
	}
    
    private void makeUnfocussable(JComponent component) {
    	component.setRequestFocusEnabled(false);
    	component.setFocusable(false);
    }
    
    private class MessageTextAreaMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			showStatusMessageDialog();
		}
    }
    
    public void showStatusMessageDialog() {
    	new StatusMessageDialog(statusMessages, soundIdReader, parentFrame).showMe();
    }
    
    public void updatePlayerCharacterValues() {
		hitPointsProgressBar.setValue(playerCharacter.getProperty(Constants.HIT_POINTS));
		foodTextProgressBar.setValue(playerCharacter.getProperty(Constants.FOOD));
		waterProgressBar.setValue(playerCharacter.getProperty(Constants.WATER));
		energyProgressBar.setValue(playerCharacter.getProperty(Constants.ENERGY));
		updateToolTips();
    }
    
    private void updateToolTips() {
    	hitPointsProgressBar.setToolTipText(HIT_POINTS_TOOL_TIP + " " + playerCharacter.getProperty(Constants.HIT_POINTS) + "/" + playerCharacter.getProperty(Constants.HIT_POINTS_MAX));
		foodTextProgressBar.setToolTipText(FOOD_TOOL_TIP + " " + playerCharacter.getProperty(Constants.FOOD) + "/1000");
		waterProgressBar.setToolTipText(WATER_TOOL_TIP + " " + playerCharacter.getProperty(Constants.WATER) + "/1000");
		energyProgressBar.setToolTipText(ENERGY_TOOL_TIP + " " + playerCharacter.getProperty(Constants.ENERGY) + "/" + EnergyPropertyUtils.calculateEnergyMax(playerCharacter));
    }
}