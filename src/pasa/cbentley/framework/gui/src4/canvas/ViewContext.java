package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
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
 * isa {@link LayoutableAbstract}
 * 
 * A Container that does not enforce clipping. Animations ?
 * 
 * {@link ILayoutable} whose x,y,w,h is not computed but set externally by the class using it.
 * 
 * It is used as a concept
 * 
 * It can be used as a ruler to control the alignement of other {@link ILayoutable}
 * 
 * As such it can be layouted as well!
 * 
 * @author Charles Bentley
 *
 */
public class ViewContext extends LayoutableAbstract implements IStringable, ILayoutable {

   protected final GuiCtx      gc;

   private ViewContext         parent;

   private TopologyDLayer      topo;

   private CanvasAppliDrawable cac;

   public ViewContext(GuiCtx gc, CanvasAppliDrawable cac) {
      super(gc.getLAC());
      this.gc = gc;
      this.cac = cac;
      engine.setManualOverride(true);
      topo = new TopologyDLayer(gc, this);
   }

   public void draw(GraphicsX g) {
      topo.drawLayers(g);
   }

   public CanvasDrawControl getDrawCtrlAppli() {
      return cac.getDrwCtrl();
   }

   public int getHeight() {
      return getSizeDrawnHeight();
   }

   public ILayoutable getLayoutableParent() {
      return parent;
   }

   /**
    * The {@link CanvasGuiCtx} to which belongs this view
    * @return
    */
   public CanvasAppliDrawable getCanvasCtx() {
      return cac;
   }

   /**
    * Null if none
    * @return
    */
   public ViewContext getParentVC() {
      return parent;
   }

   public RepaintCtrlDrawable getRepaintCtrlDraw() {
      return cac.getRepaintCtrlDraw();
   }

   public int getScreenX() {
      if (parent == null) {
         return getX();
      }
      return getX() + parent.getScreenX();
   }

   public int getScreenY() {
      int y = getY();
      if (parent == null) {
         //TODO assume it is
         return y;
      }
      return y + parent.getScreenY();
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

   public void newSize(int nw, int nh) {
      engine.setOverrideW(nw);
      engine.setOverrideH(nh);
   }

   public void setHeight(int h) {
      engine.setOverrideH(h);
   }

   public void setParent(ViewContext vc) {
      parent = vc;
   }

   public void setTopo(TopologyDLayer topo) {
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
      dc.nlLvl("Parent", parent);
      
      dc.appendVarWithNewLine("getX()", getX());
      dc.appendVarWithSpace("getY()", getY());
      dc.appendVarWithNewLine("getScreenX()", getScreenX());
      dc.appendVarWithSpace("getScreenY()", getScreenY());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("Name", toStringName());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ViewContext.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
