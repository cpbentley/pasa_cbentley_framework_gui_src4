package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

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
   public CanvasResultDrawable screenResultCause;

   /**
    * Last Input State that generated the Draw call.
    * <br>
    * When {@link GraphicsXD} is used to draw on an Image ?
    */
   public InputStateDrawable   isd;

   private GuiCtx          gc;

   public GraphicsXD(GuiCtx gc) {
      super(gc.getDC());
      this.gc = gc;
   }

}
