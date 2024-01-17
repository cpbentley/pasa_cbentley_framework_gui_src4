package pasa.cbentley.framework.gui.src4.utils;

import java.util.TimerTask;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Foreground message queue display at given place on the screen.
 * <br>
 * The message are saved?
 * 
 * @author Charles Bentley
 *
 */
public class RepaintTask extends TimerTask implements IStringable {

   IDrawable              d;

   IDrawable              rel;

   RepaintTask[]          fores;

   int                    id;

   protected final GuiCtx gc;

   public RepaintTask(GuiCtx gc, IDrawable d, int time, RepaintTask[] fores) {
      this.gc = gc;
      this.id = time;
      this.d = d;
      this.fores = fores;
   }

   public void run() {
      id = 0;
      synchronized (fores) {
         for (int i = 0; i < fores.length; i++) {
            if (fores[i] != null) {
               if (fores[i].d == d) {
                  fores[i] = null;
               }
            }
         }
      }
      //#debug
      //toLog().printFlow("#RepaintTask Repainting for erasing Message " + id + " " + Thread.currentThread());

      //request a repaint but on which canvas? all? we don't know which canvas 
      d.getVC().getRepaintCtrlDraw().repaintDrawableCycleBusiness(null);
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "RepaintTask");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RepaintTask");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug

}