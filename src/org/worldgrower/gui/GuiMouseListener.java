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

import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.gui.chooseworldobject.ChooseWorldObjectAction;
import org.worldgrower.gui.chooseworldobject.GuiDisguiseAction;
import org.worldgrower.gui.chooseworldobject.GuiVoteAction;
import org.worldgrower.gui.conversation.GuiAskQuestionAction;
import org.worldgrower.gui.debug.GuiShowCommonersOverviewAction;
import org.worldgrower.gui.debug.GuiShowEconomicOverviewAction;
import org.worldgrower.gui.debug.GuiShowPersonalitiesOverviewAction;
import org.worldgrower.gui.debug.GuiShowPropertiesAction;
import org.worldgrower.gui.debug.GuiShowReasonsOverviewAction;
import org.worldgrower.gui.debug.GuiShowSkillOverviewAction;
import org.worldgrower.gui.debug.ShowPerformedActionsAction;
import org.worldgrower.gui.inventory.GuiBuyAction;
import org.worldgrower.gui.inventory.GuiGetItemAction;
import org.worldgrower.gui.inventory.GuiPutItemAction;
import org.worldgrower.gui.inventory.GuiSellAction;
import org.worldgrower.gui.inventory.GuiStealAction;
import org.worldgrower.gui.inventory.InventoryAction;
import org.worldgrower.gui.knowledge.GuiCreateNewsPaperAction;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.start.GuiAction;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.MenuFactory;
import org.worldgrower.gui.util.ShowTextDialog;

public class GuiMouseListener extends MouseAdapter {
	private WorldPanel container;
	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private ImageInfoReader imageInfoReader;
	private KeyBindings keyBindings;
	
	private final CharacterSheetAction characterSheetAction;
	private final InventoryAction inventoryAction;
	private final MagicOverviewAction magicOverviewAction;
	private final RestAction restAction;
	private final GuiShowOrganizationsAction createOrganizationAction;
	private final GuiAssignActionToLeftMouseAction assignActionToLeftMouseAction;
	private final ShowStatusMessagesAction showStatusMessagesAction;
	private ManagedOperation leftMouseClickAction;
	private final ShowCharacterActionsAction showCharacterActionsAction;
	private final CommunityOverviewAction communityOverviewAction;
	
    public GuiMouseListener(WorldPanel container, WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, ImageInfoReader imageInfoReader, KeyBindings keyBindings) {
		super();
		this.container = container;
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.imageInfoReader = imageInfoReader;
		this.keyBindings = keyBindings;
		
		characterSheetAction = new CharacterSheetAction(playerCharacter, imageInfoReader);
		inventoryAction = new InventoryAction(playerCharacter, imageInfoReader, world, dungeonMaster, container);
		magicOverviewAction = new MagicOverviewAction(playerCharacter, imageInfoReader);
		restAction = new RestAction(playerCharacter, imageInfoReader, world, (WorldPanel)container, dungeonMaster);
		createOrganizationAction = new GuiShowOrganizationsAction(playerCharacter, world, container);
		showStatusMessagesAction = new ShowStatusMessagesAction(container);
		assignActionToLeftMouseAction = getGuiAssignActionToLeftMouseAction();
		showCharacterActionsAction = new ShowCharacterActionsAction();
		communityOverviewAction = new CommunityOverviewAction(playerCharacter, imageInfoReader, world);
		addKeyBindings(keyBindings);
	}

    public void initializeKeyBindings() {
    	addKeyBindings(keyBindings);
    }
    
	private void addKeyBindings(KeyBindings keyBindings) {
		addKeyBindingsFor(characterSheetAction, keyBindings.getValue(GuiAction.SHOW_CHARACTER_SHEET));
		addKeyBindingsFor(inventoryAction, keyBindings.getValue(GuiAction.SHOW_INVENTORY));
		addKeyBindingsFor(magicOverviewAction, keyBindings.getValue(GuiAction.SHOW_MAGIC_OVERVIEW));
		addKeyBindingsFor(restAction, keyBindings.getValue(GuiAction.REST_ACTION));
		addKeyBindingsFor(createOrganizationAction, keyBindings.getValue(GuiAction.CREATE_ORGANIZATION_ACTION));
		addKeyBindingsFor(showStatusMessagesAction, keyBindings.getValue(GuiAction.SHOW_STATUS_MESSAGES));
		addKeyBindingsFor(assignActionToLeftMouseAction, keyBindings.getValue(GuiAction.ASSIGN_ACTION_TO_LEFT_MOUSE));
		addKeyBindingsFor(showCharacterActionsAction, keyBindings.getValue(GuiAction.SHOW_CHARACTER_ACTIONS));
		addKeyBindingsFor(communityOverviewAction, keyBindings.getValue(GuiAction.COMMUNITY_OVERVIEW));
		
	}
	
