package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsXFactory;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

public class GraphicsXFactoryGui extends GraphicsXFactory {

   protected final GuiCtx gc;

   public GraphicsXFactoryGui(GuiCtx gc, DrwCtx dc) {
      super(dc);
      this.gc = gc;
   }

   public GraphicsX createGraphicsX() {
      return new GraphicsXD(gc);
   }

   public GraphicsX createGraphicsX(RgbImage rgbImg, boolean isNull) {
      return new GraphicsXD(gc, rgbImg, isNull);
   }

   public GraphicsX createGraphicsX(RgbImage rgbImg, int paintMode) {
      return new GraphicsXD(gc, rgbImg, paintMode);
   }

   public GraphicsX createGraphicsX(RgbImage rgbImg, int paintMode, int x, int y, int w, int h) {
      return new GraphicsXD(gc, rgbImg, paintMode, x, y, w, h);
   }
}
