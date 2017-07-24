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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.LookDirection;

public class AnimationPainter {
	private ActionListener guiAfterMoveAction;
	private boolean moveMode = false;
	private int moveStep = 0;
	private int moveIndex = 0;
	
	private List<WorldObject> worldObjects = new ArrayList<>();
	private List<WorldObject> deadWorldObjects = new ArrayList<>();
	private List<WorldObject> newWorldObjects = new ArrayList<>();
	private Map<Integer, Point> oldPositions = new HashMap<>();
	private Map<Integer, Point> newPositions = new HashMap<>();
	private List<WorldObject> magicCasters = new ArrayList<>();
	private List<MagicTarget> magicTargets = new ArrayList<>();
	
	public AnimationPainter(WorldPanel worldPanel) {
		initializeWorldObjects(worldPanel);
	}
	
	public void viewChanged(WorldPanel worldPanel) {
		initializeWorldObjects(worldPanel);
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
		
		initializeMovingWorldObjects(guiMoveAction, world, imageInfoReader, worldPanel);
	}

	private void initializeMovingWorldObjects(ActionListener guiMoveAction, World world, ImageInfoReader imageInfoReader, WorldPanel worldPanel) {
		int oldWorldObjectsSize = world.getWorldObjects().size();
		initializeWorldObjects(worldPanel);
		initializePositions(oldPositions, worldObjects);
		
		guiMoveAction.actionPerformed(null);
		
		initializePositions(newPositions, worldObjects);
		
		initializeMagicCastersAndTargets(world, imageInfoReader);
		initializeDeadWorldObjects();
		
		initializeNewWorldObjects(worldPanel, oldWorldObjectsSize);
	}
	
