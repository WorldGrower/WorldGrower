package org.worldgrower.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;

public class MoveMode {

	private boolean moveMode = false;
	private int moveStep = 0;
	private int moveIndex = 0;
	
	private List<WorldObject> intelligentWorldObjects = new ArrayList<>();
	private List<Point> oldPositions = new ArrayList<>();
	private List<Point> newPositions = new ArrayList<>();
	
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
				if (moveMode && moveStep < 48) {
					
					paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, i, moveStep, moveIndex);
					
					if (moveStep % 16 == 0) {
						moveIndex = (moveIndex + 1) % 3;
					}
				} else {
					//System.out.println("drawWorldObjects.notMoving: moveStep = " + moveStep + ", moveMode = " + moveMode);
					if (!moveMode) {
						worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0);
					} else {
						paintMovingWorldObject(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, i, moveStep, moveIndex);
					}
				}
			}
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
		} else {
			moveMode = false;
			moveIndex = 0;
			moveIndex = 0;
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
}
