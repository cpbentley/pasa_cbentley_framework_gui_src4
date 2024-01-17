package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.ExecutionCtxDraw;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
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

   public IDrawable getDrawableViewPort(int x, int y, ExecutionCtxDraw ex) {
      return topology.getDrawable(x, y, null);
   }

   public void init() {
      //let the drawable init
      super.init();

      //#mdebug
      String msg = "x=" + this.getContentX() + " y=" + this.getContentY() + " w=" + this.getViewPortContentW() + " h=" + this.getViewPortContentH() + " ";
      toLog().pInit1(msg, this, TopoViewDrawable.class, "init");
      //#enddebug
      //set the viewcontext
      vcContent.setWidth(getViewPortContentW());
      vcContent.setHeight(getViewPortContentH());
      vcContent.setX(getContentX());
      vcContent.setY(getContentY());

   }

   protected void initViewDrawable(LayEngineDrawable ds) {
      //we are not scrollable
      setViewFlag(ITechViewDrawable.VIEW_GENE_28_NOT_SCROLLABLE, true);
      setViewFlag(ITechViewDrawable.VIEWSTATE_10_CONTENT_PW_VIEWPORT_DW, true);
      setViewFlag(ITechViewDrawable.VIEWSTATE_11_CONTENT_PH_VIEWPORT_DH, true);
      //our preferred size depends on the content
      pw = vcContent.getWidth();
      ph = vcContent.getHeight();

      //once finished update the viewcontext
   }

   protected void initViewPortSub(int width, int height) {
      pw = width;
      ph = height;
   }

   public void layoutInvalidate(boolean noParentInvalidate) {
      super.layoutInvalidate(noParentInvalidate);
      topology.invalidateLayout();
   }

   public void manageGestureInput(InputConfig ic) {
      //#debug
      toLog().pFlow("", null, TopoViewDrawable.class, "manageGestureInput@line96", LVL_03_FINEST, true);
      topology.manageGestureInput(ic);
   }

   public void manageInput(InputConfig ic) {
      topology.manageInput(ic);
   }

   public void manageKeyInput(InputConfig ic) {
      //#debug
      toLog().pFlow("" + ic.is.getLastDeviceEvent().toString1Line(), null, TopoViewDrawable.class, "manageKeyInput@line90", LVL_03_FINEST, true);
      topology.manageKeyInput(ic);
   }

   public void manageOtherInput(InputConfig ic) {
      //#debug
      toLog().pFlow("", null, TopoViewDrawable.class, "manageOtherInput@line96", LVL_03_FINEST, true);
      topology.manageOtherInput(ic);
   }

   public void managePointerInput(InputConfig ic) {
      //#debug
      toLog().pFlow("x=" + ic.is.getX() + " y=" + ic.is.getY(), null, TopoViewDrawable.class, "managePointerInput@line96", LVL_03_FINEST, true);
      topology.managePointerInput(ic);
   }

   public void manageRepeatInput(InputConfig ic) {
      //#debug
      toLog().pFlow("", null, TopoViewDrawable.class, "manageRepeatInput@line96", LVL_03_FINEST, true);
      topology.manageRepeatInput(ic);
   }

   public void toString(Dctx sb) {
      sb.root(this, TopoViewDrawable.class, "119");
      sb.appendWithSpace("'" + getDebugName() + "'");
      super.toString(sb.sup());
      sb.nlLvl(topology, "TopologyDLayer for" + getDebugName());
      sb.nlLvl(vcContent, "ViewContext of ViewPort");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, TopoViewDrawable.class);
      dc.appendWithSpace("'" + getDebugName() + "'");
      dc.appendWithSpace(layEngine.toString1Line());
   }
}
