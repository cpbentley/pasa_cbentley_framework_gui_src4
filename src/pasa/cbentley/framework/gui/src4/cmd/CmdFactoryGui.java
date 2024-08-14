package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdFactoryAbstract;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeCanvasDebugger;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeMenuLocation;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public class CmdFactoryGui extends CmdFactoryAbstract {

   protected final GuiCtx gc;

   public CmdFactoryGui(GuiCtx gc) {
      super(gc.getCC());
      this.gc = gc;
   }

   private MCmdGuiChangeCanvasDebugger MCmdGuiChangeCanvasDebugger;

   private MCmdGuiChangeMenuLocation   MCmdGuiChangeMenuLocation;

   public MCmdGuiChangeCanvasDebugger getMCmdGuiChangeCanvasDebugger() {
      if (MCmdGuiChangeCanvasDebugger == null) {
         MCmdGuiChangeCanvasDebugger = new MCmdGuiChangeCanvasDebugger(gc);
      }
      return MCmdGuiChangeCanvasDebugger;
   }

   public MCmdGuiChangeMenuLocation getMCmdGuiChangeMenuLocation() {
      if (MCmdGuiChangeMenuLocation == null) {
         MCmdGuiChangeMenuLocation = new MCmdGuiChangeMenuLocation(gc);
      }
      return MCmdGuiChangeMenuLocation;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CmdFactoryGui.class, 20);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CmdFactoryGui.class, 20);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
