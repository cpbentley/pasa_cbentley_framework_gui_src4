package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;

/**
 * Controls several {@link OutputStateCanvasGui}
 * 
 * @author Charles Bentley
 *
 */
public class ScreensResultDrawable {
   OutputStateCanvasGui[] repaintDrawables = new OutputStateCanvasGui[5];

   protected final GuiCtx gc;

   public ScreensResultDrawable(GuiCtx gc, int cycle) {
      this.gc = gc;
   }

}
