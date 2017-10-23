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

import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;

import org.worldgrower.Version;
import org.worldgrower.World;
import org.worldgrower.attribute.GhostImageIds;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ExceptionHandler;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.TiledImageComboPopup;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.font.Fonts;
import org.worldgrower.gui.loadsave.LoadSaveDialog;
import org.worldgrower.gui.loadsave.LoadSaveMode;
import org.worldgrower.gui.loadsave.SaveGameHandler;
import org.worldgrower.gui.music.MusicPlayer;
import org.worldgrower.gui.music.SoundException;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.music.SoundOutput;
import org.worldgrower.gui.util.CustomPopupFactory;
import org.worldgrower.gui.util.DialogUtils;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.MenuFactory;

public class StartScreen implements SaveGameHandler {

	private final int BUTTON_LEFT = 75;
	private final int BUTTON_WIDTH = 173;
	private final int BUTTON_HEIGHT = 60;
	private final int NEW_BUTTON_TOP = 81;
	
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
	private static GhostImageIds ghostImageIds;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setSwingSystemProperties();
		ExceptionHandler.registerExceptionHandler();
		
		UIManager.put("Tree.rendererFillBackground", false);
		UIManager.getDefaults().put("MenuItem.disabledForeground", ColorPalette.DISABLED_FOREGROUND_COLOR);
		UIManager.getDefaults().put("Button.disabledForeground", ColorPalette.DISABLED_FOREGROUND_COLOR);
		UIManager.getDefaults().put("Button.disabledText", ColorPalette.DISABLED_FOREGROUND_COLOR);
		UIManager.getDefaults().put("Label.disabledForeground", ColorPalette.DISABLED_FOREGROUND_COLOR);
		UIManager.getDefaults().put("CheckBox.disabledText", ColorPalette.DISABLED_FOREGROUND_COLOR);		
		UIManager.put("ToolTip.font", new FontUIResource(Fonts.FONT));
		UIManager.put("ToolTip.background", Color.BLACK);
		UIManager.put("ToolTip.foreground", Color.WHITE);
		UIManager.put("ToolTip.border", new LineBorder(Color.BLACK));
		
		Color menuSelectionBackground = new Color(255, 0, 0, 0);
		Color menuSelectionForeground = Color.BLACK;
		UIManager.put("MenuItem.selectionBackground", menuSelectionBackground);
		UIManager.put("MenuItem.selectionForeground", menuSelectionForeground);
		
		UIManager.put("Menu.selectionBackground", menuSelectionBackground);
		UIManager.put("Menu.selectionForeground", menuSelectionForeground);
		
		Color acceleratorForeground = Color.WHITE;
		Color acceleratorSelectionForeground = Color.BLACK;
		UIManager.put("MenuItem.acceleratorForeground", acceleratorForeground);
		UIManager.put("MenuItem.acceleratorSelectionForeground", acceleratorSelectionForeground);
		
		UIManager.put("Menu.acceleratorForeground", acceleratorForeground);
		UIManager.put("Menu.acceleratorSelectionForeground", acceleratorSelectionForeground);
		
		Preferences preferences = Preferences.userNodeForPackage(StartScreen.class);
		loadDefaultSoundOutput(preferences);
		ghostImageIds = loadImages();
		loadSounds(preferences);
		loadMusic(preferences);
		CustomPopupFactory.setPopupFactory();
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
	
	private static void setSwingSystemProperties() {
		System.setProperty("sun.java2d.d3d", "false");
		System.setProperty("sun.java2d.opengl", "true");
	}

