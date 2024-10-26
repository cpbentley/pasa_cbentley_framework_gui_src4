package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.app.AppliGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Maps.
 * 
 * Applis sub class this class and call super on getDrawable.
 * 
 * {@link AppliGui} 
 * 
 * When several application live inside the same?
 * Register to an existing one?
 * 
 * @author Charles Bentley
 *
 */
public class IDToDrawable {

   private GuiCtx gc;

   public IDToDrawable(GuiCtx gc) {
      this.gc = gc;
   }

   public IDrawable getDrawable(int id) {
      if (id == 0) {

      }

      return null;
   }
}
