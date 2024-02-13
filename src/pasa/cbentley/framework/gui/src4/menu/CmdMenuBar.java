package pasa.cbentley.framework.gui.src4.menu;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICommandable;
import pasa.cbentley.framework.cmd.src4.interfaces.IEventCmds;
import pasa.cbentley.framework.coreui.src4.interfaces.IActionFeedback;
import pasa.cbentley.framework.coreui.src4.tech.ITechCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.LayouterEngineDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.IBOTypesGui;
import pasa.cbentley.framework.gui.src4.factories.TablePolicyFactory;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITechTable;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;

/**
 * Menu bar that mimics MIDP 2.0 menu behavior.
 * <br>
 * Specifically designed for phones with a 0-9 Keyboard keys
 * <br>
 * Widget that controls command display on a Phone Deivce with 3 soft buttons.
 * <br>
 * <br>
 * It controls exclusively {@link MCmd} inside a {@link CmdCtx}, the current active {@link CmdCtx}.
 * <br>
 * When a Drawable is made visible, it sends ist context to the {@link Controller}. 
 * <br>
 * <br>
 * Created when the first {@link ICommandable} is made active in the Controller.
 * <br>
 * <br>
 * The menu context: The current Drawable gives its {@link CmdCtx} = Forms have their own and add commands using {@link Displayable#addCommand(Command)}
 * Each form has its menu model.
 * <br>
 * <br>
 * <b>Style Management</b> : <br>
 * A root style key is given in the constructor. It has a StyleClass with sub child style links. 
 * <li> {@link CmdMenuBar#LINK_CHILD_1LEFT_STYLE} controls the style of the left button
 * <li> {@link CmdMenuBar#LINK_CHILD_3RIGHT_STYLE} controls the style of the right button
 * <li> {@link CmdMenuBar#LINK_CHILD_2MIDDLE_STYLE} controls the style of the middle button
 * <br>
 * Model slot has a ctype to determine which sub style is used.
 * <br>
 * <br>
 * <b>Menu Structure</b> 
 * <br>
 * <li>One long list of commands. Simple but unpratical for many commands. Unorganized
 * <li>Fixed Row, 1 shown column (init Table with -1, column width implicitly maxed)
 * <li>Each column is titled by CK, takes as many rows as needed. <- -> moves from one column to another
 * Selection moves to closest non null element in the new dimension.
 * <br>
 * <br>
 * Pressing *, 0 or # key shows 3 cmds 
 * <br>
 * <br>
 * <b>J2ME MenuBar</b> a comparaison : <br>
 * <li>Each {@link Form} has its own implicit {@link CmdCtx}.  
 * <li>Item command are linked to sub implicit {@link CmdCtx} of a {@link Item}.
 * <li>When a {@link Form} is active, its registered {@link Command} are given to the J2ME menu bar system.
 * <br>
 * <br>
 * On a {@link Screen}, we want to link a code action to its trigger. What is a command in a Menu? A mean to action the command. A mean to know what commands are available in a given context.
 * In regular J2ME, we have 2 model slots. 
 * Menu display Commands from the Controller. It follows the same path as MIDP 2.0 menu. <br>
 * 
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 * @see TableView
 */
public class CmdMenuBar extends TableView implements IEventConsumer, ICmdsView {

   //inner link values
   public static final int    LINK_CHILD_1LEFT_STYLE   = 1;

   public static final int    LINK_CHILD_2MIDDLE_STYLE = 2;

   public static final int    LINK_CHILD_3RIGHT_STYLE  = 3;

   public static final int    LINK_CHILD_5_MENU_ITEM   = 5;

   /**
    * Normal state.
    */
   public static final int    MENU_STATE_0_NONE        = 0;

   /**
    * Left menu is visible
    */
   public static final int    MENU_STATE_1_LEFT        = 1;

   /**
    * Right menu is visible
    */
   public static final int    MENU_STATE_2_RIGHT       = 2;

   /**
    * 
    */
   public static final int    MENU_STATE_3_MID         = 3;

   /**
    * 
    */
   public static final int    MENU_STATE_4_MODAL       = 4;

   public static final int    MODE_0_ALWAYS_VISIBLE    = 0;

   public static final int    MODE_1_SHOW_ON_USE       = 1;

