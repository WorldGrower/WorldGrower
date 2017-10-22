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

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftEquipmentAction;
import org.worldgrower.actions.CraftRangedWeaponAction;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.actions.magic.BestowCurseAction;
import org.worldgrower.actions.magic.IllusionSpell;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.actions.magic.ScribeMagicSpellAction;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.chooseworldobject.ChooseWorldObjectAction;
import org.worldgrower.gui.chooseworldobject.GuiDisguiseAction;
import org.worldgrower.gui.chooseworldobject.GuiViewCandidatesAction;
import org.worldgrower.gui.chooseworldobject.GuiVoteAction;
import org.worldgrower.gui.chooseworldobject.WorldObjectMapper;
import org.worldgrower.gui.conversation.ConversationFormatterImpl;
import org.worldgrower.gui.conversation.GuiAskQuestionAction;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.debug.GuiShowBrawlFinishedAction;
import org.worldgrower.gui.debug.GuiShowBuildingsOverviewAction;
import org.worldgrower.gui.debug.GuiShowCommonersOverviewAction;
import org.worldgrower.gui.debug.GuiShowDrinkingContestFinishedAction;
import org.worldgrower.gui.debug.GuiShowEconomicOverviewAction;
import org.worldgrower.gui.debug.GuiShowElectionResultsAction;
import org.worldgrower.gui.debug.GuiShowExplorationOverviewAction;
import org.worldgrower.gui.debug.GuiShowGoalDescriptionOverviewAction;
import org.worldgrower.gui.debug.GuiShowImagesOverviewAction;
import org.worldgrower.gui.debug.GuiShowPerformedActionsAction;
import org.worldgrower.gui.debug.GuiShowPersonalitiesOverviewAction;
import org.worldgrower.gui.debug.GuiShowPropertiesAction;
import org.worldgrower.gui.debug.GuiShowReasonsOverviewAction;
import org.worldgrower.gui.debug.GuiShowSkillOverviewAction;
import org.worldgrower.gui.debug.GuiShowThrownOutOfGroupEventsAction;
import org.worldgrower.gui.debug.ShowPerformedActionsAction;
import org.worldgrower.gui.inventory.GuiBarterAction;
import org.worldgrower.gui.inventory.ShowInventoryAction;
import org.worldgrower.gui.knowledge.GuiCreateNewsPaperAction;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.start.GuiAction;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.gui.util.IconUtils;
import org.worldgrower.gui.util.MenuFactory;
import org.worldgrower.gui.util.ShowTextDialog;
import org.worldgrower.text.FormattableText;

public class GuiMouseListener extends MouseAdapter {
	private final SkillImageIds skillImageIds = new SkillImageIds();
	private final ImageSubstituter imageSubstituter;
	private WorldPanel container;
	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private KeyBindings keyBindings;
	private JFrame parentFrame;
	
	private final CharacterSheetAction characterSheetAction;
	private final ShowInventoryAction inventoryAction;
	private final MagicOverviewAction magicOverviewAction;
	private final RestAction restAction;
	private final GuiAssignActionToLeftMouseAction assignActionToLeftMouseAction;
	private final ShowStatusMessagesAction showStatusMessagesAction;
	private ManagedOperation leftMouseClickAction;
	private final ShowCharacterActionsAction showCharacterActionsAction;
	private final CommunityOverviewAction communityOverviewAction;
	private final GuiShowGovernanceAction showGovernanceAction;
	private final GuiShowBuildingsAction showBuildingsAction;
	