	private static void initializePositions(Map<Integer, Point> positions, List<WorldObject> worldObjects) {
		positions.clear();
		for(WorldObject worldObject : worldObjects) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			positions.put(worldObject.getProperty(Constants.ID), new Point(x, y));
		}
	}

	private void initializeMagicCastersAndTargets(World world, ImageInfoReader imageInfoReader) {
		magicCasters.clear();
		magicTargets.clear();
		for(WorldObject intelligentWorldObject : worldObjects) {
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
	
	private void initializeDeadWorldObjects() {
		deadWorldObjects.clear();
		for(WorldObject worldObject : worldObjects) {
			if (worldObject.getProperty(Constants.HIT_POINTS) == 0) {
				deadWorldObjects.add(worldObject);
			}
		}
	}
	
	private void initializeNewWorldObjects(WorldPanel worldPanel, int oldWorldObjectsSize) {
		newWorldObjects.clear();
		List<WorldObject> worldObjectsAfterAction = getWorldObjects(worldPanel);
		for(WorldObject worldObject : worldObjectsAfterAction) {
			int id = worldObject.getProperty(Constants.ID);
			if (!containsId(worldObjects, id) && id >= oldWorldObjectsSize) {
				newWorldObjects.add(worldObject);
			} else {
				worldObjects.add(worldObject);
			}
		}
	}
	
	private boolean containsId(List<WorldObject> worldObjects, int id) {
		for(WorldObject worldObject : worldObjects) {
			if (worldObject.getProperty(Constants.ID).intValue() == id) {
				return true;
			}
		}
		return false;
	}

	private void initializeWorldObjects(WorldPanel worldPanel) {
		worldObjects.clear();
		worldObjects.addAll(getWorldObjects(worldPanel));
	}

	private List<WorldObject> getWorldObjects(WorldPanel worldPanel) {
		return worldPanel.getWorldObjectsOnScreen(w -> isAlive(w));
	}

	private boolean isAlive(WorldObject w) {
		if (!w.hasProperty(Constants.HIT_POINTS)) {
			throw new IllegalStateException("WorldObject has no HIT_POINTS property: " + w);
		}
		return w.getProperty(Constants.HIT_POINTS) > 0;
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
		paintWorldObjects(worldObjects, g, worldPanel, imageInfoReader, world, drawAnimation);
		if (!drawAnimation) {
			paintWorldObjects(newWorldObjects, g, worldPanel, imageInfoReader, world, drawAnimation);
		}
		if (drawAnimation) {
			paintDeadWorldObjects(g, worldPanel, imageInfoReader, world);
			paintNewWorldObjects(g, worldPanel, imageInfoReader, world);
			for(WorldObject magicCaster : magicCasters) {
				paintMagicSpellForWorldObject(g, worldPanel, magicCaster, imageInfoReader, moveStep, moveIndex, world);
			}
			for(MagicTarget magicTarget : magicTargets) {
				paintMagicTargetForWorldObject(g, worldPanel, magicTarget, imageInfoReader, moveStep, moveIndex, world);
			}
		}
		if (drawAnimation) {
			moveStep += 2;
			worldPanel.repaintWorldView();
		} else {
			moveMode = false;
			moveIndex = 0;
			moveStep = 0;
			if (this.guiAfterMoveAction != null) {
				this.guiAfterMoveAction.actionPerformed(null);
			}
		}
	}
	
	public int getDeltaX(WorldObject playerCharacter) {
		return getDeltaPosition(playerCharacter, p -> p.x);
	}
	
	public int getDeltaY(WorldObject playerCharacter) {
		return getDeltaPosition(playerCharacter, p -> p.y);
	}
	
	private int getDeltaPosition(WorldObject playerCharacter, Function<Point, Integer> posFunction) {
		Integer playerCharacterId = playerCharacter.getProperty(Constants.ID);
		Point oldPosition = oldPositions.get(playerCharacterId);
		Point newPosition = newPositions.get(playerCharacterId);
		if (moveMode && oldPosition != null && newPosition != null) {
			return (posFunction.apply(newPosition) - posFunction.apply(oldPosition)) * moveStep;
		} else if (oldPosition != null && newPosition != null) {
			return (posFunction.apply(newPosition) - posFunction.apply(oldPosition)) * 48;
		} else {
			return 0;
		}
	}

	private void paintWorldObjects(List<WorldObject> worldObjects, Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world, boolean drawAnimation) {
		for(WorldObject worldObject : worldObjects) {
			if (isAlive(worldObject)) {
				ImageIds id = worldPanel.getImageId(worldObject);
				LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
				Image image = imageInfoReader.getImage(id, lookDirection);
				
				int x = worldObject.getProperty(Constants.X);
				int y = worldObject.getProperty(Constants.Y);
				int width = worldObject.getProperty(Constants.WIDTH);
				int height = worldObject.getProperty(Constants.HEIGHT);
				
				if (world.getTerrain().isExplored(x, y, width, height)) {
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
			int width = magicCaster.getProperty(Constants.WIDTH);
			int height = magicCaster.getProperty(Constants.HEIGHT);
			if (world.getTerrain().isExplored(x, y, width, height)) {
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
			int width = target.getProperty(Constants.WIDTH);
			int height = target.getProperty(Constants.HEIGHT);
			if (world.getTerrain().isExplored(x, y, width, height)) {
				worldPanel.drawWorldObjectInPixels(g, target, null, image, x, y, 0, 0, true);
			}
		}
	}
	
	private void paintDeadWorldObjects(Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world) {
		paintWorldObjectsTransparantly(deadWorldObjects, 1f - (0.02f * moveStep), g, worldPanel, imageInfoReader, world);
	}
	
	private void paintNewWorldObjects(Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world) {
		paintWorldObjectsTransparantly(newWorldObjects, 0.02f * moveStep, g, worldPanel, imageInfoReader, world);
	}
	
	private static void paintWorldObjectsTransparantly(List<WorldObject> worldObjects, float alpha, Graphics g, WorldPanel worldPanel, ImageInfoReader imageInfoReader, World world) {
		for(WorldObject worldObject : worldObjects) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			int width = worldObject.getProperty(Constants.WIDTH);
			int height = worldObject.getProperty(Constants.HEIGHT);
			
			if (world.getTerrain().isExplored(x, y, width, height)) {
				ImageIds imageId = worldPanel.getImageId(worldObject);
				LookDirection lookDirection = worldPanel.getLookDirection(worldObject);
				Image image = imageInfoReader.getImage(imageId, lookDirection);
				
				Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				worldPanel.drawWorldObjectInPixels(g, worldObject, lookDirection, image, x, y, 0, 0, false, composite);
				worldPanel.repaintAround(x, y, worldObject);				
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

	public List<AnimationDescription> getAnimatedWorldObjects() {
		return worldObjects.stream().map(w -> new AnimationDescription(w.getProperty(Constants.NAME), w.getProperty(Constants.X).toString(), w.getProperty(Constants.Y).toString())).collect(Collectors.toList());
	}
	
	public static class AnimationDescription {
		private final String name;
		private final String x;
		private final String y;

		public AnimationDescription(String name, String x, String y) {
			super();
			this.name = name;
			this.x = x;
			this.y = y;
		}

		public String getName() {
			return name;
		}

		public String getX() {
			return x;
		}

		public String getY() {
			return y;
		}
	}
}