   public final static int    MODE_MENU_0_DEFAULT      = 0;

   public final static int    MODE_MENU_1_STAR         = 1;

   public final static int    MODE_MENU_2_POUND        = 2;

   public final static String MODE_STR_0_DEFAULT       = "";

   public final static String MODE_STR_1_STAR          = "*";

   public final static String MODE_STR_2_POUND         = "#";

   /**
    * 
    */
   public static final int    TECH_FLAG_1HIDDEN        = 1;

   /**
    * 
    */
   public static final int    TECH_FLAG_2HIDDEN        = 2;

   /**
    * 
    */
   public static final int    TECH_FLAG_3HIDDEN        = 4;

   /**
    * 
    */
   public static final int    TECH_FLAG_4HIDDEN        = 8;

   public static final int    TECH_ID                  = 2;

   protected CmdCtx           cc;

   private ObjectTableModel   cmdModelActive;

   /**
    * First cmd is shown on the right.
    * When Menu is pressed, it is changed to Select
    * All commands are shown. The first that was shown on the right may be ignored.
    */
   private ObjectTableModel   cmdModelDefault;

   private ObjectTableModel   cmdModelPound;

   private ObjectTableModel   cmdModelStar;

   /**
    * View for the sub menus
    * <br>
    * <br>
    * This class controls when it appears. It is initialized at the last
    * 
    */
   private TableView          cmdTableView;

   private int                currentModelOffset       = 0;

   private boolean            horiz;

   private boolean            isDoAnim                 = true;

   private IDrawable          itemKeyFocusBackUp;

   /**
    * Button for left menu
    */
   private StringDrawable     mLeft;

   /**
    * Middle may also be a Progress Bar
    */
   private StringDrawable     mMiddle;

   /**
    * Identifies Default,Star, Pound mode.
    */
   private int                modeMenu;

   private String             modeStr                  = "";

   /**
    * Button for right menu. Style changes when it is pressed.
    * <br>
    * Hosts a modifying String.
    */
   private StringDrawable     mRight;

   /**
    * Position is computed from relative {@link MasterCanvas}
    * <br>
    * <li> {@link C#POS_1_BOT} [default] shows menus up
    * <li> {@link C#POS_0_TOP} shows menus below
    * 
    */
   private int                position;

   private int                state;

   private ByteObject         techBar;

   private MCmd               topPriorityCmd;

   /**
    * Initialize {@link StringDrawable} with empty Strings.
    * <br>
    * <br>
    * @param styleClass
    */
   public CmdMenuBar(GuiCtx gc, StyleClass styleClass) {
      super(gc, styleClass, gc.getTablePolicyC().getMenuPolicy());

      cc = gc.getCC();
      techBar = styleClass.getByteObject(IBOTypesGui.LINK_75_MENU_BAR_TECH);
      //no need to get channel info. since we are a subclass and we are only interested by select which we override
      //EventChannel.addConsumerProducer(this, TableView.EVENT_ID_0SELECT, tv, tv.getProducerID());
      setHelperFlag(ITechTable.HELPER_FLAG_21_MODEL_STYLE, true);

      StyleClass scLeft = styleClass.getStyleClass(LINK_CHILD_1LEFT_STYLE, LINK_CHILD_5_MENU_ITEM);
      StyleClass scMiddle = styleClass.getStyleClass(LINK_CHILD_2MIDDLE_STYLE, LINK_CHILD_5_MENU_ITEM);
      StyleClass scRight = styleClass.getStyleClass(LINK_CHILD_3RIGHT_STYLE, LINK_CHILD_5_MENU_ITEM);
      //back up links if some are not defined.
      mLeft = new StringDrawable(gc, scLeft, getMenuLabel());
      
      mMiddle = new StringDrawable(gc, scMiddle, "");
      
      mRight = new StringDrawable(gc, scRight, "");

      //the Left/Middle/Right anchor is decided with the structural style

      setParentLink(mLeft);
      setParentLink(mMiddle);
      setParentLink(mRight);

      //implicit producer is the command module. default producer
      cc.getEventBusCmds().addConsumer(this, IEventCmds.PID_1_CMD, IEventCmds.EVENT_0_CMD_LABEL_CHANGE);

      
   }

