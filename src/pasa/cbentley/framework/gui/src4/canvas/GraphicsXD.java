package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;

/**
 * Graphics object used in the Drawable framework.
 * 
 * @author Charles Bentley
 *
 */
public class GraphicsXD extends GraphicsX {

   private ExecutionContextCanvasGui ec;

   protected final GuiCtx            gc;

   /**
    * Flags from .
    * Enable flagging of 
    * <li>Painting for screen
    * <li>Painting on a cache
    * <li>Painting for Image
    * <li>Repaint type
    */
   private int                       repaintFlags;

   protected GraphicsXD(GuiCtx gc) {
      super(gc.getDC());
      this.gc = gc;
   }

   protected GraphicsXD(GuiCtx gc, RgbImage rgbImg, boolean isNull) {
      super(gc.getDC(), rgbImg, isNull);
      this.gc = gc;
   }

   /**
    * Defines a mode for write operations over the whole RgbImage
    * The clip area is the area of the image.
    * @param rgbImg image cannot be null
    * @param paintingMode
    */
   protected GraphicsXD(GuiCtx gc, RgbImage rgbImg, int paintingMode) {
      this(gc, rgbImg, paintingMode, 0, 0, rgbImg.getWidth(), rgbImg.getHeight());
   }

   /**
    * Defines a clipping area and mode for write operations over the RgbImage.
    * 
    * Uses the {@link RgbImage#getGraphicsX(int, int, int, int, int)} method
    * 
    * Sets the graphics of {@link RgbImage} 
    * @param rgbImg underlying image
    * @param paintingMode TODO rephase it elsewhere if RgbImage is in RgbMode, painting mode cannot be IMAGE and is automatically
    * set to RGB_IMAGE. That is because RgbImage cannot be put back to Primitive mode with transparent pixels
    * @param x clip of drawing operations
    * @param y
    * @param w
    * @param h
    */
   protected GraphicsXD(GuiCtx gc, RgbImage rgbImg, int paintingMode, int x, int y, int w, int h) {
      super(gc.getDC(), rgbImg, paintingMode, x, y, w, h);
      this.gc = gc;
   }

   public InputStateCanvasGui getInputStateCanvasGui() {
      return ec.getInputStateCanvasGui();
   }

   public OutputStateCanvasGui getOutputStateCanvasGui() {
      return ec.getOutputStateCanvasGui();
   }

   /**
    * Bit flag are decided externally.
    * <br>
    * Default implementation of MasterCanvas.
    * <li> IMaster.SCREENSHOT for example
    * <br>
    * TODO move this method GraphicsXD
    * @param ctx
    * @return
    */
   public boolean hasPaintCtx(int ctx) {
      return BitUtils.hasFlag(repaintFlags, ctx);
   }

   public void setExecutionContextCanvasGui(ExecutionContextCanvasGui ec) {
      this.ec = ec;
   }

   /**
    * 
    * @param paintCtx
    */
   public void setPaintCtx(int paintCtx) {
      repaintFlags = paintCtx;
   }

   /**
    * ITech
    * 
    * Flags from Gui Drawable
    * @param flag
    * @param b
    */
   public void setPaintCtxFlag(int flag, boolean b) {
      repaintFlags = BitUtils.setFlag(repaintFlags, flag, b);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GraphicsXD.class, toStringGetLine(40));
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GraphicsXD.class, toStringGetLine(40));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
