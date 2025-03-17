package pasa.cbentley.framework.gui.src4.nav;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechNavCmd;
import pasa.cbentley.framework.cmd.src4.nav.INavigational;
import pasa.cbentley.framework.cmd.src4.nav.MCmdMove;
import pasa.cbentley.framework.cmd.src4.nav.MCmdNav;
import pasa.cbentley.framework.gui.src4.canvas.FocusCtrl;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.core.ViewPane;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * A command used to navigate between {@link INavigationalGui}.
 * <br>
 * <br>
 * 
 * {@link MCmdNav} and {@link MCmdMove} ?
 * 
 * In a Menu, this command will ask for a parameters. up/down/ but it is incomplete.
 * 
 * <br>
 * Depending on the {@link CmdCtx}, send the Nav command to the right
 * {@link INavigationalGui}.
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
 * <li> {@link ITechNavCmd#NAV_01_UP}
 * <li> {@link ITechNavCmd#NAV_02_DOWN}
 * <li> {@link ITechNavCmd#NAV_03_LEFT}
 * <li> {@link ITechNavCmd#NAV_04_RIGHT}
 * <li> {@link ITechNavCmd#NAV_03_LEFT}
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
 * Every items is an instance of the {@link MCmdNavGui} initialized with a parameter 
 * <li> {@link ITechNavCmd#NAV_01_UP}
 * <li> {@link ITechNavCmd#NAV_02_DOWN}
 * And the page/scroll is relative to the fact we nagivate on a Scroll bar. So the ShowCtxMenu
 * command will provide the context to the {@link MCmdNavGui} so it can get the right
 * String key 
 * and a context  command
 * @author Charles Bentley

 */
public class MCmdNavGui extends MCmdNav implements ITechNavCmdGui {

   protected final GuiCtx gc;

   /**
    * Name of command depends on direction.
    * <br>
    * Scrolling Down ?
    * The final name?
    * @param cc
    * @param direction
    */
   public MCmdNavGui(GuiCtx gc, int cmdId, int direction, int labelId) {
      super(gc.getCC(), cmdId, direction, labelId);
      this.gc = gc;
   }

   public INavigational getNavigationalFor(CmdInstance ci) {

      INavigational nav = super.getNavigationalFor(ci);

      if (nav == null) {
         nav = getNavigationalForGui((CmdInstanceGui) ci);
      }
      return nav;
   }

   private INavigational getNavigationalForGui(CmdInstanceGui ci) {
      //check 
      FocusCtrl fc = gc.getFocusCtrl();
      //when command activated through app menu. or contextual menu
      //
      IDrawable nav = null;

      //navigation command was selected in a Menu
      if (ci.getTrigger() == cc.getMenuTrigger()) {
         nav = fc.getDrawableInKeyFocus();
      } else if (isPointerTrigger()) {
         //how do we know here what nav
         nav = fc.getItemInPointerFocus();
      } else {
         //
         nav = fc.getDrawableInKeyFocus();
      }
      //ask parent?
      IDrawable pa = nav.getParent();

      return null;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, MCmdNavGui.class, toStringGetLine(130));
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, MCmdNavGui.class, toStringGetLine(130));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
