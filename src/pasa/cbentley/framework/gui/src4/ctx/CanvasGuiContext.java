package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.framework.coredraw.src4.ctx.CoreDrawCtx;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.coreui.src4.tech.ITechCanvasHost;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliDrawable;
import pasa.cbentley.framework.gui.src4.ctx.config.IConfigCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.mui.StyleAdapter;
import pasa.cbentley.framework.input.src4.ctx.InputCtx;

/**
 * 
 * Created for each Canvas
 * 
 * {@link GuiCtx} singletons are components shared by all canvases in a the {@link GuiCtx} of an application.
 * 
 * Dissociated from {@link CanvasAppliDrawable} implementation so that you can have different implementations
 * 
 * Singletons that belong to a Canvas, are located in this class.
 * 
 * THe configuration of the {@link InputCtx} decides what is the default canvas settings
 * {@link ITechCanvasHost}
 * 
 * @author Charles Bentley
 *
 */
public class CanvasGuiContext  {

   protected final CanvasAppliDrawable canvas;

   protected final GuiCtx     gc;

  
   public CanvasGuiContext(IConfigCanvasGui config, GuiCtx gc, CanvasAppliDrawable canvas) {
      this.gc = gc;
      this.canvas = canvas;
   }

   public void exitInputContext() {
      canvas.getIC().exitInputContext();
   }


   public CanvasAppliDrawable getCanvas() {
      return canvas;
   }

   public CoreDrawCtx getCDC() {
      return null;
   }

   public int getCtxID() {
      return 1222;
   }

   public CoreUiCtx getCUC() {
      return null;
   }


   public GuiCtx getGC() {
      return gc;
   }

   public CmdMenuBar getMenuBar() {
      return null;
   }

   public IDrawable getRootDrawable() {
      return canvas.getRoot();
   }
   

}
