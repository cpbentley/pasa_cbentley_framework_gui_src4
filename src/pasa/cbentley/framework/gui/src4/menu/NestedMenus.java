package pasa.cbentley.framework.gui.src4.menu;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.INavTech;
import pasa.cbentley.framework.datamodel.src4.table.ObjectTableModel;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.table.TableView;
import pasa.cbentley.framework.gui.src4.table.interfaces.ITableIconable;
import pasa.cbentley.layouter.src4.engine.PozerFactory;
import pasa.cbentley.layouter.src4.engine.SizerFactory;
import pasa.cbentley.layouter.src4.tech.ITechLayout;

/**
 * A Table of Strings.
 * <br>
 * Select command on String, activates the Command
 * or the Sub Menu
 * <br>
 * A NestedMenu has a parent or null for root
 * <br>
 * <li> Orientation, Vertical or Horizontal
 * <br>
 * Which {@link CmdCtx} to use
 * @author Charles-Philip Bentley
 * @see TableView
 */
public class NestedMenus extends TableView implements IEventConsumer, ICmdsView, ITableIconable, INavTech {

   private StringDrawable   button;

   protected CmdCtx         cc;

   private ObjectTableModel cmdModelActive;

   IDrawable cue;

   private boolean          horiz;

   private boolean          isTemporary;

   private boolean     isVertical;

   private IDrawable        itemKeyFocusBackUp;

   /**
    * Used to 
    */
   private NestedMenus parent;

   private String      title;

   private TableView        tv;

   public NestedMenus(GuiCtx gc, StyleClass sc) {
      super(gc, sc);
   }

   public void addCommand(int cmdid) {
      this.addCommand(gc.getViewCommandListener().getCmd(cmdid));
   }

   public void addCommand(MCmd cmd) {
      cmdModelActive.appendObject(cmd);
   }

   public void addMenu(NestedMenus menu) {
      //we put the name

      //flag nestedMenu to use TableName as Table container in mapper

      cmdModelActive.appendObject(menu);
   }
   //#mdebug

   /**
    * Command called when a pointer is pressed
    * <br>
    * {@link ICmdsCmd#CMD_18_NAV_PRE_SELECT}
    * @param cd
    */
   public void cmdCueSelect(CmdInstanceDrawable cd) {

      this.getCanvas().doCuePress(cd);
   }

   /**
    * From the selected index of the Table. Action
    * @param cd
    */
   public void cmdSelect(CmdInstanceDrawable cd) {

      //remove the cue

      //the select proces
      int selectedIndex = tv.getSelectedIndex();

      Object o = cmdModelActive.getObject(selectedIndex);
      if (o instanceof MCmd) {
         MCmd c = (MCmd) o;
         cmdSelectCmd(cd, c);

      } else if (o instanceof NestedMenus) {
         //we have a menu to display where?
         NestedMenus menu = (NestedMenus) o;

        cmdSelectMenu(cd,menu);
      }
   }

   private void cmdSelectMenu(CmdInstanceDrawable cd, NestedMenus menu) {

      //drawable representing the menu
      IDrawable menuItem = null;
      //position menu relative to 
      if (isVertical()) {
         //TODO if width cannot fit in remaining room. sub menu replaces parent menu visually
         //TODO define room LEFT of a Drawable.. as an ETALON size
         //position on the left or the right.
         //depends on position of menuItem in the ViewContext
         int positionVInsideViewCtx = menuItem.getHCPosInside();
         int dir = 0;
         int alignSrcX = 0;
         int alignDestX = 0;
         if (positionVInsideViewCtx == C.POS_2_LEFT) {
            dir = NAV_4_RIGHT;
            alignSrcX = C.LOGIC_3_BOTTOM_RIGHT;
            alignDestX = C.LOGIC_1_TOP_LEFT;
         } else {
            dir = NAV_3_LEFT;
            alignSrcX = C.LOGIC_1_TOP_LEFT;
            alignDestX = C.LOGIC_3_BOTTOM_RIGHT;
         }
         menuItem.setNavigate(dir, menu, false);
         
         SizerFactory sizerFac = gc.getLAC().getFactorySizer();
         PozerFactory pozerFac = gc.getLAC().getFactoryPozer();
         
         
         ByteObject posX = pozerFac.getPozerNav(alignSrcX, alignDestX, dir);

         ByteObject posXEndOfVC = pozerFac.getPozerEndToEndVC();

         //y we want first to position on the level of the menu item.
         //when preferred size is too big. the menu bump its position
         int alignSrcY = C.LOGIC_1_TOP_LEFT;
         int alignDestY = C.LOGIC_1_TOP_LEFT;

     

         ByteObject posY = pozerFac.getPozerNav(alignSrcY, alignDestY, dir);

         //alternative pozer when the conditions are met. ph bigger than
         ByteObject posYAlt = pozerFac.getPozerInsideVC(C.LOGIC_3_BOTTOM_RIGHT);

         //size of the menu? any size maxed to ViewContext - size of // or custom size by user

         ByteObject sizerVc = sizerFac.getSizerRatio100ViewCtx(100);
         
         //
         
         //max size is ViewContext area from position. size depends on position...
         ByteObject sizerWRoomOnTheLeft = sizerFac.getSizerFromPozer1Pozer2Implicit(posXEndOfVC);

         ByteObject maxH = sizerFac.getSizerFromFunctionOfSizer1Sizer2(sizerVc, null, ITechLayout.ET_FUN_6_DIFF);
         
         ByteObject sizerW = sizerFac.getSizerPref(sizerWRoomOnTheLeft);
         ByteObject sizerH = sizerFac.getSizerPref(maxH);

         menu.setSizers(sizerW, sizerH);
         //when not enough room, display above.. or inside..
         menu.setXY(posX, posY);
      } else {
         //position on the top or bottom
      }
      menu.setParent(this);
      menu.setStateFlag(ITechDrawable.STATE_28_NOT_CONTAINED_IN_PARENT_AREA, true);
      cd.getDExCtx().addDrawn(menu);
   }

