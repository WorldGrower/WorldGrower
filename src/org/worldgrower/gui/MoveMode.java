package org.worldgrower.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.magic.FireBoltAttackAction;
import org.worldgrower.actions.magic.LightningBoltAttackAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.RayOfFrostAttackAction;
import org.worldgrower.attribute.LookDirection;

public class MoveMode {

	private boolean moveMode = false;
	private int moveStep = 0;
	private int moveIndex = 0;
	
	private List<WorldObject> intelligentWorldObjects = new ArrayList<>();
	private List<Point> oldPositions = new ArrayList<>();
	private List<Point> newPositions = new ArrayList<>();
	private List<WorldObject> magicCasters = new ArrayList<>();
	private List<MagicTarget> magicTargets = new ArrayList<>();
	
	public void startMove(WorldPanel worldPanel, int[] args, ActionListener guiMoveAction, WorldObject worldObject, World world) {
		if (moveMode) {
			return;
		}
		
		moveMode = true;
		moveStep = 0;
		moveIndex = 0;
		//System.out.println("startMove: moveStep = " + moveStep);
		
		initializeMovingWorldObjects(guiMoveAction, world);
	}

	private void initializeMovingWorldObjects(ActionListener guiMoveAction, World world) {
		initializeIntelligentWorldObjects(world);
		
		oldPositions.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			int x = intelligentWorldObject.getProperty(Constants.X);
			int y = intelligentWorldObject.getProperty(Constants.Y);
			oldPositions.add(new Point(x, y));
		}
		
		guiMoveAction.actionPerformed(null);
		
		newPositions.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			int x = intelligentWorldObject.getProperty(Constants.X);
			int y = intelligentWorldObject.getProperty(Constants.Y);
			newPositions.add(new Point(x, y));
		}
		
		magicCasters.clear();
		magicTargets.clear();
		for(WorldObject intelligentWorldObject : intelligentWorldObjects) {
			OperationInfo lastPerformedOperationInfo = world.getHistory().getLastPerformedOperation(intelligentWorldObject).getOperationInfo();
			if (lastPerformedOperationInfo.getManagedOperation() instanceof MagicSpell) {
				magicCasters.add(intelligentWorldObject);
			}
			if (lastPerformedOperationInfo.getManagedOperation() instanceof FireBoltAttackAction) {
				magicTargets.add(new MagicTarget(lastPerformedOperationInfo.getTarget(), ImageIds.FIRE1, 20));
			}
			if (lastPerformedOperationInfo.getManagedOperation() instanceof RayOfFrostAttackAction) {
				magicTargets.add(new MagicTarget(lastPerformedOperationInfo.getTarget(), ImageIds.ICE1, 30));
			}
			if (lastPerformedOperationInfo.getManagedOperation() instanceof LightningBoltAttackAction) {
				magicTargets.add(new MagicTarget(lastPerformedOperationInfo.getTarget(), ImageIds.THUNDER1, 30));
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
		if (intelligentWorldObjects.size() == 0) {
			initializeIntelligentWorldObjects(world);
		}
		for(int i=0; i<intelligentWorldObjects.size(); i++) {
			WorldObject worldObject = intelligentWorldObjects.get(i);
			ImageIds id = worldPanel.getImageId(worldObject);
			LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
			Image image = imageInfoReader.getImage(id, lookDirection);
			
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			
			if (world.getTerrain().isExplored(x, y)) {
				boolean positionRemainsSame = positionRemainsSame(i);
				if (moveMode && moveStep < 48 && !positionRemainsSame) {
					
					paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, i, moveStep, moveIndex);
					
					if (moveStep % 16 == 0) {
						moveIndex = (moveIndex + 1) % 3;
					}
				} else {
					//System.out.println("drawWorldObjects.notMoving: moveStep = " + moveStep + ", moveMode = " + moveMode);
					if (!moveMode || positionRemainsSame) {
						worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0);
					} else {
						paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, i, moveStep, moveIndex);
					}
				}
			}
		}
		for(int i=0; i<magicCasters.size(); i++) {
			paintMagicSpellForWorldObject(g, worldPanel, magicCasters.get(i), imageInfoReader, moveStep, moveIndex);
		}
		for(int i=0; i<magicTargets.size(); i++) {
			paintMagicTargetForWorldObject(g, worldPanel, magicTargets.get(i), imageInfoReader, moveStep, moveIndex);
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
		}
	}
	
	private boolean positionRemainsSame(int positionIndex) {
		if (oldPositions.size() == 0) {
			return true;
		} else {
			int x = oldPositions.get(positionIndex).x;
			int y = oldPositions.get(positionIndex).y;
			
			int newX = newPositions.get(positionIndex).x;
			int newY = newPositions.get(positionIndex).y;
			
			return ((x == newX) && (y == newY));
		}
	}

	private void paintMovingWorldObject(Graphics g, WorldPanel worldPanel,
			WorldObject worldObject, ImageInfoReader imageInfoReader,
			ImageIds id, LookDirection lookDirection, int positionIndex,
			int moveStep, int moveIndex) {
		
		Image image;
		
		int x = oldPositions.get(positionIndex).x;
		int y = oldPositions.get(positionIndex).y;
		
		int deltaX = (newPositions.get(positionIndex).x - x) * moveStep;
		int deltaY = (newPositions.get(positionIndex).y - y) * moveStep;
		
		image = imageInfoReader.getImage(id, lookDirection, moveIndex);
		worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, deltaX, deltaY);
	}
	
	private void paintMagicSpellForWorldObject(Graphics g, WorldPanel worldPanel,
			WorldObject magicCaster, ImageInfoReader imageInfoReader,
			int moveStep, int moveIndex) {

		int imageIndex = (moveStep / 2);
		if (moveStep < 47) {
			Image image = imageInfoReader.getImage(ImageIds.MAGIC1, imageIndex);
			Integer x = magicCaster.getProperty(Constants.X) - 1;
			Integer y = magicCaster.getProperty(Constants.Y) - 1;
			worldPanel.drawWorldObjectInPixels(g, magicCaster, null, image, x, y, 0, 0);
		}
	}
	
	private void paintMagicTargetForWorldObject(Graphics g, WorldPanel worldPanel,
			MagicTarget magicTarget, ImageInfoReader imageInfoReader,
			int moveStep, int moveIndex) {

		int numberOfFrames = magicTarget.getNumberOfFrames();
		final int imageIndex;
		if (numberOfFrames == 20) {
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
			worldPanel.drawWorldObjectInPixels(g, target, null, image, x, y, 0, 0);
		}
	}
	
	private static class MagicTarget {
		private final WorldObject target;
		private final ImageIds imageId;
		private final int numberOfFrames;
		
		public MagicTarget(WorldObject target, ImageIds imageId, int numberOfFrames) {
			this.target = target;
			this.imageId = imageId;
			this.numberOfFrames = numberOfFrames;
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
