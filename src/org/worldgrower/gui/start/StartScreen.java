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

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.worldgrower.Version;
import org.worldgrower.World;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ExceptionHandler;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.loadsave.LoadSaveDialog;
import org.worldgrower.gui.loadsave.LoadSaveMode;
import org.worldgrower.gui.loadsave.SaveGameHandler;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundException;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.music.SoundOutput;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.MenuFactory;

public class StartScreen implements SaveGameHandler {

	private static final String PLAY_MUSIC = "playMusic";
	private static final String PLAY_SOUNDS = "playSounds";
	private static final String SOUND_OUTPUT = "soundOutput";
	private StartScreenDialog frame;
	private JButton btnSaveGame;
	private World world;
	private final KeyBindings keyBindings;
	private final Preferences preferences = Preferences.userNodeForPackage(getClass());
	private JFrame parentFrame = null;
	
	private static SoundOutput soundOutput;
	
	private static ImageInfoReader imageInfoReader = null;
	private static SoundIdReader soundIdReader = null;
	private static MusicPlayer musicPlayer = null; 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ExceptionHandler.registerExceptionHandler();
		
		Preferences preferences = Preferences.userNodeForPackage(StartScreen.class);
		loadDefaultSoundOutput(preferences);
		loadImages();
		loadSounds(preferences);
		loadMusic(preferences);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartScreen window = new StartScreen(imageInfoReader, soundIdReader, musicPlayer);
					window.frame.setVisible(true);
				} catch (Exception e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}
	
	private static void loadImages() {
		try {
			imageInfoReader = new ImageInfoReader();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}
	
	private static void loadSounds(Preferences preferences) {
		try {
			soundIdReader = new SoundIdReader(soundOutput, preferences.getBoolean(PLAY_SOUNDS, true));
		} catch(SoundException e) {
			askUserForSoundOutput();
			try {
				soundIdReader = new SoundIdReader(soundOutput, preferences.getBoolean(PLAY_SOUNDS, true));
				saveSoundSettings(preferences);
			} catch (Exception e2) {
				disableSound(e2);
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	private static void disableSound(Exception e) {
		try {
			soundIdReader = new SoundIdReader(soundOutput, false);
			JOptionPane.showMessageDialog(null, "<html>Sound effects have been disabled due to problem loading sounds.<br>Detailed error message: " + e.getMessage() + "</html>");
		} catch (SoundException e2) {
			ExceptionHandler.handle(e2);
		}
	}

	private static void askUserForSoundOutput() {
		String message = "There was a problem loading sounds using mixer '" + soundOutput.getDescription() + "'. Please select another mixer:";
		String title = "Problem loading sounds";
		Object[] selectionValues = SoundOutput.getAllSoundOutputs().toArray(new SoundOutput[0]);
		Object initialSelectionValue = SoundOutput.getDefaultSoundOutput();
		SoundOutput selectedSoundOutput = (SoundOutput) JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelectionValue);
		if (selectedSoundOutput != null) {
			soundOutput = selectedSoundOutput;
		}
	}
	
	private static void loadDefaultSoundOutput(Preferences preferences) {
		String soundOutputDescription = preferences.get(SOUND_OUTPUT, null);
		if (soundOutputDescription != null) {
			SoundOutput soundOutputValue = SoundOutput.create(soundOutputDescription);
			if (soundOutputValue != null) {
				soundOutput = soundOutputValue;
			}
		}
		if (soundOutput == null) {
			soundOutput = SoundOutput.getDefaultSoundOutput();
		}
	}
	
	private static void saveSoundSettings(Preferences preferences) {
		preferences.put(SOUND_OUTPUT, soundOutput.getDescription());
	}
	
	private static void loadMusic(Preferences preferences) {
		try {
			musicPlayer = new MusicPlayer(soundOutput, preferences.getBoolean(PLAY_MUSIC, true));
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	public StartScreen(ImageInfoReader imageInfoReaderValue, SoundIdReader soundIdReaderValue, MusicPlayer musicPlayerValue) {
		initialize(null);
		imageInfoReader = imageInfoReaderValue;
		soundIdReader = soundIdReaderValue;
		musicPlayer = musicPlayerValue;
		this.keyBindings = createKeyBindings(preferences);
	}
	
	public StartScreen(ImageInfoReader imageInfoReaderValue, SoundIdReader soundIdReaderValue, MusicPlayer musicPlayerValue, KeyBindings keyBindings) {
		initialize(null);
		imageInfoReader = imageInfoReaderValue;
		soundIdReader = soundIdReaderValue;
		musicPlayer = musicPlayerValue;
		this.keyBindings = keyBindings;
	}
	
	public StartScreen(ImageInfoReader imageInfoReaderValue, SoundIdReader soundIdReaderValue, MusicPlayer musicPlayerValue, KeyBindings keyBindings, JFrame parentFrame) {
		initialize(parentFrame);
		imageInfoReader = imageInfoReaderValue;
		soundIdReader = soundIdReaderValue;
		musicPlayer = musicPlayerValue;
		this.keyBindings = keyBindings;
		this.parentFrame = parentFrame;
	}
	
	private static KeyBindings createKeyBindings(Preferences preferences) {
		char[] values = new char[GuiAction.values().length];
		for(int i=0; i<values.length; i++) {
			values[i] = GuiAction.values()[i].getDefaultValue();
		}
		KeyBindings keyBindings = new KeyBindings(Arrays.asList(GuiAction.values()), values);
		keyBindings.loadSettings(preferences);
		return keyBindings;
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	private void showNewGamePopupMenu() {
		JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
		
		popupMenu.add(createMenuItem(new TutorialAction(), ImageIds.CUDGEL, "Start a tutorial game in which the basics of the game are explained"));
		popupMenu.add(createMenuItem(new StandardGameAction(), ImageIds.LARGE_CUDGEL, "Start a game with default settings"));
		popupMenu.add(createMenuItem(new CustomGameAction(), ImageIds.GOLDEN_AXE, "Start a game with customizing settings beforehand"));
		
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouseLocation, frame);
		popupMenu.show(frame, mouseLocation.x, mouseLocation.y);
	}
	
	private JMenuItem createMenuItem(Action action, ImageIds imageId, String toolTipText) {
		JMenuItem menuItem = MenuFactory.createJMenuItem(action, soundIdReader);
		menuItem.setIcon(new ImageIcon(imageInfoReader.getImage(imageId, null)));
		menuItem.setToolTipText(toolTipText);
		return menuItem;
	}
	
	private class TutorialAction extends AbstractAction {

		public TutorialAction() {
			super("Tutorial");
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			frame.setVisible(false);
			new Thread() {
				public void run() {
					try {
						Game.run(new CharacterAttributes(10, 10, 10, 10, 10, 10), imageInfoReader, soundIdReader, musicPlayer, ImageIds.KNIGHT, new TutorialGameParameters(), keyBindings);
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				}
			}.start();
		}
	}
	
	private class StandardGameAction extends AbstractAction {

		public StandardGameAction() {
			super("Standard Game");
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			frame.setVisible(false);
			new Thread() {
				public void run() {
					try {
						Game.run(new CharacterAttributes(12, 12, 12, 12, 12, 12), imageInfoReader, soundIdReader, musicPlayer, ImageIds.KNIGHT, new CustomGameParameters(), keyBindings);
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				}
			}.start();
		}
	}
	
	private class CustomGameAction extends AbstractAction {

		public CustomGameAction() {
			super("Custom Game");
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			frame.setVisible(false);
			try {
				CharacterCustomizationScreen characterCustomizationScreen = new CharacterCustomizationScreen(imageInfoReader, soundIdReader, musicPlayer, keyBindings, parentFrame);
				characterCustomizationScreen.setVisible(true);
			} catch (Exception e1) {
				ExceptionHandler.handle(e1);
			}
		}
	}
	
	private void initialize(JFrame parentFrame) {
		frame = new StartScreenDialog();
		
		addNewButton();
		addLoadButton();
		addSaveButton();
		addControlsButton(preferences);
		addCreditsButton();
		addExitButton();
		
		addVersionLabel();
		
		if (parentFrame != null) {
			DialogUtils.createDialogBackPanel(frame, parentFrame.getContentPane());
		}
	}

	private void addNewButton() {
		JButton btnNewGame = JButtonFactory.createButton("New Game", IconUtils.getNewIcon(), soundIdReader);
		btnNewGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnNewGame.setToolTipText("Starts a new game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showNewGamePopupMenu();
			}
		});
		
		frame.getRootPane().setDefaultButton(btnNewGame);
		frame.addComponent(btnNewGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnNewGame, 80, 81, 167, 60);
	}

	private void addLoadButton() {
		JButton btnLoadGame = JButtonFactory.createButton("Load Game", IconUtils.getLoadIcon(), soundIdReader);
		btnLoadGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnLoadGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnLoadGame.setToolTipText("Loads a game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoadSaveDialog loadSaveDialog = new LoadSaveDialog(StartScreen.this, LoadSaveMode.LOAD, soundIdReader);
				loadSaveDialog.showMe();
			}
		});
		frame.addComponent(btnLoadGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnLoadGame, 78, 150, 167, 60);
	}

	private void addSaveButton() {
		btnSaveGame = JButtonFactory.createButton("Save Game", IconUtils.getSaveIcon(), soundIdReader);
		btnSaveGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnSaveGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnSaveGame.setToolTipText("Saves current game");
		btnSaveGame.setEnabled(false);
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoadSaveDialog loadSaveDialog = new LoadSaveDialog(StartScreen.this, LoadSaveMode.SAVE, soundIdReader);
				loadSaveDialog.showMe();				
			}
		});
		frame.addComponent(btnSaveGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnSaveGame, 78, 220, 167, 60);
	}
	
	private void addControlsButton(Preferences preferences) {
		JButton btnControlsGame = JButtonFactory.createButton("Controls", IconUtils.getControlsIcon(), soundIdReader);
		btnControlsGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnControlsGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnControlsGame.setToolTipText("View and change game controls");
		btnControlsGame.setEnabled(true);
		btnControlsGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControlsDialog controlsDialog = new ControlsDialog(keyBindings, soundIdReader, musicPlayer);
				controlsDialog.showMe();
				keyBindings.saveSettings(preferences);
				preferences.putBoolean(PLAY_SOUNDS, soundIdReader.isEnabled());
				preferences.putBoolean(PLAY_MUSIC, musicPlayer.isEnabled());
			}
		});
		frame.addComponent(btnControlsGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnControlsGame, 78, 290, 167, 60);
	}

	private void addCreditsButton() {
		JButton btnCredits = JButtonFactory.createButton("Credits", IconUtils.getImageIcon48(), soundIdReader);
		btnCredits.setHorizontalAlignment(SwingConstants.LEFT);
		btnCredits.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnCredits.setToolTipText("Credits");
		btnCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CreditsDialog creditsDialog = new CreditsDialog(soundIdReader);
					creditsDialog.showMe();
				} catch (Exception e1) {
					ExceptionHandler.handle(e1);
				}
			}
		});
		frame.addComponent(btnCredits);
		SwingUtils.setBoundsAndCenterHorizontally(btnCredits, 78, 360, 167, 60);
	}
	
	private void addExitButton() {
		JButton btnExit = JButtonFactory.createButton("Exit", IconUtils.getExitIcon(), soundIdReader);
		btnExit.setHorizontalAlignment(SwingConstants.LEFT);
		btnExit.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnExit.setToolTipText("Exits program");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		frame.addComponent(btnExit);
		SwingUtils.setBoundsAndCenterHorizontally(btnExit, 78, 430, 167, 60);
	}

	private void addVersionLabel() {
		JLabel lblVersion = JLabelFactory.createJLabel("Version " + Version.getVersion());
		lblVersion.setToolTipText("Current version");
		frame.addComponent(lblVersion);
		SwingUtils.setBoundsAndCenterHorizontally(lblVersion, 83, 520, 167, 21);
	}
	
	@Override
	public void loadGame(File selectedFile) {
		Game.load(selectedFile, imageInfoReader, soundIdReader, musicPlayer, keyBindings);
		setVisible(false);
	}
	
	@Override
	public void saveGame(File fileToSave) {
		if (world == null) {
			throw new IllegalStateException("world is null");
		}
		world.save(fileToSave);
	}

	public void enableSave(boolean enabled, World world) {
		this.btnSaveGame.setEnabled(enabled);
		this.world = world;
	}
	
	private static class StartScreenDialog extends AbstractDialog {

		public StartScreenDialog() {
			super(337, 590);
		}
	}
}
