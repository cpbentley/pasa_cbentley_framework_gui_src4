package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.input.Commander;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;

/**
 * A Commander in the context of the {@link GuiCtx}.
 * 
 * An application that use
 * 
 * 
 * @author Charles Bentley
 *
 */
public class CommanderGui extends Commander implements ICmdsGui {

   protected final GuiCtx gc;

   public CommanderGui(GuiCtx gc, int id) {
      super(gc.getCC(), id);
      this.gc = gc;

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CommanderGui.class, 70);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CommanderGui.class, 70);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
