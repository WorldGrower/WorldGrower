package org.worldgrower.gui;

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
	private Map<Integer, Point> oldPositions = new HashMap<>();
	private Map<Integer, Point> newPositions = new HashMap<>();
	private List<WorldObject> magicCasters = new ArrayList<>();
	private List<MagicTarget> magicTargets = new ArrayList<>();
	
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

	private void initializeIntelligentWorldObjects(World world) {
		intelligentWorldObjects.clear();
		intelligentWorldObjects.addAll(world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasIntelligence()));
	}
	
	public void drawWorldObjects(Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world) {
		//System.out.println("drawWorldObjects: moveStep = " + moveStep + ", moveMode = " + moveMode);
		try {
			Thread.sleep(8);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
		initializeIntelligentWorldObjects(world);
		for(int i=0; i<intelligentWorldObjects.size(); i++) {
			WorldObject worldObject = intelligentWorldObjects.get(i);
			ImageIds id = worldPanel.getImageId(worldObject);
			LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
			Image image = imageInfoReader.getImage(id, lookDirection);
			
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			
			if (world.getTerrain().isExplored(x, y)) {
				boolean positionRemainsSame = positionRemainsSame(worldObject.getProperty(Constants.ID));
				if (moveMode && moveStep < 48 && !positionRemainsSame) {
					
					paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, worldObject.getProperty(Constants.ID), moveStep, moveIndex);
					
					if (moveStep % 16 == 0) {
						moveIndex = (moveIndex + 1) % 3;
					}
				} else {
					//System.out.println("drawWorldObjects.notMoving: moveStep = " + moveStep + ", moveMode = " + moveMode);
					if (!moveMode || positionRemainsSame) {
						worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0);
					} else {
						paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, worldObject.getProperty(Constants.ID), moveStep, moveIndex);
					}
				}
			}
		}
		for(int i=0; i<magicCasters.size(); i++) {
			paintMagicSpellForWorldObject(g, worldPanel, magicCasters.get(i), imageInfoReader, moveStep, moveIndex, world);
		}
		for(int i=0; i<magicTargets.size(); i++) {
			paintMagicTargetForWorldObject(g, worldPanel, magicTargets.get(i), imageInfoReader, moveStep, moveIndex, world);
		}
		if (moveMode && moveStep < 48) {
			moveStep += 2;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for(int i=0; i<intelligentWorldObjects.size(); i++) {
						WorldObject worldObject = intelligentWorldObjects.get(i);
						int x = worldObject.getProperty(Constants.X);
						int y = worldObject.getProperty(Constants.Y);
						worldPanel.repaintAround(x, y, worldObject);
						//worldPanel.repaint();
					}
				}
			});
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for(int i=0; i<magicCasters.size(); i++) {
						WorldObject magicCaster = magicCasters.get(i);
						int x = magicCaster.getProperty(Constants.X);
						int y = magicCaster.getProperty(Constants.Y);
						worldPanel.repaintAround(x, y, magicCaster);
						//worldPanel.repaint();
					}
				}
			});
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for(int i=0; i<magicTargets.size(); i++) {
						WorldObject magicTarget = magicTargets.get(i).getTarget();
						int x = magicTarget.getProperty(Constants.X);
						int y = magicTarget.getProperty(Constants.Y);
						worldPanel.repaintAround(x, y, magicTarget);
						//worldPanel.repaint();
					}
				}
			});
			
		} else {
			moveMode = false;
			moveIndex = 0;
			moveIndex = 0;
			if (this.guiAfterMoveAction != null) {
				this.guiAfterMoveAction.actionPerformed(null);
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
		
		Image image;
		
		Point oldPosition = oldPositions.get(worldObjectId);
		int x = oldPosition.x;
		int y = oldPosition.y;
		
		Point newPosition = newPositions.get(worldObjectId);
		int deltaX = (newPosition.x - x) * moveStep;
		int deltaY = (newPosition.y - y) * moveStep;
		
		image = imageInfoReader.getImage(id, lookDirection, moveIndex);
		worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, deltaX, deltaY);
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
				worldPanel.drawWorldObjectInPixels(g, magicCaster, null, image, x, y, 0, 0);
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
				worldPanel.drawWorldObjectInPixels(g, target, null, image, x, y, 0, 0);
			}
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
