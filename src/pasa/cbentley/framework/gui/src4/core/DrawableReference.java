package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Encapsulates a possibly null reference to a {@link Drawable}
 * 
 * When null, frame is deemd inactive. 
 * 
 * {@link DrawableReference#isInactive()}
 * 
 * @author Charles Bentley
 *
 */
public class DrawableReference implements IStringable {

   protected final GuiCtx gc;

   private Drawable    drawable;

   public DrawableReference(GuiCtx sc) {
      this.gc = sc;
   }

   public Drawable getDrawable() {
      return drawable;
   }

   public boolean isInactive() {
      return drawable == null;
   }

   public boolean isActive() {
      return drawable != null;
   }

   public void showDrawable(InputConfig ic) {
      if (drawable != null) {
         drawable.shShowDrawableOver(ic);
      }
   }

   public void closeDrawable(InputConfig ic) {
      if (drawable != null) {
         drawable.shHideDrawable(ic);
      }
   }

   public void setDrawable(Drawable frame) {
      this.drawable = frame;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "FrameReference");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "FrameReference");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug

}
