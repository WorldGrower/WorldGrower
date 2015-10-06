package org.worldgrower.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;

public class MoveMode {

	private boolean moveMode = false;
	private int[] moveDirection = null;
	private int moveStep = 0;
	private ActionListener guiMoveAction;
	private int moveIndex = 0;
	
	public void startMove(WorldPanel worldPanel, int[] args, ActionListener guiMoveAction, WorldObject worldObject) {
		if (moveMode) {
			return;
		}
		
		moveMode = true;
		moveDirection = args;
		moveStep = 0;
		this.guiMoveAction = guiMoveAction;
		moveIndex = 0;
		
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		worldPanel.repaintAround(x, y, worldObject);
	}
	
	public void drawWorldObject(Graphics g, WorldPanel worldPanel, WorldObject worldObject, ImageInfoReader imageInfoReader) {
		
		ImageIds id = worldPanel.getImageId(worldObject);
		LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
		Image image = imageInfoReader.getImage(id, lookDirection);
		
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		
		if (moveMode && moveStep < 48) {
			
			
			paintMovingPlayerCharacter(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, x, y);
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			
			SwingUtilities.invokeLater(
					  new Runnable() {
						public void run() {
							worldPanel.repaintAround(x, y, worldObject);
						}
					  }
					);
			
			moveStep += 2;
			if (moveStep % 16 == 0) {
				moveIndex = (moveIndex + 1) % 3;
			}
		} else {
			
			//moveDirection = null;
			//moveStep = 0;
			if (guiMoveAction != null) {
				SwingUtilities.invokeLater(
						new Runnable() {
							  public void run() {
								  guiMoveAction.actionPerformed(null);
								  moveMode = false;
								  guiMoveAction = null;
								  moveIndex = 0;
							  }
						});
			}
			
			if (!moveMode) {
				worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0);
			} else {
				paintMovingPlayerCharacter(g, worldPanel, worldObject, imageInfoReader, id, lookDirection, x, y);
			}
		}
	}

	private void paintMovingPlayerCharacter(Graphics g, WorldPanel worldPanel,
			WorldObject worldObject, ImageInfoReader imageInfoReader,
			ImageIds id, LookDirection lookDirection, int x, int y) {
		Image image;
		int deltaX = moveDirection[0] * moveStep;
		int deltaY = moveDirection[1] * moveStep;
		
		image = imageInfoReader.getImage(id, lookDirection, moveIndex);
		worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, deltaX, deltaY);
	}
}
