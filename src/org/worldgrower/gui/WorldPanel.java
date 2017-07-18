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
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.worldgrower.Constants;
import org.worldgrower.CreaturePositionCondition;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.OperationInfo;
import org.worldgrower.ReadOnlyLocationWorldObjectsCache;
import org.worldgrower.World;
import org.worldgrower.WorldFacade;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.BerryBushOnTurn;
import org.worldgrower.generator.CottonPlantOnTurn;
import org.worldgrower.generator.GrapeVineOnTurn;
import org.worldgrower.generator.NightShadeOnTurn;
import org.worldgrower.generator.TreeOnTurn;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.gui.conversation.GuiRespondToQuestion;
import org.worldgrower.gui.conversation.GuiShowBrawlResult;
import org.worldgrower.gui.conversation.GuiShowDrinkingContestResult;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.terrain.TerrainType;

public final class WorldPanel extends JPanel implements ImageFactory, MouseLocationProvider {

	private WorldObject playerCharacter;
	private World world;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	private final MusicPlayer musicPlayer;
	private final GuiMouseListener guiMouseListener;
	private final ConditionIconDrawer conditionIconDrawer;
	private final BuySellIconsDrawer buySellIconsDrawer;
	private int offsetX = 0;
	private int offsetY = 0;
	
	private final InfoPanel infoPanel;
	
	private BuildModeOutline buildModeOutline = new BuildModeOutline();
	private MouseMotionListener mouseMotionListener;
	private final BonusDescriptions bonusDescriptions = new BonusDescriptions();
	
	private final AnimationPainter animationPainter;
	private final BackgroundPainter backgroundPainter;
	private final GoToPainter goToPainter;
	private final KeyBindings keyBindings;
	private final JFrame parentFrame;
	private final PlayerCharacterVisionPainter playerCharacterVisionPainter;
	
    public WorldPanel(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, String initialStatusMessage, KeyBindings keyBindings, JFrame parentFrame) throws IOException {
        super(new BorderLayout());
        this.imageInfoReader = imageInfoReader;
        this.soundIdReader = soundIdReader;
        this.musicPlayer = musicPlayer;
        this.keyBindings = keyBindings;
        this.parentFrame = parentFrame;

        guiMouseListener = new GuiMouseListener(this, playerCharacter, world, dungeonMaster, imageInfoReader, soundIdReader, keyBindings, parentFrame);
		addMouseListener(guiMouseListener);
		ToolTipManager.sharedInstance().registerComponent(this);
		setCursor(Cursors.CURSOR);

		conditionIconDrawer = new ConditionIconDrawer(imageInfoReader);
		buySellIconsDrawer = new BuySellIconsDrawer(imageInfoReader);
		
        int width = 1200;
        int height = 900;
        
        Rectangle bounds = getScreenWorkingArea();
        
        int screenWidth = bounds.width;
        int screenHeight = bounds.height;
        
       	width = Math.max(width, screenWidth);
       	height = Math.max(height, screenHeight);
        
        setBounds(0, 0, width, height);
        this.setMinimumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        
        setBackground(Color.BLACK);
        
        initializeKeyBindings(playerCharacter, world, dungeonMaster);
        
        this.infoPanel = new InfoPanel(playerCharacter, world, imageInfoReader, soundIdReader, initialStatusMessage, parentFrame, this);        
        add(infoPanel, BorderLayout.SOUTH);
        
        this.playerCharacter = playerCharacter;
        this.world = world;
        this.animationPainter = new AnimationPainter(this);
        Image grassBackground = imageInfoReader.getImage(ImageIds.GRASS_BACKGROUND, null);
        Image grassFlowersBackground = imageInfoReader.getImage(ImageIds.SMALL_FLOWERS, null);
		this.backgroundPainter = new BackgroundPainter(grassBackground, grassFlowersBackground, imageInfoReader, world);
		this.goToPainter = new GoToPainter(imageInfoReader);
		this.playerCharacterVisionPainter = new PlayerCharacterVisionPainter(new PlayerCharacterPositionImpl(this, animationPainter));
		
		centerIfNecessary(width, height, world);
    }

