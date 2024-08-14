package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.exec.OutputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

/**
 * Special objects around the canvas such as a menubar
 * 
 * The goal is to make {@link CanvasAppliInputGui} more readable
 * 
 * @author Charles Bentley
 *
 */
public class CanvasExtras extends ObjectGC {

   protected final CanvasAppliInputGui canvas;

   private CanvasDebugger              canvasDebug;

   private CmdMenuBar                  cmdMenuBar;

   public CanvasExtras(GuiCtx gc, CanvasAppliInputGui canvas) {
      super(gc);
      this.canvas = canvas;
   }

   public CanvasBOHelper getCanvasBOHelper() {
      return canvas.getCanvasBOHelper();
   }

   public void setMenuBarMode(boolean useMenuBar, int pos, CmdInstanceGui cd) {
      //if only root canvas
      if (useMenuBar) {
         if (cmdMenuBar == null) {
            //TODO the style class will depend on the menu type
            //how can this be static
            cmdMenuBar = new CmdMenuBar(gc, gc.getStyleClass(IUIView.SC_1_MENU));
            cmdMenuBar.getLay().laySiz_Preferred();
         }
         canvas.getTopoViewDrawable().setHeader(cmdMenuBar, pos, ITechViewPane.PLANET_MODE_0_EAT);
      } else {
         if (cmdMenuBar != null) {
            canvas.getTopoViewDrawable().removeDrawable(cmdMenuBar);
         }
      }
      //update with the menu bar
      this.setMenuBar(cmdMenuBar);
   }

   void ctrlUIEvent(InputState ic, OutputStateCanvasGui sd) {
      if (canvasDebug != null) {
         sd.setActionDoneRepaint(canvasDebug);
      }
   }

   public CanvasDebugger getDebugCanvas() {
      return canvasDebug;
   }

   public CmdMenuBar getMenuBar() {
      return cmdMenuBar;
   }

   void paintCanvasContent(GraphicsXD g, long time) {
      if (canvasDebug != null) {
         canvasDebug.paintDebugComplete(g, time);
      }
   }

   public void renderDebugEnd(GraphicsXD g) {
      if (canvasDebug != null) {
         canvasDebug.debugRender1End(g, canvas.getRenderMetrics());
      }
   }

   public void renderDebugStart(GraphicsXD g) {
      if (canvasDebug != null) {
         canvasDebug.debugRender1Start(g);
      }
   }
   //#enddebug

   /**
    * We change the state of the view.
    * 
    * 2 possibilities
    *  command unique purpose is to change the debug mode
    *  command is part of a command chain. doing other things before and after 
    * @param mode
    */
   public void setDebugMode(int mode, CmdInstanceGui cd) {

   }

   public void setMenuBar(CmdMenuBar cmd) {
      this.cmdMenuBar = cmd;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CanvasExtras.class, 50);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CanvasExtras.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

}