   private String getMenuLabel() {
      MCmd cmd = cc.getCmd(CMD_52_SHOW_MENU);
      return cmd.getLabel();
   }

   /**
    * Change the menu model 
    * @param ic
    * @param otm
    */
   private void changeMenuModel(InputConfig ic, ObjectTableModel otm) {
      if (otm != null && cmdModelActive != otm) {
         setActiveMenuModel(otm);
         ic.srMenuRepaint();
      }
   }

   /**
    * Cmd act on event and decides if a repaint is needed.
    * <br>
    * <br>
    * When selection event comes from a Pointer event, the state of the Menu Item button must be finalized.
    * <br>
    * For example, if event is generated on a press, the release event will not reach the button and the drawable 
    * state is not reset.
    * <br>
    * TODO change this to the Select Command
    * @param e {@link BusEvent}
    */
   public void consumeEvent(BusEvent e) {
      if (e.getEventID() == IEventCmds.EVENT_4_COMMAND_EXECUTED) {
         //update undo? visuals? only if currently shown.
         CmdInstance ci = (CmdInstance) e.getParamO1();

      }
      if (e.getProducer() == cmdTableView) {
         if (e.getEventID() == ITechTable.EVENT_ID_00_SELECT) {
            //
            FocusCtrl fc = gc.getFocusCtrl();
            int focusCount = fc.getKeyFocusCount();
            executeSelectedCmd();
            //check focus change by command how?
            if (focusCount != fc.getKeyFocusCount()) {
               itemKeyFocusBackUp = fc.getItemInKeyFocus();
            }
            InputConfig ic = (InputConfig) e.getParamO2();
            hideAndGoState0(ic);
            e.setFlag(BusEvent.FLAG_1_ACTED, true);
         }
      } else if (e.getEventID() == IEventCmds.EVENT_0_CMD_LABEL_CHANGE) {
         //get the producer
         doUpdateCmdModel(e, cmdModelDefault);
         doUpdateCmdModel(e, cmdModelPound);
         doUpdateCmdModel(e, cmdModelStar);
         setState0Strings(); //update string menus
      }

   }

   /**
    * 
    * @param e
    * @param objectTableModel can be null in which case nothing happens
    */
   public void doUpdateCmdModel(BusEvent e, ObjectTableModel objectTableModel) {
      if (objectTableModel == null) {
         return;
      }
      if (e.getProducer() != null) {
         //or search for it
         MCmd producer = (MCmd) e.getProducer();
         int index = objectTableModel.findObjectRef(producer);
         if (index != -1) {
            objectTableModel.eventRefresh(index);
         } else {
            objectTableModel.eventRefresh();
         }
      } else {
         //refresh all command states. we don't know the position in the datamodel of the producer 
         objectTableModel.eventRefresh();
      }
   }

   public void doUpdateCmdTableView() {
      if (cmdTableView != null) {
         initSizeSubMenuTable();
         initSubMenuTablePosition();
         //#debug
         toDLog().pInit("Menu Table Created : ", cmdTableView, CmdMenuBar.class, "updateCmdTableView");
      }
   }

   public void doUpdateLayoutNoSubMenu(boolean topDown) {
      super.layoutInvalidate(topDown);
      initSize();
   }

   /**
    * Fetch selected Index in Command TableView.
    * <br>
    * <br>
    * 
    */
   public void executeSelectedCmd() {
      //the selected index is not offset modified?
      int selectedIndex = cmdTableView.getSelectedIndex();
      selectedIndex += currentModelOffset;
      if (cmdModelActive != null) {
         MCmd cmd = (MCmd) cmdModelActive.getObject(selectedIndex);
         //#debug
         toDLog().pCmd("executing", cmd, CmdMenuBar.class, "executeSelectedCmd", LVL_05_FINE, true);
         //execute menu command within the right Feedback

         gc.getViewCommandListener().executeMenuCmd(cmd);
      } else {
         //#debug
         toDLog().pCmd("No Command Model Active", this, CmdMenuBar.class, "executeSelectedCmd", LVL_05_FINE, true);
      }

   }

   public ByteObject getMenuBarTech() {
      return techBar;
   }