   private void cmdSelectCmd(CmdInstanceDrawable cd, MCmd c) {
      //hide menu? if must be hidden
      hideMenu(cd);

    
      //execute with trigger being Select Cmd?
      //add the command to the execution ctx
      cd.queueMenuCmd(c);

      //update trigger for select menued?

      //mark select cmd as navigational... not business
   }

   /**
    * Nav command
    */
   public void commandAction(CmdInstanceDrawable cd) {
      if (cd.getCmdID() == ICmdsCmd.CMD_18_NAV_PRE_SELECT) {
         cmdCueSelect(cd);
      }
   }

   public void consumeEvent(BusEvent e) {
      // TODO Auto-generated method stub

   }

   public Object getTableIcon() {
      return title;
   }

   public String getTitle() {
      return title;
   }

   private void hideMenu(CmdInstanceDrawable cd) {
      if (isTemp()) {
         cd.getDExCtx().addDrawnHide(this);
         if (parent != null) {
            parent.hideMenu(cd);
         }
      }
   }

   public boolean isTemp() {
      return isTemporary;
   }

   public boolean isVertical() {
      return isVertical;
   }

   public void manageNavigate(CmdInstanceDrawable cd, int navEvent) {
      if (navEvent == INavTech.NAV_6_UNSELECT) {
         //give back focus to parent menu
         gc.getFocusCtrl().drawableHide(cd, this, parent);
         //else give back focus to parent drawable
      }
      if (navEvent == INavTech.NAV_5_SELECT) {
         cmdSelect(cd);
      }
      if (isVertical()) {
         if (navEvent == INavTech.NAV_1_UP) {
            //if we are vertical menu
            //ask for a navigation
            tv.navigateUp(cd);
         } else if (navEvent == INavTech.NAV_2_DOWN) {
            //if we are vertical menu
            //ask for a navigation
            tv.navigateDown(cd);
         }
      } else {
         //horizontal
         if (navEvent == INavTech.NAV_3_LEFT) {
            //if we are vertical menu
            //ask for a navigation
            tv.navigateLeft(cd);
         } else if (navEvent == INavTech.NAV_4_RIGHT) {
            //if we are vertical menu
            //ask for a navigation
            tv.navigateRight(cd);
         }
      }

      if (parent != null) {
         boolean b = false;
         //3 possibilities to delegate to parent menu
         if (isVertical()) {
            b = navEvent == INavTech.NAV_3_LEFT || navEvent == INavTech.NAV_4_RIGHT;
         } else {
            b = navEvent == INavTech.NAV_1_UP || navEvent == INavTech.NAV_2_DOWN;
         }
         //was nav command succesfull?
         b = b || cd.isContinue();
         if (b) {
            //ask parent
            parent.manageNavigate(cd, navEvent);
         }
      }

   }

   public void setVertical(boolean b) {
      isVertical = b;
   }

   public void toString(Dctx dc) {
      dc.root(this, "NestedMenus");
      super.toString(dc.newLevel());
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "NestedMenus");
   }
   //#enddebug
}
