package pasa.cbentley.framework.gui.src4.utils;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.core.ui.src4.interfaces.IUserInteraction;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Tracks the rendering time of frames, average
 * <br>
 * 
 * Tracks event time processing
 * @author Charles Bentley
 *
 */
public class RenderMetrics implements IStringable {

   private long lastDuration;

   private int  numPaintCalls;

   protected final GuiCtx gc;

   public RenderMetrics(GuiCtx gc) {
      this.gc = gc;
   }
   public void addFrame(long start, long duration) {
      // TODO Auto-generated method stub
      lastDuration = duration;
   }

   public long getLastDuration() {
      return lastDuration;
   }

   /**
    * A value of the last paint cycle.
    * <br>
    * Animation framework will use this value to diminish the load on the system
    * @return
    */
   public int speedOfPaint() {
      if (lastDuration < 30) {
         return IUserInteraction.SPEED_FAST;
      }
      if (lastDuration < 75)
         return IUserInteraction.SPEED_AVERAGE;
      return IUserInteraction.SPEED_SLOW;
   }


   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "RenderMetrics");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {
      dc.nlVar("lastDuration", (int)lastDuration);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RenderMetrics");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   //#enddebug
   

   
}