	private void addKeyBindingsFor(Action action, char binding) {
		String bindingValue = Character.toString(binding);
		container.getInputMap().put(KeyStroke.getKeyStroke(bindingValue), action.getClass().getSimpleName());
		container.getActionMap().put(action.getClass().getSimpleName(), action);
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(bindingValue));
	}

	@Override
	public void mousePressed(MouseEvent e){
		mouseAction(e);
    }
 
	private boolean isDefaultLeftMouseButton(MouseEvent e) {
		if (keyBindings.leftMouseClickCentersMap()) {
			return SwingUtilities.isLeftMouseButton(e);
		} else {
			return SwingUtilities.isRightMouseButton(e);
		}
	}
	
	private boolean isDefaultRightMouseButton(MouseEvent e) {
		if (keyBindings.leftMouseClickCentersMap()) {
			return SwingUtilities.isRightMouseButton(e);
		} else {
			return SwingUtilities.isLeftMouseButton(e);
		}
	}
	
    private void mouseAction(MouseEvent e) {
        int x = (int) e.getPoint().getX() / 48;
        int y = (int) e.getPoint().getY() / 48;

		WorldObject worldObject = ((WorldPanel)container).findWorldObject(x, y);
		
    	if (((WorldPanel)container).inBuildMode()) {
			if (isDefaultRightMouseButton(e)) {
				((WorldPanel)container).endBuildMode(false);
			} else {
				((WorldPanel)container).endBuildMode(true);
		    }
		} else {
	        if (SwingUtilities.isLeftMouseButton(e) && isCtrlPressed(e) && worldObject != null) {
	        	performTalkAction(worldObject);
	        } else if (SwingUtilities.isLeftMouseButton(e) && leftMouseClickAction != null && worldObject != null) {
	        	performLeftMouseAction(worldObject);
	        } else if (isDefaultRightMouseButton(e)) {
		            doPop(e);
	        } else {
	        	centerOnScreen(e);
	        }
		}
    }

	private boolean isCtrlPressed(MouseEvent evt) {
		return (evt.getModifiers() & InputEvent.CTRL_MASK) != 0;
	}

    private void performLeftMouseAction(WorldObject worldObject) {
    	if (Game.canActionExecute(playerCharacter, leftMouseClickAction, Args.EMPTY, world, worldObject)) {
    		Game.executeAction(playerCharacter, leftMouseClickAction, Args.EMPTY, world, dungeonMaster, worldObject, container);
    	} else {
    		new ShowTextDialog("Cannot execute action '" + leftMouseClickAction.getSimpleDescription() + "' on " + worldObject.getProperty(Constants.NAME)).showMe();
    	}
	}
    
    private void performTalkAction(WorldObject worldObject) {
    	if (canPlayerCharacterPerformTalkAction(worldObject, Actions.TALK_ACTION)) {
    		GuiAskQuestionAction guiAskQuestionAction = new GuiAskQuestionAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader);
    		guiAskQuestionAction.actionPerformed(null);
		}
    }

	private void centerOnScreen(MouseEvent e) {
    	int x = (int) e.getPoint().getX() / 48;
        int y = (int) e.getPoint().getY() / 48;
        
        ((WorldPanel)container).centerOffsetsOn(x, y);
        container.repaint();
	}

	private void doPop(MouseEvent e){
        int x = (int) e.getPoint().getX() / 48;
        int y = (int) e.getPoint().getY() / 48;

		WorldObject worldObject = ((WorldPanel)container).findWorldObject(x, y);
		
        if (worldObject != null) {
            if (worldObject.getProperty(Constants.ID) == 0) {
            	showPlayerCharacterMenu(e.getX(), e.getY());
            } else {
            	JPopupMenu menu = MenuFactory.createJPopupMenu();
            	if (worldObject.hasIntelligence()) {
            		addCommunicationActions(menu, worldObject);
            	} else {
            		addInventoryActions(menu, worldObject);
            		addVoteActions(menu, worldObject);
            		addResearchActions(menu, worldObject);
            		addRestActions(menu, worldObject);
            	}
            	addPropertiesMenu(menu, worldObject);
            	addPerformedActionsMenu(menu, worldObject);
            	addAllActions(menu, worldObject);
            	
            	menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

	private void showPlayerCharacterMenu(int x, int y) {
		JPopupMenu menu = MenuFactory.createJPopupMenu();
		addPlayerCharacterInformationMenus(menu);
		
		JMenu organizationMenu = MenuFactory.createJMenu("Organization");
		JMenu miscMenu = MenuFactory.createJMenu("Miscellaneous");
		
		addDisguiseMenu(miscMenu);
		
		addPropertiesMenu(menu, playerCharacter);
		addBuildActions(menu);
		addCraftActions(menu);
		addBrewActions(menu);
		addPlantActions(menu);
		addIllusionActions(menu);
		addRestorationActions(menu);
		addTransmutationActions(menu);
		addEvocationActions(menu);
		addNecromancyActions(menu);
		addScribeMagicSpells(menu);
		addRestMenu(menu);
		menu.add(organizationMenu);
		addCreateOrganizationMenu(organizationMenu);
		addShowLegalActionsMenu(organizationMenu);
		addShowOrganizationsActionMenu(organizationMenu);
		addShowCommunityActionMenu(organizationMenu);
		addChooseDeityMenu(miscMenu);
		addCreateHumanMeatMenu(miscMenu);
		addInvestigateMenu(miscMenu);
		menu.add(miscMenu);
		addAssignActionsToLeftMouse(menu);
		
		menu.show(container, x, y);
	}

	private void addDisguiseMenu(JMenu menu) {
		JMenuItem disguiseMenuItem = MenuFactory.createJMenuItem(new GuiDisguiseAction(playerCharacter, imageInfoReader, world, (WorldPanel)container, dungeonMaster, Actions.DISGUISE_ACTION));
		disguiseMenuItem.setText("Disguise...");
		setMenuIcon(disguiseMenuItem, Actions.DISGUISE_ACTION.getImageIds());
		menu.add(disguiseMenuItem);
	}

	private void addChooseDeityMenu(JMenu menu) {
		JMenuItem chooseDeityMenuItem = MenuFactory.createJMenuItem(new ChooseDeityAction(playerCharacter, imageInfoReader, world, (WorldPanel)container, dungeonMaster));
		chooseDeityMenuItem.setText("Choose Deity...");
		setMenuIcon(chooseDeityMenuItem, Actions.CHOOSE_DEITY_ACTION.getImageIds());
		menu.add(chooseDeityMenuItem);
	}

	private void addCreateHumanMeatMenu(JMenu menu) {
		ManagedOperation[] actions = { Actions.CREATE_HUMAN_MEAT_ACTION };
		addActions(menu, actions);
	}
	
	private void addInvestigateMenu(JMenu menu) {
		ManagedOperation[] actions = { Actions.INVESTIGATE_ACTION };
		addActions(menu, actions);
	}
	
	private void addShowLegalActionsMenu(JMenu menu) {
		JMenuItem showLegalActionsMenuItem = MenuFactory.createJMenuItem(new GuiShowLegalActionsAction(playerCharacter, dungeonMaster, world, container));
		showLegalActionsMenuItem.setText("Show legal actions...");
		setMenuIcon(showLegalActionsMenuItem, Actions.SET_LEGAL_ACTIONS_ACTION.getImageIds());
		menu.add(showLegalActionsMenuItem);
	}

	private void addShowOrganizationsActionMenu(JMenu menu) {
		JMenuItem showOrganizationsMenuItem = MenuFactory.createJMenuItem(createOrganizationAction);
		showOrganizationsMenuItem.setText("Organization Membership Overview");
		setMenuIcon(showOrganizationsMenuItem, ImageIds.BLACK_CROSS);
		menu.add(showOrganizationsMenuItem);
	}
	
	private void addShowCommunityActionMenu(JMenu menu) {
		JMenuItem showCommunityMenuItem = MenuFactory.createJMenuItem(communityOverviewAction);
		showCommunityMenuItem.setText("Community Overview");
		setMenuIcon(showCommunityMenuItem, ImageIds.BLACK_CROSS);
		menu.add(showCommunityMenuItem);
	}
	
	private void addCreateOrganizationMenu(JMenu menu) {
		JMenuItem createOrganizationMenuItem = MenuFactory.createJMenuItem(new GuiCreateOrganizationAction(playerCharacter, imageInfoReader, world, (WorldPanel)container, dungeonMaster));
		createOrganizationMenuItem.setText("Create Organization...");
		setMenuIcon(createOrganizationMenuItem, Actions.CREATE_PROFESSION_ORGANIZATION_ACTION.getImageIds());
		menu.add(createOrganizationMenuItem);
	}

	private void addRestMenu(JPopupMenu menu) {
		JMenuItem restMenuItem = MenuFactory.createJMenuItem(restAction);
		setMenuIcon(restMenuItem, ImageIds.SLEEPING_INDICATOR);
		restMenuItem.setText("Rest...");
		menu.add(restMenuItem);
	}

	private void setMenuIcon(JMenuItem menuItem, ImageIds imageIds) {
		Image image = imageInfoReader.getImage(imageIds, null);
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		if (imageWidth > 96 || imageHeight > 96) {
			image = cropImage((BufferedImage)image, Math.min(imageWidth, 96), Math.min(imageHeight, 96));
		}
		menuItem.setIcon(new ImageIcon(image));
	}
	
	private BufferedImage cropImage(BufferedImage src, int width, int height) {
	      BufferedImage dest = src.getSubimage(0, 0, width, height);
	      return dest; 
	   }

	private void addCommunicationActions(JPopupMenu menu, WorldObject worldObject) {
		if (canPlayerCharacterPerformTalkAction(worldObject, Actions.TALK_ACTION)) {
			JMenuItem guiTalkMenuItem = MenuFactory.createJMenuItem(new GuiAskQuestionAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader));
			guiTalkMenuItem.setText("Talk...");
			menu.add(guiTalkMenuItem);
		}
		
		if (canPlayerCharacterPerformAction(worldObject, Actions.SELL_ACTION)) {
			JMenuItem guiSellMenuItem = MenuFactory.createJMenuItem(new GuiSellAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader));
			guiSellMenuItem.setText("Sell...");
			menu.add(guiSellMenuItem);
		}
		
		if (canPlayerCharacterPerformBuyAction(worldObject, Actions.BUY_ACTION)) {
			JMenuItem guiBuyMenuItem = MenuFactory.createJMenuItem(new GuiBuyAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader));
			guiBuyMenuItem.setText("Buy...");
			menu.add(guiBuyMenuItem);
		}
		
		if (canPlayerCharacterPerformAction(worldObject, Actions.STEAL_ACTION)) {
			JMenuItem guiStealMenuItem = MenuFactory.createJMenuItem(new GuiStealAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader));
			guiStealMenuItem.setText("Steal...");
			menu.add(guiStealMenuItem);
		}
	}
	
	private void addInventoryActions(JPopupMenu menu, WorldObject worldObject) {
		if (canPlayerCharacterPerformAction(worldObject, Actions.GET_ITEM_FROM_INVENTORY_ACTION)) {
			JMenuItem guiStealMenuItem = MenuFactory.createJMenuItem(new GuiGetItemAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader));
			guiStealMenuItem.setText("Get Item...");
			menu.add(guiStealMenuItem);
		}
		
		if (canPlayerCharacterPerformAction(worldObject, Actions.PUT_ITEM_INTO_INVENTORY_ACTION)) {
			JMenuItem guiStealMenuItem = MenuFactory.createJMenuItem(new GuiPutItemAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader));
			guiStealMenuItem.setText("Put Item...");
			menu.add(guiStealMenuItem);
		}
	}
	
	private void addVoteActions(JPopupMenu menu, WorldObject worldObject) {
		if (canPlayerCharacterPerformAction(worldObject, Actions.VOTE_FOR_LEADER_ACTION)) {
			JMenuItem guiVoteMenuItem = MenuFactory.createJMenuItem(new GuiVoteAction(playerCharacter, imageInfoReader, world, container, dungeonMaster, worldObject));
			guiVoteMenuItem.setText("Vote...");
			menu.add(guiVoteMenuItem);
		}
	}
	
	private void addResearchActions(JPopupMenu menu, WorldObject worldObject) {
		if (ResearchSpellAction.isValidTarget(worldObject) && Actions.getMagicSpellsToResearch(playerCharacter).size() > 0) {
			JMenuItem guiResearchMagicSpellMenuItem = MenuFactory.createJMenuItem(new GuiResearchMagicSpellAction(playerCharacter, imageInfoReader, world, container, dungeonMaster, worldObject));
			guiResearchMagicSpellMenuItem.setText("Research multiple turns...");
			menu.add(guiResearchMagicSpellMenuItem);
		}
	}
	
	private void addRestActions(JPopupMenu menu, WorldObject worldObject) {
		if (Game.canActionExecute(playerCharacter, Actions.SLEEP_ACTION, Args.EMPTY, world, worldObject)) {
			JMenuItem restMultipleTurnsMenuItem = MenuFactory.createJMenuItem(new GuiRestMultipleTurnsAction(playerCharacter, imageInfoReader, world, container, dungeonMaster, worldObject));
			restMultipleTurnsMenuItem.setText("Sleep multiple turns...");
			menu.add(restMultipleTurnsMenuItem);
		}
	}

	private void addPlayerCharacterInformationMenus(JPopupMenu menu) {
		JMenuItem characterSheetMenuItem = MenuFactory.createJMenuItem(characterSheetAction);
		characterSheetMenuItem.setText("Character Sheet");
		setMenuIcon(characterSheetMenuItem, ImageIds.WOODEN_SHIELD);
		menu.add(characterSheetMenuItem);
		
		JMenuItem inventoryMenuItem = MenuFactory.createJMenuItem(inventoryAction);
		setMenuIcon(inventoryMenuItem, ImageIds.CHEST);
		inventoryMenuItem.setText("Inventory");
		menu.add(inventoryMenuItem);
		
		JMenuItem magicOverviewMenuItem = MenuFactory.createJMenuItem(magicOverviewAction);
		setMenuIcon(magicOverviewMenuItem, ImageIds.MAGIC_ICON);
		magicOverviewMenuItem.setText("Magic Overview");
		menu.add(magicOverviewMenuItem);
	}

	private void addBuildActions(JPopupMenu menu) {
		BuildAction[] buildActions = { Actions.BUILD_SHACK_ACTION, Actions.BUILD_HOUSE_ACTION, Actions.BUILD_SHRINE_ACTION, Actions.BUILD_SMITH_ACTION, Actions.BUILD_WELL_ACTION, Actions.BUILD_PAPER_MILL_ACTION, Actions.BUILD_LIBRARY_ACTION, Actions.CREATE_GRAVE_ACTION, Actions.CONSTRUCT_TRAINING_DUMMY_ACTION, Actions.BUILD_JAIL_ACTION, Actions.BUILD_SACRIFICAL_ALTAR_ACTION, Actions.BUILD_ARENA_ACTION, Actions.BUILD_WEAVERY_ACTION };
		addBuildActions(menu, "Build", buildActions);
	}
	
	private void addPlantActions(JPopupMenu menu) {
		BuildAction[] buildActions = { Actions.PLANT_BERRY_BUSH_ACTION, Actions.PLANT_GRAPE_VINE_ACTION, Actions.PLANT_TREE_ACTION, Actions.PLANT_COTTON_PLANT_ACTION, Actions.PLANT_NIGHT_SHADE_ACTION };
		addBuildActions(menu, "Plant", buildActions);
	}
	
	private void addIllusionActions(JPopupMenu menu) {
		BuildAction[] buildActions = { Actions.MINOR_ILLUSION_ACTION };
		MagicSpell[] illusionActions = { Actions.INVISIBILITY_ACTION };
		JMenu illusionMenu = addBuildActions(menu, "Illusions", buildActions, buildAction -> new ChooseWorldObjectAction(playerCharacter, imageInfoReader, world, ((WorldPanel)container), dungeonMaster, new StartBuildModeAction(playerCharacter, imageInfoReader, ((WorldPanel)container), buildAction)));
		addActions(illusionMenu, illusionActions);
		
    	JMenuItem disguiseMenuItem = MenuFactory.createJMenuItem(new GuiDisguiseAction(playerCharacter, imageInfoReader, world, (WorldPanel)container, dungeonMaster, Actions.DISGUISE_MAGIC_SPELL_ACTION));
    	disguiseMenuItem.setText("Disguise self");
    	disguiseMenuItem.setEnabled(canPlayerCharacterPerformBuildAction(Actions.DISGUISE_MAGIC_SPELL_ACTION));
    	disguiseMenuItem.setToolTipText(Actions.DISGUISE_MAGIC_SPELL_ACTION.getRequirementsDescription());
    	setMenuIcon(disguiseMenuItem, Actions.DISGUISE_MAGIC_SPELL_ACTION.getImageIds());
    	illusionMenu.add(disguiseMenuItem);
	}
	
	private void addRestorationActions(JPopupMenu menu) {
		MagicSpell[] restorationActions = { Actions.MINOR_HEAL_ACTION, Actions.CURE_DISEASE_ACTION, Actions.CURE_POISON_ACTION, Actions.DISPEL_MAGIC_ACTION, Actions.SILENCE_MAGIC_ACTION };
		addActions(menu, "Restoration", restorationActions);
	}
	
	private void addTransmutationActions(JPopupMenu menu) {
		MagicSpell[] transmutationActions = { Actions.ENLARGE_ACTION, Actions.REDUCE_ACTION, Actions.SLEEP_MAGIC_SPELL_ACTION, Actions.WATER_WALK_ACTION, Actions.BURDEN_ACTION, Actions.FEATHER_ACTION };
		addActions(menu, "Transmute", transmutationActions);
	}
	
	private void addEvocationActions(JPopupMenu menu) {
		MagicSpell[] actions = { Actions.DETECT_MAGIC_ACTION, Actions.DETECT_POISON_AND_DISEASE_ACTION };
		JMenu evocationMenu = addActions(menu, "Evocation", actions);
		addBuildAction(evocationMenu, Actions.FIRE_TRAP_ACTION, startBuildMode());
		addBuildAction(evocationMenu, Actions.ENTANGLE_ACTION, startBuildMode());
	}
	
	private void addNecromancyActions(JPopupMenu menu) {
		MagicSpell[] actions = { Actions.LICH_TRANSFORMATION_ACTION };
		addActions(menu, "Necromancy", actions);
	}
	
	private void addScribeMagicSpells(JPopupMenu menu) {
		ManagedOperation[] scribeActions = Actions.getAllScribeMagicSpellActions().toArray(new ManagedOperation[0]);
		addActions(menu, "Scribe spells", scribeActions);
	}
	
	private void addBuildActions(JPopupMenu menu, String menuTitle, BuildAction[] buildActions) {
		addBuildActions(menu, menuTitle, buildActions, startBuildMode());
	}

	private Function<BuildAction, Action> startBuildMode() {
		return buildAction -> new StartBuildModeAction(playerCharacter, imageInfoReader, ((WorldPanel)container), buildAction);
	}
	
	private JMenu addBuildActions(JPopupMenu menu, String menuTitle, BuildAction[] buildActions, Function<BuildAction, Action> guiActionBuilder) {
		JMenu parentMenuItem = MenuFactory.createJMenu(menuTitle);
		menu.add(parentMenuItem);
		
		
		for(BuildAction buildAction : buildActions) {
			addBuildAction(parentMenuItem, buildAction, guiActionBuilder);
		}
		return parentMenuItem;
	}

	private void addBuildAction(JMenu parentMenuItem, BuildAction buildAction, Function<BuildAction, Action> guiActionBuilder) {
		final JMenuItem buildMenuItem;
		if (canPlayerCharacterPerformBuildAction(buildAction)) {
			buildMenuItem = MenuFactory.createJMenuItem(guiActionBuilder.apply(buildAction));
			buildMenuItem.setText(buildAction.getDescription(playerCharacter, null, null, world) + "...");
			parentMenuItem.add(buildMenuItem);
		} else {
			buildMenuItem = createDisabledActionMenuItem(parentMenuItem, buildAction);
		}
		buildMenuItem.setToolTipText(buildAction.getRequirementsDescription());
		addImageIcon(buildAction, buildMenuItem);
	}
	
	private void addCraftActions(JPopupMenu menu) {
		CraftAction[] craftActions = { Actions.CRAFT_LONG_BOW_ACTION, Actions.MINT_GOLD_ACTION, Actions.CREATE_PAPER_ACTION, Actions.CONSTRUCT_BED_ACTION, Actions.CONSTRUCT_FISHING_POLE_ACTION };
		JMenu parentMenu = addActions(menu, "Craft", craftActions);
		
		addNewsPaperAction(parentMenu);
	}
	
	private void addNewsPaperAction(JMenu menu) {
		JMenuItem guiCreateNewsPaperMenuItem = MenuFactory.createJMenuItem(new GuiCreateNewsPaperAction(playerCharacter, imageInfoReader, world, container, dungeonMaster));
		guiCreateNewsPaperMenuItem.setText("Create newspaper...");
		boolean enabled = (Game.canActionExecute(playerCharacter, Actions.CREATE_NEWS_PAPER_ACTION, Args.EMPTY, world, playerCharacter));
		guiCreateNewsPaperMenuItem.setEnabled(enabled);
		addToolTips(Actions.CREATE_NEWS_PAPER_ACTION, guiCreateNewsPaperMenuItem);
		addImageIcon(Actions.CREATE_NEWS_PAPER_ACTION, guiCreateNewsPaperMenuItem);
		menu.add(guiCreateNewsPaperMenuItem);
	}
	
	private void addBrewActions(JPopupMenu menu) {
		CraftAction[] brewActions = { Actions.BREW_WINE_ACTION, Actions.BREW_POISON_ACTION, Actions.BREW_SLEEPING_POTION_ACTION };
		addActions(menu, "Brew", brewActions);
	}
	
	private JMenu addActions(JPopupMenu menu, String menuTitle, ManagedOperation[] actions) {
		JMenu parentMenuItem = MenuFactory.createJMenu(menuTitle);
		menu.add(parentMenuItem);
		
		addActions(parentMenuItem, actions);
		return parentMenuItem;
	}

	private void addActions(JMenu parentMenuItem, ManagedOperation[] actions) {
		for(ManagedOperation action : actions) {
			final JMenuItem menuItem;
			if (canPlayerCharacterPerformBuildAction(action)) {
				PlayerCharacterAction guiAction = new PlayerCharacterAction(playerCharacter, world, container, dungeonMaster, action, playerCharacter);
				menuItem = MenuFactory.createJMenuItem(guiAction);
				menuItem.setText(action.getDescription(playerCharacter, playerCharacter, null, world) + "...");
				parentMenuItem.add(menuItem);
			} else {
				menuItem = createDisabledActionMenuItem(parentMenuItem, action);
			}
			addToolTips(action, menuItem);
			addImageIcon(action, menuItem);
		}
	}

	private void addImageIcon(ManagedOperation action, JMenuItem menuItem) {
		if (menuItem != null) {
			setMenuIcon(menuItem, action.getImageIds());
		}
	}

	private void addToolTips(ManagedOperation action, final JMenuItem menuItem) {
		if (menuItem != null) {
			menuItem.setToolTipText(action.getRequirementsDescription());
		}
	}

	private JMenuItem createDisabledActionMenuItem(JMenuItem menu, ManagedOperation craftAction) {
		JMenuItem menuItem = MenuFactory.createJMenuItem(craftAction.getDescription(playerCharacter, playerCharacter, null, world) + "...");
		menuItem.setEnabled(false);
		menu.add(menuItem);
		return menuItem;
	}

	private void addAllActions(JPopupMenu menu, WorldObject worldObject) {
		for(ManagedOperation action : playerCharacter.getOperations()) {
			if (!action.requiresArguments()) {
				final JMenuItem menuItem;
				if (canPlayerCharacterPerformAction(worldObject, action)) {
					PlayerCharacterAction guiAction = new PlayerCharacterAction(playerCharacter, world, container, dungeonMaster, action, worldObject);
					menuItem = MenuFactory.createJMenuItem(guiAction);
					menuItem.setText(action.getSimpleDescription());
					menu.add(menuItem);
				} else if (canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action)) {
					menuItem = MenuFactory.createJMenuItem(action.getSimpleDescription());
					menuItem.setEnabled(false);
					menu.add(menuItem);
				} else {
					menuItem = null;
				}
				addToolTips(action, menuItem);
				addImageIcon(action, menuItem);
			}
			
			if (action == Actions.OBFUSCATE_DEATH_REASON_ACTION) {
				final JMenuItem menuItem;
				if (canPlayerCharacterPerformAction(worldObject, action)) {
					ChooseDeathReasonAction guiAction = new ChooseDeathReasonAction(playerCharacter, imageInfoReader, world, container, dungeonMaster, worldObject);
					menuItem = MenuFactory.createJMenuItem(guiAction);
					menuItem.setText(action.getSimpleDescription());
					menu.add(menuItem);
				} else if (canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action)) {
					menuItem = MenuFactory.createJMenuItem(action.getSimpleDescription());
					menuItem.setEnabled(false);
					menu.add(menuItem);
				} else {
					menuItem = null;
				}
				addToolTips(action, menuItem);
				addImageIcon(action, menuItem);
			}
		}
	}
	
	private List<ManagedOperation> getActions() {
		List<ManagedOperation> actions = new ArrayList<>();
		for(ManagedOperation action : playerCharacter.getOperations()) {
			if (!action.requiresArguments() && !(action instanceof BuildAction) && !(Actions.getInventoryActions().contains(action))) {
				actions.add(action);
			}
		}
		Actions.sortActionsByDescription(actions);
		return actions;
	}
	
	private void addAssignActionsToLeftMouse(JPopupMenu menu) {
		JMenuItem guiAssignActionsToLeftMouseItem = MenuFactory.createJMenuItem(assignActionToLeftMouseAction);
		guiAssignActionsToLeftMouseItem.setText("Assign action to left mouse click...");
		guiAssignActionsToLeftMouseItem.setIcon(IconUtils.getMouseIcon());
		menu.add(guiAssignActionsToLeftMouseItem);
	}

	private GuiAssignActionToLeftMouseAction getGuiAssignActionToLeftMouseAction() {
		return new GuiAssignActionToLeftMouseAction(getActions(), container, this);
	}

	private void addPropertiesMenu(JPopupMenu menu, WorldObject worldObject) {
		if (Boolean.getBoolean("DEBUG")) {
			JMenuItem guiPropertiesItem = MenuFactory.createJMenuItem(new GuiShowPropertiesAction(worldObject));
			guiPropertiesItem.setText("Properties...");
			menu.add(guiPropertiesItem);
			
			JMenuItem guiShowCommonersOverviewItem = MenuFactory.createJMenuItem(new GuiShowCommonersOverviewAction(world));
			guiShowCommonersOverviewItem.setText("Show Commoners Overview...");
			menu.add(guiShowCommonersOverviewItem);
			
			JMenuItem guiShowEconomicOverviewItem = MenuFactory.createJMenuItem(new GuiShowEconomicOverviewAction(world));
			guiShowEconomicOverviewItem.setText("Show Economic Overview...");
			menu.add(guiShowEconomicOverviewItem);
			
			JMenuItem guiShowSkillOverviewItem = MenuFactory.createJMenuItem(new GuiShowSkillOverviewAction(world));
			guiShowSkillOverviewItem.setText("Show Skill Overview...");
			menu.add(guiShowSkillOverviewItem);
			
			JMenuItem guiShowPersonalityOverviewItem = MenuFactory.createJMenuItem(new GuiShowPersonalitiesOverviewAction(world));
			guiShowPersonalityOverviewItem.setText("Show Personality Overview...");
			menu.add(guiShowPersonalityOverviewItem);
			
			JMenuItem guiShowReasonsOverviewItem = MenuFactory.createJMenuItem(new GuiShowReasonsOverviewAction(world));
			guiShowReasonsOverviewItem.setText("Show Reasons Overview...");
			menu.add(guiShowReasonsOverviewItem);
		}
	}
	
	private void addPerformedActionsMenu(JPopupMenu menu, WorldObject worldObject) {
		if (Boolean.getBoolean("DEBUG")) {
			JMenuItem showPerformedActionsItem = MenuFactory.createJMenuItem(new ShowPerformedActionsAction(worldObject, world));
			showPerformedActionsItem.setText("Show performed actions...");
			menu.add(showPerformedActionsItem);
		}
	}

	private boolean canPlayerCharacterPerformAction(WorldObject worldObject, ManagedOperation action) {
		return canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action) && action.isActionPossible(playerCharacter, worldObject, Args.EMPTY, world);
	}
	
	private boolean canPlayerCharacterPerformBuyAction(WorldObject worldObject, ManagedOperation action) {
		return canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action) && Actions.BUY_ACTION.distanceInSquares(playerCharacter, worldObject, Args.EMPTY, world) == 0;
	}
	
	private boolean canPlayerCharacterPerformTalkAction(WorldObject worldObject, ManagedOperation action) {
		return canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action) && action.isActionPossible(playerCharacter, worldObject, Conversations.createArgs(Conversations.NAME_CONVERSATION), world);
	}
	
	private boolean canPlayerCharacterPerformActionUnderCorrectCircumstances(WorldObject worldObject, ManagedOperation action) {
		return action.isValidTarget(playerCharacter, worldObject, world) && playerCharacter.canWorldObjectPerformAction(action);
	}
	
	private boolean canPlayerCharacterPerformBuildAction(ManagedOperation action) {
		return action.isActionPossible(playerCharacter, playerCharacter, Args.EMPTY, world) && playerCharacter.canWorldObjectPerformAction(action);
	}

	public void executeBuildAction(ManagedOperation buildAction, WorldObject buildLocation, int[] args) {
		Game.executeAction(playerCharacter, buildAction, args, world, dungeonMaster, buildLocation, container);
	}

	public void setLeftMouseClickAction(ManagedOperation action) {
		leftMouseClickAction = action;
	}
	
	private class ShowCharacterActionsAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Point location = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(location, container);
			int x = location.x;
			int y = location.y;
			showPlayerCharacterMenu(x, y);
		}
	}
}