   /**
    * Renders menu buttons as {@link StringDrawable}. 
    * <br>
    * {@link CmdMenuBar} acts as Model and a Wrapper.
    * Overrides {@link TableView#getModelDrawable(int)}.
    * <br>
    * <br>
    * When method returns null, a {@link TableView#getNullCellPrint()} is drawn instead
    */
   public IDrawable getModelDrawable(int index) {
      if (index == 0) {
         if (mLeft.getLen() != 0) {
            return mLeft;
         } else {
            return null;
         }
      } else if (index == 1) {
         if (mMiddle.getLen() != 0) {
            return mMiddle;
         } else {
            return null;
         }
      } else if (index == 2) {
         if (mRight.getLen() != 0) {
            return mRight;
         } else {
            return null;
         }
      } else {
         throw new IllegalArgumentException("" + index);
      }
   }

   /**
    * 
    */
   public int getSize() {
      return 3;
   }

   /**
    * Called to hide the current menu items. The key focus is given back to where it was previously.
    * <br>
    * or it is given to the newly focused drawable because of command action? How do you know if the cmd issued a focus change?
    * 
    * <br>
    * @param ic
    */
   private void hideAndGoState0(InputConfig ic) {
      //TODO give focus to saved focus

      cmdTableView.removeDrawable(ic, itemKeyFocusBackUp);
      ic.srActionDoneRepaint(this);
      setState0Strings();
      setState(MENU_STATE_0_NONE);
   }

   private void icRelease(InputConfig ic) {
      if (ic.is.getKeyCode() == ITechCodes.KEY_MENU_LEFT) {
         mLeft.setStateStyle(ITechDrawable.STYLE_08_PRESSED, false);
         //send a menu repaint
         ic.srActionDoneRepaint(mLeft);
      } else if (ic.is.getKeyCode() == ITechCodes.KEY_MENU_RIGHT) {
         mRight.setStateStyle(ITechDrawable.STYLE_08_PRESSED, false);
         //send a menu repaint
         ic.srActionDoneRepaint(mRight);
      } else if (ic.is.getKeyCode() == ITechCodes.KEY_FIRE) {
         if (mMiddle.hasStateStyle(ITechDrawable.STYLE_08_PRESSED)) {
            mMiddle.setStateStyle(ITechDrawable.STYLE_08_PRESSED, false);
            //send a menu repaint
            ic.srActionDoneRepaint(mMiddle);
         }
      }
   }

   /**
    * TODO when initialized before the {@link CmdMenuBar}, we have a DrawnWidth of zero.
    * 
    * The Menus {@link TableView} is first initialized without a model?
    * 
    */
   public void initSizeSubMenuTable() {
      if (cmdTableView != null) {
         //size available to 
         int pos = getMenuBarTech().get1(IMenus.MENUS_OFFSET_02_POSITION1);
         int hsize = 0;
         int wsize = 0;
         if (pos == C.POS_1_BOT || pos == C.POS_0_TOP) {
            hsize = getVC().getHeight() - getDrawnHeight();
            wsize = getDrawnWidth();
         } else {
            hsize = getDrawnHeight();
            wsize = getVC().getWidth() - getDrawnWidth();
         }
         //#debug
         toDLog().pInit("[" + wsize + "," + hsize + "]", this, CmdMenuBar.class, "initSizeSubMenuTable", LVL_05_FINE, true);
         cmdTableView.init(wsize, hsize);
      }
   }