    public GuiMouseListener(WorldPanel container, WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, KeyBindings keyBindings, JFrame parentFrame) {
		super();
		this.container = container;
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.keyBindings = keyBindings;
		this.parentFrame = parentFrame;
		this.imageSubstituter = new ImageSubstituter(imageInfoReader);
		
		characterSheetAction = new CharacterSheetAction(playerCharacter, imageInfoReader, soundIdReader, world, parentFrame);
		inventoryAction = new ShowInventoryAction(playerCharacter, imageInfoReader, soundIdReader, world, dungeonMaster, container, parentFrame);
		magicOverviewAction = new MagicOverviewAction(playerCharacter, imageInfoReader, soundIdReader, parentFrame);
		restAction = new RestAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, parentFrame);
		showStatusMessagesAction = new ShowStatusMessagesAction(container);
		assignActionToLeftMouseAction = getGuiAssignActionToLeftMouseAction();
		showCharacterActionsAction = new ShowCharacterActionsAction();
		communityOverviewAction = new CommunityOverviewAction(playerCharacter, imageInfoReader, soundIdReader, world, parentFrame);
		showGovernanceAction = new GuiShowGovernanceAction(playerCharacter, dungeonMaster, world, container, soundIdReader, parentFrame, imageInfoReader);
		showBuildingsAction = new GuiShowBuildingsAction();
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
		addKeyBindingsFor(showStatusMessagesAction, keyBindings.getValue(GuiAction.SHOW_STATUS_MESSAGES));
		addKeyBindingsFor(assignActionToLeftMouseAction, keyBindings.getValue(GuiAction.ASSIGN_ACTION_TO_LEFT_MOUSE));
		addKeyBindingsFor(showCharacterActionsAction, keyBindings.getValue(GuiAction.SHOW_CHARACTER_ACTIONS));
		addKeyBindingsFor(communityOverviewAction, keyBindings.getValue(GuiAction.COMMUNITY_OVERVIEW));
		addKeyBindingsFor(showGovernanceAction, keyBindings.getValue(GuiAction.SHOW_GOVERNANCE));
		addKeyBindingsFor(showBuildingsAction, keyBindings.getValue(GuiAction.SHOW_BUILDINGS));
	}
	
	private void addKeyBindingsFor(Action action, char binding) {
		String bindingValue = Character.toString(binding);
		JRootPane rootPane = parentFrame.getRootPane();
		rootPane.getInputMap().put(KeyStroke.getKeyStroke(bindingValue), action.getClass().getSimpleName());
		rootPane.getActionMap().put(action.getClass().getSimpleName(), action);
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
			((WorldPanel)container).endBuildMode(true);
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
    		Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, leftMouseClickAction, Args.EMPTY, world, dungeonMaster, worldObject, container, imageInfoReader, soundIdReader);
    	} else {
    		new ShowTextDialog("Cannot execute action '" + leftMouseClickAction.getSimpleDescription() + "' on " + worldObject.getProperty(Constants.NAME), imageInfoReader, soundIdReader, parentFrame).showMe();
    	}
	}
    
    private void performTalkAction(WorldObject worldObject) {
    	if (canPlayerCharacterPerformTalkAction(worldObject, Actions.TALK_ACTION)) {
    		GuiAskQuestionAction guiAskQuestionAction = new GuiAskQuestionAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader, soundIdReader, parentFrame);
    		guiAskQuestionAction.actionPerformed(null);
		}
    }

	private void centerOnScreen(MouseEvent e) {
    	int x = (int) e.getPoint().getX() / 48;
        int y = (int) e.getPoint().getY() / 48;
        
        ((WorldPanel)container).centerOffsetsOn(x, y);
        container.repaintWorldView();
	}

	private void doPop(MouseEvent e){
        int x = (int) e.getPoint().getX() / 48;
        int y = (int) e.getPoint().getY() / 48;

		WorldPanel worldPanel = (WorldPanel)container;
		WorldObject worldObject = worldPanel.findWorldObject(x, y);
		
        if (worldObject != null) {
            if (CommonerGenerator.isPlayerCharacter(worldObject)) {
            	showPlayerCharacterMenu(e.getX(), e.getY());
            } else {
            	JPopupMenu menu = MenuFactory.createJPopupMenu(imageInfoReader);
            	if (worldObject.hasIntelligence()) {
            		addCommunicationActions(menu, worldObject);
            	} else {
            		addVoteActions(menu, worldObject);
            		addResearchActions(menu, worldObject);
            		addRestActions(menu, worldObject);
            		addCookAction(menu, worldObject);
            		addScribeMagicSpells(menu, worldObject);
            	}
            	addBarterAction(menu, worldObject);
            	addAccessContainerAction(menu, worldObject);
            	addPropertiesMenu(menu, worldObject);
            	addPerformedActionsMenu(menu, worldObject);
            	addAllActions(menu, worldObject);
            	
            	menu.show(e.getComponent(), e.getX(), e.getY());
            }
        } else {
        	JPopupMenu menu = MenuFactory.createJPopupMenu(imageInfoReader);
        	addGotoMenu(menu, worldPanel.getRealX(x), worldPanel.getRealY(y));
        	menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

	private void addGotoMenu(JPopupMenu menu, int x, int y) {
		JMenuItem gotoMenuItem = MenuFactory.createJMenuItem(new GuiGotoAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, parentFrame, x, y), soundIdReader);
		gotoMenuItem.setText("Go to");
		setMenuIcon(gotoMenuItem, ImageIds.MOVING_CHARACTER);
		menu.add(gotoMenuItem);
		gotoMenuItem.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				clearPath();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				clearPath();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				container.setGotoPath(GuiGotoAction.calculatePath(playerCharacter, x, y, world));
				container.repaintWorldView();
			}
		});
		
		menu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				clearPath();
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}
		});
	}
	
	void clearPath() {
		container.setGotoPath(new ArrayList<>());
		container.repaintWorldView();
	}

	private void showPlayerCharacterMenu(int x, int y) {
		JPopupMenu menu = MenuFactory.createJPopupMenu(imageInfoReader);
		addPlayerCharacterInformationMenus(menu);
		
		JMenu organizationMenu = MenuFactory.createJMenu("Organization", ImageIds.BLACK_CROSS, imageInfoReader, soundIdReader);
		JMenu miscMenu = MenuFactory.createJMenu("Miscellaneous", ImageIds.INVESTIGATE, imageInfoReader, soundIdReader);
		
		addDisguiseMenu(miscMenu);
		
		addPropertiesMenu(menu, playerCharacter);
		createBuildActionsSubMenu(menu);
		addBuildProductionActions(menu);
		addPlantActions(menu);
		addIllusionActions(menu);
		addRestorationActions(menu);
		addTransmutationActions(menu);
		addEvocationActions(menu);
		addNecromancyActions(menu);
		addRestMenu(menu);
		menu.add(organizationMenu);
		addCreateOrganizationMenu(organizationMenu);
		addShowGovernanceMenu(organizationMenu);
		addShowCommunityActionMenu(organizationMenu);
		addChooseDeityMenu(miscMenu);
		addCreateHumanMeatMenu(miscMenu);
		addInvestigateMenu(miscMenu);
		addNewsPaperAction(miscMenu);
		menu.add(miscMenu);
		addAssignActionsToLeftMouse(menu);
		
		menu.show(container, x, y);
	}

	private void addDisguiseMenu(JMenu menu) {
		JMenuItem disguiseMenuItem = MenuFactory.createJMenuItem(new GuiDisguiseAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, Actions.DISGUISE_ACTION, parentFrame), soundIdReader);
		disguiseMenuItem.setText("Disguise...");
		setMenuIcon(disguiseMenuItem, Actions.DISGUISE_ACTION.getImageIds(playerCharacter));
		addToolTips(Actions.DISGUISE_ACTION, disguiseMenuItem);
		menu.add(disguiseMenuItem);
	}

	private void addChooseDeityMenu(JMenu menu) {
		JMenuItem chooseDeityMenuItem = MenuFactory.createJMenuItem(new ChooseDeityAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, parentFrame), soundIdReader);
		chooseDeityMenuItem.setText("Choose Deity...");
		setMenuIcon(chooseDeityMenuItem, Actions.CHOOSE_DEITY_ACTION.getImageIds(playerCharacter));
		addToolTips(Actions.CHOOSE_DEITY_ACTION, chooseDeityMenuItem);
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
	
	private void addShowGovernanceMenu(JMenu menu) {
		JMenuItem showGovernanceMenuItem = MenuFactory.createJMenuItem(showGovernanceAction, soundIdReader);
		showGovernanceMenuItem.setText("Show governance overview...");
		setMenuIcon(showGovernanceMenuItem, Actions.SET_GOVERNANCE_ACTION.getImageIds(playerCharacter));
		addToolTips(Actions.SET_GOVERNANCE_ACTION, showGovernanceMenuItem);
		menu.add(showGovernanceMenuItem);
	}

	private void addShowCommunityActionMenu(JMenu menu) {
		JMenuItem showCommunityMenuItem = MenuFactory.createJMenuItem(communityOverviewAction, soundIdReader);
		showCommunityMenuItem.setText("Community Overview");
		setMenuIcon(showCommunityMenuItem, ImageIds.BLACK_CROSS);
		showCommunityMenuItem.setToolTipText("show family members, acquaintances and organizations");
		menu.add(showCommunityMenuItem);
	}
	
	private void addCreateOrganizationMenu(JMenu menu) {
		JMenuItem createOrganizationMenuItem = MenuFactory.createJMenuItem(new GuiCreateOrganizationAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, parentFrame), soundIdReader);
		createOrganizationMenuItem.setText("Create Organization...");
		setMenuIcon(createOrganizationMenuItem, Actions.CREATE_PROFESSION_ORGANIZATION_ACTION.getImageIds(playerCharacter));
		createOrganizationMenuItem.setToolTipText("create an organization based on a deity or profession");
		menu.add(createOrganizationMenuItem);
	}

	private void addRestMenu(JPopupMenu menu) {
		JMenuItem restMenuItem = MenuFactory.createJMenuItem(restAction, soundIdReader);
		setMenuIcon(restMenuItem, ImageIds.SLEEPING_INDICATOR);
		restMenuItem.setText("Rest...");
		restMenuItem.setToolTipText(Actions.REST_ACTION.getDescription());
		menu.add(restMenuItem);
	}

	private void setMenuIcon(JMenuItem menuItem, ImageIds imageIds) {
		SwingUtils.setMenuIcon(menuItem, imageIds, imageInfoReader);
	}

	private void addCommunicationActions(JPopupMenu menu, WorldObject worldObject) {
		if (canPlayerCharacterPerformTalkAction(worldObject, Actions.TALK_ACTION)) {
			JMenuItem guiTalkMenuItem = MenuFactory.createJMenuItem(new GuiAskQuestionAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader, soundIdReader, parentFrame), soundIdReader);
			guiTalkMenuItem.setText("Talk...");
			setMenuIcon(guiTalkMenuItem, ImageIds.GOLD_AMULET);
			addToolTips(Actions.TALK_ACTION, guiTalkMenuItem);
			menu.add(guiTalkMenuItem);
		}
	}

	private void addBarterAction(JPopupMenu menu, WorldObject worldObject) {
		if (canTrade(worldObject) && !worldObject.hasProperty(Constants.ILLUSION_CREATOR_ID)) {
			JMenuItem guiBarterItem = MenuFactory.createJMenuItem(new GuiBarterAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader, soundIdReader, parentFrame), soundIdReader);
			guiBarterItem.setText("Barter...");
			setMenuIcon(guiBarterItem, Actions.SELL_ACTION.getImageIds(playerCharacter));
			guiBarterItem.setToolTipText("buy or sell items for gold");
			menu.add(guiBarterItem);
		}
	}

	private boolean canTrade(WorldObject worldObject) {
		return Actions.BUY_ACTION.isValidTarget(playerCharacter, worldObject, world) && Actions.BUY_ACTION.distance(playerCharacter, worldObject, Args.EMPTY, world) == 0;
	}
	
	private void addAccessContainerAction(JPopupMenu menu, WorldObject worldObject) {
		if (Actions.GET_ITEM_FROM_INVENTORY_ACTION.canExecute(playerCharacter, worldObject, Args.EMPTY, world) && !worldObject.hasProperty(Constants.ILLUSION_CREATOR_ID)) {
			JMenuItem guiBarterItem = MenuFactory.createJMenuItem(new GuiBarterAction(playerCharacter, world, dungeonMaster, container, worldObject, imageInfoReader, soundIdReader, parentFrame), soundIdReader);
			guiBarterItem.setText("Access container...");
			setMenuIcon(guiBarterItem, Actions.SELL_ACTION.getImageIds(playerCharacter));
			guiBarterItem.setToolTipText("store or retrieve items from a container");
			menu.add(guiBarterItem);
		}
	}
	
	private void addVoteActions(JPopupMenu menu, WorldObject worldObject) {
		if (canPlayerCharacterPerformAction(worldObject, Actions.VOTE_FOR_LEADER_ACTION)) {
			JMenuItem guiVoteMenuItem = MenuFactory.createJMenuItem(new GuiVoteAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, worldObject, parentFrame), soundIdReader);
			guiVoteMenuItem.setText("Vote...");
			setMenuIcon(guiVoteMenuItem, Actions.VOTE_FOR_LEADER_ACTION.getImageIds(playerCharacter));
			guiVoteMenuItem.setToolTipText(Actions.VOTE_FOR_LEADER_ACTION.getDescription());
			menu.add(guiVoteMenuItem);
		}
		if (VotingPropertyUtils.isVotingBox(worldObject)) {
			JMenuItem guiViewCandidatesMenuItem = MenuFactory.createJMenuItem(new GuiViewCandidatesAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, worldObject, parentFrame), soundIdReader);
			guiViewCandidatesMenuItem.setText("View Candidates...");
			setMenuIcon(guiViewCandidatesMenuItem, Actions.VOTE_FOR_LEADER_ACTION.getImageIds(playerCharacter));
			guiViewCandidatesMenuItem.setToolTipText("View list of candidates for the election");
			menu.add(guiViewCandidatesMenuItem);
		}
	}
	
	private void addResearchActions(JPopupMenu menu, WorldObject worldObject) {
		if (ResearchSpellAction.isValidTarget(worldObject) && Actions.getMagicSpellsToResearch(playerCharacter).size() > 0) {
			JMenuItem guiResearchMagicSpellMenuItem = MenuFactory.createJMenuItem(new GuiResearchMagicSpellAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, worldObject, parentFrame), soundIdReader);
			guiResearchMagicSpellMenuItem.setText("Research...");
			setMenuIcon(guiResearchMagicSpellMenuItem, ImageIds.SPELL_BOOK);
			menu.add(guiResearchMagicSpellMenuItem);
		}
	}
	
	private void addRestActions(JPopupMenu menu, WorldObject worldObject) {
		if (Game.canActionExecute(playerCharacter, Actions.SLEEP_ACTION, Args.EMPTY, world, worldObject)) {
			JMenuItem restMultipleTurnsMenuItem = MenuFactory.createJMenuItem(new GuiRestMultipleTurnsAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, worldObject, parentFrame), soundIdReader);
			restMultipleTurnsMenuItem.setText("Sleep multiple turns...");
			setMenuIcon(restMultipleTurnsMenuItem, ImageIds.SLEEPING_INDICATOR);
			restMultipleTurnsMenuItem.setToolTipText(Actions.SLEEP_ACTION.getDescription());
			menu.add(restMultipleTurnsMenuItem);
			
			GuiRestUntilRestedAction guiRestUntilRestedAction = new GuiRestUntilRestedAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, worldObject, parentFrame);
			JMenuItem restUntilRestedMenuItem = MenuFactory.createJMenuItem(guiRestUntilRestedAction, soundIdReader);
			restUntilRestedMenuItem.setText("Sleep " +  guiRestUntilRestedAction.getTurns() + " turns until energy is recovered");
			setMenuIcon(restUntilRestedMenuItem, ImageIds.SLEEPING_INDICATOR);
			restUntilRestedMenuItem.setEnabled(guiRestUntilRestedAction.getTurns() > 0);
			restUntilRestedMenuItem.setToolTipText(Actions.SLEEP_ACTION.getDescription());
			menu.add(restUntilRestedMenuItem);
		}
	}
	
	private void addCookAction(JPopupMenu menu, WorldObject worldObject) {
		if (Actions.COOK_ACTION.isValidTarget(playerCharacter, worldObject, world)) {
			DefaultActionContainingArgsAction defaultActionContainingArgsAction = new DefaultActionContainingArgsAction(playerCharacter, imageInfoReader, container, Actions.COOK_ACTION, world, dungeonMaster, worldObject, soundIdReader);
			List<WorldObject> worldObjects = playerCharacter.getProperty(Constants.INVENTORY).getWorldObjectsByFunction(Constants.FOOD, w -> true);
			JMenuItem cookMenuItem = MenuFactory.createJMenuItem(new ChooseWorldObjectAction(worldObjects, playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, defaultActionContainingArgsAction, parentFrame, WorldObjectMapper.INVENTORY_ID), soundIdReader);
			cookMenuItem.setText("Cook...");
			cookMenuItem.setEnabled(worldObjects.size() > 0);
			setMenuIcon(cookMenuItem, ImageIds.COOKING);
			cookMenuItem.setToolTipText(Actions.COOK_ACTION.getDescription());
			menu.add(cookMenuItem);
		}
	}

	private void addPlayerCharacterInformationMenus(JPopupMenu menu) {
		JMenuItem characterSheetMenuItem = MenuFactory.createJMenuItem(characterSheetAction, soundIdReader);
		characterSheetMenuItem.setText("Character Sheet");
		setMenuIcon(characterSheetMenuItem, ImageIds.WOODEN_SHIELD);
		characterSheetMenuItem.setToolTipText("character sheet shows character attributes, skills and equipment");
		menu.add(characterSheetMenuItem);
		
		JMenuItem inventoryMenuItem = MenuFactory.createJMenuItem(inventoryAction, soundIdReader);
		setMenuIcon(inventoryMenuItem, ImageIds.CHEST);
		inventoryMenuItem.setToolTipText("inventory screen shows contents of inventory");
		inventoryMenuItem.setText("Inventory");
		menu.add(inventoryMenuItem);
		
		JMenuItem magicOverviewMenuItem = MenuFactory.createJMenuItem(magicOverviewAction, soundIdReader);
		setMenuIcon(magicOverviewMenuItem, ImageIds.MAGIC_ICON);
		magicOverviewMenuItem.setToolTipText("magic overview screen shows magic spells information");
		magicOverviewMenuItem.setText("Magic Overview");
		menu.add(magicOverviewMenuItem);
	}

	private void createBuildActionsSubMenu(JPopupMenu menu) {
		createBuildActionsSubMenu(menu, ImageIds.HAMMER, "Build", getBuildActions()).setAccelerator((KeyStroke)showBuildingsAction.getValue(Action.ACCELERATOR_KEY));
	}

	private BuildActions getBuildActions() {
		return new BuildActions(startBuildMode(), Actions.BUILD_SHACK_ACTION, Actions.BUILD_HOUSE_ACTION, Actions.BUILD_SHRINE_ACTION, Actions.BUILD_WELL_ACTION, Actions.BUILD_LIBRARY_ACTION, Actions.CREATE_GRAVE_ACTION, Actions.CONSTRUCT_TRAINING_DUMMY_ACTION, Actions.BUILD_JAIL_ACTION, Actions.BUILD_SACRIFICAL_ALTAR_ACTION, Actions.BUILD_ARENA_ACTION, Actions.CONSTRUCT_CHEST_ACTION);
	}
	
	private void addBuildProductionActions(JPopupMenu menu) {
		BuildActions buildActions = new BuildActions(startBuildMode(), Actions.BUILD_SMITH_ACTION, Actions.BUILD_PAPER_MILL_ACTION, Actions.BUILD_WEAVERY_ACTION, Actions.BUILD_WORKBENCH_ACTION, Actions.BUILD_BREWERY_ACTION, Actions.BUILD_APOTHECARY_ACTION);
		createBuildActionsSubMenu(menu, ImageIds.HAMMER, "Build production buildings", buildActions);
	}
	
	private void addPlantActions(JPopupMenu menu) {
		BuildActions buildActions = new BuildActions(startBuildMode(), Actions.PLANT_BERRY_BUSH_ACTION, Actions.PLANT_GRAPE_VINE_ACTION, Actions.PLANT_TREE_ACTION, Actions.PLANT_COTTON_PLANT_ACTION, Actions.PLANT_NIGHT_SHADE_ACTION, Actions.PLANT_PALM_TREE_ACTION);
		createBuildActionsSubMenu(menu, ImageIds.BUSH, "Plant", buildActions);
	}
	
	private void addIllusionActions(JPopupMenu menu) {
		Function<BuildAction, Action> function = buildAction -> new ChooseWorldObjectAction(getIllusionWorldObjects((IllusionSpell) buildAction), playerCharacter, imageInfoReader, soundIdReader, world, ((WorldPanel)container), dungeonMaster, new StartBuildModeAction(playerCharacter, imageInfoReader, ((WorldPanel)container), buildAction), parentFrame, WorldObjectMapper.WORLD_OBJECT_ID);
		BuildActions buildActions = new BuildActions(function, Actions.MINOR_ILLUSION_ACTION, Actions.MAJOR_ILLUSION_ACTION);
		MagicSpell[] illusionActions = { Actions.INVISIBILITY_ACTION };
		
		JMenu illusionMenu = createBuildActionsSubMenu(menu, ImageIds.MINOR_ILLUSION_MAGIC_SPELL, "Illusions", buildActions);
		addActions(illusionMenu, illusionActions);
		
    	JMenuItem disguiseMenuItem = MenuFactory.createJMenuItem(new GuiDisguiseAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, Actions.DISGUISE_MAGIC_SPELL_ACTION, parentFrame), soundIdReader);
    	disguiseMenuItem.setText("Disguise self");
    	disguiseMenuItem.setEnabled(canPlayerCharacterPerformBuildAction(Actions.DISGUISE_MAGIC_SPELL_ACTION));
    	addToolTips(Actions.DISGUISE_MAGIC_SPELL_ACTION, disguiseMenuItem);
    	setMenuIcon(disguiseMenuItem, skillImageIds.getImageFor(Constants.ILLUSION_SKILL));
    	illusionMenu.add(disguiseMenuItem);
	}
	
	private List<WorldObject> getIllusionWorldObjects(IllusionSpell illusionSpell) {
		return illusionSpell.getIllusionSources(playerCharacter, world);
	}
	
	private void addRestorationActions(JPopupMenu menu) {
		MagicSpell[] restorationActions = { Actions.MINOR_HEAL_ACTION, Actions.CURE_DISEASE_ACTION, Actions.CURE_POISON_ACTION, Actions.PROTECTION_FROM_FIRE_ACTION, Actions.PROTECTION_FROM_ICE_ACTION, Actions.PROTECTION_FROM_LIGHTNING_ACTION };
		JMenu restorationMenu = addActions(menu, skillImageIds.getImageFor(Constants.RESTORATION_SKILL), "Restoration", restorationActions);
		addBuildAction(restorationMenu, Actions.TURN_UNDEAD_ACTION, startBuildMode());
	}
	
	private void addTransmutationActions(JPopupMenu menu) {
		MagicSpell[] transmutationActions = { Actions.ENLARGE_ACTION, Actions.REDUCE_ACTION, Actions.SLEEP_MAGIC_SPELL_ACTION, Actions.WATER_WALK_ACTION, Actions.BURDEN_ACTION, Actions.FEATHER_ACTION, Actions.DARK_VISION_SPELL_ACTION, Actions.FREEDOM_OF_MOVEMENT_ACTION };
		JMenu parentMenuItem = addActions(menu, skillImageIds.getImageFor(Constants.TRANSMUTATION_SKILL), "Transmute", transmutationActions);
	
		addBuildAction(parentMenuItem, Actions.PLANT_GROWTH_ACTION, startBuildMode());
	}
	
	private void addEvocationActions(JPopupMenu menu) {
		MagicSpell[] actions = { Actions.DETECT_MAGIC_ACTION, Actions.DETECT_POISON_AND_DISEASE_ACTION, Actions.DISPEL_MAGIC_ACTION, Actions.SILENCE_MAGIC_ACTION };
		JMenu evocationMenu = addActions(menu, skillImageIds.getImageFor(Constants.EVOCATION_SKILL), "Evocation", actions);
		addBuildAction(evocationMenu, Actions.FIRE_TRAP_ACTION, startBuildMode());
		addBuildAction(evocationMenu, Actions.ENTANGLE_ACTION, startBuildMode());
		addBuildAction(evocationMenu, Actions.DIMENSION_DOOR_ACTION, startBuildMode());
		addBuildAction(evocationMenu, Actions.FIRE_BALL_ATTACK_ACTION, startBuildMode());
	}
	
	private void addNecromancyActions(JPopupMenu menu) {
		MagicSpell[] actions = { Actions.LICH_TRANSFORMATION_ACTION };
		JMenu necromancyMenu = addActions(menu, skillImageIds.getImageFor(Constants.NECROMANCY_SKILL), "Necromancy", actions);
		
		JMenuItem bestowCurseMenuItem = createBestowCurseMenu(playerCharacter);
    	addToMenu(necromancyMenu, bestowCurseMenuItem);
	}

	private JMenuItem createBestowCurseMenu(WorldObject target) {
		JMenuItem bestowCurseMenuItem = MenuFactory.createJMenuItem(new ChooseCurseAction(playerCharacter, imageInfoReader, soundIdReader, world, (WorldPanel)container, dungeonMaster, parentFrame, target), soundIdReader);
		bestowCurseMenuItem.setText(Actions.BESTOW_CURSE_ACTION.getSimpleDescription());
		bestowCurseMenuItem.setEnabled(Game.canActionExecute(playerCharacter, Actions.BESTOW_CURSE_ACTION, Args.EMPTY, world, target));
    	addToolTips(Actions.BESTOW_CURSE_ACTION, bestowCurseMenuItem);
    	setMenuIcon(bestowCurseMenuItem, Actions.BESTOW_CURSE_ACTION.getImageIds(playerCharacter));
		return bestowCurseMenuItem;
	}
	
	private void addScribeMagicSpells(JPopupMenu menu, WorldObject worldObject) {
		if (Actions.getScribeMagicSpellActionFor(Actions.FIRE_BOLT_ATTACK_ACTION).isValidTarget(playerCharacter, worldObject, world)) {
			JMenu scribeMenu = MenuFactory.createJMenu("Scribe spells", ImageIds.SPELL_BOOK, imageInfoReader, soundIdReader);
			menu.add(scribeMenu);
			Map<SkillProperty, List<ManagedOperation>> scribeActionsMap = Actions.getScribeMagicSpellActions();
			List<SkillProperty> skillsList = Actions.getSortedSkillProperties(scribeActionsMap);
			for(SkillProperty skillProperty : skillsList) {
				JMenu skillMenuItem = createSkillMenu(skillProperty);
				scribeMenu.add(skillMenuItem);
				addActions(skillMenuItem, scribeActionsMap.get(skillProperty).toArray(new ManagedOperation[0]));
			}
		}
	}
	
	private Function<BuildAction, Action> startBuildMode() {
		return buildAction -> new StartBuildModeAction(playerCharacter, imageInfoReader, ((WorldPanel)container), buildAction);
	}
	
	private JMenu createBuildActionsSubMenu(JPopupMenu menu, ImageIds imageId, String menuTitle, BuildActions buildActions) {
		JMenu parentMenuItem = MenuFactory.createJMenu(menuTitle, imageId, imageInfoReader, soundIdReader);
		menu.add(parentMenuItem);
		
		buildActions.add(parentMenuItem);
		return parentMenuItem;
	}

	private class BuildActions {
		private final BuildAction[] buildActions;
		private final Function<BuildAction, Action> guiActionBuilder;

		public BuildActions(Function<BuildAction, Action> guiActionBuilder, BuildAction... buildActions) {
			this.buildActions = buildActions;
			this.guiActionBuilder = guiActionBuilder;
		}
		
		public void add(JComponent menuItem) {
			for(BuildAction buildAction : buildActions) {
				addBuildAction(menuItem, buildAction, guiActionBuilder);
			}
		}
	}

	private void addBuildAction(JComponent parentMenuItem, BuildAction buildAction, Function<BuildAction, Action> guiActionBuilder) {
		final JMenuItem buildMenuItem;
		if (canPlayerCharacterPerformBuildAction(buildAction)) {
			buildMenuItem = MenuFactory.createJMenuItem(guiActionBuilder.apply(buildAction), soundIdReader);
			buildMenuItem.setText(buildAction.getSimpleDescription() + "...");
			parentMenuItem.add(buildMenuItem);
		} else {
			buildMenuItem = createDisabledActionMenuItem(parentMenuItem, buildAction);
		}
		
		String tooltip = createTooltipForBuildAction(buildAction);
		buildMenuItem.setToolTipText(tooltip);
		addImageIcon(buildAction, buildMenuItem);
	}

	private String createTooltipForBuildAction(BuildAction buildAction) {
		String requirementsDescription = imageSubstituter.substituteImagesInHtml(buildAction.getRequirementsDescription());
		List<ManagedOperation> allowedCraftActions = buildAction.getAllowedCraftActions(playerCharacter, world);
		String allowedCraftActionsDescription = createAllowedCraftActionsDescription(allowedCraftActions);
		
		String tooltip = "<html>" + requirementsDescription;
		if (buildAction.getDescription().length() > 0) {
			if (requirementsDescription.length() > 0) {
				tooltip += "<br>";
			}
			FormattableText formattableDescription = buildAction.getFormattableDescription();
			if (formattableDescription != null) {
				tooltip += new ConversationFormatterImpl(imageSubstituter).format(formattableDescription);
			} else {
				tooltip += buildAction.getDescription();
			}
		}
		if (allowedCraftActions.size() > 0) {
			tooltip += "<br>" + allowedCraftActionsDescription;
		}
		tooltip += "</html>";
		return tooltip;
	}
	
	private String createAllowedCraftActionsDescription(List<ManagedOperation> allowedCraftActions) {
		StringBuilder allowedCraftActionsDescription = new StringBuilder("Allows actions:<br>");
		for(ManagedOperation allowedCraftAction : allowedCraftActions) {
			allowedCraftActionsDescription.append("&nbsp;&nbsp;").append(createMenuDescription(allowedCraftAction)).append("<br>");
		}
		return allowedCraftActionsDescription.toString();
	}
	
	private void addNewsPaperAction(JMenu menu) {
		JMenuItem guiCreateNewsPaperMenuItem = MenuFactory.createJMenuItem(new GuiCreateNewsPaperAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, parentFrame), soundIdReader);
		guiCreateNewsPaperMenuItem.setText("Create newspaper...");
		boolean enabled = (Game.canActionExecute(playerCharacter, Actions.CREATE_NEWS_PAPER_ACTION, Args.EMPTY, world, playerCharacter));
		guiCreateNewsPaperMenuItem.setEnabled(enabled);
		addToolTips(Actions.CREATE_NEWS_PAPER_ACTION, guiCreateNewsPaperMenuItem);
		addImageIcon(Actions.CREATE_NEWS_PAPER_ACTION, guiCreateNewsPaperMenuItem);
		addToMenu(menu, guiCreateNewsPaperMenuItem);
	}
	
	private void addToMenu(JComponent parentMenu, JMenuItem menuItem) {
		parentMenu.add(menuItem);
		
		//bugfix to make disabled components have the correct cursor:
		//https://bugs.openjdk.java.net/browse/JDK-4380700
		menuItem.getParent().setCursor(Cursors.CURSOR);
	}
	
	private JMenu addActions(JPopupMenu menu, ImageIds imageId, String menuTitle, ManagedOperation[] actions) {
		JMenu parentMenuItem = MenuFactory.createJMenu(menuTitle, imageId, imageInfoReader, soundIdReader);
		menu.add(parentMenuItem);
		
		addActions(parentMenuItem, actions);
		return parentMenuItem;
	}

	private void addActions(JMenu parentMenuItem, ManagedOperation[] actions) {
		for(ManagedOperation action : actions) {
			final JMenuItem menuItem;
			if (canPlayerCharacterPerformBuildAction(action)) {
				PlayerCharacterAction guiAction = new PlayerCharacterAction(playerCharacter, world, container, dungeonMaster, action, playerCharacter, imageInfoReader, soundIdReader);
				menuItem = MenuFactory.createJMenuItem(guiAction, soundIdReader);
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
			setMenuIcon(menuItem, action.getImageIds(playerCharacter));
		}
	}

	private void addToolTips(ManagedOperation action, final JMenuItem menuItem) {
		if (menuItem != null) {
			String tooltip = "<html>" + action.getRequirementsDescription() + "<br>" + action.getDescription();
			
			if (action instanceof CraftEquipmentAction) {
				CraftEquipmentAction craftEquipmentAction = (CraftEquipmentAction) action;
				Item craftedItem = craftEquipmentAction.getItem();
				WorldObject craftedWorldObject = craftedItem.generate(1f);
				if (craftedWorldObject.hasProperty(Constants.DAMAGE)) {
					tooltip += ("<br>damage: " + craftedWorldObject.getProperty(Constants.DAMAGE));
				}
				if (craftedWorldObject.hasProperty(Constants.ARMOR)) {
					tooltip += ("<br>armor: " + craftedWorldObject.getProperty(Constants.ARMOR));
				}
				if (craftedWorldObject.hasProperty(Constants.WEIGHT)) {
					tooltip += ("<br>weight: " + craftedWorldObject.getProperty(Constants.WEIGHT));
				}
			}
			if (action instanceof CraftRangedWeaponAction) {
				CraftRangedWeaponAction craftRangedEquipmentAction = (CraftRangedWeaponAction) action;
				Item craftedItem = craftRangedEquipmentAction.getItem();
				WorldObject craftedWorldObject = craftedItem.generate(1f);
				if (craftedWorldObject.hasProperty(Constants.DAMAGE)) {
					tooltip += ("<br>damage: " + craftedWorldObject.getProperty(Constants.DAMAGE));
				}
				if (craftedWorldObject.hasProperty(Constants.RANGE)) {
					tooltip += ("<br>range: " + craftedWorldObject.getProperty(Constants.RANGE));
				}
			}
			
			tooltip += "</html>";
			
			tooltip = imageSubstituter.substituteImagesInHtml(tooltip);
			menuItem.setToolTipText(tooltip);
		}
	}

	private JMenuItem createDisabledActionMenuItem(JComponent menu, ManagedOperation action) {
		String description = action.getSimpleDescription() + "...";
		JMenuItem menuItem = MenuFactory.createJMenuItem(description, soundIdReader);
		menuItem.setEnabled(false);
		addToolTips(action, menuItem);
		menu.add(menuItem);
		
		//bugfix to make disabled components have the correct cursor:
		//https://bugs.openjdk.java.net/browse/JDK-4380700
		menuItem.getParent().setCursor(Cursors.CURSOR);
		
		return menuItem;
	}

	private void addAllActions(JPopupMenu menu, WorldObject worldObject) {
		MagicSpellSubMenuStructure magicSpellSubMenuStructure = new MagicSpellSubMenuStructure(imageInfoReader, soundIdReader);
		EquipmentTypeSubMenuStructure equipmentTypeSubMenuStructure = new EquipmentTypeSubMenuStructure(imageInfoReader, soundIdReader);
		for(ManagedOperation action : playerCharacter.getOperations()) {
			boolean isValidAction = !action.requiresArguments() && (!(action instanceof ScribeMagicSpellAction)) && (!(action instanceof ResearchSpellAction));
			if (isValidAction || action instanceof BestowCurseAction) {
				JComponent parentMenu = menu;
				parentMenu = magicSpellSubMenuStructure.addAction(action, parentMenu);
				parentMenu = equipmentTypeSubMenuStructure.addAction(action, parentMenu);
				
				if (action instanceof BestowCurseAction) {
					addBestowCurseActionToMenu(worldObject, action, parentMenu);
				} else {
					addActionToMenu(worldObject, action, parentMenu);
				}
			}
			addObfuscateAction(menu, worldObject, action);
		}
		
		magicSpellSubMenuStructure.addSubMenus(menu);
		equipmentTypeSubMenuStructure.addSubMenus(menu);
	}

	private void addBestowCurseActionToMenu(WorldObject worldObject, ManagedOperation action, JComponent parentMenu) {
		if (canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action)) {
			JMenuItem bestowCurseMenuItem = createBestowCurseMenu(worldObject);
			addToMenu(parentMenu, bestowCurseMenuItem);
		}
	}
	
	private void addActionToMenu(WorldObject worldObject, ManagedOperation action, JComponent parentMenu) {
		final JMenuItem menuItem;
		if (canPlayerCharacterPerformAction(worldObject, action)) {
			menuItem = createEnabledMenuItem(worldObject, action);
			parentMenu.add(menuItem);
		} else if (canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action)) {
			menuItem = createDisabledMenuItem(action);
			addToMenu(parentMenu, menuItem);
		} else {
			menuItem = null;
		}
		addToolTips(action, menuItem);
		addImageIcon(action, menuItem);
	}

	private JMenu createSkillMenu(SkillProperty skillProperty) {
		String skillName = skillProperty.getName();
		skillName = Character.toUpperCase(skillName.charAt(0)) + skillName.substring(1);
		return MenuFactory.createJMenu(skillName, skillImageIds.getImageFor(skillProperty), imageInfoReader, soundIdReader);
	}

	private JMenuItem createDisabledMenuItem(ManagedOperation action) {
		final JMenuItem menuItem;
		menuItem = MenuFactory.createJMenuItem(action.getSimpleDescription(), soundIdReader);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createEnabledMenuItem(WorldObject worldObject, ManagedOperation action) {
		final JMenuItem menuItem;
		PlayerCharacterAction guiAction = new PlayerCharacterAction(playerCharacter, world, container, dungeonMaster, action, worldObject, imageInfoReader, soundIdReader);
		menuItem = MenuFactory.createJMenuItem(guiAction, soundIdReader);
		menuItem.setText(action.getSimpleDescription());
		return menuItem;
	}
	
	private String createMenuDescription(ManagedOperation action) {
		FormattableText formattableText = action.getFormattableSimpleDescription();
		if (formattableText != null) {
			return new ConversationFormatterImpl(imageSubstituter).format(formattableText);
		} else {
			return action.getSimpleDescription();
		}
	}

	private void addObfuscateAction(JPopupMenu menu, WorldObject worldObject, ManagedOperation action) {
		if (action == Actions.OBFUSCATE_DEATH_REASON_ACTION) {
			final JMenuItem menuItem;
			if (canPlayerCharacterPerformAction(worldObject, action)) {
				ChooseDeathReasonAction guiAction = new ChooseDeathReasonAction(playerCharacter, imageInfoReader, soundIdReader, world, container, dungeonMaster, worldObject, parentFrame);
				menuItem = MenuFactory.createJMenuItem(guiAction, soundIdReader);
				menuItem.setText(action.getSimpleDescription());
				menu.add(menuItem);
			} else if (canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action)) {
				menuItem = createDisabledMenuItem(action);
				menu.add(menuItem);
			} else {
				menuItem = null;
			}
			addToolTips(action, menuItem);
			addImageIcon(action, menuItem);
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
		JMenuItem guiAssignActionsToLeftMouseItem = MenuFactory.createJMenuItem(assignActionToLeftMouseAction, soundIdReader);
		guiAssignActionsToLeftMouseItem.setText("Assign action to left mouse click...");
		guiAssignActionsToLeftMouseItem.setIcon(IconUtils.getMouseIcon());
		guiAssignActionsToLeftMouseItem.setToolTipText("Assigns an action to the left mouse button");
		menu.add(guiAssignActionsToLeftMouseItem);
	}

	private GuiAssignActionToLeftMouseAction getGuiAssignActionToLeftMouseAction() {
		return new GuiAssignActionToLeftMouseAction(playerCharacter, getActions(), container, this, soundIdReader, parentFrame, imageInfoReader);
	}

	private void addPropertiesMenu(JPopupMenu menu, WorldObject worldObject) {
		if (Boolean.getBoolean("DEBUG")) {
			JMenu debugMenu = MenuFactory.createJMenu("Debug", imageInfoReader, soundIdReader);
			menu.add(debugMenu);
			
			JMenuItem guiPropertiesItem = MenuFactory.createJMenuItem(new GuiShowPropertiesAction(worldObject), soundIdReader);
			guiPropertiesItem.setText("Properties...");
			debugMenu.add(guiPropertiesItem);
			
			JMenuItem guiShowCommonersOverviewItem = MenuFactory.createJMenuItem(new GuiShowCommonersOverviewAction(world), soundIdReader);
			guiShowCommonersOverviewItem.setText("Show Commoners Overview...");
			debugMenu.add(guiShowCommonersOverviewItem);
			
			JMenuItem guiShowEconomicOverviewItem = MenuFactory.createJMenuItem(new GuiShowEconomicOverviewAction(world), soundIdReader);
			guiShowEconomicOverviewItem.setText("Show Economic Overview...");
			debugMenu.add(guiShowEconomicOverviewItem);
			
			JMenuItem guiShowSkillOverviewItem = MenuFactory.createJMenuItem(new GuiShowSkillOverviewAction(world), soundIdReader);
			guiShowSkillOverviewItem.setText("Show Skill Overview...");
			debugMenu.add(guiShowSkillOverviewItem);
			
			JMenuItem guiShowPersonalityOverviewItem = MenuFactory.createJMenuItem(new GuiShowPersonalitiesOverviewAction(world), soundIdReader);
			guiShowPersonalityOverviewItem.setText("Show Personality Overview...");
			debugMenu.add(guiShowPersonalityOverviewItem);
			
			JMenuItem guiShowReasonsOverviewItem = MenuFactory.createJMenuItem(new GuiShowReasonsOverviewAction(world), soundIdReader);
			guiShowReasonsOverviewItem.setText("Show Reasons Overview...");
			debugMenu.add(guiShowReasonsOverviewItem);
			
			JMenuItem guiShowPerformedActionsItem = MenuFactory.createJMenuItem(new GuiShowPerformedActionsAction(world), soundIdReader);
			guiShowPerformedActionsItem.setText("Show history items...");
			debugMenu.add(guiShowPerformedActionsItem);
			
			JMenuItem guiShowBuildingsItem = MenuFactory.createJMenuItem(new GuiShowBuildingsOverviewAction(world), soundIdReader);
			guiShowBuildingsItem.setText("Show buildings...");
			debugMenu.add(guiShowBuildingsItem);

			JMenuItem guiShowThrownOutOfGroupEventsAction = MenuFactory.createJMenuItem(new GuiShowThrownOutOfGroupEventsAction(), soundIdReader);
			guiShowThrownOutOfGroupEventsAction.setText("Show thrown out of group events...");
			debugMenu.add(guiShowThrownOutOfGroupEventsAction);
			
			JMenuItem guiShowElectionEventsAction = MenuFactory.createJMenuItem(new GuiShowElectionResultsAction(), soundIdReader);
			guiShowElectionEventsAction.setText("Show election events...");
			debugMenu.add(guiShowElectionEventsAction);
			
			JMenuItem guiShowImagesAction = MenuFactory.createJMenuItem(new GuiShowImagesOverviewAction(imageInfoReader), soundIdReader);
			guiShowImagesAction.setText("Show images...");
			debugMenu.add(guiShowImagesAction);		

			JMenuItem guiShowGoalDescriptionAction = MenuFactory.createJMenuItem(new GuiShowGoalDescriptionOverviewAction(imageInfoReader), soundIdReader);
			guiShowGoalDescriptionAction.setText("Show goal descriptions...");
			debugMenu.add(guiShowGoalDescriptionAction);
			
			JMenuItem guiShowBrawlFinishedAction = MenuFactory.createJMenuItem(new GuiShowBrawlFinishedAction(worldObject, playerCharacter, imageInfoReader, soundIdReader, container, world, parentFrame), soundIdReader);
			guiShowBrawlFinishedAction.setText("Show brawl finished dialog...");
			debugMenu.add(guiShowBrawlFinishedAction);
			
			JMenuItem guiShowDrinkingContestFinishedAction = MenuFactory.createJMenuItem(new GuiShowDrinkingContestFinishedAction(worldObject, playerCharacter, imageInfoReader, soundIdReader, container, world, parentFrame), soundIdReader);
			guiShowDrinkingContestFinishedAction.setText("Show drinking contest finished dialog...");
			debugMenu.add(guiShowDrinkingContestFinishedAction);
			
			JMenuItem guiShowExplorationOverviewAction = MenuFactory.createJMenuItem(new GuiShowExplorationOverviewAction(container, world), soundIdReader);
			guiShowExplorationOverviewAction.setText("Show exploration dialog...");
			debugMenu.add(guiShowExplorationOverviewAction);
		}
	}
	
	private void addPerformedActionsMenu(JPopupMenu menu, WorldObject worldObject) {
		if (Boolean.getBoolean("DEBUG")) {
			if (worldObject.hasIntelligence()) {
				JMenuItem showPerformedActionsItem = MenuFactory.createJMenuItem(new ShowPerformedActionsAction(worldObject, world), soundIdReader);
				showPerformedActionsItem.setText("Show performed actions...");
				menu.add(showPerformedActionsItem);
			}
		}
	}

	private boolean canPlayerCharacterPerformAction(WorldObject worldObject, ManagedOperation action) {
		return canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action) 
				&& action.isActionPossible(playerCharacter, worldObject, Args.EMPTY, world)
				&& action.distance(playerCharacter, worldObject, Args.EMPTY, world) == 0;
	}
	
	private boolean canPlayerCharacterPerformTalkAction(WorldObject worldObject, ManagedOperation action) {
		return canPlayerCharacterPerformActionUnderCorrectCircumstances(worldObject, action) 
				&& action.isActionPossible(playerCharacter, worldObject, Conversations.createArgs(Conversations.NAME_CONVERSATION), world)
				&& action.distance(playerCharacter, worldObject, Args.EMPTY, world) == 0;
	}
	
	private boolean canPlayerCharacterPerformActionUnderCorrectCircumstances(WorldObject worldObject, ManagedOperation action) {
		return action.isValidTarget(playerCharacter, worldObject, world) && playerCharacter.canWorldObjectPerformAction(action);
	}
	
	private boolean canPlayerCharacterPerformBuildAction(ManagedOperation action) {
		return action.isActionPossible(playerCharacter, playerCharacter, Args.EMPTY, world)
				&& playerCharacter.canWorldObjectPerformAction(action);
	}

	public void executeBuildAction(ManagedOperation buildAction, WorldObject buildLocation, int[] args) {
		Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, buildAction, args, world, dungeonMaster, buildLocation, container, imageInfoReader, soundIdReader);
	}

	public void setLeftMouseClickAction(ManagedOperation action) {
		leftMouseClickAction = action;
	}
	
	private class ShowCharacterActionsAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Point location = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(location, container);
			showPlayerCharacterMenu(location.x, location.y);
		}
	}
	
	private class GuiShowBuildingsAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Point location = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(location, container);
			showBuildingsAction(location.x, location.y);
		}
	}
	
	private void showBuildingsAction(int x, int y) {
		JPopupMenu menu = MenuFactory.createJPopupMenu(imageInfoReader);
		getBuildActions().add(menu);
		menu.show(container, x, y);
	}
}