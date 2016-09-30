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
package org.worldgrower.gui.start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;

import org.worldgrower.CommonerNameGenerator;
import org.worldgrower.CommonerNameGeneratorImpl;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldOnTurnImpl;
import org.worldgrower.actions.ArenaFightOnTurn;
import org.worldgrower.actions.BrawlListener;
import org.worldgrower.actions.DrinkingContestListener;
import org.worldgrower.condition.ConditionListener;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.curse.CurseListener;
import org.worldgrower.deity.DeityWorldOnTurn;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.WorldGenerator;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.goal.PerceptionPropertyUtils;
import org.worldgrower.gui.AdditionalManagedOperationListenerFactory;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.ProgressDialog;
import org.worldgrower.gui.util.ShowTextDialog;
import org.worldgrower.terrain.TerrainType;

/**
 * This class is responsible for building the world and creating the gui.
 */
public class Game {

	private static JFrame frame = null;
	
	public static void run(CharacterAttributes characterAttributes, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, ImageIds playerCharacterImageId, GameParameters gameParameters, KeyBindings keyBindings) throws Exception {
		int seed = gameParameters.getSeed();
		int startTurn = gameParameters.getStartTurn();
		DungeonMaster dungeonMaster = new DungeonMaster();
		WorldOnTurnImpl worldOnTurn = new WorldOnTurnImpl(new DeityWorldOnTurn(), new ArenaFightOnTurn());
		World world = new WorldImpl(gameParameters.getWorldWidth(), gameParameters.getWorldHeight(), dungeonMaster, worldOnTurn);
		int playerCharacterId = world.generateUniqueId();
		
		final CommonerImageIds commonerImageIds = new CommonerImageIds();
		final CommonerNameGenerator commonerNameGenerator = new CommonerNameGeneratorImpl();
		final WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		final CommonerGenerator commonerGenerator = new CommonerGenerator(seed, commonerImageIds, commonerNameGenerator);
		
		final WorldObject verminOrganization = GroupPropertyUtils.create(null, "vermin", world);
		final CreatureGenerator creatureGenerator = new CreatureGenerator(verminOrganization);
		
		gameParameters.addDefaultWorldObjects(world, commonerGenerator, creatureGenerator, organization, gameParameters.getVillagerCount(), seed);
		
		addWorldListeners(world);
		
		addEnemiesAndFriendlyAnimals(gameParameters.getEnemyDensity(), verminOrganization, creatureGenerator, world, seed);

		//runWorld(startTurn, dungeonMaster, world);
		RunWorldSwingWorker runWorldSwingWorker = new RunWorldSwingWorker(startTurn, dungeonMaster, world);
		runWorldSwingWorker.execute();
		runWorldSwingWorker.get();
		
		final WorldObject playerCharacter = addPlayerCharacter(characterAttributes, playerCharacterImageId, gameParameters, world, playerCharacterId, organization, commonerGenerator);
		exploreWorld(playerCharacter, world);
		
		createAndShowGUIInvokeLater(playerCharacter, world, dungeonMaster, imageInfoReader, soundIdReader, musicPlayer, gameParameters.getInitialStatusMessage(), gameParameters.getAdditionalManagedOperationListenerFactory(), keyBindings);
	}
	
	private static class RunWorldSwingWorker extends SwingWorker<Integer, Integer> {

		private final int startTurn;
		private final DungeonMaster dungeonMaster;
		private final World world;
		
		private final ProgressDialog progressDialog;
		
		public RunWorldSwingWorker(int startTurn, DungeonMaster dungeonMaster, World world) {
			super();
			this.startTurn = startTurn;
			this.dungeonMaster = dungeonMaster;
			this.world = world;
			
			this.progressDialog = new ProgressDialog("Processing turns...", startTurn);
			if (startTurn > 1000) {
				progressDialog.showMe();
			}
		}

