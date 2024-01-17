package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.CanvasAppliInput;
import pasa.cbentley.framework.input.src4.CanvasResult;

public class CanvasResultDrawable extends CanvasResult {
   IDrawable[]       repaintDrawables = new IDrawable[5];

   private int       repaintDrawablesNum;

   private IDrawable screenMsgDrawable;

   public boolean    isOutside;

   protected final GuiCtx gc;

   public CanvasResultDrawable(GuiCtx gc, CanvasAppliInput ctrl, int cycle) {
      super(gc.getIC(), ctrl, cycle);
      this.gc = gc;
   }

   public IDrawable getMessageDrawable() {
      return screenMsgDrawable;
   }

   public void resetAll() {
      super.resetAll();
      isStateStyleDrawablesSet = false;
   }

   /**
    * TODO z ordering
    * @param g
    */
   public void paintDrawables(GraphicsX g) {
      for (int i = 0; i < repaintDrawablesNum; i++) {
         IDrawable d = repaintDrawables[i];
         if (d != null) {
            d.draw(g);
         }
      }
   }

   /**
    * Special repaint on a {@link IDrawable} with an array of integer as parameter.
    * <br>
    * <br>
    * If no other repaint are made that will invalidate this one, when repainted,
    * {@link IDrawable} will check special flag and fetch integer array it stored.
    * <br>
    * This is used to repaint a special part of TableView/Drawable for example that does not
    * implement. I.e. a paramter of the repaint
    * @param is
    */
   public void setActionDoneRepaintSpecial(IDrawable d) {
      repaintDrawables[repaintDrawablesNum] = d;
      repaintDrawablesNum++;
      setFlag(FLAG_01_ACTION_DONE, true);
      setFlag(FLAG_07_SPECIAL, true);
   }

   /**
    * Force a full flush of all cache
    */
   public void srRenewLayout() {
      this.setFlag(FLAG_03_MENU_REPAINT, true);
      this.setFlag(FLAG_04_RENEW_LAYOUT, true);
   }

