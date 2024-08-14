package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;

public class BusEventGui extends BusEvent {

   protected final GuiCtx gc;
   private ExecutionContextCanvasGui ec;

   public BusEventGui(GuiCtx gc, ExecutionContextCanvasGui ec, int pid, int eid) {
      super(gc.getUC(), gc.getEventsBusGui(), pid, eid);
      this.gc = gc;
      this.ec = ec;
   }

   public ExecutionContextCanvasGui getExecutionContext() {
      return ec;
   }
   public GuiCtx getGC() {
      return gc;
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, BusEventGui.class, 25);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BusEventGui.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
