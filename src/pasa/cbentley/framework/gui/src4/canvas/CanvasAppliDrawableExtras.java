package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;

/**
 * Special objects around the canvas such as a menubar
 * @author Charles Bentley
 *
 */
public class CanvasAppliDrawableExtras {

   protected final CanvasAppliDrawable canvas;

   protected final GuiCtx              gc;

   private CmdMenuBar                  menuBar;

   public CanvasAppliDrawableExtras(GuiCtx gc, CanvasAppliDrawable canvas) {
      this.gc = gc;
      this.canvas = canvas;
   }

   public CmdMenuBar getMenuBar() {
      return menuBar;
   }
   
   public void setMenuBar(CmdMenuBar cmd) {
      this.menuBar = cmd;
   }
}
