package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.core.src4.utils.Geo2dUtils;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IAnimable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.InputState;
import pasa.cbentley.framework.input.src4.ctx.IFlagsToStringInput;

/**
 * Controller class. Provides the services of a Pindrawable and more! The menu bar is used to control the active layer
 * <br>
 * <br>
 * 
 * Provides an understanding of relationships between a logical set of {@link IDrawable}.
 * <br>
 * <br>
 * <li>Contained : {@link IDrawable#getParent()}
 * <li>Z Order: different layers of Drawable
 * <li>TBLR: relationships between drawable on different layers
 * <li>Selection: Selectability
 * <li>UnSelectability: hiding layer and go to next layer.
 * <li>Boundary service: Repaint a layer equal repaint all layers?
 * <br>
 * <br>
 * The Root {@link TopologyDLayer} is used by the {@link MasterCanvas}. {@link TopLevelCtrl} will always replace when a new TopLevel is
 * shown.
 * <br>
 * {@link PinBoardDrawable} or other {@link IDrawable} using a {@link TopologyDLayer} are children of the root {@link TopologyDLayer}.
 * <br>
 * <br>
 * Navigation events are working according to the <b>Bottom Up Model</b>:
 * <br>
 * When a {@link IDrawable} <b>D</b> has the focus and recieves a {@link IDrawable#NAV_3_RIGHT}.
 * <br>
 * <b>case 1:</b>
 * <br>
 * <li>If no horizontal navigation, delegates to {@link IDrawable} up in the hierarchy
 * <br>
 * <b>case 2:</b>
 * <br>
 * <li>If inside, and navigation reached the end of the right navigation, it calls nav Out Right at the topology of {@link IDrawable} up
 * in the hierarchy.
 * <li>Topology decides if there is a focusable {@link Drawable} right of <b>D</b>
 * <br>
 * <br>
 * <b>Visual Topology</b> also provides boundary services.
 * <br>
 * <br>
 * <br>
 * <br>
 * <b>Navigational Topology</b>
 * <br>
 * <br>
 * Some Parent drawables have their own knowledge of their children topology. The {@link TableView} controls the TBLR relationship of its cells.
 * <br>
 * <br>
 * Repainting a Drawable is repainting
 * <li>bottommost opaque layer fully containing Drawable area
 * <li>drawable
 * <li>layers above Drawable intersecting drawable area
 * 
 * <br>
 * What is Parent container Drawable? Is it same ZIndex?
 * <br>
 * The root topology is the one maintained by {@link MasterCanvas}.
 * <br>
 * A Drawable belongs to a topology.
 * <br>
 * May a Drawable overlap 2 topologies?
 * <br>
 * Is any Parent the creator of a Topology? {@link TableView} manages its topolgy
 * <br>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class TopologyDLayer implements IStringable {

   private int            dlayerNextEmpty;

   private IDrawable[]    dlayers             = new IDrawable[10];

   protected final GuiCtx gc;

   private IDrawable[]    intercepts          = new IDrawable[10];

   private int            nextEmptyIntercepts = 0;

   /**
    * Number of Drawables in background Back
    */
   private int            numBgs              = 0;

   private ViewContext    vc;

   public TopologyDLayer(GuiCtx gc, ViewContext vc) {
      this.gc = gc;
      this.vc = vc;
   }

   public ViewContext getVC() {
      return vc;
   }

   /**
    * Set a {@link IDrawable} as background.
    * <br>
    * This layer will not be removed or replace by method {@link TopologyDLayer#addDLayer(IDrawable, int)}
    * <br>
    * @param view
    */
   public synchronized void addBackground(IDrawable view) {
      numBgs++;
      addTo(view, numBgs);
   }

   /**
    * Deals with the visuals of {@link Drawable} transition. 
    * <br>
    * <br>
    * 
    * <br>
    * <br>
    * <b>Type</b>:
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_0_REPLACE}
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_1_OVER}
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_2_OVER_INACTIVE}
    * <br>
    * In case of Replace, keep an history for the {@link CmdController#CMD_HISTORY_BACK} to work on.
    * Replaced {@link IDrawable} gets hidden (exit animation) while new {@link IDrawable} is drawn (entry animation)
    * <br>
    * <br>
    * Topology
    * <br>
    * @param view
    * @param type
    * 
    */
   public void addDLayer(IDrawable view, int type) {
      switch (type) {
         case ITechCanvasDrawable.SHOW_TYPE_0_REPLACE:
            //active exit animation of old drawable.
            DrawableUtilz.aboutToHide(dlayers[0]);
            //just add the child as the root
            dlayers[0] = view;
            dlayerNextEmpty = 1;
            //about the be shown
            DrawableUtilz.aboutToShow(view);
            break;
         case ITechCanvasDrawable.SHOW_TYPE_1_OVER:
            addEnsureLayer(view);
            //about the be shown
            DrawableUtilz.aboutToShow(view);
            break;
         case ITechCanvasDrawable.SHOW_TYPE_2_OVER_INACTIVE:
            dlayers[dlayerNextEmpty] = view;
            dlayerNextEmpty++;
            for (int i = 0; i < dlayerNextEmpty; i++) {
               dlayers[i].setStateStyle(ITechDrawable.STATE_19_HIDDEN_OVER, true);
            }
            break;
         default:
            throw new IllegalArgumentException();
      }
   }

   /**
    * 
    * @param d
    */
   private void addEnsureLayer(IDrawable d) {
      int id = isContained(d);
      if (id == -1) {
         physicalAdd(d);
      } else {
         //#debug
         toDLog().pNull("Drawable already contained " + d.toString1Line(), this, TopologyDLayer.class, "addEnsureLayer", LVL_05_FINE, true);
         removeDrawableNoAnimHooks(d);
         physicalAdd(d);
      }
   }

   /**
    * 
    * @param d
    */
   public void addInterceptor(IDrawable d) {
      if (!hasInterceptor(d)) {
         intercepts[nextEmptyIntercepts] = d;
         nextEmptyIntercepts++;
      }
   }

   private synchronized void addTo(IDrawable d, int index) {
      dlayers = DrawableArrays.ensureCapacity(dlayers, dlayerNextEmpty + 1);
      for (int i = dlayerNextEmpty - 1; i >= index; i--) {
         dlayers[i + 1] = dlayers[i];
      }
      dlayers[index] = d;
      dlayerNextEmpty++;
   }

   /**
    * Paint DLayer {@link Drawable}.
    * <br>
    * <br>
    * <br>
    * This method might take a long time to draw depending on the complexity and caching services of the {@link Drawable}s.
    * <br>
    * When Top DLayer is a {@link Drawable} that has the state {@link ITechDrawable#STATE_11_ANIMATING},
    * and layers below are not, {@link MasterCanvas} take a cache of all DLayers,
    * until a {@link ScreenResult} invalidates that cache, a layer is removed or added.
    * <br>
    * All Animation are hosted inside a {@link IDrawable}. One {@link IAnimable} is mimicking the {@link Sprite}
    * <br>
    * <br>
    * @param g
    */
   public void drawLayers(GraphicsX g) {
      //#debug
      toDLog().pDraw("#numLayers:" + dlayerNextEmpty, this, TopologyDLayer.class, "drawLayers", LVL_05_FINE, true);

      g.setTranslationShift(vc.getX(), vc.getY());
      g.clipSet(0, 0, vc.getWidth(), vc.getHeight());
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {

            //#mdebug
            if (gc.getIC().toStringHasToStringFlag(IFlagsToStringInput.Debug_16_RootDrawableDLayers)) {
               //#debug
               toDLog().pDraw("TopologyDLayer#" + (i + 1), dlayers[i], TopologyDLayer.class, "drawLayers", LVL_05_FINE, false);
            }
            //#enddebug

            //any drawable using getWidth and getHeight will update automatically
            dlayers[i].draw(g);
            g.drawPostponed();
         }
      }
      g.setTranslationShift(-vc.getX(), -vc.getY());

   }

   public void drawLayersExclude(GraphicsX g, IDrawable exclude) {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {
            IDrawable d = dlayers[i];
            boolean sameFamily = DrawableUtilz.isUpInFamily(d, exclude);
            if (!sameFamily) {
               //any drawable using getWidth and getHeight will update automatically
               dlayers[i].draw(g);
            }
         }
      }
   }

   public IDrawable getDrawableAtIndex(int index) {
      return dlayers[index];
   }

   public int getNumLayers() {
      int count = 0;
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {
            count++;
         }
      }
      return count;
   }

   public IDrawable getTop() {
      if (dlayerNextEmpty < 1) {
         return null;
      }
      IDrawable removed = dlayers[dlayerNextEmpty - 1];
      return removed;
   }

   /**
    * Finds on which DLayer the {@link IDrawable} is.
    * <br>
    * @param d
    * @return
    */
   public int getZIndex(IDrawable d) {
      for (int dlayerIndex = 0; dlayerIndex < dlayerNextEmpty; dlayerIndex++) {
         if (dlayers[dlayerIndex] == d) {
            return dlayerIndex;
         } else {
            IDrawable[] children = dlayers[dlayerIndex].getChildren();
            for (int j = 0; j < children.length; j++) {
               if (children[j] == d) {
                  return dlayerIndex;
               } else {

               }
            }
         }
      }
      return -1;
   }

   public boolean hasInterceptor(IDrawable d) {
      for (int i = 0; i < nextEmptyIntercepts; i++) {
         if (intercepts[i] == d) {
            return true;
         }
      }
      return false;
   }

   public boolean intercepts(InputConfig icc) {
      if (nextEmptyIntercepts != 0) {
         for (int i = 0; i < nextEmptyIntercepts; i++) {
            intercepts[i].manageInput(icc);
            if (icc.isActionDone()) {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Return the DLayers intersecting with the given area.
    * <br>
    *  
    * @param x
    * @param y
    * @param w
    * @param h
    * @return
    */
   public int[] intersect(int x, int y, int w, int h) {
      UCtx uc = gc.getUCtx();
      IntBuffer ib = new IntBuffer(uc, dlayerNextEmpty);
      Geo2dUtils geo = uc.getGeo2dUtils();
      for (int i = 0; i < dlayerNextEmpty; i++) {
         IDrawable d = dlayers[i];
         if (geo.hasCollision(d.getX(), d.getY(), d.getDrawnWidth(), d.getDrawnHeight(), x, y, w, h)) {
            ib.addInt(i);
         }
      }
      return ib.getIntsClonedTrimmed();
   }

   /**
    * Returns the index of this {@link IDrawable} in this topology.
    * <br>
    * -1 if not inside.
    * @param d
    * @return
    */
   public int isContained(IDrawable d) {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != d) {
            return i;
         }
      }
      return -1;
   }

   /**
    * True when an opaque DLayer above zindex completely hides the given area
    * <br>
    * <br>
    * @param zindex
    * @param x
    * @param y
    * @param w
    * @param h
    * @return
    */
   public boolean isHidden(int zindex, int x, int y, int w, int h) {
      // TODO Auto-generated method stub
      return false;
   }

   public boolean isInTopology(IDrawable d) {
      return getZIndex(d) != -1;
   }

   /**
    * Is the event accepted
    * @param ic
    * @param d
    * @return
    */
   private boolean isKeyEventAccepted(InputConfig ic, IDrawable d) {
      if (d != null) {
         if (!ic.isActionDone()) {
            //nothing goes down. so buggy.
            //if (gc.getFocusCtrl().getItemInKeyFocus() == d) {
            return true;
            //}
         }
      }
      return false;
   }

   public void manageInput(InputConfig ic) {
      InputState is = ic.getInputState();
      //#debug
      toDLog().pFlow("Current BEvent", is.getEventCurrent(), TopologyDLayer.class, "manageInput@line414", LVL_03_FINEST, true);
      //
      if (is.isGestured()) {
         manageGestureInput(ic);
      } else if (is.isTypeRepeat()) {
         manageRepeatInput(ic);
      } else if (is.isTypeDevice()) {
         if (is.isTypeDeviceKeyboard()) {
            manageKeyInput(ic);
         } else if (is.isTypeDevicePointer()) {
            managePointerInput(ic);
         }
      } else {
         manageOtherInput(ic);
      }
   }

   /**
    * Start with the highest
    * @param ic
    */
   public void manageKeyInput(InputConfig ic) {
      InputState is = ic.getInputState();
      //#debug
      toDLog().pFlow("KeyCode=", is.getLastDeviceEvent(), TopologyDLayer.class, "manageKeyInput", LVL_03_FINEST, true);
      for (int i = dlayerNextEmpty - 1; i >= 0; i--) {
         if (isKeyEventAccepted(ic, dlayers[i])) {
            dlayers[i].manageKeyInput(ic);
         }
      }
   }

   public void manageRepeatInput(InputConfig ic) {
      InputState is = ic.getInputState();
      //#debug
      toDLog().pFlow("RepeatEvent=", is.getRepeatEvent(), TopologyDLayer.class, "manageRepeatInput@line451", LVL_03_FINEST, true);

      for (int i = dlayerNextEmpty - 1; i >= 0; i--) {
         if (isKeyEventAccepted(ic, dlayers[i])) {
            dlayers[i].manageRepeatInput(ic);
         }
      }
   }

   public void manageOtherInput(InputConfig ic) {
      InputState is = ic.getInputState();
      //#debug
      toDLog().pFlow("KeyCode=", is.getLastDeviceEvent(), TopologyDLayer.class, "manageOtherInput", LVL_03_FINEST, true);

      for (int i = dlayerNextEmpty - 1; i >= 0; i--) {
         if (isKeyEventAccepted(ic, dlayers[i])) {
            dlayers[i].manageOtherInput(ic);
         }
      }
   }

   public void manageGestureInput(InputConfig ic) {
      InputState is = ic.getInputState();
      //#debug
      toDLog().pFlow("KeyCode=", is.getLastDeviceEvent(), TopologyDLayer.class, "manageGestureInput", LVL_03_FINEST, true);

      for (int i = dlayerNextEmpty - 1; i >= 0; i--) {
         if (isKeyEventAccepted(ic, dlayers[i])) {
            dlayers[i].manageGestureInput(ic);
         }
      }
   }

   /**
    * Manages the input for all layers
    * @param ic
    */
   public void managePointerInput(InputConfig ic) {
      for (int i = dlayerNextEmpty - 1; i >= 0; i--) {
         if (ic.isPointerEventAccepted(dlayers[i])) {
            dlayers[i].managePointerInput(ic);
            return;
         }
      }
      //when move, check for a move out 
      if (ic.is.getMode() == IInput.MOD_3_MOVED && !ic.isActionDone()) {
         gc.getFocusCtrl().newFocusPointerPress(ic, null);
      }
   }

   /**
    * TODO gets a list of drawable located at
    * First is closest to the Top.
    * <br>
    * When moving the Mouse, {@link CmdCtx}? depends on business logic.
    * In a Drag, if there is a dragged drawable, it will be the drawable after the dragged
    * but maybe the dragged drawable is not drawn below the pointer.
    * <br>
    * Returns the first drawable met.
    * 
    * @param x
    * @param y
    * @return
    */
   public IDrawable getDrawable(int x, int y, ExecutionCtxDraw ex) {
      for (int i = dlayerNextEmpty - 1; i >= 0; i--) {
         IDrawable d = dlayers[i];
         if (d != null) {
            if (DrawableUtilz.isInside(x, y, d)) {
               return d.getDrawable(x, y, ex);
            }
         }
      }
      return null;
   }

   protected void physicalAdd(IDrawable d) {
      if (dlayerNextEmpty >= dlayers.length) {
         dlayers = DrawableArrays.ensureCapacity(dlayers, dlayerNextEmpty + 2);
      }
      dlayers[dlayerNextEmpty] = d;
      dlayerNextEmpty++;

      //#debug
      toDLog().pFlow("dlayerNextEmpty=" + dlayerNextEmpty, d, TopologyDLayer.class, "physicalAdd", LVL_05_FINE, true);
   }

   /**
    * Removes the top DLayer {@link IDrawable}.
    * <br>
    * <br>
    * Controller
    * Method
    * If only 1 active drawable left in the List ? TODO
    * Repaint call must be externally made.
    * <br>
    */
   public synchronized void popActive() {
      if (dlayerNextEmpty < 1)
         return;
      IDrawable removed = dlayers[dlayerNextEmpty - 1];
      removeDrawable(removed);
   }

   /**
    * Removes {@link IDrawable} and its DLayer from the topology.
    * <br>
    * During the next repaint, its area MUST be fully repainted. i.e. this is a Repaint Erase on that DLayer index  in screen result. 
    * <br>
    * Work on {@link InputConfig}.
    * @param d
    */
   public void removeDrawable(IDrawable d) {
      //#debug
      toDLog().pFlow("", d, TopologyDLayer.class, "removeDrawable", LVL_05_FINE, true);
      DrawableUtilz.aboutToHide(d);
      if (d.hasState(ITechDrawable.STATE_13_DISAPPEARING)) {
         return;
      } else {
         removeDrawableNoAnimHooks(d);
      }
   }

   public void removeDrawableNoAnimHooks(IDrawable d) {
      //#debug
      toDLog().pFlow("null", d, TopologyDLayer.class, "removeDrawableNoAnimHooks", LVL_05_FINE, true);
      dlayerNextEmpty = DrawableArrays.removeDrawable(dlayers, dlayerNextEmpty, d);
   }

   public void removeInterceptor(IDrawable d) {
      nextEmptyIntercepts = DrawableArrays.removeDrawable(intercepts, nextEmptyIntercepts, d);
   }

   /**
    * Removes all topologies using {@link IDrawable}.
    * <br>
    * <br>
    * @param d
    */
   public void removeTopologies(IDrawable d) {

   }

   /**
    * Calls {@link IDrawable#layoutInvalidate(boolean)} with true as parameter for all DLayers.
    */
   public void renewLayout() {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {
            //any drawable using getWidth and getHeight will update automatically
            dlayers[i].layoutUpdate(null);
         }
      }
   }

   public void invalidateLayout() {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {
            //any drawable using getWidth and getHeight will update automatically
            dlayers[i].layoutInvalidate(false);
         }
      }
   }

   public void init() {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {
            dlayers[i].init();
         }
      }
   }

   public void setStateFlags(int flag, boolean val) {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         if (dlayers[i] != null) {
            dlayers[i].setStateFlag(flag, val);
         }
      }
   }

   /**
    * The strings have to be updated. which may force a layout update 
    */
   public void stringUpdate() {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         dlayers[i].stringUpdate();
      }
   }

   /**
    * Styles have changed.. update everything style related.
    */
   public void styleUpdate() {
      for (int i = 0; i < dlayerNextEmpty; i++) {
         int id = dlayers[i].getStyleClass().getID();
         dlayers[i].setStyleClass(gc.getClass(id));
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public IDLog toDLog() {
      return gc.toDLog();
   }

   public void toString(Dctx sb) {
      sb.root(this, TopologyDLayer.class);
      sb.appendVarWithSpace("#Background Layers", numBgs);
      Dctx dcLayers = sb.newLevel();
      dcLayers.nlLvlArrayNotNull("Layers", dlayers, 0, dlayerNextEmpty);

      sb.nlLvlArray1Line(intercepts, 0, nextEmptyIntercepts, "Intercepts");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   /**
    * Called when  {@link Dctx} see the same object for another time
    * @param dc
    */
   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Topology");
   }

   public UCtx toStringGetUCtx() {
      return gc.getUCtx();
   }
   //#enddebug

}