   /**
    * Create {@link TableView} for sub menu.
    * <br>
    * <br>
    * Reads the {@link StyleClass} of the Table from the {@link CmdMenuBar} style class.
    * <br>
    * <br>
    * The size is done in another method.
    * <br>
    * <br>
    * The menu table policy is {@link TablePolicyFactory#getMenuSubPolicy()}.
    */
   private void initSubMenuTable() {
      if (cmdTableView == null) {
         ByteObject policy = gc.getTablePolicyC().getMenuSubPolicy();
         //
         StyleClass tableSC = styleClass.getSCNotNull(IBOTypesGui.LINK_74_STYLE_CLASS_MENU);
         cmdTableView = new TableView(gc, tableSC, policy);
         cmdTableView.setShrink(true, true);
         cmdTableView.setParent(this);
         cmdTableView.setModelOffset(currentModelOffset);
         cmdTableView.addEventListener(this, ITechTable.EVENT_ID_00_SELECT);

         //there is lots of tweaks to be made based on the device
         //         if (getDevice().isAnimated()) {
         //
         //            int type = IAnim.ANIM_TIME_1_ENTRY;
         //            //here we want an animation based on time.
         //            // so the animation framework adjuts the frame and its values based on the performance.
         //            int timeMillis = getDevice().getAnimTimeFast();
         //            ByteObject animDrw = MAnimC.getAnimationAlpha(mod, type, 0, new int[] { 36, 120, 150, 200, 220, 250 }, 50);
         //            boolean origin = true;
         //            ByteObject anchor = gc.getDrwEng().getBoxEng().getBoxCenter();
         //            ByteObject animMove = MAnimC.getAnimMove(mod, type, true, anchor);
         //
         //            IAnimable anim = null;
         //            if (animDrw != null) {
         //               anim = getGuiContext().getAnimCreator().createDrawableAnimable(animDrw, cmdTableView);
         //            }
         //            cmdTableView.addFullAnimation(anim);
         //            int typeExit = IAnim.ANIM_TIME_2_EXIT;
         //            ByteObject animDrwExit = MAnimC.getAnimationAlpha(mod, typeExit, 0, new int[] { 220, 150, 100, 50, 20 }, 50);
         //
         //            if (animDrw != null) {
         //               anim = getGuiContext().getAnimCreator().createDrawableAnimable(animDrwExit, cmdTableView);
         //            }
         //            cmdTableView.addFullAnimation(anim);
         //
         //         }
      }
   }

   /**
    * Update menu table position based on menubar position on the Canvas.
    * <br>
    * Must be called once the {@link CmdMenuBar} has been initialized
    */
   private void initSubMenuTablePosition() {
      //positioning of the first page
      int pos = getMenuBarTech().get1(IMenus.MENUS_OFFSET_02_POSITION1);
      if (pos == C.POS_1_BOT) {
         cmdTableView.setXY(this.getX(), this.getY() - cmdTableView.getDrawnHeight());
      } else if (pos == C.POS_0_TOP) {
         cmdTableView.setXY(this.getX(), this.getY() + this.getDrawnHeight());
      } else if (pos == C.POS_2_LEFT) {
         cmdTableView.setXY(this.getX() + this.getDrawnWidth(), this.getY());
      } else if (pos == C.POS_3_RIGHT) {
         cmdTableView.setXY(this.getX() - cmdTableView.getDrawnWidth(), this.getY());
      }
   }

   
   protected void initViewDrawable(LayouterEngineDrawable ds) {
      if (cmdTableView != null) {
         //make an update on the menu sizes
         //initSizeSubMenuTable();
      }
   }

   private boolean isInterCept(InputConfig ic) {
      boolean doIt = false;
      //only check interceptions when in state
      if (state != MENU_STATE_0_NONE) {
         if (!DrawableUtilz.isInside(ic, this) && (cmdTableView != null && !cmdTableView.hasState(ITechDrawable.STATE_03_HIDDEN))) {
            if (!DrawableUtilz.isInside(ic, cmdTableView)) {
               doIt = true;
            }
         }
      }
      return doIt;
   }

   public boolean isOnDemand() {
      return getMenuBarTech().get1(IMenus.MENUS_OFFSET_03_SHOW_MODE1) == IMenus.SHOW_MODE_1_ON_DEMAND;
   }

   public void layoutInvalidate(boolean topDown) {
      super.layoutInvalidate(topDown);
      initSize();
      doUpdateCmdTableView();
   }

   /**
    * Sets the model containing the {@link MCmd}.
    * <br>
    * <br>
    * Flat list of {@link MCmd}.
    * <br>
    * Updates state
    * <br>
    * <br>
    * @param cmdModel
    */
   private void loadCmdModelInSubMenu(ObjectTableModel cmdModel) {
      initSubMenuTable();
      cmdTableView.setDataModel(cmdModel);
      //in some cases, we want to show a command in some cells directly visible.
      cmdTableView.setModelOffset(currentModelOffset); //not yet implemented
      initSizeSubMenuTable();
   }

