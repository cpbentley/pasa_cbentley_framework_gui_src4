package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.INavTech;
import pasa.cbentley.framework.cmd.src4.interfaces.IStringsCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.core.ui.src4.tech.IInput;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.INavigational;

/**
 * A command used to navigate between {@link INavigational}.
 * <br>
 * <br>
 * In a Menu, this command will ask for a parameters. up/down/ but it is incomplete.
 * 
 * <br>
 * Depending on the {@link CmdCtx}, send the Nav command to the right
 * {@link INavigational}.
 * <br>
 * <li> Menu Context send the Command Event to the Key Navigatinal
 * <li> PointerOver Context sends the command to PointerOver Navigational
 * <li> Pointer context send the command (usually SELECT) to the Base Drawable
 * so that it selects which Drawable to put into Focus.
 * <br>
 * <br>
 * Case of the Text Editor in Android
 * <li> Single Pointer Typed selected inside text
 * <li> Single Pointer Press Long Press Selects Word and  shows a contextual menu
 * <li> Single Pointer Press and Drag activate Scrollbar navigation
 * 
 * If a keyboard was connected u and down would?
 * <li> {@link INavTech#NAV_1_UP}
 * <li> {@link INavTech#NAV_2_DOWN}
 * <li> {@link INavTech#NAV_3_LEFT}
 * <li> {@link INavTech#NAV_4_RIGHT}
 * <li> {@link INavTech#NAV_3_LEFT}
 * So over the ViewDrawable we have a pointer context for wheel up and dragging.
 * <br>
 * When a {@link ViewPane} appears, the Navigational {@link CmdCtx} is added to the Context of the {@link ViewDrawable}.
 * Removed when ViewPane disappears. Tested at each Init. It creates a Branch
 * 
 * <br>
 * <br>
 * <b>The Context Menu of a Scrollbar</b>
 * <br>
 * <br>
 * When right click on a Scrollbar one can see the command
 * <li>Scroll Here
 * <li>Top (home) 
 * <li>Bottom (home)
 * <li>Page Up 
 * <li>Page Down 
 * <li>Scroll Up  
 * <li>Scroll Down 
 * <br>
 * <br>
 * Every items is an instance of the {@link NavigationCmd} initialized with a parameter 
 * <li> {@link INavTech#NAV_1_UP}
 * <li> {@link INavTech#NAV_2_DOWN}
 * And the page/scroll is relative to the fact we nagivate on a Scroll bar. So the ShowCtxMenu
 * command will provide the context to the {@link NavigationCmd} so it can get the right
 * String key 
 * and a context  command
 * @author Charles Bentley

 */
public class NavigationCmd extends MCmdAbstract implements INavTech {

   protected final GuiCtx gc;

   /**
    * Generic Nav command where direction will be computed from the trigger.
    * There is no label to this command
    * Label 
    * @param cc
    * @param labelPointer
    */
   public NavigationCmd(GuiCtx gc) {
      super(gc.getCC(), 0);
      this.gc = gc;
      labelID = IStringsCmd.STR_CMD_050_NAVIGATE;
   }

   /**
    * Name of command depends on direction.
    * <br>
    * Scrolling Down ?
    * The final name?
    * @param cc
    * @param direction
    */
   public NavigationCmd(GuiCtx gc, int direction) {
      super(gc.getCC(), 0);
      this.gc = gc;
      this.param = direction;
   }

   /**
    * 
    * @param gc
    * @param direction
    * @param cmdID
    */
   public NavigationCmd(GuiCtx gc, int direction, int cmdID) {
      super(gc.getCC(), 0);
      this.gc = gc;
      int[] labelsID = new int[2];
      labelsID[0] = 0; //unknown yet. might be scroll/page/move will depends on context
      switch (cmdID) {
         case ICmdsCmd.CMD_11_NAV_UP:

            break;

         default:
            break;
      }
      labelID = IStringsCmd.STR_CMD_050_NAVIGATE;
   }

