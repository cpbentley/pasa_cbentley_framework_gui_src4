package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.cmd.src4.input.UserProcessor;
import pasa.cbentley.framework.cmd.src4.interfaces.ITriggers;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * 
 * @author Charles Bentley
 *
 */
public class UserProcessorGui extends UserProcessor implements ITriggers, IStringable {


   public UserProcessorGui(GuiCtx gc) {
      super(gc.getCC());
   }





   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, UserProcessorGui.class, 600);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, UserProcessorGui.class, 600);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug
   

}