	private static GhostImageIds loadImages() {
		GhostImageIds ghostImageIds = new GhostImageIds();
		try {
			imageInfoReader = new ImageInfoReader(ghostImageIds);
			TiledImageComboPopup.initializeImageInfoReader(imageInfoReader);
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		return ghostImageIds;
	}
	
	private static void loadSounds(Preferences preferences) {
		if (soundOutput.supportsSound()) {
			loadSoundsImpl(preferences);
		} else {
			disableSound();
		}
	}

	private static void loadSoundsImpl(Preferences preferences) {
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
		disableSound("<html>Sound effects have been disabled due to problem loading sounds.<br>Detailed error message: " + e.getMessage() + "</html>");
	}
	
	private static void disableSound() {
		disableSound("<html>Sound effects have been disabled because no audio device was found.</html>");
	}
	
	private static void disableSound(String message) {
		try {
			soundIdReader = new SoundIdReader(soundOutput, false);
			JOptionPane.showMessageDialog(null, message);
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
			boolean soundEnabled = preferences.getBoolean(PLAY_MUSIC, true);
			soundEnabled &= soundOutput.supportsSound();
			musicPlayer = new MusicPlayer(soundOutput, soundEnabled);
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
		JPopupMenu popupMenu = MenuFactory.createJPopupMenu(imageInfoReader);
		
		popupMenu.add(createMenuItem(new TutorialAction(), IconUtils.getNewTutorialGameIcon(), "Start a tutorial game in which the basics of the game are explained"));
		popupMenu.add(createMenuItem(new StandardGameAction(), IconUtils.getNewStandardGameIcon(), "Start a game with default settings"));
		popupMenu.add(createMenuItem(new CustomGameAction(), IconUtils.getNewCustomGameIcon(), "Start a game with customizing settings beforehand"));
		
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouseLocation, frame);
		popupMenu.show(frame, mouseLocation.x, mouseLocation.y);
	}
	
	private JMenuItem createMenuItem(Action action, ImageIcon imageIcon, String toolTipText) {
		JMenuItem menuItem = MenuFactory.createJMenuItem(action, soundIdReader);
		menuItem.setIcon(imageIcon);
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
						Game.run(new CharacterAttributes(10, 10, 10, 10, 10, 10), imageInfoReader, soundIdReader, musicPlayer, ImageIds.KNIGHT, new TutorialGameParameters(), keyBindings, ghostImageIds);
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
						Game.run(new CharacterAttributes(12, 12, 12, 12, 12, 12), imageInfoReader, soundIdReader, musicPlayer, ImageIds.KNIGHT, new CustomGameParameters(), keyBindings, ghostImageIds);
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
				CharacterCustomizationScreen characterCustomizationScreen = new CharacterCustomizationScreen(imageInfoReader, soundIdReader, musicPlayer, keyBindings, ghostImageIds, parentFrame);
				characterCustomizationScreen.setVisible(true);
			} catch (Exception e1) {
				ExceptionHandler.handle(e1);
			}
		}
	}
	
	private void initialize(JFrame parentFrame) {
		frame = new StartScreenDialog();
		frame.setCursor(Cursors.CURSOR);
		((JComponent)frame.getRootPane()).setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		
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
		JButton btnNewGame = JButtonFactory.createButton("New Game", IconUtils.getNewIcon(), imageInfoReader, soundIdReader);
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
		SwingUtils.setBoundsAndCenterHorizontally(btnNewGame, BUTTON_LEFT, NEW_BUTTON_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void addLoadButton() {
		JButton btnLoadGame = JButtonFactory.createButton("Load Game", IconUtils.getLoadIcon(), imageInfoReader, soundIdReader);
		btnLoadGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnLoadGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnLoadGame.setToolTipText("Loads a game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoadSaveDialog loadSaveDialog = new LoadSaveDialog(StartScreen.this, LoadSaveMode.LOAD, imageInfoReader, soundIdReader);
				loadSaveDialog.showMe();
			}
		});
		frame.addComponent(btnLoadGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnLoadGame, BUTTON_LEFT, 150, BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void addSaveButton() {
		btnSaveGame = JButtonFactory.createButton("Save Game", IconUtils.getSaveIcon(), imageInfoReader, soundIdReader);
		btnSaveGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnSaveGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnSaveGame.setToolTipText("Saves current game");
		btnSaveGame.setEnabled(false);
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoadSaveDialog loadSaveDialog = new LoadSaveDialog(StartScreen.this, LoadSaveMode.SAVE, imageInfoReader, soundIdReader);
				loadSaveDialog.showMe();				
			}
		});
		frame.addComponent(btnSaveGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnSaveGame, BUTTON_LEFT, 220, BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	private void addControlsButton(Preferences preferences) {
		JButton btnControlsGame = JButtonFactory.createButton("Controls", IconUtils.getControlsIcon(), imageInfoReader, soundIdReader);
		btnControlsGame.setHorizontalAlignment(SwingConstants.LEFT);
		btnControlsGame.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnControlsGame.setToolTipText("View and change game controls");
		btnControlsGame.setEnabled(true);
		btnControlsGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControlsDialog controlsDialog = new ControlsDialog(keyBindings, imageInfoReader, soundIdReader, musicPlayer);
				controlsDialog.showMe();
				keyBindings.saveSettings(preferences);
				preferences.putBoolean(PLAY_SOUNDS, soundIdReader.isEnabled());
				preferences.putBoolean(PLAY_MUSIC, musicPlayer.isEnabled());
			}
		});
		frame.addComponent(btnControlsGame);
		SwingUtils.setBoundsAndCenterHorizontally(btnControlsGame, BUTTON_LEFT, 290, BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void addCreditsButton() {
		JButton btnCredits = JButtonFactory.createButton("Credits", IconUtils.getCreditsIcon(), imageInfoReader, soundIdReader);
		btnCredits.setHorizontalAlignment(SwingConstants.LEFT);
		btnCredits.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnCredits.setToolTipText("Credits");
		btnCredits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CreditsDialog creditsDialog = new CreditsDialog(imageInfoReader, soundIdReader);
					creditsDialog.showMe();
				} catch (Exception e1) {
					ExceptionHandler.handle(e1);
				}
			}
		});
		frame.addComponent(btnCredits);
		SwingUtils.setBoundsAndCenterHorizontally(btnCredits, BUTTON_LEFT, 360, BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	private void addExitButton() {
		JButton btnExit = JButtonFactory.createButton("Exit", IconUtils.getExitIcon(), imageInfoReader, soundIdReader);
		btnExit.setHorizontalAlignment(SwingConstants.LEFT);
		btnExit.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnExit.setToolTipText("Exits program");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		frame.addComponent(btnExit);
		SwingUtils.setBoundsAndCenterHorizontally(btnExit, BUTTON_LEFT, 430, BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	private void addVersionLabel() {
		JLabel lblVersion = JLabelFactory.createJLabel("Version " + Version.getVersion());
		lblVersion.setToolTipText("Current version");
		frame.addComponent(lblVersion);
		SwingUtils.setBoundsAndCenterHorizontally(lblVersion, BUTTON_LEFT, 520, BUTTON_WIDTH, 21);
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
			super(337, 590, imageInfoReader);
		}
	}
}
