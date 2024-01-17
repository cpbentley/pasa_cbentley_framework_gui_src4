package pasa.cbentley.framework.gui.src4.mui;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.layouter.src4.interfaces.IStyler;

/**
 * Adapt the style based on screen densities and screen size.
 * 
 * It associated with a Canvas {@link CanvasGuiContext}
 * <br>
 * A {@link GuiCtx} will have a default styleadapter though.
 * But a canvas may use its own
 * <br>
 * A Swing app can have several style adapter depending on the size of the screen.
 * <br>
 * It modifies the size of certain borders
 * 
 * It is created by the Host Launcher.. provides several adapters
 * 
 * The {@link GuiCtx} has a default singleton, configured by the 
 * 
 * Each canvas may take it or create its own.
 * 
 * @author Charles-Philip Bentley
 *
 */
public class StyleAdapter implements IStyler {

   protected IMFont       z_refFont       = null;

   protected IMFont       z_refFontDebug  = null;

   protected int          z_refSize       = -1;

   protected float        scaleA          = 1;

   public boolean         gradientPlusOne = false;

   public boolean         useGradient     = false;

   public boolean         useFocusStyle   = false;

   public int             gradientIncr    = 1;

   protected final GuiCtx gc;

   public StyleAdapter(GuiCtx gc) {
      this(gc,1.0f);
   }

   public StyleAdapter(GuiCtx gc, float scaleA) {
      this.gc = gc;
      this.scaleA = scaleA;
   }

   /**
    * Non null {@link IMFont}
    * @return
    */
   public IMFont getRefFont() {
      return z_refFont;
   }

   /**
    * This will depends on the Graphical Context.
    * <br>
    * Standard (Etalon) pixel value for adjusting dimensions
    * @return
    */
   public int getRefSize() {
      if (z_refSize > 0)
         return z_refSize;
      return z_refFont.getHeight();
   }

   /**
    * Gradient step just increase one unit after  a given threshold
    * @param size
    * @return
    */
   public int getScaledGradientStep(int size) {
      if (gradientPlusOne) {
         return size + gradientIncr;
      }
      return size;
   }

   public int getScaledMargin(int size) {
      return scaleIt(size);
   }

   public int getScaledPadding(int size) {
      return scaleIt(size);
   }

   public int getScaledSBBGBlockSize(int size) {
      return scaleIt(size);
   }

   public int getScaledSBBlockSize(int size) {
      return scaleIt(size);
   }

   public int getScaledSBBlockWrapper(int size) {
      return scaleIt(size);
   }

   public boolean haveFocusStyles() {
      if (useFocusStyle) {
      }
      return useFocusStyle;
   }

   public boolean isUseGradient(boolean val) {
      if (useGradient) {
         return val;
      }
      return false;
   }

   /**
    * Driver may override this method.
    * 
    * TODO in 
    * @param size
    * @return
    */
   public int getScaledSize(int size) {
      return scaleIt(size);
   }

   public int getScaledArc(int size) {
      return scaleIt(size);
   }

   public int getScaledGridH(int size) {
      return scaleIt(size);
   }

   protected int scaleIt(int size) {
      return (int) Math.floor((double) size + 0.5d);
      //return Math.round(((float) size * scaleA));
   }

   public int getCaretWidth() {
      return scaleIt(1);
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "StyleAdapter");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "StyleAdapter");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }

   //#enddebug

}
