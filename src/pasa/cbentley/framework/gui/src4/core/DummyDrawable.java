package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;

/**
 * Simply Draws Drawables.
 * Just Forward calls to drawables inside. Uses the ViewContext as
 * <br>
 * <br>
 * It is not supposed be used as a reference.
 * 
 * @author Charles Bentley
 *
 */
public class DummyDrawable extends Drawable {

   IDrawable[] my        = new IDrawable[5];

   private int nextEmpty = 0;

   public DummyDrawable(GuiCtx gc, StyleClass sc, ViewContext vc) {
      super(gc, sc);
      setViewContext(vc);
   }

   public void addDrawable(IDrawable d) {
      my = DrawableArrays.ensureCapacity(my, nextEmpty);
      my[nextEmpty] = d;
   }

   protected void drawDrawableContent(GraphicsXD g, int x, int y, int w, int h) {
      for (int i = 0; i < nextEmpty; i++) {
         if (my[i] != null) {
            my[i].draw(g);
         }
      }
   }

   public void initSize() {
      setDw(vc.getWidth());
      setDh(vc.getHeight());
   }

   /**
    * Overrides this method
    */
   public void manageKeyInput(ExecutionContextCanvasGui ec) {
      for (int i = 0; i < nextEmpty; i++) {
         if (my[i] != null) {
            my[i].manageKeyInput(ec);
         }
      }
   }

   public void managePointerInput(ExecutionContextCanvasGui ec) {
      for (int i = 0; i < nextEmpty; i++) {
         if (my[i] != null) {
            my[i].managePointerInput(ec);
         }
      }
   }

}
