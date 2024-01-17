package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.thread.AbstractBRunnable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public abstract class GuiBackgroundTask extends AbstractBRunnable implements Runnable, IStringable {

   
   protected final GuiCtx gc;
   protected final RepaintCtrlDrawable ctrl;

   public GuiBackgroundTask(GuiCtx gc, RepaintCtrlDrawable ctrl) {
      super(gc.getUCtx());
      this.gc = gc;
      this.ctrl = ctrl;
      
   }
   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "GuiBackgroundTask");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "GuiBackgroundTask");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug
   

}
