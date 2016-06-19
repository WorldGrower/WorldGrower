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

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.worldgrower.Constants;
import org.worldgrower.CreaturePositionCondition;
import org.worldgrower.DungeonMaster;
import org.worldgrower.LogMessage;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.TargetKnowsAction;
import org.worldgrower.World;
import org.worldgrower.WorldFacade;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.EnergyPropertyUtils;
import org.worldgrower.gui.conversation.GuiRespondToQuestion;
import org.worldgrower.gui.conversation.GuiShowBrawlResult;
import org.worldgrower.gui.conversation.GuiShowDrinkingContestResult;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.gui.status.StatusMessageDialog;
import org.worldgrower.gui.util.JTextAreaFactory;
import org.worldgrower.history.HistoryItem;

public final class WorldPanel extends JPanel {

	private static final String ENERGY_TOOL_TIP = "energy";
	private static final String WATER_TOOL_TIP = "water";
	private static final String FOOD_TOOL_TIP = "food";
	private static final String HIT_POINTS_TOOL_TIP = "hit points";
	private WorldObject playerCharacter;
	private World world;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final MusicPlayer musicPlayer;
	private GuiMouseListener guiMouseListener;
	private int offsetX = 0;
	private int offsetY = 0;
	
	private JTextArea messageTextArea;
	private JProgressBar hitPointsProgressBar;
	private JProgressBar foodTextProgressBar;
	private JProgressBar waterProgressBar;
	private JProgressBar energyProgressBar;
	
	private BuildModeOutline buildModeOutline = new BuildModeOutline();
	private MouseMotionListener mouseMotionListener;
	
	private final MoveMode moveMode = new MoveMode();
	private final BackgroundPainter backgroundPainter;
	private final KeyBindings keyBindings;
	
	private final List<String> statusMessages = new ArrayList<>();
	
    public WorldPanel(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, String initialStatusMessage, KeyBindings keyBindings, JFrame parentFrame) throws IOException {
        super(new BorderLayout());
        this.imageInfoReader = imageInfoReader;
        this.soundIdReader = soundIdReader;
        this.musicPlayer = musicPlayer;
        this.keyBindings = keyBindings;

        guiMouseListener = new GuiMouseListener(this, playerCharacter, world, dungeonMaster, imageInfoReader, soundIdReader, keyBindings, parentFrame);
		addMouseListener(guiMouseListener);
		ToolTipManager.sharedInstance().registerComponent(this);

        int width = 1024;
        int height = 768;
        
        setBounds(0, 0, width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        
        initializeKeyBindings(playerCharacter, world, dungeonMaster);
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.RED);
        SpringLayout layout = new SpringLayout();
		infoPanel.setLayout(layout);
		makeUnfocussable(infoPanel);
        
        messageTextArea = JTextAreaFactory.createJTextArea(3, 30);
        messageTextArea.setEditable(false);
        setStatusMessage(initialStatusMessage);
        messageTextArea.setToolTipText("This area displays messages like combat or dialogues. Click to show previous messages.");
        makeUnfocussable(messageTextArea);
        world.addListener(new MessageManagedOperationListener());
        messageTextArea.addMouseListener(new MessageTextAreaMouseListener());
        
        hitPointsProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, playerCharacter.getProperty(Constants.HIT_POINTS_MAX));
        hitPointsProgressBar.setBackground(Color.BLACK);
        hitPointsProgressBar.setForeground(Color.RED);
        hitPointsProgressBar.setToolTipText(HIT_POINTS_TOOL_TIP);
        makeUnfocussable(hitPointsProgressBar);
        
        infoPanel.add(hitPointsProgressBar);
        layout.putConstraint(SpringLayout.WEST, hitPointsProgressBar, 0, SpringLayout.EAST, messageTextArea);
        layout.putConstraint(SpringLayout.NORTH, hitPointsProgressBar, 0, SpringLayout.NORTH, messageTextArea);
        layout.putConstraint(SpringLayout.SOUTH, hitPointsProgressBar, 0, SpringLayout.SOUTH, messageTextArea);
        
        foodTextProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 1000);
        foodTextProgressBar.setBackground(Color.BLACK);
        foodTextProgressBar.setForeground(Color.YELLOW);
        foodTextProgressBar.setToolTipText(FOOD_TOOL_TIP);
        makeUnfocussable(foodTextProgressBar);
        
        infoPanel.add(foodTextProgressBar);
        layout.putConstraint(SpringLayout.WEST, foodTextProgressBar, 0, SpringLayout.EAST, hitPointsProgressBar);
        layout.putConstraint(SpringLayout.NORTH, foodTextProgressBar, 0, SpringLayout.NORTH, hitPointsProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, foodTextProgressBar, 0, SpringLayout.SOUTH, messageTextArea);
        
        waterProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, 1000);
        waterProgressBar.setBackground(Color.BLACK);
        waterProgressBar.setForeground(Color.BLUE);
        waterProgressBar.setToolTipText(WATER_TOOL_TIP);
        makeUnfocussable(waterProgressBar);
        
        infoPanel.add(waterProgressBar);
        layout.putConstraint(SpringLayout.WEST, waterProgressBar, 0, SpringLayout.EAST, foodTextProgressBar);
        layout.putConstraint(SpringLayout.NORTH, waterProgressBar, 0, SpringLayout.NORTH, foodTextProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, waterProgressBar, 0, SpringLayout.SOUTH, messageTextArea);

        energyProgressBar = new JProgressBar(JProgressBar.VERTICAL, 0, EnergyPropertyUtils.calculateEnergyMax(playerCharacter));
        energyProgressBar.setBackground(Color.BLACK);
        energyProgressBar.setForeground(Color.GREEN);
        energyProgressBar.setToolTipText(ENERGY_TOOL_TIP);
        makeUnfocussable(energyProgressBar);
        
        infoPanel.add(energyProgressBar);
        layout.putConstraint(SpringLayout.WEST, energyProgressBar, 0, SpringLayout.EAST, waterProgressBar);
        layout.putConstraint(SpringLayout.NORTH, energyProgressBar, 0, SpringLayout.NORTH, waterProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, energyProgressBar, 0, SpringLayout.SOUTH, messageTextArea);

        infoPanel.add(messageTextArea);
        layout.putConstraint(SpringLayout.WEST, messageTextArea, 0, SpringLayout.WEST, infoPanel);
        layout.putConstraint(SpringLayout.NORTH, messageTextArea, 0, SpringLayout.NORTH, infoPanel);
        
        add(infoPanel, BorderLayout.SOUTH);
        
        layout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, energyProgressBar);
        layout.putConstraint(SpringLayout.SOUTH, infoPanel, 0, SpringLayout.SOUTH, messageTextArea);
        
        this.playerCharacter = playerCharacter;
        this.world = world;
        Image grassBackground = imageInfoReader.getImage(ImageIds.GRASS_BACKGROUND, null);
        Image grassFlowersBackground = imageInfoReader.getImage(ImageIds.SMALL_FLOWERS, null);
		this.backgroundPainter = new BackgroundPainter(grassBackground, grassFlowersBackground, imageInfoReader, world);
    }

	private void initializeKeyBindings(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster) {
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        bindEscapeButtonToStartScreen(world);
        
        getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "up");
        getActionMap().put("up", new GuiMoveAction(new int[] { 0,  -1 }, playerCharacter, world, dungeonMaster, this, soundIdReader));
        
        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "down");
        getActionMap().put("down", new GuiMoveAction(new int[] { 0,  1 }, playerCharacter, world, dungeonMaster, this, soundIdReader));
        
        getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "left");
        getActionMap().put("left", new GuiMoveAction(new int[] { -1,  0 }, playerCharacter, world, dungeonMaster, this, soundIdReader));
        
        getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "right");
        getActionMap().put("right", new GuiMoveAction(new int[] { 1,  0 }, playerCharacter, world, dungeonMaster, this, soundIdReader));

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "7");
        getActionMap().put("7", new GuiMoveAction(new int[] { -1,  -1 }, playerCharacter, world, dungeonMaster, this, soundIdReader));
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "9");
        getActionMap().put("9", new GuiMoveAction(new int[] { 1,  -1 }, playerCharacter, world, dungeonMaster, this, soundIdReader));

        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "1");
        getActionMap().put("1", new GuiMoveAction(new int[] { -1,  1 }, playerCharacter, world, dungeonMaster, this, soundIdReader));
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "3");
        getActionMap().put("3", new GuiMoveAction(new int[] { 1,  1 }, playerCharacter, world, dungeonMaster, this, soundIdReader));
	}

	private void bindEscapeButtonToStartScreen(World world) {
		getActionMap().put("Cancel", new ShowStartScreenAction(this, imageInfoReader, soundIdReader, musicPlayer, keyBindings, world));
	}
	
	private void bindEscapeButtonToCalcelBuildMode() {
		getActionMap().put("Cancel", new CancelBuildModeAction(this));
	}
    
    private class MessageManagedOperationListener implements ManagedOperationListener {

		@Override
		public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object message) {
			if (performer.equals(playerCharacter) || target.equals(playerCharacter)) {
				if (message instanceof String) {
					setStatusMessage((String) message);
				} else if (message instanceof LogMessage) {
					LogMessage logMessage = (LogMessage) message;
					if (target.equals(playerCharacter)) {
						if (logMessage.getTargetKnowsAction() == TargetKnowsAction.TRUE) {
							setStatusMessage(logMessage.getMessage());
						}
					} else {
						setStatusMessage(logMessage.getMessage());
					}
				}
			}
		}
    }
    
    public void initializeKeyBindings() {
    	guiMouseListener.initializeKeyBindings();
    }
    
    public void setStatusMessage(String message) {
    	statusMessages.add(message);
    	messageTextArea.setText(message);
    }
    
    public void setStatusMessage(Icon icon, String message) {
    	statusMessages.add(message);
    	messageTextArea.setText(message);
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
    	new StatusMessageDialog(statusMessages, soundIdReader).showMe();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        backgroundPainter.paint(g, world, this);
        
		paintStaticWorldObjects(g);
		
		moveMode.drawWorldObjects(g, this, imageInfoReader, world);
		
		showHitPointsOfPlayerCharacterTarget(g);
		
		hitPointsProgressBar.setValue(playerCharacter.getProperty(Constants.HIT_POINTS));
		foodTextProgressBar.setValue(playerCharacter.getProperty(Constants.FOOD));
		waterProgressBar.setValue(playerCharacter.getProperty(Constants.WATER));
		energyProgressBar.setValue(playerCharacter.getProperty(Constants.ENERGY));
		buildModeOutline.repaintBuildMode(g, getMouseLocation(), offsetX, offsetY, playerCharacter, world);
		updateToolTips();
    }
    
    private void updateToolTips() {
    	hitPointsProgressBar.setToolTipText(HIT_POINTS_TOOL_TIP + " " + playerCharacter.getProperty(Constants.HIT_POINTS) + "/" + playerCharacter.getProperty(Constants.HIT_POINTS_MAX));
		foodTextProgressBar.setToolTipText(FOOD_TOOL_TIP + " " + playerCharacter.getProperty(Constants.FOOD) + "/1000");
		waterProgressBar.setToolTipText(WATER_TOOL_TIP + " " + playerCharacter.getProperty(Constants.WATER) + "/1000");
		energyProgressBar.setToolTipText(ENERGY_TOOL_TIP + " " + playerCharacter.getProperty(Constants.ENERGY) + "/" + EnergyPropertyUtils.calculateEnergyMax(playerCharacter));
    }

	private void paintStaticWorldObjects(Graphics g) {
		List<WorldObject> worldObjects = world.getWorldObjects();
		for(WorldObject worldObject : new ArrayList<>(worldObjects)) {
			ImageIds id = getImageId(worldObject);
			LookDirection lookDirection = getLookDirection(worldObject);
    		Image image = imageInfoReader.getImage(id, lookDirection);
			
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			
			if (!worldObject.hasIntelligence()) {
				if (world.getTerrain().isExplored(x, y) && isWorldObjectVisible(worldObject)) {
					drawWorldObject(g, worldObject, lookDirection, image, x, y);
				}
			}
		}
	}

	private void showHitPointsOfPlayerCharacterTarget(Graphics g) {
		HistoryItem lastPerformedOperation = world.getHistory().getLastPerformedOperation(playerCharacter);
		if (lastPerformedOperation != null) {
			WorldObject target = lastPerformedOperation.getOperationInfo().getTarget();
			if (target.hasProperty(Constants.HIT_POINTS)
					&& world.exists(target)
					&& !target.equals(playerCharacter)) {
				target = world.findWorldObject(Constants.ID, target.getProperty(Constants.ID));
				int targetX = target.getProperty(Constants.X);
				int targetY = target.getProperty(Constants.Y);
				g.setColor(Color.RED);
				
				int x = (targetX+offsetX) * 48;
				int y = (targetY+offsetY) * 48;
				float percentageHitPointsLeft = (100f * target.getProperty(Constants.HIT_POINTS)) / target.getProperty(Constants.HIT_POINTS_MAX);
				int lineLength = (int)(48 * (percentageHitPointsLeft / 100));
				g.drawLine(x, y, x+lineLength, y);
			}
		}
	}

	private void drawWorldObject(Graphics g, WorldObject worldObject, LookDirection lookDirection, Image image, int x, int y) {
		boolean isIllusion = worldObject.hasProperty(Constants.ILLUSION_CREATOR_ID);
		boolean isTransparant = isIllusion && playerCharacter.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(worldObject, Constants.ILLUSION_CREATOR_ID);
		Composite originalComposite = null;
		Graphics2D graphics2d = (Graphics2D)g;
		if (isTransparant) {
			originalComposite = graphics2d.getComposite();
			graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}
		drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0);
		if (isTransparant) {
			graphics2d.setComposite(originalComposite);
		}
	}
	
	private static final int CONDITION_IMAGE_WIDTH = 12;
	private static final int CONDITION_IMAGE_HEIGHT = 12;
	
	public void drawWorldObjectInPixels(Graphics g, WorldObject worldObject, LookDirection lookDirection, Image image, int xInSquares, int yInSquares, int xDeltaInPixels, int yDeltaInPixels) {
		image = changeSize(worldObject, image);
		int worldObjectX = (xInSquares+offsetX) * 48 + xDeltaInPixels;
		int worldObjectY = (yInSquares+offsetY) * 48 + yDeltaInPixels;
		g.drawImage(image, worldObjectX, worldObjectY, null);
		
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
		
		List<Image> overlayingImages = getOverlayingImages(worldObject);
		int imageIndex = 0;
		for(Image overlayingImage : overlayingImages) {
			
			overlayingImage = createResizedCopy(overlayingImage, CONDITION_IMAGE_WIDTH, CONDITION_IMAGE_HEIGHT, false);
			g.setColor(Color.BLACK);
			int overlayingImageX = calculateConditionX(worldObjectX, worldObjectWidth, imageIndex);
			int overlayingImageY = calculateConditionY(worldObjectY, worldObjectHeight, imageIndex);
			g.drawRect(overlayingImageX, overlayingImageY, CONDITION_IMAGE_WIDTH, CONDITION_IMAGE_HEIGHT);
			g.drawImage(overlayingImage, overlayingImageX, overlayingImageY, null);
			
			imageIndex++;
		}
	}

	int calculateConditionY(int worldObjectY, int worldObjectHeight, int imageIndex) {
		return worldObjectY + worldObjectHeight - CONDITION_IMAGE_HEIGHT * (imageIndex / 4) - 1;
	}

	int calculateConditionX(int worldObjectX, int worldObjectWidth, int imageIndex) {
		return worldObjectX + worldObjectWidth - CONDITION_IMAGE_WIDTH - CONDITION_IMAGE_WIDTH * imageIndex - 1;
	}
	
	private String getConditionDescriptionFor(WorldObject worldObject, int panelX, int panelY) {
		List<String> conditionDescriptions = getConditionDescriptions(worldObject);
		int worldObjectX = (worldObject.getProperty(Constants.X)+offsetX) * 48;
		int worldObjectY = (worldObject.getProperty(Constants.Y)+offsetY) * 48;
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
		
		int imageIndex = 0;
		for(String conditionDescription : conditionDescriptions) {
			int overlayingImageX = calculateConditionX(worldObjectX, worldObjectWidth, imageIndex);
			int overlayingImageY = calculateConditionY(worldObjectY, worldObjectHeight, imageIndex);

			if (overlayingImageX <= panelX && panelX <= overlayingImageX + CONDITION_IMAGE_WIDTH) {
				if (overlayingImageY <= panelY && panelY <= overlayingImageY + CONDITION_IMAGE_HEIGHT) {
					return conditionDescription;
				}
			}
			
			imageIndex++;
		}
		return null;
	}
	
	BufferedImage createResizedCopy(Image originalImage, 
    		int scaledWidth, int scaledHeight, 
    		boolean preserveAlpha)
    {
    	int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    	BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
    	Graphics2D g = scaledBI.createGraphics();
    	if (preserveAlpha) {
    		g.setComposite(AlphaComposite.Src);
    	}
    	g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
    	g.dispose();
    	return scaledBI;
    }
	
	public void drawBackgroundImage(Graphics g, Image image, int x, int y) {
		g.drawImage(image, (x+offsetX) * 48, (y+offsetY) * 48, null);
	}
	
	public void drawUnexploredTerrain(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillRect((x+offsetX) * 48, (y+offsetY) * 48, 48, 48);
	}

	private Image changeSize(WorldObject worldObject, Image image) {
		if (hasCondition(worldObject, Condition.ENLARGED_CONDITION)) {
			int imageWidth = 48 * worldObject.getProperty(Constants.WIDTH);
			int imageHeight = 48 * worldObject.getProperty(Constants.HEIGHT);
			image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
		}
		if (hasCondition(worldObject, Condition.REDUCED_CONDITION)) {
			final int imageWidth;
			if (worldObject.getProperty(Constants.ORIGINAL_WIDTH) == 1) {
				imageWidth = 24 * worldObject.getProperty(Constants.WIDTH);
			} else {
				imageWidth = 48 * worldObject.getProperty(Constants.WIDTH);
			}
			final int imageHeight;
			if (worldObject.getProperty(Constants.ORIGINAL_HEIGHT) == 1) {
				imageHeight = 24 * worldObject.getProperty(Constants.HEIGHT);
			} else {
				imageHeight = 48 * worldObject.getProperty(Constants.HEIGHT);
			}
			image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
		}
		return image;
	}

	//TODO: replace by code in WorldFacade?
    private boolean isWorldObjectVisible(WorldObject worldObject) {
		if (worldObject.equals(playerCharacter)) {
			return true;
		} else if (worldObject.hasProperty(Constants.CONDITIONS) && worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.INVISIBLE_CONDITION)) {
			return false;
		} else if (new WorldFacade(playerCharacter, world).isMaskedByIllusion(worldObject, world)) {
			return false;
		} else {
			return true;
		}
	}

	ImageIds getImageId(WorldObject worldObject) {
		WorldObject facade = worldObject.getProperty(Constants.FACADE);
		if (worldObject.hasProperty(Constants.CONDITIONS) && worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.COCOONED_CONDITION)) {
			return ImageIds.COCOON;
		} else if ((facade != null) && facade.getProperty(Constants.IMAGE_ID) != null) {
			return facade.getProperty(Constants.IMAGE_ID);
		} else {
			return worldObject.getProperty(Constants.IMAGE_ID);
		}
	}
    
    private List<Image> getOverlayingImages(WorldObject worldObject) {
    	List<Image> overlayingImages = new ArrayList<>();
    	if (worldObject.hasProperty(Constants.CONDITIONS)) {
	    	List<ImageIds> imageIds = worldObject.getProperty(Constants.CONDITIONS).getImageIds(); 
	    	for(ImageIds imageId : imageIds) {
	    		overlayingImages.add(imageInfoReader.getImage(imageId, null));
	    	}
    	}
    	return overlayingImages;
    }
    
    private List<String> getConditionDescriptions(WorldObject worldObject) {
    	if (worldObject.hasProperty(Constants.CONDITIONS)) {
	    	return worldObject.getProperty(Constants.CONDITIONS).getDescriptions(); 
    	} else {
    		return new ArrayList<>();
    	}
    }

	private boolean hasCondition(WorldObject worldObject, Condition condition) {
		return worldObject.hasProperty(Constants.CONDITIONS) && worldObject.getProperty(Constants.CONDITIONS).hasCondition(condition);
	}
    
	LookDirection getLookDirection(WorldObject worldObject) {
		if (worldObject.hasProperty(Constants.LOOK_DIRECTION)) {
			return worldObject.getProperty(Constants.LOOK_DIRECTION);
		} else {
			return null;
		}
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
    
	public WorldObject findWorldObject(int x, int y) {
        List<WorldObject> worldObjects = world.findWorldObjects(w -> new CreaturePositionCondition(y-offsetY, x-offsetX).isWorldObjectValid(w));
		final WorldObject worldObject;
		if (worldObjects.size() > 0) {
			worldObject = worldObjects.get(0);
		} else {
			worldObject = null;
		}
		return worldObject;
	}

	public void addGuiListeners(AdditionalManagedOperationListenerFactory additionalManagedOperationListenerFactory) {
		new GuiRespondToQuestion(playerCharacter, world, imageInfoReader, soundIdReader);
		new GuiShowReadAction(playerCharacter, world, this, imageInfoReader);
		new GuiShowBrawlResult(imageInfoReader, soundIdReader, this, world);
		new GuiShowDrinkingContestResult(imageInfoReader, soundIdReader, this, world);
		new GuiGameOverAction(playerCharacter, world, this, imageInfoReader, soundIdReader, musicPlayer, keyBindings);
		world.addWorldStateChangedListener(createWorldStateChangedListener());
		
		List<ManagedOperationListener> additionalManagedOperationListeners = additionalManagedOperationListenerFactory.create(world, this, imageInfoReader);
		for(ManagedOperationListener additionalManagedOperationListener : additionalManagedOperationListeners) {
			world.addListener(additionalManagedOperationListener);
		}
	}
	
	private WorldStateChangedListener createWorldStateChangedListener() {
		return new GuiShowEventHappenedAction(playerCharacter, world, this, imageInfoReader);
	}
	
	public WorldStateChangedListeners getWorldStateChangedListeners() {
		WorldStateChangedListeners worldStateChangedListeners = new WorldStateChangedListeners();
		worldStateChangedListeners.addWorldStateChangedListener(createWorldStateChangedListener());
		return worldStateChangedListeners;
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
		bindEscapeButtonToCalcelBuildMode();
	}

	public void endBuildMode(boolean executeBuildAction) {
		this.buildModeOutline.endBuildMode(executeBuildAction, getMouseLocation(), offsetX, offsetY, playerCharacter, world, guiMouseListener);
		this.removeMouseMotionListener(this.mouseMotionListener);
		repaint();
		bindEscapeButtonToStartScreen(world);
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
		
		if ((xInView < 48) || (xInView > this.getWidth() - 96) || (yInView < 48) || (yInView > this.getHeight() - 144)) {
			centerOffsetsOn(x+offsetX, y+offsetY);
		}
	}

	public void movePlayerCharacter(int[] args, ActionListener guiMoveAction) {
		moveMode.startMove(this, args, guiMoveAction, playerCharacter, world);
	}

	public void repaintAround(int x, int y, WorldObject worldObject) {
		int width = worldObject.getProperty(Constants.WIDTH);
		int height = worldObject.getProperty(Constants.HEIGHT);
		WorldPanel.this.repaint((x + offsetX) * 48 - 48, (y + offsetY) * 48 - 48, 48 * (2 + width), 48 * (2 + height));
	}
	
	@Override
	public String getToolTipText(MouseEvent e) {
		int worldPanelX = (int) e.getPoint().getX();
		int worldPanelY = (int) e.getPoint().getY();
		int x = worldPanelX / 48;
		int y = worldPanelY / 48;
		
		WorldObject worldObject = findWorldObject(x, y);
		WorldObject worldObjectNorth = findWorldObject(x, y-1);
		if (worldObject != null) {
			return getDescriptionFor(worldPanelX, worldPanelY, worldObject);
		} else if (worldObjectNorth != null) {
			return getConditionDescriptionFor(worldObjectNorth, worldPanelX, worldPanelY);
		} else {
			return null;
		}
	}

	private String getDescriptionFor(int worldPanelX, int worldPanelY, WorldObject worldObject) {
		String conditionDescription = getConditionDescriptionFor(worldObject, worldPanelX, worldPanelY);
		if (conditionDescription != null) {
			return conditionDescription;
		} else {
			return worldObject.getProperty(Constants.NAME);
		}
	}
	
	public void iterateOverVisibleTiles(DrawFunction drawFunction) {
		int widthInTiles = (getWidth() / 48) + 1;
		int heightInTiles = getHeight() / 48;
		for(int x = -offsetX; x<-offsetX+widthInTiles ;x++) {
			for(int y = -offsetY; y<-offsetY+heightInTiles; y++) {
				drawFunction.draw(x, y);
			}
		}
	}

	public void playSound(ManagedOperation action) {
		SoundIds soundId = action.getSoundId();
		if (soundId != null) {
			soundIdReader.playSoundEffect(soundId);
		}
	}
}