package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public class BusEventGui extends BusEvent {

   protected final GuiCtx gc;
   private InputConfig ic;

   public BusEventGui(GuiCtx gc, InputConfig ic, int pid, int eid) {
      super(gc.getUC(), gc.getEventsBusGui(), pid, eid);
      this.gc = gc;
      this.ic = ic;
   }

   public InputConfig getIC() {
      return ic;
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
