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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.condition.Condition;
import org.worldgrower.gui.conversation.GuiRespondToQuestion;
import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainType;

public class WorldPanel extends JPanel {

	private WorldObject playerCharacter;
	private World world;
	private ImageInfoReader imageInfoReader = new ImageInfoReader();
	private GuiMouseListener guiMouseListener;
	private int offsetX = 0;
	private int offsetY = 0;
	
	private JTextArea hpTextArea;
	private JTextArea foodTextArea;
	private JTextArea waterTextArea;
	private JTextArea energyTextArea;
	
	private BuildModeOutline buildModeOutline = new BuildModeOutline();
	private MouseMotionListener mouseMotionListener;
	
    public WorldPanel(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster) throws IOException {
        super(new BorderLayout());

        guiMouseListener = new GuiMouseListener(this, playerCharacter, world, dungeonMaster, imageInfoReader);
		addMouseListener(guiMouseListener);

        int width = 1024;
        int height = 768;
        
        setBounds(0, 0, width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        getActionMap().put("Cancel", new ShowStartScreenAction(world));
        
        getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "up");
        getActionMap().put("up", new GuiMoveAction(new int[] { 0,  -1 }, playerCharacter, world, dungeonMaster, this));
        
        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "down");
        getActionMap().put("down", new GuiMoveAction(new int[] { 0,  1 }, playerCharacter, world, dungeonMaster, this));
        
        getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "left");
        getActionMap().put("left", new GuiMoveAction(new int[] { -1,  0 }, playerCharacter, world, dungeonMaster, this));
        
        getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "right");
        getActionMap().put("right", new GuiMoveAction(new int[] { 1,  0 }, playerCharacter, world, dungeonMaster, this));

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "7");
        getActionMap().put("7", new GuiMoveAction(new int[] { -1,  -1 }, playerCharacter, world, dungeonMaster, this));
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "9");
        getActionMap().put("9", new GuiMoveAction(new int[] { 1,  -1 }, playerCharacter, world, dungeonMaster, this));

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "1");
        getActionMap().put("1", new GuiMoveAction(new int[] { -1,  1 }, playerCharacter, world, dungeonMaster, this));
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "3");
        getActionMap().put("3", new GuiMoveAction(new int[] { 1,  1 }, playerCharacter, world, dungeonMaster, this));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.RED);
        SpringLayout layout = new SpringLayout();
		infoPanel.setLayout(layout);
        
        final JTextArea messageTextArea = new JTextArea(3, 30);
        messageTextArea.setEditable(false);
        makeUnfocussable(messageTextArea);
        world.addListener((operation, performer, target, args, message) -> messageTextArea.setText(message.toString()));
        
        JLabel hpLabel = new JLabel("HP/HPmax    ");
        hpLabel.setBackground(Color.WHITE);
        hpLabel.setOpaque(true);
        infoPanel.add(hpLabel);
        layout.putConstraint(SpringLayout.WEST, hpLabel, 0, SpringLayout.EAST, messageTextArea);
        layout.putConstraint(SpringLayout.NORTH, hpLabel, 0, SpringLayout.NORTH, messageTextArea);
        
        hpTextArea = new JTextArea(1, 10);
        hpTextArea.setEditable(false);
        hpTextArea.setEnabled(false);
        makeUnfocussable(hpTextArea);
        infoPanel.add(hpTextArea);
        layout.putConstraint(SpringLayout.WEST, hpTextArea, 0, SpringLayout.EAST, messageTextArea);
        layout.putConstraint(SpringLayout.NORTH, hpTextArea, 0, SpringLayout.SOUTH, hpLabel);
        
        JLabel foodLabel = new JLabel("Food            ");
        foodLabel.setBackground(Color.WHITE);
        foodLabel.setOpaque(true);
        infoPanel.add(foodLabel);
        layout.putConstraint(SpringLayout.WEST, foodLabel, 5, SpringLayout.EAST, hpLabel);
        layout.putConstraint(SpringLayout.NORTH, foodLabel, 0, SpringLayout.NORTH, hpLabel);
        
        foodTextArea = new JTextArea(1, 10);
        foodTextArea.setEditable(false);
        foodTextArea.setEnabled(false);
        makeUnfocussable(foodTextArea);
        infoPanel.add(foodTextArea);
        layout.putConstraint(SpringLayout.WEST, foodTextArea, 0, SpringLayout.WEST, foodLabel);
        layout.putConstraint(SpringLayout.NORTH, foodTextArea, 0, SpringLayout.SOUTH, foodLabel);
        
        JLabel waterLabel = new JLabel("Water            ");
        waterLabel.setBackground(Color.WHITE);
        waterLabel.setOpaque(true);
        infoPanel.add(waterLabel);
        layout.putConstraint(SpringLayout.WEST, waterLabel, 5, SpringLayout.EAST, foodLabel);
        layout.putConstraint(SpringLayout.NORTH, waterLabel, 0, SpringLayout.NORTH, foodLabel);
        
        waterTextArea = new JTextArea(1, 10);
        waterTextArea.setEditable(false);
        waterTextArea.setEnabled(false);
        makeUnfocussable(waterTextArea);
        infoPanel.add(waterTextArea);
        layout.putConstraint(SpringLayout.WEST, waterTextArea, 0, SpringLayout.WEST, waterLabel);
        layout.putConstraint(SpringLayout.NORTH, waterTextArea, 0, SpringLayout.SOUTH, waterLabel);

        JLabel energyLabel = new JLabel("Energy          ");
        energyLabel.setBackground(Color.WHITE);
        energyLabel.setOpaque(true);
        infoPanel.add(energyLabel);
        layout.putConstraint(SpringLayout.WEST, energyLabel, 5, SpringLayout.EAST, waterLabel);
        layout.putConstraint(SpringLayout.NORTH, energyLabel, 0, SpringLayout.NORTH, waterLabel);
        
        energyTextArea = new JTextArea(1, 10);
        energyTextArea.setEditable(false);
        energyTextArea.setEnabled(false);
        makeUnfocussable(energyTextArea);
        infoPanel.add(energyTextArea);
        layout.putConstraint(SpringLayout.WEST, energyTextArea, 0, SpringLayout.WEST, energyLabel);
        layout.putConstraint(SpringLayout.NORTH, energyTextArea, 0, SpringLayout.SOUTH, energyLabel);

        infoPanel.add(messageTextArea);
        layout.putConstraint(SpringLayout.WEST, messageTextArea, 0, SpringLayout.WEST, infoPanel);
        layout.putConstraint(SpringLayout.NORTH, messageTextArea, 0, SpringLayout.NORTH, infoPanel);
        
        add(infoPanel, BorderLayout.SOUTH);
        
        layout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, energyLabel);
        layout.putConstraint(SpringLayout.SOUTH, infoPanel, 0, SpringLayout.SOUTH, messageTextArea);
        
        this.playerCharacter = playerCharacter;
        this.world = world;
    }
    
    private void makeUnfocussable(JComponent component) {
    	component.setRequestFocusEnabled(false);
    	component.setFocusable(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for(int x = 0; x<world.getWidth() ;x++) {
			for(int y = 0; y<world.getHeight(); y++) {
				g.setColor(getBackgroundColor(x, y));
				g.fillRect((x+offsetX) * 48, (y+offsetY) * 48, 48, 48);
			}
		}
        
		List<WorldObject> worldObjects = world.getWorldObjects();
		for(WorldObject worldObject : worldObjects) {
			ImageIds id = getImageId(worldObject);
			LookDirection lookDirection = getLookDirection(worldObject);
    		Image image = imageInfoReader.getImage(id, lookDirection);
			
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			
			if (world.getTerrain().isExplored(x, y)) {
				g.drawImage(image, (x+offsetX) * 48, (y+offsetY) * 48, null);
				
				ImageIds overlayingImageId = getOverlayingImageId(worldObject);
				if (overlayingImageId != null) {
					Image overlayingImage = imageInfoReader.getImage(overlayingImageId, lookDirection);
					g.drawImage(overlayingImage, (x+offsetX) * 48, (y+offsetY) * 48, null);
				}
			}
		}
		
		repaintHpTextAreaRepaint();
		repaintFoodTextAreaRepaint();
		repaintWaterTextAreaRepaint();
		repaintEnergyTextAreaRepaint();
		buildModeOutline.repaintBuildMode(g, getMouseLocation(), offsetX, offsetY, playerCharacter, world);
    }

    private ImageIds getImageId(WorldObject worldObject) {
		WorldObject facade = worldObject.getProperty(Constants.FACADE);
		if (worldObject.hasProperty(Constants.CONDITIONS) && worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.COCOONED_CONDITION)) {
			return ImageIds.COCOON;
		} else if ((facade != null) && facade.getProperty(Constants.IMAGE_ID) != null) {
			return facade.getProperty(Constants.IMAGE_ID);
		} else {
			return worldObject.getProperty(Constants.IMAGE_ID);
		}
	}
    
    private ImageIds getOverlayingImageId(WorldObject worldObject) {
    	if (worldObject.hasProperty(Constants.CONDITIONS) && worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.BURNING_CONDITION)) {
    		return ImageIds.BURNING;
    	} else {
    		return null;
    	}
    }
    
	private LookDirection getLookDirection(WorldObject worldObject) {
		if (worldObject.hasProperty(Constants.LOOK_DIRECTION)) {
			return worldObject.getProperty(Constants.LOOK_DIRECTION);
		} else {
			return null;
		}
	}

	private void repaintHpTextAreaRepaint() {
		StringBuilder hpDescription = new StringBuilder();
		hpDescription.append(playerCharacter.getProperty(Constants.HIT_POINTS));
		hpDescription.append("/");
		hpDescription.append(playerCharacter.getProperty(Constants.HIT_POINTS_MAX));
		hpTextArea.setText(hpDescription.toString());
	}
	
	private void repaintFoodTextAreaRepaint() {
		StringBuilder foodDescription = new StringBuilder();
		foodDescription.append(playerCharacter.getProperty(Constants.FOOD));
		foodDescription.append("/1000");
		foodTextArea.setText(foodDescription.toString());
	}
	
	private void repaintWaterTextAreaRepaint() {
		StringBuilder waterDescription = new StringBuilder();
		waterDescription.append(playerCharacter.getProperty(Constants.WATER));
		waterDescription.append("/1000");
		waterTextArea.setText(waterDescription.toString());
	}
	
	private void repaintEnergyTextAreaRepaint() {
		StringBuilder energyDescription = new StringBuilder();
		energyDescription.append(playerCharacter.getProperty(Constants.ENERGY));
		energyDescription.append("/1000");
		energyTextArea.setText(energyDescription.toString());
	}
    
    public void centerOffsetsOn(int x, int y) {
    	int width = this.getWidth() / 48;
    	int height = this.getHeight() / 48;
    	this.offsetX = offsetX - (x - width / 2);
    	this.offsetY = offsetY - (y - height / 2);
    	
    	if (offsetX > 0) {
    		offsetX = 0;
    	}
    	if (offsetY > 0) {
    		offsetY = 0;
    	}
    	if (offsetX < -world.getWidth() + width) {
    		offsetX = -world.getWidth() + width;
    	}
    	if (offsetY < -world.getHeight() + height) {
    		offsetY = -world.getHeight() + height;
    	}
    }
    
	private Color getBackgroundColor(int x, int y) {
		final Color backgroundColor;
		Terrain terrain = world.getTerrain();
		if (terrain.isExplored(x, y)) {
			TerrainType terrainType = terrain.getTerrainInfo(x, y).getTerrainType();
			switch(terrainType) {
				case GRASLAND:
					backgroundColor = new Color(110, 196, 88);
					break;
				case PLAINS:
					backgroundColor = new Color(235, 195, 75);
					break;
				case HILL:
					backgroundColor = new Color(171, 140, 17);
					break;
				case MOUNTAIN:
					backgroundColor = new Color(161, 161, 161);
					break;
				default:
					backgroundColor = Color.BLACK;
			}
		} else {
			backgroundColor = Color.BLACK;
		}
		return backgroundColor;
	}

	public WorldObject findWorldObject(int x, int y) {
        List<WorldObject> worldObjects = world.findWorldObjects(w -> w.getProperty(Constants.X) == (x-offsetX) && w.getProperty(Constants.Y) == (y-offsetY));
		final WorldObject worldObject;
		if (worldObjects.size() > 0) {
			worldObject = worldObjects.get(0);
		} else {
			worldObject = null;
		}
		return worldObject;
	}

	public void createGuiRespondToImage() {
		new GuiRespondToQuestion(playerCharacter, world, imageInfoReader);
		new GuiShowReadAction(playerCharacter, world, (JComponent) this.getParent(), imageInfoReader);
	}

	public void startBuildMode(BuildAction buildAction, int[] args) {
		this.buildModeOutline.startBuildMode(buildAction, args);
		this.mouseMotionListener = new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent mouseEvent) {
				super.mouseMoved(mouseEvent);
				WorldPanel.this.repaint();
			}
			
		};
		this.addMouseMotionListener(this.mouseMotionListener);
	}
	
	public void endBuildMode(boolean executeBuildAction) {
		this.buildModeOutline.endBuildMode(executeBuildAction, getMouseLocation(), offsetX, offsetY, playerCharacter, world, guiMouseListener);
		this.removeMouseMotionListener(this.mouseMotionListener);
	}

	private Point getMouseLocation() {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouseLocation, this);
		return mouseLocation;
	}
	
	public boolean inBuildMode() {
		return buildModeOutline.inBuildMode();
	}

	public void centerViewOnPlayerCharacter() {
		int x = playerCharacter.getProperty(Constants.X);
		int y = playerCharacter.getProperty(Constants.Y);
		
		int xInView = (x+offsetX) * 48;
		int yInView = (y+offsetY) * 48;
		
		if ((xInView < 48) || (xInView > this.getWidth() - 48) || (yInView < 48) || (yInView > this.getHeight() - 96)) {
			centerOffsetsOn(x+offsetX, y+offsetY);
		}
	}
}