		@Override
		protected Integer doInBackground() throws Exception {
			for(int i=0; i<startTurn; i++) {
				dungeonMaster.runWorld(world, new WorldStateChangedListeners());
				publish(i);
				final int value = i;
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						progressDialog.setValue(value);
					}
				});
			}
			progressDialog.close();
			
			return 0;
		}

		@Override
		protected void done() {
			progressDialog.close();
		}

		@Override
		protected void process(List<Integer> chunks) {
			progressDialog.setValue(chunks.get(0));
		}
		
		
	}

	private static WorldObject addPlayerCharacter(CharacterAttributes characterAttributes, ImageIds playerCharacterImageId,
			GameParameters gameParameters, World world, int playerCharacterId, final WorldObject organization,
			final CommonerGenerator commonerGenerator) {
		final WorldObject playerCharacter = CommonerGenerator.createPlayerCharacter(playerCharacterId, gameParameters.getPlayerName(), gameParameters.getPlayerProfession(), gameParameters.getGender(), world, commonerGenerator, organization, characterAttributes, playerCharacterImageId);
		world.addWorldObject(playerCharacter);
		
		movePlayerCharacterIfOnOccupiedSquare(world, playerCharacter);
		
		return playerCharacter;
	}

	static void movePlayerCharacterIfOnOccupiedSquare(World world, final WorldObject playerCharacter) {
		int x = playerCharacter.getProperty(Constants.X);
		int y = playerCharacter.getProperty(Constants.Y);
		List<WorldObject> worldObjects = LocationPropertyUtils.getWorldObjects(x, y, world);
		
		if (worldObjects.size() > 1) {
			for(int newCoordinate = x; newCoordinate<world.getWidth() && newCoordinate < world.getHeight(); newCoordinate++) {
				if (GoalUtils.isOpenSpace(newCoordinate, newCoordinate, 1, 1, world)) {
					LocationPropertyUtils.updateLocation(playerCharacter, newCoordinate, newCoordinate, world);
					break;
				}
			}
		}
	}

	private static void addWorldListeners(World world) {
		world.addListener(new CurseListener(world));
		world.addListener(new ConditionListener(world));
		world.addListener(new BrawlListener());
		world.addListener(new DrinkingContestListener());
	}

	private static void addEnemiesAndFriendlyAnimals(int enemyDensity, WorldObject verminOrganization, CreatureGenerator creatureGenerator, World world, int seed) {
		WorldGenerator worldGenerator = new WorldGenerator(seed);
		
		if (enemyDensity > 0) {
			PlantGenerator plantGenerator = new PlantGenerator(verminOrganization);
			worldGenerator.addWorldObjects(world, 1, 1, 5, TerrainType.GRASLAND, creatureGenerator::generateRat);
			worldGenerator.addWorldObjects(world, 1, 1, 5, TerrainType.GRASLAND, creatureGenerator::generateSlime);
			worldGenerator.addWorldObjects(world, 2, 2, 4, TerrainType.PLAINS, plantGenerator::generateDemonTree);
		}
		
		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 20, TerrainType.WATER, creatureGenerator::generateFish);
	}
	
	public static void load(File fileToLoad, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, KeyBindings keyBindings) {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = WorldImpl.load(fileToLoad);
		final WorldObject playerCharacter = world.findWorldObjectById(0);
		
		addWorldListeners(world);
		
		//TODO: load playBackgroundMusic flag from file
		createAndShowGUIInvokeLater(playerCharacter, world, dungeonMaster, imageInfoReader, soundIdReader, musicPlayer, StatusMessages.WELCOME, new NullAdditionalManagedOperationListenerFactory(), keyBindings);
	}

	private static void createAndShowGUIInvokeLater(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, String initialStatusMessage, AdditionalManagedOperationListenerFactory additionalManagedOperationListenerFactory, KeyBindings keyBindings) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
                try {
					createAndShowGUI(playerCharacter, world, dungeonMaster, imageInfoReader, soundIdReader, musicPlayer, initialStatusMessage, additionalManagedOperationListenerFactory, keyBindings);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
            }
        });
	}

	public static void closeMainPanel() {
    	if (frame != null) {
    		frame.dispose();
    	}
	}
	
    private static void createAndShowGUI(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, MusicPlayer musicPlayer, String initialStatusMessage, AdditionalManagedOperationListenerFactory additionalManagedOperationListenerFactory, KeyBindings keyBindings) throws IOException {
    	closeMainPanel();
        frame = new JFrame("WorldGrower");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        IconUtils.setIcon(frame);
        
        WorldPanel worldPanel = new WorldPanel(playerCharacter, world, dungeonMaster, imageInfoReader, soundIdReader, musicPlayer, initialStatusMessage, keyBindings, frame);
        worldPanel.setOpaque(true);
        frame.setContentPane(worldPanel);
        
        if (Boolean.getBoolean("DEBUG")) {
            frame.pack();
            SwingUtils.centerFrame(frame);
        } else {
        	FullScreenUtils.makeFullScreen(frame);
        	frame.pack();
        }
        frame.setVisible(true);
        
        ToolTipManager.sharedInstance().setDismissDelay(9999999);
        
        worldPanel.addGuiListeners(additionalManagedOperationListenerFactory, frame);
        
        musicPlayer.play();
    }

    public static void executeActionAndMoveIntelligentWorldObjects(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, DungeonMaster dungeonMaster, WorldObject target, WorldPanel worldPanel, SoundIdReader soundIdReader) {
    	if (canActionExecute(playerCharacter, action, args, world, target)) {
    		worldPanel.movePlayerCharacter(args, new ActionListener() {
    			
				@Override
				public void actionPerformed(ActionEvent arg0) {
					executeAction(playerCharacter, action, args, world, dungeonMaster, target, worldPanel, soundIdReader);
				}
			}, null);
    	}
	}
    
    public static void executeMultipleActionsAndMoveIntelligentWorldObjects(WorldObject playerCharacter, List<OperationInfo> tasks, World world, DungeonMaster dungeonMaster, WorldObject target, WorldPanel worldPanel, SoundIdReader soundIdReader) {
    	if (tasks.size() > 0) {
    		OperationInfo task = tasks.get(0);
    		ManagedOperation action = task.getManagedOperation();
    		int[] args = task.getArgs();
    		if (canActionExecute(playerCharacter, action, args, world, target)) {
        		ActionListener guiMoveAction = new ActionListener() {
        			
    				@Override
    				public void actionPerformed(ActionEvent arg0) {
    					executeAction(playerCharacter, action, args, world, dungeonMaster, target, worldPanel, soundIdReader);
    				}
    			};
    			
    			ActionListener guiAfterMoveAction = new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent arg0) {
    					List<OperationInfo> subTasks = tasks.subList(1, tasks.size());
    					executeMultipleActionsAndMoveIntelligentWorldObjects(playerCharacter, subTasks, world, dungeonMaster, playerCharacter, worldPanel, soundIdReader);
    				}
    			};
    			
				worldPanel.movePlayerCharacter(args, guiMoveAction, guiAfterMoveAction);
        	}
    	}
	}
    
    public static void executeAction(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, DungeonMaster dungeonMaster, WorldObject target, WorldPanel worldPanel, SoundIdReader soundIdReader) {
    	if (canActionExecute(playerCharacter, action, args, world, target)) {
    		dungeonMaster.executeAction(action, playerCharacter, target, args, world);
    		worldPanel.playSound(action);
    		runWorld(playerCharacter, world, dungeonMaster, worldPanel);
    		checkToSkipTurn(playerCharacter, world, dungeonMaster, worldPanel, soundIdReader);
    	}
	}
    
    public static void executeMultipleTurns(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, DungeonMaster dungeonMaster, WorldObject target, WorldPanel worldPanel, int turns, SoundIdReader soundIdReader) {
    	for(int i=0; i<turns; i++) {
			int hitPointsBeforeRest = playerCharacter.getProperty(Constants.HIT_POINTS);
			Game.executeAction(playerCharacter, action, args, world, dungeonMaster, target, worldPanel, soundIdReader);
			int hitPointsAfterRest = playerCharacter.getProperty(Constants.HIT_POINTS);
			
			if (hitPointsAfterRest < hitPointsBeforeRest) {
				worldPanel.setStatusMessage("Action '" + action.getDescription() + "' was interrupted by enemy attack");
				break;
			}
			
			if (!Goals.PROTECT_ONE_SELF_GOAL.isGoalMet(playerCharacter, world)) {
				worldPanel.setStatusMessage("Action '" + action.getDescription() + "' was interrupted due to nearby enemy");
				break;
			}
		}
	}

	private static void runWorld(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel worldPanel) {
		dungeonMaster.runWorld(world, worldPanel.getWorldStateChangedListeners());
		exploreWorld(playerCharacter, world);
		worldPanel.centerViewOnPlayerCharacter();
		worldPanel.repaint();
	}
    
    public static boolean canActionExecute(WorldObject playerCharacter, ManagedOperation action, int[] args, World world, WorldObject target) {
    	return new OperationInfo(playerCharacter, target, args, action).canExecute(playerCharacter, world);
    }

	private static void exploreWorld(WorldObject playerCharacter, World world) {
		int x = playerCharacter.getProperty(Constants.X);
		int y = playerCharacter.getProperty(Constants.Y);
		world.getTerrain().explore(x, y, PerceptionPropertyUtils.calculateRadius(playerCharacter, world));
	}
	
	private static void checkToSkipTurn(WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel worldPanel, SoundIdReader soundIdReader) {
		while (!playerCharacter.getProperty(Constants.CONDITIONS).canTakeAction()) {
			String text = "You can't take any actions. Your player character will skip its turn.";			
			new ShowTextDialog(text, soundIdReader, frame).showMe();
			runWorld(playerCharacter, world, dungeonMaster, worldPanel);
		}
	}
}
