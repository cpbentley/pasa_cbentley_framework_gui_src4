package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechViewDrawable;

/**
 * Container for a {@link ViewContext} and a {@link TopologyDLayer}
 * <br>
 * 
 * Creates a new {@link ViewContext} and its associated {@link TopologyDLayer}
 * 
 * @author Charles Bentley
 *
 */
public class TopoViewDrawable extends ViewDrawable {

   private TopologyDLayer topology;

   /**
    * Don't confuse it with {@link ViewContext} of the Drawable
    */
   private ViewContext    vcContent;

   /**
    * 
    * @param gc
    * @param sc
    * @param vc
    * @param vcContent
    */
   public TopoViewDrawable(GuiCtx gc, StyleClass sc, ViewContext vc, ViewContext vcContent) {
      super(gc, sc, vc);
      this.vcContent = vcContent;
      this.topology = vcContent.getTopo();
   }

   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scx, ScrollConfig scy) {
      //to be subclassed
      topology.drawLayers(g);
   }

   public IDrawable getDrawableViewPort(int x, int y, ExecutionContextCanvasGui ec) {
      return topology.getDrawable(x, y, ec);
   }

   public ViewContext getViewContextContent() {
      return vcContent;
   }

   public void initSize() {
      //let the drawable init
      super.initSize();

      //#mdebug
      String msg = "x=" + this.getContentX() + " y=" + this.getContentY() + " w=" + this.getViewPortContentW() + " h=" + this.getViewPortContentH() + " ";
      toDLog().pInit1(msg, this, TopoViewDrawable.class, "initSize@63");
      //#enddebug
      //set the viewcontext
      vcContent.setWidth(getViewPortContentW());
      vcContent.setHeight(getViewPortContentH());
      vcContent.setX(getContentX());
      vcContent.setY(getContentY());

   }

   protected void initViewDrawable(LayouterEngineDrawable ds) {
      //we are not scrollable
      setFlagGene(ITechViewDrawable.FLAG_GENE_28_NOT_SCROLLABLE, true);
      setFlagView(ITechViewDrawable.FLAG_VSTATE_10_CONTENT_PW_VIEWPORT_DW, true);
      setFlagView(ITechViewDrawable.FLAG_VSTATE_11_CONTENT_PH_VIEWPORT_DH, true);
      //our preferred size depends on the content
      int pw = vcContent.getWidth();
      int ph = vcContent.getHeight();
      layEngine.setPw(pw);
      layEngine.setPh(ph);

      //once finished update the viewcontext
   }

   protected void initViewPortSub(int width, int height) {
      int pw = width;
      int ph = height;
      layEngine.setPw(pw);
      layEngine.setPh(ph);
   }

   public void layoutInvalidate(boolean noParentInvalidate) {
      super.layoutInvalidate(noParentInvalidate);
      topology.invalidateLayout();
   }

   public void manageGestureInput(ExecutionContextCanvasGui ec) {
      //#debug
      toDLog().pFlow("", null, TopoViewDrawable.class, "manageGestureInput@line96", LVL_03_FINEST, true);
      topology.manageGestureInput(ec);
   }

   public void manageInput(ExecutionContextCanvasGui ec) {
      topology.manageInput(ec);
   }

   public void manageKeyInput(ExecutionContextCanvasGui ec) {
      //#debug
      toDLog().pFlow("" + ec.getInputStateDrawable().getLastDeviceEvent().toString1Line(), null, TopoViewDrawable.class, "manageKeyInput@line90", LVL_03_FINEST, true);
      topology.manageKeyInput(ec);
   }

   public void manageOtherInput(ExecutionContextCanvasGui ec) {
      //#debug
      toDLog().pFlow("", null, TopoViewDrawable.class, "manageOtherInput@line96", LVL_03_FINEST, true);
      topology.manageOtherInput(ec);
   }

   public void managePointerInput(ExecutionContextCanvasGui ec) {
      //#debug
      toDLog().pFlow("x=" + ec.getInputStateDrawable().getX() + " y=" + ec.getInputStateDrawable().getY(), null, TopoViewDrawable.class, "managePointerInput@line96", LVL_03_FINEST, true);
      topology.managePointerInput(ec);
   }

   public void manageRepeatInput(ExecutionContextCanvasGui ec) {
      //#debug
      toDLog().pFlow("", null, TopoViewDrawable.class, "manageRepeatInput@line96", LVL_03_FINEST, true);
      topology.manageRepeatInput(ec);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, TopoViewDrawable.class, 136);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvl(topology, "TopologyDLayer for " + toStringGetName());
      dc.nlLvl(vcContent, "ViewContext of ViewPort");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, TopoViewDrawable.class,143);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendWithSpace("'" + toStringGetName() + "'");
   }
   //#enddebug

}
