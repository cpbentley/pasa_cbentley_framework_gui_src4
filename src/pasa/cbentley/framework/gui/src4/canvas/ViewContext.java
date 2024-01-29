package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.layouter.src4.engine.LayoutableAbstract;
import pasa.cbentley.layouter.src4.interfaces.ILayoutable;

/**
 * 
 * {@link ViewContext} defines an rectangular area controlled by a {@link TopologyDLayer} populated
 * by {@link IDrawable}.
 * 
 * <li> ViewContext is a {@link LayoutableAbstract}
 * 
 * It has a {@link CanvasGuiCtx}
 * 
 * A Container that does not enforce clipping. Animations ?
 * 
 * <li>{@link ILayoutable} whose x,y,w,h is not computed but set externally by the class using it.
 * 
 * It is used as a concept
 * 
 * <li>It can be used as a ruler to control the alignement of other {@link ILayoutable}
 * 
 * As such it can be layouted as well!
 * 
 * @author Charles Bentley
 *
 */
public class ViewContext extends LayoutableAbstract implements IStringable, ILayoutable {

   private CanvasGuiCtx   canvasGC;

   protected final GuiCtx gc;

   private ViewContext    parentVC;

   private TopologyDLayer topo;

   public ViewContext(GuiCtx gc) {
      super(gc.getLAC());
      this.gc = gc;
      engine.setManualOverride(true);
      topo = new TopologyDLayer(gc, this);
   }

   public ViewContext(GuiCtx gc, CanvasGuiCtx canvasGC) {
      super(gc.getLAC());
      this.gc = gc;
      this.canvasGC = canvasGC;
      engine.setManualOverride(true);
      topo = new TopologyDLayer(gc, this);
   }

   /**
    * The {@link CanvasGuiCtx} to which belongs this view
    * @return
    */
   public CanvasAppliInputGui getCanvasDrawable() {
      return canvasGC.getCanvas();
   }

   public CanvasDrawControl getDrawCtrlAppli() {
      return canvasGC.getCanvas().getDrwCtrl();
   }

   public int getHeight() {
      return getSizeDrawnHeight();
   }

   public ILayoutable getLayoutableParent() {
      return parentVC;
   }

   /**
    * Null if none
    * @return
    */
   public ViewContext getParentVC() {
      return parentVC;
   }

   public RepaintCtrlGui getRepaintCtrlDraw() {
      return getCanvasDrawable().getRepaintCtrlDraw();
   }

   /**
    * The total X value based on the chain of {@link ViewContext} Parents.
    * @return
    */
   public int getScreenX() {
      if (parentVC == null) {
         return getX();
      }
      return getX() + parentVC.getScreenX();
   }

   public int getScreenY() {
      if (parentVC == null) {
         return getY();
      }
      return getY() + parentVC.getScreenY();
   }

   public TopologyDLayer getTopo() {
      return topo;
   }

   public int getWidth() {
      return getSizeDrawnWidth();
   }

   /**
    * X pos relative to parent {@link ViewContext}
    */
   public int getX() {
      return getPozeX();
   }

   /**
    * Y pos relative to parent {@link ViewContext} or the Screen is null
    */
   public int getY() {
      return getPozeY();
   }

   public void setNewSize(int nw, int nh) {
      engine.setOverrideW(nw);
      engine.setOverrideH(nh);
   }

   public void setHeight(int h) {
      engine.setOverrideH(h);
   }

   public void setParentVC(ViewContext vc) {
      parentVC = vc;
   }

   public void setTopo(TopologyDLayer topo) {
      if (topo == null) {
         throw new NullPointerException();
      }
      this.topo = topo;
   }

   public void setWidth(int w) {
      engine.setOverrideW(w);
   }

   public void setX(int x) {
      engine.setOverrideX(x);
   }

   public void setY(int y) {
      engine.setOverrideY(y);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ViewContext.class);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvl("Parent", parentVC);

      dc.appendVarWithNewLine("getX()", getX());
      dc.appendVarWithSpace("getY()", getY());
      dc.appendVarWithNewLine("getScreenX()", getScreenX());
      dc.appendVarWithSpace("getScreenY()", getScreenY());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ViewContext.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("Name", toStringName());
   }

   //#enddebug

}
