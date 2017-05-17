package org.worldgrower.gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.LookDirection;

public class MoveMode {
	private ActionListener guiAfterMoveAction;
	private boolean moveMode = false;
	private int moveStep = 0;
	private int moveIndex = 0;
	
	private List<WorldObject> intelligentWorldObjects = new ArrayList<>();
	private List<WorldObject> deadIntelligentWorldObjects = new ArrayList<>();
	private Map<Integer, Point> oldPositions = new HashMap<>();
	private Map<Integer, Point> newPositions = new HashMap<>();
	private List<WorldObject> magicCasters = new ArrayList<>();
	private List<MagicTarget> magicTargets = new ArrayList<>();
	
	public MoveMode(World world) {
		initializeIntelligentWorldObjects(world);
	}
	
	public void startMove(WorldPanel worldPanel, int[] args, ActionListener guiMoveAction, ActionListener guiAfterMoveAction, WorldObject worldObject, World world, ImageInfoReader imageInfoReader) {
		if (moveMode) {
			return;
		}
		
		moveMode = true;
		moveStep = 0;
		moveIndex = 0;
		this.guiAfterMoveAction = guiAfterMoveAction;
		
		//System.out.println("startMove: moveStep = " + moveStep);
		
		initializeMovingWorldObjects(guiMoveAction, world, imageInfoReader);
	}

	private void initializeMovingWorldObjects(ActionListener guiMoveAction, World world, ImageInfoReader imageInfoReader) {
		initializeIntelligentWorldObjects(world);
		
		oldPositions.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			int x = intelligentWorldObject.getProperty(Constants.X);
			int y = intelligentWorldObject.getProperty(Constants.Y);
			oldPositions.put(intelligentWorldObject.getProperty(Constants.ID), new Point(x, y));
		}
		
		guiMoveAction.actionPerformed(null);
		
