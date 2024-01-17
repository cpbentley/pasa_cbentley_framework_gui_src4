package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
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

   public void init() {
      setDw(vc.getWidth());
      setDh(vc.getHeight());
   }

   protected void drawDrawableContent(GraphicsX g, int x, int y, int w, int h) {
      for (int i = 0; i < nextEmpty; i++) {
         if (my[i] != null) {
            my[i].draw(g);
         }
      }
   }

   /**
    * Overrides this method
    */
   public void manageKeyInput(InputConfig ic) {
      for (int i = 0; i < nextEmpty; i++) {
         if (my[i] != null) {
            my[i].manageKeyInput(ic);
         }
      }
   }

   public void managePointerInput(InputConfig ic) {
      for (int i = 0; i < nextEmpty; i++) {
         if (my[i] != null) {
            my[i].managePointerInput(ic);
         }
      }
   }

   public void addDrawable(IDrawable d) {
      my = DrawableArrays.ensureCapacity(my, nextEmpty);
      my[nextEmpty] = d;
   }

}
