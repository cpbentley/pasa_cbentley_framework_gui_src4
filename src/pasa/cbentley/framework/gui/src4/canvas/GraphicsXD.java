package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;

/**
 * Graphics object used in the Drawable framework.
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class GraphicsXD extends GraphicsX {

   /**
    * 
    */
   public OutputStateCanvasGui screenResultCause;

   /**
    * Last Input State that generated the Draw call.
    * <br>
    * When {@link GraphicsXD} is used to draw on an Image ?
    */
   public InputStateCanvasGui   isd;

   private GuiCtx          gc;

   public GraphicsXD(GuiCtx gc) {
      super(gc.getDC());
      this.gc = gc;
   }

}