	private void centerIfNecessary(int width, int height, World world) {
		int widthInSquares = width / 48;
		int heightInSquares = height / 48;
		int worldWidth = world.getWidth();
		int worldHeight = world.getHeight();
		
		if (worldWidth < widthInSquares || worldHeight < heightInSquares) {
			centerOffsetsOn(worldWidth / 2, worldHeight / 2);
		}
	}

	private Rectangle getScreenWorkingArea() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(ge.getDefaultScreenDevice().getDefaultConfiguration());
        Rectangle bounds = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= (insets.left + insets.right);
        bounds.height -= (insets.top + insets.bottom);
		return bounds;
	}
    
	private void initializeKeyBindings(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster) {
		getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        bindEscapeButtonToStartScreen(world);
        
        getScreenInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "up");
        getScreenActionMap().put("up", new GuiMoveAction(new int[] { 0,  -1 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));
        
        getScreenInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "down");
        getScreenActionMap().put("down", new GuiMoveAction(new int[] { 0,  1 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));
        
        getScreenInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "left");
        getScreenActionMap().put("left", new GuiMoveAction(new int[] { -1,  0 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));
        
        getScreenInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "right");
        getScreenActionMap().put("right", new GuiMoveAction(new int[] { 1,  0 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));

        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "7");
        getScreenActionMap().put("7", new GuiMoveAction(new int[] { -1,  -1 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));
        
        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "9");
        getScreenActionMap().put("9", new GuiMoveAction(new int[] { 1,  -1 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));

        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "1");
        getScreenActionMap().put("1", new GuiMoveAction(new int[] { -1,  1 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));
        
        getScreenInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "3");
        getScreenActionMap().put("3", new GuiMoveAction(new int[] { 1,  1 }, playerCharacter, world, dungeonMaster, this, imageInfoReader, soundIdReader));
	}

	private InputMap getScreenInputMap() {
		return parentFrame.getRootPane().getInputMap();
	}

	private void bindEscapeButtonToStartScreen(World world) {
		getScreenActionMap().put("Cancel", new ShowStartScreenAction(this, imageInfoReader, soundIdReader, musicPlayer, keyBindings, world, parentFrame));
	}

	private ActionMap getScreenActionMap() {
		return parentFrame.getRootPane().getActionMap();
	}
	
	private void bindEscapeButtonToCancelBuildMode() {
		getScreenActionMap().put("Cancel", new CancelBuildModeAction(this));
	}
    
    public void initializeKeyBindings() {
    	guiMouseListener.initializeKeyBindings();
    }
    
    public void clearStatusMessages() {
    	infoPanel.clearStatusMessages();
    }
    
    public void setStatusMessage(String message) {
    	infoPanel.setStatusMessage(message);
    }
    
    public void setStatusMessage(Image image, String message) {
    	infoPanel.setStatusMessage(image, message);
    }
    
    public void showStatusMessageDialog() {
    	infoPanel.showStatusMessageDialog();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        backgroundPainter.paint(g, world, this);
        
		Shape clipShape = calculateClipShape();
		g.setClip(clipShape);
		animationPainter.drawWorldObjects(g, this, imageInfoReader, world);
		g.setClip(null);
		
		goToPainter.paint(g, world, this);
		
		showTargetHitPoints(g);
		
		playerCharacterVisionPainter.paintPlayerCharacterVision(g, playerCharacter, world, this);
		
		buildModeOutline.repaintBuildMode(g, this, offsetX, offsetY, playerCharacter, world);
		infoPanel.updatePlayerCharacterValues();
    }
	
	public List<WorldObject> getWorldObjectsOnScreen(Function<WorldObject, Boolean> worldObjectAcceptancefunction) {
		List<WorldObject> worldObjectsOnScreen = new ArrayList<>();
		int screenWidth = this.getWidth() / 48;
    	int screenHeight = this.getHeight() / 48;
    	ReadOnlyLocationWorldObjectsCache cache = new ReadOnlyLocationWorldObjectsCache(world);
    	
		for(int x=-offsetX; x<-offsetX + screenWidth; x++) {
			for(int y=-offsetY; y<-offsetY + screenHeight; y++) {
				if (!LocationUtils.areInvalidCoordinates(x, y, world)) {
					for(WorldObject worldObject : cache.getworldObjects(x, y, world)) {
						if (worldObject.getProperty(Constants.X).intValue() == x && worldObject.getProperty(Constants.Y).intValue() == y) {
							int width = worldObject.getProperty(Constants.WIDTH);
							int height = worldObject.getProperty(Constants.HEIGHT);
							
							if (worldObjectAcceptancefunction.apply(worldObject)) {
								if (shouldDrawWorldObject(worldObject, x, y, width, height)) {
									worldObjectsOnScreen.add(worldObject);
								}
							}
						}
					}
				}
			}
		}
		return worldObjectsOnScreen;
	}

	private boolean shouldDrawWorldObject(WorldObject worldObject, int x, int y, int width, int height) {
		return worldObjectIsExplored(x, y, width, height) && isWorldObjectVisible(worldObject);
	}
	
	private Shape calculateClipShape() {
		Rectangle exploredBoundsInSquares = world.getTerrain().getExploredBoundsInSquares();
		return new Rectangle((exploredBoundsInSquares.x+offsetX) * 48, (exploredBoundsInSquares.y+offsetY) * 48, exploredBoundsInSquares.width * 48, exploredBoundsInSquares.height * 48);
	}

	private boolean worldObjectIsExplored(int x, int y, int width, int height) {
		return world.getTerrain().isExplored(x, y, width, height);
	}

	private void showTargetHitPoints(Graphics g) {
		OperationInfo lastPerformedOperation = world.getHistory().getLastPerformedOperation(playerCharacter);
		if (lastPerformedOperation != null) {
			WorldObject target = lastPerformedOperation.getTarget();
			if (target.hasProperty(Constants.HIT_POINTS)
					&& world.exists(target)
					&& !target.equals(playerCharacter)) {
				target = world.findWorldObjectById(target.getProperty(Constants.ID));
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
		drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0, true);
		if (isTransparant) {
			graphics2d.setComposite(originalComposite);
		}
	}
	
	public void drawWorldObjectInPixels(Graphics g, WorldObject worldObject, LookDirection lookDirection, Image image, int xInSquares, int yInSquares, int xDeltaInPixels, int yDeltaInPixels, boolean drawConditions, Composite composite) {
		Graphics2D g2 = (Graphics2D) g;
		Composite originalComposite = g2.getComposite();
		g2.setComposite(composite);
		
		drawWorldObjectInPixels(g, worldObject, lookDirection, image, xInSquares, yInSquares, xDeltaInPixels, yDeltaInPixels, drawConditions);
		g2.setComposite(originalComposite);
	}
	
	public void drawWorldObjectInPixels(Graphics g, WorldObject worldObject, LookDirection lookDirection, Image image, int xInSquares, int yInSquares, int xDeltaInPixels, int yDeltaInPixels, boolean drawConditions) {
		image = changeSize(worldObject, image);
		int worldObjectX = (xInSquares+offsetX) * 48 + xDeltaInPixels;
		int worldObjectY = (yInSquares+offsetY) * 48 + yDeltaInPixels;
		g.drawImage(image, worldObjectX, worldObjectY, null);
		
		if (drawConditions) {
			conditionIconDrawer.drawConditions(g, worldObject, lookDirection, image, worldObjectX, worldObjectY);		
			buySellIconsDrawer.drawIcons(g, worldObject, lookDirection, image, worldObjectX, worldObjectY);
			drawSellableMarker(g, worldObject, worldObjectX, worldObjectY);
		}
	}
	
	private void drawSellableMarker(Graphics g, WorldObject worldObject, int worldObjectX, int worldObjectY) {
		if (worldObject.hasProperty(Constants.SELLABLE) && worldObject.getProperty(Constants.SELLABLE)) {
			Image image = imageInfoReader.getImage(Actions.SELL_ACTION.getImageIds(worldObject), null);
			int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
			int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
			
			int sellableImageX = worldObjectX + worldObjectWidth - 48;
			int sellableImageY = worldObjectY + worldObjectHeight - 48;
			g.drawImage(image, sellableImageX, sellableImageY, null);
		}
	}

	public void drawBackgroundImage(Graphics g, Image image, int x, int y) {
		g.drawImage(image, (x+offsetX) * 48, (y+offsetY) * 48, null);
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
	
	@Override
	public Image getImage(WorldObject worldObject) {
		ImageIds imageId = getImageId(worldObject);
		return imageInfoReader.getImage(imageId, null);
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
    	int screenWidth = this.getWidth() / 48;
    	int screenHeight = (this.getHeight() - this.infoPanel.getHeight()) / 48;
    	this.offsetX = offsetX - (x - screenWidth / 2);
    	this.offsetY = offsetY - (y - screenHeight / 2);
    	
    	if (offsetX > 0) {
    		offsetX = 0;
    	}
    	if (offsetY > 0) {
    		offsetY = 0;
    	}
    	
    	if (world.getWidth() < screenWidth) {
    		offsetX = (screenWidth - world.getWidth()) / 2;
    	} else {
	    	if (offsetX < -world.getWidth() + screenWidth) {
	    		offsetX = -world.getWidth() + screenWidth;
	    	}
    	}
    	
    	if (world.getHeight() < screenHeight) {
    		offsetY = (screenHeight - world.getHeight()) / 2;
    	} else {
	    	if (offsetY < -world.getHeight() + screenHeight) {
	    		offsetY = -world.getHeight() + screenHeight;
	    	}
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

	public void addGuiListeners(AdditionalManagedOperationListenerFactory additionalManagedOperationListenerFactory, JFrame parentFrame) {
		new GuiRespondToQuestion(playerCharacter, world, imageInfoReader, soundIdReader, parentFrame);
		new GuiShowReadAction(playerCharacter, world, this, imageInfoReader);
		new GuiShowBrawlResult(imageInfoReader, soundIdReader, this, world, parentFrame);
		new GuiShowDrinkingContestResult(imageInfoReader, soundIdReader, this, world, parentFrame);
		new GuiGameOverAction(playerCharacter, world, this, imageInfoReader, soundIdReader, musicPlayer, keyBindings, parentFrame);
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
		this.buildModeOutline.startBuildMode(playerCharacter, buildAction, args, imageInfoReader);
		this.mouseMotionListener = new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent mouseEvent) {
				super.mouseMoved(mouseEvent);
				WorldPanel.this.repaint();
			}
			
		};
		this.addMouseMotionListener(this.mouseMotionListener);
		bindEscapeButtonToCancelBuildMode();
	}

	public void endBuildMode(boolean executeBuildAction) {
		this.buildModeOutline.endBuildMode(executeBuildAction, this, offsetX, offsetY, playerCharacter, world, guiMouseListener);
		this.removeMouseMotionListener(this.mouseMotionListener);
		repaint();
		bindEscapeButtonToStartScreen(world);
	}

	@Override
	public Point getMouseLocation() {
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

	public void movePlayerCharacter(int[] args, ActionListener guiMoveAction, ActionListener guiAfterMoveAction) {
		animationPainter.startMove(this, args, guiMoveAction, guiAfterMoveAction, playerCharacter, world, imageInfoReader);
	}

	public void repaintAround(int x, int y, WorldObject worldObject) {
		int worldObjectX = x + offsetX;
		int worldObjectY = y + offsetY;
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH);
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT);
		
		int repaintX = worldObjectX * 48 - 48;
		int repaintY = worldObjectY * 48 - 48;
		int repaintWidth = 48 * (3 + worldObjectWidth);
		int repaintHeight = 48 * (3 + worldObjectHeight);
		WorldPanel.this.repaint(repaintX, repaintY, repaintWidth, repaintHeight);
	}
	
	@Override
	public Point getToolTipLocation(MouseEvent event) {
		final int mouseCutoff = 20;
		Point currentMouseLocation = event.getPoint();
		int x = (currentMouseLocation.x / mouseCutoff) * mouseCutoff;
		int y = (currentMouseLocation.y / mouseCutoff) * mouseCutoff;
		return new Point(x, y);
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
			return getWorldObjectTooltip(worldPanelX, worldPanelY, x, y, worldObject);
		} else if (worldObjectNorth != null) {
			return getIconTooltip(worldPanelX, worldPanelY, worldObjectNorth);
		} else {
			return getTerrainTooltip(x, y);
		}
	}

	private String getIconTooltip(int worldPanelX, int worldPanelY, WorldObject worldObjectNorth) {
		String conditionDescription =  conditionIconDrawer.getConditionDescriptionFor(worldObjectNorth, worldPanelX, worldPanelY, offsetX, offsetY);
		if (conditionDescription != null) {
			return conditionDescription;
		} else {
			return buySellIconsDrawer.getItemDescriptionFor(worldObjectNorth, worldPanelX, worldPanelY, offsetX, offsetY);
		}
	}

	private String getWorldObjectTooltip(int worldPanelX, int worldPanelY, int x, int y, WorldObject worldObject) {
		int width = worldObject.getProperty(Constants.WIDTH);
		int height = worldObject.getProperty(Constants.HEIGHT);
		int actualX = x-offsetX;
		int actualY = y-offsetY;
		if (shouldDrawWorldObject(worldObject, actualX, actualY, width, height)) {
			return getDescriptionFor(worldPanelX, worldPanelY, worldObject);
		} else {
			return null;
		}
	}

	private String getTerrainTooltip(int x, int y) {
		int actualX = x-offsetX;
		int actualY = y-offsetY;
		if (worldObjectIsExplored(actualX, actualY, 1, 1)) {
			TerrainType terrainType = world.getTerrain().getTerrainInfo(actualX, actualY).getTerrainType();
			StringBuilder terrainTooltipBuilder = new StringBuilder();
			terrainTooltipBuilder.append("<html>");
			terrainTooltipBuilder.append(terrainType.getDescription()).append("<br>");
			terrainTooltipBuilder.append("<table>");
			terrainTooltipBuilder.append("<tr><td>").append(imageInfoReader.smallImageTag(ImageIds.BERRY)).append(" food bonus </td><td>").append(BerryBushOnTurn.getPercentageFoodBonus(terrainType)).append("</td></tr>");
			terrainTooltipBuilder.append("<tr><td>").append(imageInfoReader.smallImageTag(ImageIds.WOOD)).append(" wood bonus </td><td>").append(TreeOnTurn.getPercentageWoodBonus(terrainType)).append("</td></tr>");
			terrainTooltipBuilder.append("<tr><td>").append(imageInfoReader.smallImageTag(ImageIds.COTTON)).append(" cotton bonus </td><td>").append(CottonPlantOnTurn.getPercentageCottonBonus(terrainType)).append("</td></tr>");
			terrainTooltipBuilder.append("<tr><td>").append(imageInfoReader.smallImageTag(ImageIds.GRAPES)).append(" grapes bonus </td><td>").append(GrapeVineOnTurn.getPercentageGrapesBonus(terrainType)).append("</td></tr>");
			terrainTooltipBuilder.append("<tr><td>").append(imageInfoReader.smallImageTag(ImageIds.NIGHT_SHADE)).append(" nightshade bonus </td><td>").append(NightShadeOnTurn.getPercentageNightShadeBonus(terrainType)).append("</td></tr>");
			terrainTooltipBuilder.append("</html>");
			return terrainTooltipBuilder.toString();
		} else {
			return null;
		}
	}

	private String getDescriptionFor(int worldPanelX, int worldPanelY, WorldObject worldObject) {
		String conditionDescription = conditionIconDrawer.getConditionDescriptionFor(worldObject, worldPanelX, worldPanelY, offsetX, offsetY);
		String buySellDescription = buySellIconsDrawer.getItemDescriptionFor(worldObject, worldPanelX, worldPanelY, offsetX, offsetY);
		if (conditionDescription != null) {
			return conditionDescription;
		} else if (buySellDescription != null) {
			return buySellDescription;
		} else {
			WorldObject facade = worldObject.getProperty(Constants.FACADE);
			if ((facade != null) && facade.getProperty(Constants.NAME) != null) {
				return bonusDescriptions.getWorldObjectDescription(facade, world);
			} else {
				return bonusDescriptions.getWorldObjectDescription(worldObject, world);
			}
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

	public int getRealX(int x) {
		return x-offsetX;
	}
	
	public int getScreenX(int x) {
		return x+offsetX;
	}
	
	public int getRealY(int y) {
		return y-offsetY;
	}
	
	public int getScreenY(int y) {
		return y+offsetY;
	}

	@Override
	public Image getMoreMessagesImage() {
		return imageInfoReader.getImage(ImageIds.BOOK, null);
	}
	
	public void setGotoPath(List<OperationInfo> operationInfos) {
		this.goToPainter.setGotoOperations(playerCharacter, operationInfos);
	}

	public int getWorldViewWidth() {
		return getWidth();
	}

	public int getWorldViewHeight() {
		return getHeight() - infoPanel.getHeight();
	}
}