   /**
    * <ol>
    * <li> Finds which {@link INavigational} should process the command.
    * <ol>
    * <li>That depends on the Trigger. Menu, Key or Pointer
    * </ol>
    * </ol>
    */
   public void cmdExecuteFinal(CmdInstanceGui cd) {

      //check 
      FocusCtrl fc = gc.getFocusCtrl();
      //when command activated through app menu. or contextual menu
      //
      IDrawable nav = null;

      //navigation command was selected in a Menu
      if (ci.getTrigger() == cc.getMenuTrigger()) {
         nav = fc.getItemInKeyFocus();
      } else if (isPointerTrigger()) {
         //how do we know here what nav
         nav = fc.getItemInPointerFocus();
      } else {
         //
         nav = fc.getItemInKeyFocus();
      }
      int direction = getNavDir(cd);

      //when item is not navigational like a cell in a TableView? all drawables are navigational
      //
      if (nav instanceof INavigational) {
         INavigational navi = (INavigational) nav;
         boolean b = navi.canNavigate(direction);
         if (b) {
            nav.manageNavigate(cd, direction);
         }
      } else {
         //ask parent?
         IDrawable pa = nav.getParent();

      }
      if (direction == NAV_6_UNSELECT) {
         IDrawable d = nav.getParent();
         //give back focus to the parent, the parent knowing focus is coming from down under.
         //therefore does not fire up init state due to first life focus.
         d.manageNavigate(cd, NAV_7_SELECT_FROM);

      } else {
      }

   }

   public void cmdExecuteFinal(CmdInstance cd) {
      cmdExecuteFinal((CmdInstanceGui) cd);
   }

   public void cmdExecuteParam() {
      // TODO Auto-generated method stub

   }

   public void cmdRedo() {
      // TODO Auto-generated method stub

   }

   public void cmdUndo() {
      // TODO Auto-generated method stub

   }

   public MCmdAbstract createInstance(CmdInstance ci) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Compute Label based on command instance because command
    * is without params.
    * <br>
    * user readable string of what the navigation command is about
    * Scrolling?
    * @param ci
    */
   public int[] getLabelID(CmdInstanceGui cd) {
      int direction = getNavDir(cd);
      switch (direction) {
         case NAV_6_UNSELECT:

            break;

         default:
            break;
      }

      return null;
   }

   private int getNavDir(CmdInstanceGui cd) {
      int direction = param;
      //do we have a direction associated?
      if (!hasFlag(FLAG_13_PARAMED)) {
         //read the CmdInstance trigger
         InputConfig ic = cd.getIC();
         InputState is = ic.getInputStateDrawable();
         if (is.isTypeDevicePointer()) {
            if (is.getKeyCode() == ITechCodes.PBUTTON_0_DEFAULT) {
               direction = NAV_5_SELECT;
            } else if (is.getKeyCode() == ITechCodes.PBUTTON_3_WHEEL_UP) {
               direction = NAV_1_UP;
            } else if (is.getKeyCode() == ITechCodes.PBUTTON_4_WHEEL_DOWN) {
               direction = NAV_2_DOWN;
            }
         } else if (is.isTypeDeviceKeyboard()) {
            int key = is.getKeyCode();
            switch (key) {
               case ITechCodes.KEY_DOWN:
                  direction = NAV_2_DOWN;
                  break;
               case ITechCodes.KEY_UP:
                  direction = NAV_1_UP;
                  break;
               case ITechCodes.KEY_LEFT:
                  direction = NAV_3_LEFT;
                  break;
               case ITechCodes.KEY_RIGHT:
                  direction = NAV_4_RIGHT;
                  break;
               case ITechCodes.KEY_ESCAPE:
                  direction = NAV_6_UNSELECT;
                  break;
               case ITechCodes.KEY_FIRE: {
                  //TODO unselect?
                  direction = NAV_5_SELECT;
                  break;

               }
               default:
                  break;
            }
         }
      }
      return direction;
   }

   /**
    * True when last element is a pointer trigger
    * @return
    */
   public boolean isPointerTrigger() {
      int val = ci.getTrigger().getLastTriggerCode();
      int type = CmdTrigger.getUnitType(val);
      return type == IInput.DEVICE_1_MOUSE || type == IInput.DEVICE_3_FINGER;
   }

   public void toString(Dctx sb) {
      sb.root(this, NavigationCmd.class, 289);
      if (!hasFlag(FLAG_13_PARAMED)) {

      } else {
         //parameter 
         sb.append("Parameter From Trigger");
      }
      super.toString(sb.sup());

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "NavigationCmd");
      super.toString1Line(dc.sup());
   }
}
