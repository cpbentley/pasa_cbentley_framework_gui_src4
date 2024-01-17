package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public class MEventDrawable extends BusEvent {

   InputConfig    ic;

   private GuiCtx gc;

   public MEventDrawable(GuiCtx gc, InputConfig ic, int pid, int eid) {
      super(gc.getUCtx(), gc.getEventsBusGui(), pid, eid);
      this.gc = gc;
      this.ic = ic;
   }

   public InputConfig getIC() {
      return ic;
   }
}
