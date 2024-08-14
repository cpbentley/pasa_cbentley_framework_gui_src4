package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.input.src4.engine.CanvasAppliInput;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;

public class PointerGestureDrawable extends GestureDetector {

   protected final GuiCtx gc;

   public PointerGestureDrawable(GuiCtx gc, CanvasAppliInput ctrl) {
      super(gc.getIC(), ctrl);
      this.gc = gc;
   }

}