   /**
    * Controller code matching KeyInput to graphical action.
    * <br>
    * <br>
    * Uses system command, Show Menu Left, Show Menu Right
    * <br>
    * When {@link CmdMenuBar} exists, it always gets this call before other {@link Drawable}s
    * in the {@link MasterCanvas}.
    * <br>
    * <br>
    * Menu Actions on Key Release with visual feedback on Key Press.
    */
   public void manageKeyInput(InputConfig ic) {
      if (ic.is.getMode() == IInput.MOD_1_RELEASED) {
         icRelease(ic);
      } else {
         if (ic.isCancel() && state != MENU_STATE_0_NONE) {
            setState(MENU_STATE_0_NONE);
            ic.srActionDoneRepaint();
            return;
         }
         if (ic.isSoftLeftP()) {
            if (isOnDemand() && this.hasState(ITechDrawable.STATE_03_HIDDEN)) {
               //make the menu bar visible 
            }
            menuActionOnModel(ic, mLeft);
            //mLeft.setStateStyle(IDrawable.STYLE_08_PRESSED, true);
            //ic.setActionDoneRepaint(mLeft);
         }
         if (ic.isSoftRightP()) {
            //show Right menu
            menuActionOnModel(ic, mRight);
            //mRight.setStateStyle(IDrawable.STYLE_08_PRESSED, true);
            //ic.setActionDoneRepaint(mRight);
         }
         if (ic.isFireP()) {
            menuActionOnModel(ic, mMiddle);
         }

         //when modifiers key are kept pressed, modifies the base Model content. with ctx specifics
         if (ic.isKeyTypedAlone(ITechCodes.KEY_STAR)) {
            //modifies 3 to show 3 starred menu cmds. modifies active model
            if (modeMenu == MODE_MENU_1_STAR) {
               changeMenuModel(ic, cmdModelDefault);
            } else {
               changeMenuModel(ic, cmdModelStar);
            }
         }
         if (ic.isKeyTypedAlone(ITechCodes.KEY_POUND)) {
            //modifies 3 to show 3 starred menu cmds. modifies active model
            if (modeMenu == MODE_MENU_2_POUND) {
               changeMenuModel(ic, cmdModelDefault);
            } else {
               changeMenuModel(ic, cmdModelPound);
            }
         }
      }
      //Table does not get an key events
   }

   /**
    * Interceptor was removed.
    * <li> register for a pointer press context change and link it to this command
    * {@link ICmdsView#CTX_CAT_1_POINTER_PRESSED}
    * <li> register for a pointer press in any context.. less safe because
    * we can change context without using a pointer press
    * @param ic
    */
   private void cmdCloseMenuTable(InputConfig ic) {
      //call routine to close menu and 
      cmdTableView.removeDrawable(ic, null);
      setState0Strings();
      setState(MENU_STATE_0_NONE);
      ic.srActionDoneRepaint(this);
      ic.srActionDoneRepaint(cmdTableView);
      ic.sr.setFlag(IActionFeedback.FLAG_01_ACTION_DONE, false);
      //remove interceptor
      this.getCanvas().getVCRoot().getTopo().removeInterceptor(this);
   }

   /**
    * Implements action on slave drawable.s
    * <br>
    * Listener method for events on cell {@link Drawable}s of this {@link CmdMenuBar}.
    * <br>
    * <br>
    * 
    */
   protected void managePointerInputSlaveCell(InputConfig ic, IDrawable slave) {
      //#debug
      toDLog().pFlow("", slave, CmdMenuBar.class, "managePointerInputSlaveCell", LVL_05_FINE, true);
      if (ic.isReleased()) {
         if (slave == mLeft && ic.isInside(mLeft)) {
            menuActionOnModel(ic, mLeft);
         } else if (slave == mRight && ic.isInside(mRight)) {
            menuActionOnModel(ic, mRight);
         }
      }
      super.managePointerInputSlaveCell(ic, slave);
   }

   /**
    * Called when Left soft button is pressed with {@link Drawable} mLeft in parameter.
    * <br>
    * <br>
    * <li>Left: Shows Menu [State0] or Select [State1]
    * <li>Right: Execute Cmd [State0] / Cancel [State1]
    * @param ic
    * @param w
    * @param index
    */
   private void menuActionOnModel(InputConfig ic, Drawable w) {
      if (w == mLeft) {
         if (state == MENU_STATE_0_NONE) {
            state0LeftActionShowMenu(ic);
         } else if (state == MENU_STATE_1_LEFT) {
            state1LeftActionSelect(ic);
         }
      } else if (w == mRight) {
         if (state == MENU_STATE_0_NONE) {
            state0RightActionExecCmd(ic);
         } else if (state == MENU_STATE_1_LEFT) {
            state1RightActionCancelMenu(ic);
         }
      }
   }

