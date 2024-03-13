package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.thread.AbstractBRunnable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public abstract class GuiBackgroundTask extends AbstractBRunnable implements Runnable, IStringable {

   protected final GuiCtx         gc;

   protected final RepaintCtrlGui ctrl;

   public GuiBackgroundTask(GuiCtx gc, RepaintCtrlGui ctrl) {
      super(gc.getUC());
      this.gc = gc;
      this.ctrl = ctrl;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GuiBackgroundTask.class, 30);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(ctrl, "ctrl");
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GuiBackgroundTask.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