   /**
    * InputConfig result requires the drawable to be repainted in the next paint cycle.
    * <br>
    * <br>
    * When the {@link ViewDrawable} is set, Controller flag tells wether to repaint content or the whole thing.
    * <br>
    * Impact the {@link Controller#screenResult()} method.
    * <br>
    * <br>
    * When {@link IDrawable} does not have the flag {@link ITechDrawable#STATE_17_OPAQUE}, we force {@link IDrawable#draw(GraphicsX)} to draw
    * <li>itself fully
    * <li>only style layers. 
    * <li>nothing
    * <br>
    * The caller is responsible to know. A TableView will know if it has style layers
    * <br>
    * <br>
    * Therefore for efficiency, having an opaque bg layer is a good thing. Note:Swing assume that by default.
    * <br>
    * <br>
    * <b>Visual Topology</b>
    * <br>
    * When repainting a {@link Drawable} outside the normal painting order that would occur in a full repaint, we must 
    * <li>repaint all above DLayers intersecting/colliding the Drawable area in the {@link MasterCanvas} topology
    * <li>repaint DLayers in {@link Drawable} ParentContainer topology
    * <li>repaint all DLayer between that opaque DLayer and the repaint Drawable DLayer.
    * <br>
    * <br>
    * When repainting a scrollbar, if not opaque, ask ViewPane is opaque? Else ask Container of ViewDrawable to repaint Bg Layers
    * else repaint up in chain until MasterCanvas is reached.
    * <br>
    * <br>
    * Usually an action imply a localized repaint.
    * <br>
    * Selecting an item in a popup,  repaint the update component and the Drawables colliding with the popup area.
    * <br>
    * When colliding is a drawable contained in a Container and nothing else, redraw repaints container background and then the colliding drawables.
    * <br>
    * <br>
    * topology.
    * <br>
    * <br>
    * TODO what about the Graphical Context such as Clip and Translate?
    * <br>
    * Also when the Drawable is moved, previous location must be erased. Create an erase Drawable at position
    * <br>
    * We need a PaintCtx object. the paintctx is updated at each draw, contains speed and stuff like that.
    * -last repainted. flag for erase previous boundary
    * {@link ITechDrawable#ZSTATE_26_MOVED_SINCE_LAST_REPAINT} is set when setXY method is called.
    * False when painted.
    * <br>
    * when a Drawable is lonely repainted, it loads the paint context
    * @param drawable
    */
   public void setActionDoneRepaint(IDrawable drawable) {
      setFlag(FLAG_01_ACTION_DONE, true);
      if (hasResultFlag(FLAG_02_FULL_REPAINT)) {
         return;
      }
      if (drawable != null) {
         //get Drawable's topology and zindex
         TopologyDLayer topology = drawable.getVC().getTopo();
         int zindex = drawable.getZIndex();
         if (topology == null) {
            setFlag(FLAG_02_FULL_REPAINT, true);
            return;
         }
         //check if Drawable is not completely hidden by an opaque DLayer above.
         boolean isHidden = topology.isHidden(zindex, drawable.getX(), drawable.getY(), drawable.getDrawnWidth(), drawable.getDrawnHeight());
         if (isHidden) {
            return;
         }
         //TODO drawable reapint of scrollbar blockbg overlayed by blockfigure.
         boolean isOverlayed = drawable.hasState(ITechDrawable.STATE_27_OVERLAYED);

         repaintDrawables[repaintDrawablesNum] = drawable;
         repaintDrawablesNum++;
         if (repaintDrawablesNum >= repaintDrawables.length) {
            repaintDrawables = DrawableArrays.ensureCapacity(repaintDrawables, repaintDrawablesNum + 2);
         }
         //try to repaint only drawable
         if (drawable.hasState(ITechDrawable.STATE_04_DRAWN_OUTSIDE)) {
            //ask Drawable utilz to compute outside area
            int[] area = DrawableUtilz.computeOutsideArea(drawable);
            //
            //do a topology check on the given Drawable utils to compute full area. if not intersecting with other, non parent drawables.
            //check TBLR drawables intersecting with that area and repaint those as well

            //most common

            //clip on that area and redraw. else full parent drawable repaint
            //check where the area of the full drawn and any drawable intersecting this area will also be repainted.
            throw new RuntimeException("Repaint DrawnOutSide Not Implemented");
         }
         //TODO when repainted? how do you know the translation of G. that translation is only known when the full draw order
         //is respected. That's why X,Y must represent absolute position on screen. a shift occurs when set in PaneDrawable
         if (!drawable.isOpaque()) {
            IDrawable opaque = DrawableUtilz.getFirstNonNullOpaque(drawable);
            //compute parent, canvas or full repaint when too complicated
            if (opaque != null) {
               if (opaque.hasBehavior(ITechDrawable.BEHAVIOR_24_PARENT_CHILD_OVERLAP)) {

                  drawable.setBehaviorFlag(ITechDrawable.BEHAVIOR_13_REPAINT_PARENT_STYLE, true);
               } else {
                  //make a full repaint
                  setFlag(FLAG_02_FULL_REPAINT, true);
                  return;
               }
            } else {
               drawable.setBehaviorFlag(ITechDrawable.BEHAVIOR_14_REPAINT_MASTER_CANVAS, true);
            }
            //in all cases, repaint all above zlayers drawable colliding
         }

         //we must force the repaint of the drawables located below if any.
         //AND
         //we must repaint style layers of the parent if contained.
         //set repaint directive
         //drawable must be repainted in order of zindex
         //how are drawables above repainted? Clipped to Area of Drawable, another special context repaint flag
      }
   }

   public void setActionDoneRepaintMessage(IDrawable msg, int timeout, int anchor) {
      screenMessageTimeOut = timeout;
      screenMessage = "";
      screenMsgDrawable = msg;
      screenMessageAnchor = anchor;
      setFlag(FLAG_01_ACTION_DONE, true);
      setFlag(FLAG_02_FULL_REPAINT, true);
   }

   public void srMenuRepaint() {
      this.setFlag(FLAG_03_MENU_REPAINT, true);
   }

   private boolean isStateStyleDrawablesSet = false;

   /**
    * At least one drawable has been set their state styles with
    * {@link IDrawable#managePointerStateStyle(mordan.controller.InputConfig)}
    * @return
    */
   public boolean isDrawableStatesSet() {
      return isStateStyleDrawablesSet;
   }

   public void setDrawableStates() {
      isStateStyleDrawablesSet = true;
   }

   /**
    * Set to true when pointer event is generated outside the Drawable
    * @param b
    */
   public void setOutsideDrawable(boolean b) {
      isOutside =b;
   }

  

}
