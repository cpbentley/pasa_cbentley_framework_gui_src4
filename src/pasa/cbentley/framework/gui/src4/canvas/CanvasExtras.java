package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.input.src4.CanvasResult;
import pasa.cbentley.framework.input.src4.InputState;

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

   void ctrlUIEvent(InputState ic, CanvasResultDrawable sd) {
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
    * 
    * @param mode
    */
   public void setDebugMode(int mode) {
      if (mode != ITechCanvasDrawable.DEBUG_0_NONE) {
         if (canvasDebug == null) {
            canvasDebug = new CanvasDebugger(gc);
            ViewContext vcRoot = canvas.getVCRoot();
            canvasDebug.setViewContext(vcRoot);
         }
         canvasDebug.setDebugMode(mode);
         int pos = canvas.getCanvasBOHelper().getDebugBarPosition();
         canvasDebug.setOrientation(pos);
         //sets the header with no specific sizer. ViewPane sets its own.
         canvas.getTopoViewDrawable().setHeader(canvasDebug, C.POS_0_TOP, ITechViewPane.PLANET_MODE_0_EAT);
      } else {
         if (canvasDebug != null) {
            //remove it from
            canvas.getTopoViewDrawable().removeDrawable(canvasDebug);
         }
         canvasDebug = null;
      }
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

   public void applySettingsCanvas() {
      CanvasBOHelper settingsHelper = canvas.getCanvasBOHelper();
      boolean useMenuBar = settingsHelper.isUsingMenuBar();
      ByteObject settingsGuiCtx = gc.getBOCtxSettings();
      //define which menu bar to use
      //if only root canvas
      if (useMenuBar) {
         if (cmdMenuBar == null) {
            //TODO the style class will depend on the menu type
            //how can this be static
            cmdMenuBar = new CmdMenuBar(gc, gc.getStyleClass(IUIView.SC_1_MENU));
            cmdMenuBar.getLay().laySiz_Preferred();
         }
         int pos = settingsGuiCtx.get1(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_04_MENU_BAR_POSITION1);
         canvas.getTopoViewDrawable().setHeader(cmdMenuBar, pos, ITechViewPane.PLANET_MODE_0_EAT);
      } else {
         if (cmdMenuBar != null) {
            canvas.getTopoViewDrawable().removeDrawable(cmdMenuBar);
         }
      }
      //update with the menu bar
      this.setMenuBar(cmdMenuBar);

      int debugMode = settingsHelper.getDebugMode();
      setDebugMode(debugMode);
   }

}