		newPositions.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			int x = intelligentWorldObject.getProperty(Constants.X);
			int y = intelligentWorldObject.getProperty(Constants.Y);
			newPositions.put(intelligentWorldObject.getProperty(Constants.ID), new Point(x, y));
		}
		
		
		initializeMagicCastersAndTargets(world, imageInfoReader);
		initializeDeadIntelligentWorldObjects();
	}

	private void initializeMagicCastersAndTargets(World world, ImageInfoReader imageInfoReader) {
		magicCasters.clear();
		magicTargets.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			OperationInfo lastPerformedOperationInfo = world.getHistory().getLastPerformedOperation(intelligentWorldObject);
			if (lastPerformedOperationInfo != null) {
				if (lastPerformedOperationInfo.getManagedOperation() instanceof MagicSpell) {
					magicCasters.add(intelligentWorldObject);
				}
				if (lastPerformedOperationInfo.getManagedOperation() instanceof AnimatedAction) {
					AnimatedAction animatedAction = (AnimatedAction) lastPerformedOperationInfo.getManagedOperation();
					List<WorldObject> affectedTargets = animatedAction.getAffectedTargets(lastPerformedOperationInfo.getTarget(), world);
					for(WorldObject affectedTarget : affectedTargets) {
						magicTargets.add(new MagicTarget(affectedTarget, animatedAction.getAnimationImageId(), imageInfoReader));
					}
				}
			}
		}
	}
	
	private void initializeDeadIntelligentWorldObjects() {
		deadIntelligentWorldObjects.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			if (intelligentWorldObject.getProperty(Constants.HIT_POINTS) == 0) {
				deadIntelligentWorldObjects.add(intelligentWorldObject);
			}
		}
	}

	private void initializeIntelligentWorldObjects(World world) {
		intelligentWorldObjects.clear();
		intelligentWorldObjects.addAll(world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasIntelligence() && w.getProperty(Constants.HIT_POINTS) > 0));
	}
	
	public void drawWorldObjects(Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world) {
		//System.out.println("drawWorldObjects: moveStep = " + moveStep + ", moveMode = " + moveMode);
		if (moveStep > 0 && moveStep < 48) {
			try {
				Thread.sleep(8);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
		
		boolean drawAnimation = moveMode && moveStep < 48;
		paintIntelligentWorldObjects(g, worldPanel, imageInfoReader, world, drawAnimation);
		if (drawAnimation) {
			paintDeadIntelligentWorldObjects(g, worldPanel, imageInfoReader, world);
		}
		
		for(WorldObject magicCaster : magicCasters) {
			paintMagicSpellForWorldObject(g, worldPanel, magicCaster, imageInfoReader, moveStep, moveIndex, world);
		}
		for(MagicTarget magicTarget : magicTargets) {
			paintMagicTargetForWorldObject(g, worldPanel, magicTarget, imageInfoReader, moveStep, moveIndex, world);
		}
		if (drawAnimation) {
			moveStep += 2;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaintIntelligentWorldObjects(worldPanel);
				}
			});
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaintMagicCaster(worldPanel);
				}
			});
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaintMagicTargets(worldPanel);
				}
			});
			
		} else {
			moveMode = false;
			moveIndex = 0;
			moveStep = 0;
			if (this.guiAfterMoveAction != null) {
				this.guiAfterMoveAction.actionPerformed(null);
			}
		}
	}

	private void paintIntelligentWorldObjects(Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world, boolean drawAnimation) {
		for(int i=0; i<intelligentWorldObjects.size(); i++) {
			WorldObject worldObject = intelligentWorldObjects.get(i);
			if (worldObject.getProperty(Constants.HIT_POINTS) > 0) {
				ImageIds id = worldPanel.getImageId(worldObject);
				LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
				Image image = imageInfoReader.getImage(id, lookDirection);
				
				int x = worldObject.getProperty(Constants.X);
				int y = worldObject.getProperty(Constants.Y);
				
				if (world.getTerrain().isExplored(x, y)) {
					boolean positionRemainsSame = positionRemainsSame(worldObject.getProperty(Constants.ID));
					if (drawAnimation && !positionRemainsSame) {
						
						paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, worldObject.getProperty(Constants.ID), moveStep, moveIndex);
						
						if (moveStep % 16 == 0) {
							moveIndex = (moveIndex + 1) % 3;
						}
					} else {
						//System.out.println("drawWorldObjects.notMoving: moveStep = " + moveStep + ", moveMode = " + moveMode);
						if (!moveMode || positionRemainsSame) {
							worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0, true);
						} else {
							paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, worldObject.getProperty(Constants.ID), moveStep, moveIndex);
						}
					}
				}
			}
		}
	}
	
	private boolean positionRemainsSame(int id) {
		if (oldPositions.size() == 0) {
			return true;
		} else {
			Point oldPosition = oldPositions.get(id);
			Point newPosition = newPositions.get(id);
			
			if (oldPosition != null && newPosition != null) {
				int x = oldPosition.x;
				int y = oldPosition.y;
				
				int newX = newPosition.x;
				int newY = newPosition.y;
				
				return ((x == newX) && (y == newY));
			} else {
				return true;
			}
		}
	}

	private void paintMovingWorldObject(Graphics g, WorldPanel worldPanel,
			WorldObject worldObject, ImageInfoReader imageInfoReader,
			ImageIds id, LookDirection lookDirection, int worldObjectId,
			int moveStep, int moveIndex) {
		
		Point oldPosition = oldPositions.get(worldObjectId);
		int x = oldPosition.x;
		int y = oldPosition.y;
		
		Point newPosition = newPositions.get(worldObjectId);
		int deltaX = (newPosition.x - x) * moveStep;
		int deltaY = (newPosition.y - y) * moveStep;
		
		Image image = imageInfoReader.getImage(id, lookDirection, moveIndex);
		worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, deltaX, deltaY, true);
	}
	
	private void paintMagicSpellForWorldObject(Graphics g, WorldPanel worldPanel,
			WorldObject magicCaster, ImageInfoReader imageInfoReader,
			int moveStep, int moveIndex, World world) {

		int imageIndex = (moveStep / 2);
		if (moveStep < 47) {
			Image image = imageInfoReader.getImage(ImageIds.MAGIC1, imageIndex);
			int x = magicCaster.getProperty(Constants.X) - 1;
			int y = magicCaster.getProperty(Constants.Y) - 1;
			if (world.getTerrain().isExplored(x, y)) {
				worldPanel.drawWorldObjectInPixels(g, magicCaster, null, image, x, y, 0, 0, false);
			}
		}
	}
	
	private void paintMagicTargetForWorldObject(Graphics g, WorldPanel worldPanel,
			MagicTarget magicTarget, ImageInfoReader imageInfoReader,
			int moveStep, int moveIndex, World world) {

		int numberOfFrames = magicTarget.getNumberOfFrames();
		final int imageIndex;
		if (numberOfFrames == 10) {
			imageIndex = moveStep / 5;
		} else if (numberOfFrames == 20) {
			imageIndex = moveStep / 3;
		} else if (numberOfFrames == 25) {
			imageIndex = moveStep / 3;
		} else if (numberOfFrames == 30) {
			imageIndex = moveStep / 3;
		} else {
			imageIndex = moveStep;
		}
		if (moveStep < 47) {
			Image image = imageInfoReader.getImage(magicTarget.getImageId(), imageIndex);
			WorldObject target = magicTarget.getTarget();
			Integer x = target.getProperty(Constants.X);
			Integer y = target.getProperty(Constants.Y);
			if (world.getTerrain().isExplored(x, y)) {
				worldPanel.drawWorldObjectInPixels(g, target, null, image, x, y, 0, 0, true);
			}
		}
	}
	
	public void paintDeadIntelligentWorldObjects(Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader,
			World world) {
		for(WorldObject worldObject : deadIntelligentWorldObjects) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			
			if (world.getTerrain().isExplored(x, y)) {
				ImageIds imageId = worldPanel.getImageId(worldObject);
				LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
				Image image = imageInfoReader.getImage(imageId, lookDirection);
				
				Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - (0.02f * moveStep));
				worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0, false, composite);
				worldPanel.repaintAround(x, y, worldObject);				
				//worldPanel.repaint();
			}
		}
	}

	private void repaintIntelligentWorldObjects(WorldPanel worldPanel) {
		for(WorldObject worldObject : intelligentWorldObjects) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			worldPanel.repaintAround(x, y, worldObject);
			//worldPanel.repaint();
		}
	}

	private void repaintMagicCaster(WorldPanel worldPanel) {
		for(WorldObject magicCaster : magicCasters) {
			int x = magicCaster.getProperty(Constants.X);
			int y = magicCaster.getProperty(Constants.Y);
			worldPanel.repaintAround(x, y, magicCaster);
			//worldPanel.repaint();
		}
	}

	private void repaintMagicTargets(WorldPanel worldPanel) {
		for(MagicTarget magicTarget : magicTargets) {
			WorldObject target = magicTarget.getTarget();
			int x = target.getProperty(Constants.X);
			int y = target.getProperty(Constants.Y);
			worldPanel.repaintAround(x, y, target);
			//worldPanel.repaint();
		}
	}

	private static class MagicTarget {
		private final WorldObject target;
		private final ImageIds imageId;
		private final int numberOfFrames;
		
		public MagicTarget(WorldObject target, ImageIds imageId, ImageInfoReader imageInfoReader) {
			this.target = target;
			this.imageId = imageId;
			this.numberOfFrames = imageInfoReader.getNumberOfFrames(imageId);
		}

		public WorldObject getTarget() {
			return target;
		}

		public ImageIds getImageId() {
			return imageId;
		}

		public int getNumberOfFrames() {
			return numberOfFrames;
		}
	}
}
