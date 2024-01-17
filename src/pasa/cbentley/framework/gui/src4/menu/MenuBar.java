package pasa.cbentley.framework.gui.src4.menu;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.coreui.src4.tech.IBCodes;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;

/**
 * MenuBar class control the drawing of a MenuBar and its associated Menus.
 * <br>
 * <br>
 * <b>Concepts</b>: <br>
 * <li>Model : data to be shown in a {@link TableView}.
 * <li>model slot : area to display model title.
 * <li>model plane : logical groups of model slots. Iteration over plane is made using
 * <ol>
 * <li>specific key event for iteration
 * <li>while associated plane key is kept pressed down.
 * <li>when one thumb mode, associated plane key is pressed and released. pressing and releasing again bring back to root plane.
 * </ol>
 * <br>
 * <b>Features & Responsabilities</b>: 
 * <li>Model slot for displaying a message. Timer may be set to remove it.
 * <li>Support one thumb mode (only single key press events)
 * <li>Slot Model is anchored left, middle or right to widget displaying slot title.
 * <li>When a slot item is "actioned", model event is generated.
 * <li>Controls
 * <br>
 * <br>
 * <b>J2ME MenuBar</b> : <br>
 * On a Screen, we want to link a code action to its trigger. What is a command in a Menu? A mean to action the command. A mean to know what commands are available in a given context.
 * In regular J2ME, we have 2 model slots. 
 * Menu display Commands from the Controller. It follows the same path as MIDP 2.0 menu. <br>
 * The menu context: The current Drawable gives its CmdContext = Forms have their own and add commands using {@link Displayable#addCommand(Command)}
 * Each form has its menu model.
 * <br>
 * <li>In Prototyping, Menu Model for a Drawable are the actions implemented in {@link Drawable#manageKeyInput(InputConfig)}.
 * <br>
 * <li>In the {@link ClassKey} model, commands modify Classed Fields. View/Model refresh based on updated ClassKey.
 * <br>
 * <br>
 * <b>Style Management</b> : <br>
 * A root style key is given in the constructor. It has a StyleClass with sub child style links.
 * Model slot has a ctype to determine which sub style is used.
 * <br>
 * <br>
 * <b>Graphical Options</b> : <br>
 * <li>Animation is linked in the StyleKey<br>
 * <li>Hidden becomes visible upon a given user input event. Could be MenuBar keys.
 * <li> On Pointing Devices, it shows just a few pixel in a corner drawn over the top of the Canvas. It hides automatically  when pointer<br>
 * is pressed at the normal position (that is no pointing event may do downwards at the canvas layer.
 * <br>
 * <br>
 * <b>Events</b>: <br> 
 * The MenuBar registers as the {@link ITableListener} for its Menus.
 * <li> When a Select event is recorded on a Menu Command, the command code is executed, if none, the command is sent to {@link CommandListener}. They are also other command triggers.
 * <br>
 * <b>Implementation Note</b> : <br>
 * It uses a {@link TableView} for its structure. Other {@link TableView}s are summoned to display slot models on the Foreground layer
 * of the {@link MasterCanvas}.
 * <br>
 * <br>
 * <b>State Machine</b> <br>
 * When a menu slot is active, it may feed the other two slots for its own need.
 * <li>Starts at {@link MenuBar#STATE_0NONE}
 * <li>When slot menu is showing  {@link MenuBar#STATE_1LEFT}.
 * <br>

 * <br>
 *
 * <br>
 * <br>
 * <b>Menu Slot Navigation Tricks</b> <br>
 * Uses a several columns structure fitting as many rows as possible. (example of strong row Flow Table).
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class MenuBar extends Drawable {

   public static final int    CHILD_LINK_LEFT_DRAWABLE   = 1;

   private static final int   CHILD_LINK_LEFT_MENU       = 4;

   public static final int    CHILD_LINK_MIDDLE_DRAWABLE = 2;

   public static final int    CHILD_LINK_RIGHT_DRAWABLE  = 3;

   private static final int   CHILD_LINK_RIGHT_MENU      = 5;

   public static final int    STATE_0NONE                = 0;

   public static final int    STATE_1LEFT                = 1;

   public static final int    STATE_2RIGHT               = 2;

   public static final int    STATE_3MID                 = 3;

   /**
    * 
    */
   public static final int    TECH_FLAG_1HIDDEN          = 1;

   /**
    * 
    */
   public static final int    TECH_FLAG_2HIDDEN          = 2;

   /**
    * 
    */
   public static final int    TECH_FLAG_3HIDDEN          = 4;

   /**
    * 
    */
   public static final int    TECH_FLAG_4HIDDEN          = 8;

   public static final int    TECH_ID                    = 2;

   public static final int    TMODEL_0ROOT               = 0;

   public static final int    TMODEL_1STAR               = 1;

   public static final int    TMODEL_2POUND              = 2;

   public static final int    TMODEL_3ZERO               = 3;


   /**
    * View for the left menu model
    */
   private TableView          leftView;

   /**
    * Which slate of model is used.
    * <li>root
    * <li>
    */
   private int                level;

   /**
    * Button for left menu
    */
   private StringDrawable     mLeft;

   /**
    * Middle may also be a Progress Bar
    */
   private StringDrawable     mMiddle;

   /**
    * Button for right menu. Style changes when it is pressed.
    */
   private StringDrawable     mRight;

   /**
    * View for the right menu model
    */
   private TableView          rightView;

   private int                state;

   /**
    * The model currently active .
    * <br>
    * Provide content for the 3 menubar buttons. 
    */
   private ObjectTableModel   tActiveModel;

   /**
    * Models
    */
   private ObjectTableModel[] tmodels                    = new ObjectTableModel[4];

   /**
    * 
    */
   private TableView          tv;

   /**
    * Initialize {@link StringDrawable} with empty Strings.
    * @param styleKey
    */
   public MenuBar(GuiCtx gc, StyleClass styleKey) {
      super(gc, styleKey);
      //the table is only an organizer
      tv = new TableView(gc, styleKey, gc.getTablePolicyC().getMenuPolicy());
      int type = ITechStringDrawable.TYPE_1_TITLE;
      mRight = new StringDrawable(gc, styleClass.getStyleClass(CHILD_LINK_RIGHT_DRAWABLE), "Right", type);
      mRight.setStateFlag(ITechDrawable.STATE_06_STYLED, true);
      mMiddle = new StringDrawable(gc, styleClass.getStyleClass(CHILD_LINK_MIDDLE_DRAWABLE), "M", type);
      mMiddle.setStateFlag(ITechDrawable.STATE_06_STYLED, true);
      mLeft = new StringDrawable(gc, styleClass.getStyleClass(CHILD_LINK_LEFT_DRAWABLE), "Left", type);
      mLeft.setStateFlag(ITechDrawable.STATE_06_STYLED, true);

   }

   public void drawDrawable(GraphicsX g) {
      super.drawDrawableBg(g);
      tv.drawDrawable(g);
      //draw menu if needed
      super.drawDrawableFg(g);
   }

   public IDrawable getDrawable(int index) {
      if (index == 0)
         return mLeft;
      else if (index == 1)
         return mMiddle;
      else if (index == 2)
         return mRight;
      else
         throw new IllegalArgumentException("" + index);
   }

   public ObjectTableModel getModel(int id) {
      return tmodels[id];
   }

   public int getSize() {
      return 3;
   }

   private ByteObject getSubMenuPolicy() {
      int hsize = getVC().getHeight() - getDrawnHeight();
      ByteObject policy = gc.getTablePolicyC().getMenuSubPolicy(getDrawnWidth(), hsize);
      return policy;
   }

   private void icRelease(InputConfig ic) {
      if (ic.getIdKeyBut() == IBCodes.KEY_MENU_LEFT) {
         mLeft.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         //send a menu repaint
         ic.srActionDoneRepaint(mLeft);
      } else if (ic.getIdKeyBut() == IBCodes.KEY_MENU_RIGHT) {
         mRight.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         //send a menu repaint
         ic.srActionDoneRepaint(mRight);
      } else if (ic.getIdKeyBut() == IBCodes.KEY_FIRE) {
         mMiddle.setStateStyle(ITechDrawable.STYLE_05_SELECTED, false);
         //send a menu repaint
         ic.srActionDoneRepaint(mMiddle);
      }
   }

   /**
    * Special overriding of
    * {@link Drawable#init(int, int)}
    * First initialized the {@link TableView}. Its preferred height is used to initialized the MenuBar
    */
   public void init(int w, int h) {
      tv.init(w, h);
      int rh = tv.getPreferredHeight();
      super.init(w, rh);
   }

   /**
    * Controller code matching KeyInput to graphical action
    * Uses system command, Show Menu Left, Show Menu Right
    */
   public void manageKeyInput(InputConfig ic) {
      if (ic.is.getMode() == IInput.MOD_1_RELEASED) {
         icRelease(ic);
      } else {
         if (ic.isCancel() && state != STATE_0NONE) {
            state = STATE_0NONE;
            ic.srActionDoneRepaint();
            return;
         }
         if (ic.isSoftLeftP()) {
            //first check if menu is hidden. 
            if (hasState(ITechDrawable.STATE_03_HIDDEN)) {
               //show it as an overlay and return
               this.notifyEventShow();
               //it will automatically hide after x seconds
            } else {
               //asks tmodel if we have a menu, a command or nothing
               if (tActiveModel != null) {
                  //get the object linked
                  Object o = tActiveModel.getObject(0);
                  if (o != null) {
                     //in all case put state as selected
                     mLeft.setStateStyle(ITechDrawable.STYLE_05_SELECTED, true);
                     if (o instanceof String) {
                        //show menu
                        int x = 0;
                        int y = getY() - leftView.getDrawnHeight();
                        leftView.setXY(x, y);
                        //kick start Left menu
                        leftView.notifyEventShow();
                        state = STATE_1LEFT;
                     } else if (o instanceof MCmd) {
                        gc.getViewCommandListener().executeMenuCmd((MCmd) o);
                     }
                     ic.srActionDoneRepaint();
                  }
               }
            }
         }
         if (ic.isSoftRightP()) {
            //show Right menu
         }
         //when * is pressed 
         if (ic.isStarP() && gc.isOneThumb()) {
            //show alternative menu
            if (tmodels[TMODEL_1STAR] != null) {
               tActiveModel = tmodels[TMODEL_1STAR];
               ic.srMenuRepaint();
            }
         }
         if (ic.isPoundP()) {
            if (tmodels[TMODEL_2POUND] != null) {
               tActiveModel = tmodels[TMODEL_2POUND];
               ic.srMenuRepaint();
            }
         }
         if (ic.isZeroP()) {
            if (tmodels[TMODEL_3ZERO] != null) {
               tActiveModel = tmodels[TMODEL_3ZERO];
               ic.srMenuRepaint();
            }
         }
      }
      //Table does not get an key events
   }

   /**
    * MenuBar
    * Menu selection works like this:
    * 1: pointer event in a cell of table -> cell goes selected style
    * 2: for the menu action to be fired, the
    * 
    */
   public void managePointerInput(InputConfig ic) {
      tv.managePointerInput(ic);
      if (mLeft.hasStateStyle(ITechDrawable.STYLE_05_SELECTED)) {
         //show Left Menu
      }
   }

   /**
    * 
    */
   public void setLeftModel(ObjectTableModel otm) {
      if (leftView == null) {
         StyleClass styleKey = styleClass.getStyleClass(CHILD_LINK_LEFT_MENU);
         ByteObject policy = getSubMenuPolicy();
         leftView = new TableView(gc,styleKey, policy);
         leftView.setShrink(true, true);
      }
      leftView.setDataModel(otm);
   }

   /**
    * 
    * @param str
    */
   public void setMiddleString(String str) {
      mMiddle.setStringNoUpdate(str);
   }

   /**
    * 3 size object model for the Left Middle and Right Menu Command Strings. <br>
    * <li>Default Model {@link MenuBar#TMODEL_0ROOT}
    * <li>{@link MenuBar#TMODEL_0ROOT}
    * @param otm
    */
   public void setModel(ObjectTableModel otm, int id) {
      tmodels[id] = otm;
   }

   /**
    * 
    */
   public void setRightModel(ObjectTableModel otm) {
      if (rightView == null) {
         StyleClass styleKey = styleClass.getStyleClass(CHILD_LINK_RIGHT_MENU);
         ByteObject policy = getSubMenuPolicy();
         rightView = new TableView(gc,styleKey, policy);
         rightView.setShrink(true, true);
      }
      rightView.setDataModel(otm);
   }

   public void setTableView(TableView tv) {
      //nothing to do here
   }

   public void setXY(int x, int y) {
      int xd = x - this.getX();
      int yd = y - this.getY();
      tv.shiftXY(xd, yd);
      super.setXY(x, y);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "MenuBar");
      toStringPrivate(dc);
      dc.nlLvl(tv, "TableView");
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "MenuBar");
      toStringPrivate(dc);
      dc.nlLvl1Line(mLeft, "Left MenuButton");
      dc.nlLvl1Line(mMiddle, "Middle MenuButton");
      dc.nlLvl1Line(mRight, "Right MenuButton");
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
