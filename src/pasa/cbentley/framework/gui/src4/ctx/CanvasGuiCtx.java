package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.coredraw.src4.ctx.CoreDrawCtx;
import pasa.cbentley.framework.coreui.src4.ctx.CoreUiCtx;
import pasa.cbentley.framework.coreui.src4.tech.IBOCanvasHost;
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
 * {@link IBOCanvasHost}
 * 
 * @author Charles Bentley
 *
 */
public class CanvasGuiCtx extends ObjectGC {

   protected final CanvasAppliDrawable canvas;

   public CanvasGuiCtx(GuiCtx gc, CanvasAppliDrawable canvas) {
      super(gc);
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
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CanvasGuiCtx.class, 90);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CanvasGuiCtx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   


}