   /**
    * Refresh all commands Strings
    */
   public void notifyEventRefresh() {
      if (cmdModelDefault != null)
         cmdModelDefault.eventRefresh();
      if (cmdModelPound != null)
         cmdModelPound.eventRefresh();
      if (cmdModelStar != null)
         cmdModelStar.eventRefresh();
   }

   /**
    * Temporarily create a new model.
    * Modal. Exclusive
    * @param cmdAccept
    */
   public void setActiveCommand(MCmd cmdAccept) {
      // TODO Auto-generated method stub

   }

   /**
    * Sets the active menu model. Must be set from the outside
    * <br>
    * <br>
    * Each time this happens, the TableView size is computed again.
    * <br>
    * <br>
    * 
    * @param otm
    */
   public void setActiveMenuModel(ObjectTableModel otm) {
      if (otm != null && cmdModelActive != otm) {
         if (otm == cmdModelDefault) {
            modeMenu = MODE_MENU_0_DEFAULT;
            modeStr = MODE_STR_0_DEFAULT;
         } else if (otm == cmdModelStar) {
            modeMenu = MODE_MENU_2_POUND;
            modeStr = MODE_STR_1_STAR;
         } else if (otm == cmdModelPound) {
            modeMenu = MODE_MENU_2_POUND;
            modeStr = MODE_STR_2_POUND;
         }
         cmdModelActive = otm;
         loadCmdModelInSubMenu(otm);
         setState0Strings();
      }
   }

   /**
    * Maybe called before the {@link CmdMenuBar} has been initialized.
    * <br>
    * <br>
    * @param cmdModel
    */
   public void setCmdModelDefault(ObjectTableModel cmdModel) {
      this.cmdModelDefault = cmdModel;
   }

   /**
    * Single command. Modal.
    * <br>
    * <br>
    * 
    * @param cmdOK
    */
   public void setCmdModelOk(MCmd cmdOK) {

   }

   /**
    * 
    * @param okString
    */
   public void setCmdModelOKCAncel(String okString) {
      state = MENU_STATE_4_MODAL;
      setStrings(okString, null, getLabelCancel());
   }

   private String getLabelSelect() {
      return cc.getCmd(CMD_24_SELECT).getLabel();
   }

   private String getLabelCancel() {
      return cc.getCmd(CMD_05_CANCEL).getLabel();
   }

   public void setCmdModelPound(ObjectTableModel cmdModel) {
      this.cmdModelPound = cmdModel;
   }

   public void setCmdModelStar(ObjectTableModel cmdModel) {
      this.cmdModelStar = cmdModel;
   }

   /**
    * How is decided the width of the MenuBar in horizontal mode? It should be fixed and cells use a trick to show
    * full Strings.
    * <br>
    * <br>
    * 
    * @param v
    */
   public void setHoriz(boolean v) {
      TablePolicyFactory tabc = gc.getTablePolicyC();
      if (!horiz && v) {
         //set horiz mode, change table policy
         int w = 0;
         int h = 0;
         ByteObject polh = tabc.getButtonLine(true, w, h);
         super.updateTablePolicy(polh);
         horiz = true;
      } else if (horiz && !v) {
         //set horizontal
         ByteObject polh = tabc.getMenuPolicy();
         super.updateTablePolicy(polh);
         horiz = false;
      }
   }

   public void setState(int state) {
      //SystemLog.println("#CmdMenuBar#setState " + state);
      this.state = state;
   }

   /**
    * Sets the {@link CmdMenuBar#MENU_STATE_0_NONE} Strings. Which are Menu - null - Main Command
    */
   private void setState0Strings() {
      if (cmdModelActive == null) {
         setActiveMenuModel(cmdModelDefault);
      }
      String cmdStrRight = null;
      if (cmdModelActive != null && cmdModelActive.getSizeModel() > 1) {
         cmdStrRight = ((MCmd) cmdModelActive.getObject(0)).getLabel();
         if (topPriorityCmd != null) {
            cmdStrRight = topPriorityCmd.getLabel();
         }
         String cmdStrLeft = getMenuLabel() + modeStr;
         setStrings(cmdStrLeft, null, cmdStrRight);
      } else {
         //only one command
         setStrings("No Menu", null, null);
      }
   }

