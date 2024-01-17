package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Controls several {@link CanvasResultDrawable}
 * 
 * @author Charles Bentley
 *
 */
public class ScreensResultDrawable {
   CanvasResultDrawable[] repaintDrawables = new CanvasResultDrawable[5];

   protected final GuiCtx gc;

   public ScreensResultDrawable(GuiCtx gc, int cycle) {
      this.gc = gc;
   }

}