   /**
    * Sets strings for the 3 buttons. null reference empty the content.
    * <br>
    * <br>
    * @param left
    * @param mid
    * @param right
    */
   public void setStrings(String left, String mid, String right) {
      //#debug
      toDLog().pState(left + " - " + mid + " - " + right, this, CmdMenuBar.class, "setStrings", LVL_05_FINE, true);
      boolean update = false;
      if (left != null) {
         mLeft.setStringNoUpdate(left);
         update = true;
      }
      if (mid != null) {
         mMiddle.setStringNoUpdate(mid);
         update = true;
      }
      if (right != null) {
         mRight.setStringNoUpdate(right);
         update = true;
      }
      if (update) {
         this.doUpdateLayoutNoSubMenu(true);
      }
   }

   public void setTopPriorityCommand(MCmd cmd) {
      topPriorityCmd = cmd;
      setState0Strings();
   }

   public void showCtxMenu(InputConfig ic) {
      if (cmdTableView != null) {
         initSizeSubMenuTable();
      }
      int x = ic.is.getX();
      int y = ic.is.getY();

      cmdTableView.setXY(x, y);
      showSubMenuTable(ic);
   }

   /**
    * The Menu should disappear when it loses the pointer press focus?
    * <br>
    * Or should it act a Model Move Layer, Move events
    * are not more registered outside this layer. Presses however go through.
    * <br>
    * If press selects another , we need a way for the Menu to know that.
    * how to intercept the event of a press outside? Focus loss event.
    * <br>
    * Well, the command Select. Listen to executed command.
    * <br> 
    * @param ic
    */
   protected void showSubMenuTable(InputConfig ic) {
      cmdTableView.shShowDrawable(ic, ITechCanvasDrawable.SHOW_TYPE_1_OVER);
      //registers as an event interceptor
      this.getCanvas().getVCRoot().getTopo().addInterceptor(this);

      //previous focus
      itemKeyFocusBackUp = gc.getFocusCtrl().getItemInKeyFocus();
      //give focus control
      gc.getFocusCtrl().newFocusKey(ic, cmdTableView);
      ic.srActionDoneRepaint(cmdTableView);
      ic.srActionDoneRepaint(this);
      setState(MENU_STATE_1_LEFT);
      setStrings(getLabelSelect(), null, getLabelCancel());
   }

   /**
    * Method called when the left Menu Soft button is actioned.
    * <li> by key
    * <li> by pointer pressed and released.
    * <br>
    * <br>
    * @param ic
    */
   private void state0LeftActionShowMenu(InputConfig ic) {
      if (cmdTableView == null) {
         initSubMenuTable();
         initSizeSubMenuTable();
      }
      //make sure the model is loaded
      if (cmdTableView.getTableModel() == null) {
         setActiveMenuModel(cmdModelDefault);
      }
      initSubMenuTablePosition();

      showSubMenuTable(ic);
   }

   /**
    * Execute first model cmd shown on the right
    * <br>
    * <br>
    * @param ic
    */
   private void state0RightActionExecCmd(InputConfig ic) {
      MCmd cmd = (MCmd) cmdModelActive.getObject(0);
      if (topPriorityCmd != null) {
         cmd = topPriorityCmd;
      }
      gc.getViewCommandListener().executeMenuCmd(cmd);
   }

   //#enddebug

   /**
    * 
    * @param ic
    */
   private void state1LeftActionSelect(InputConfig ic) {
      executeSelectedCmd();
      hideAndGoState0(ic);
   }

   private void state1RightActionCancelMenu(InputConfig ic) {
      hideAndGoState0(ic);
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, CmdMenuBar.class,1023);
      super.toString(dc.newLevel());
      dc.nlLvl("Left MenuButton", mLeft);
      dc.nlLvl("Middle MenuButton", mMiddle);
      dc.nlLvl("Right MenuButton", mRight);
      dc.nlLvl("Menus TableView with Model", cmdTableView);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, CmdMenuBar.class);
   }
   //#enddebug
}
