package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.ArrayUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.framework.core.ui.src4.tech.ITechCodes;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.PointerGestureDrawable;
import pasa.cbentley.framework.gui.src4.canvas.TopologyDLayer;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.menu.MenuBar;
import pasa.cbentley.framework.gui.src4.utils.DrawableArrays;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;

/**
 * Drawable drawn from Panel origin. Use a {@link TopologyDLayer} behaviour to the {@link MasterCanvas} topology wise.
 * <br>
 * <br>
 * X,Y of Drawables is relative to X,Y of Panel. That's why {@link GraphicsX} is translated.
 * <br>
 * <br>
 * {@link MasterCanvas} draws a {@link PinBoardDrawable}
 * <br>
 * <br>
 * A Panel may have a {@link MenuBar} set in {@link ViewDrawable} 's {@link ViewPane} Top or Bottom header.
 * <br>
 * <br>
 * <br>
 * Navigation Topology is only along the ZIndex
 * <br>
 * 
 * @author Mordan
 *
 */
public class PinBoardDrawable extends ViewDrawable {

   public static final int DRAG_MODE_0_RESIZE   = 0;

   public static final int DRAG_MODE_1_PINBOARD = 1;

   public static final int DRAG_MODE_2_DRAWABLE = 2;

   private boolean         allowH               = true;

   private boolean         allowW               = true;

   private int             draggingMode         = 0;

   IDrawable[]             drawables            = new IDrawable[5];

   private int             keyFocusZIndex       = -1;

   private int             nextempty;

   private int             pointerFocusZIndex   = -1;

   private int             pointerPressedZIndex = -1;

   /**
    * Stores x offset coordinates of {@link IDrawable}.
    */
   int[]                   xs;

   /**
    * Stores x offset coordinates of {@link IDrawable}.
    */
   int[]                   ys;

   public PinBoardDrawable(GuiCtx gc, StyleClass sc) {
      super(gc, sc);
      setBehaviorFlag(ITechDrawable.BEHAVIOR_25_CONTAINER, true);
   }

   public void addDrawable(IDrawable d) {
      this.addDrawable(d, d.getX(), d.getY());
   }

   /**
    * {@link IDrawable} must be layouted or it will be layouted using the default scheme.
    * <br>
    * <br>
    * @param d
    */
   public void addDrawable(IDrawable d, int rx, int ry) {
      int nn = nextempty + 1;
      drawables = DrawableArrays.ensureCapacity(drawables, nn);
      xs = gc.getUC().getMem().ensureCapacity(xs, nn);
      ys = gc.getUC().getMem().ensureCapacity(ys, nn);
      drawables[nextempty] = d;
      xs[nextempty] = rx;
      ys[nextempty] = ry;

      d.initSize();
      //update every time the style changes
      d.setXY(this.getContentX() + rx, this.getContentY() + ry);
      d.initPosition();
      d.setBehaviorFlag(ITechDrawable.BEHAVIOR_23_PARENT_EVENT_CONTROL, true);
      d.setParent(this);
      nextempty++;
   }

   public int getNumDrawables() {
      return nextempty;
   }

   /**
    * Drags according to 3 modes
    * <li> {@link PinBoardDrawable#DRAG_MODE_0_RESIZE}
    * <li> {@link PinBoardDrawable#DRAG_MODE_1_PINBOARD}
    * <li> {@link PinBoardDrawable#DRAG_MODE_2_DRAWABLE}
    * 
    * @param ic
    */
   public void draggingProcess(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      GestureDetector pg = ic.getInputStateDrawable().getOrCreateGesture(this);
      int pressedX = pg.getPressed(PointerGestureDrawable.ID_0_X);
      int pressedY = pg.getPressed(PointerGestureDrawable.ID_1_Y);
      //check if one has pointer focus
      if (draggingMode == DRAG_MODE_2_DRAWABLE) {
         IDrawable d = getPointerFocusDrawable();
         d.setStateStyle(ITechDrawable.STYLE_09_DRAGGED, true);
         int newX = pressedX + ic.getDraggedVectorX();
         int newY = pressedY + ic.getDraggedVectorY();
         //don't allow negatives 
         if (newX < this.getContentX()) {
            newX = this.getContentX();
         }
         if (newY < this.getContentY()) {
            newY = this.getContentY();
         }
         if (!allowW) {
            if (newX + d.getDrawnWidth() > this.getX() + this.getDrawnWidth()) {
               newX = this.getX() + this.getDrawnWidth() - d.getDrawnWidth();
            }
         }
         if (!allowH) {
            if (newY + d.getDrawnHeight() > this.getY() + this.getDrawnHeight()) {
               newY = this.getY() + this.getDrawnHeight() - d.getDrawnHeight();
            }
         }
         xs[pointerFocusZIndex] = newX - this.getContentX();
         ys[pointerFocusZIndex] = newY - this.getContentY();
         int[] bounds = new int[] { d.getX(), d.getY(), d.getDrawnWidth(), d.getDrawnHeight() };
         d.setXY(newX, newY);
         //update layout of 
         this.layoutInvalidate(false);
         //TODO if goes out of visible viewport, moves viewport as well
         ic.srActionDoneRepaint(d);
         //TODO specify 
      } else if (draggingMode == DRAG_MODE_0_RESIZE) {
         //else this is a drag for resize
         int newX = pressedX + ic.getDraggedVectorX();
         int newY = pressedY + ic.getDraggedVectorY();
         if (newX < 10) {
            newX = 10;
         }
         if (newY < 10) {
            newY = 10;
         }
         //TODO what happens when ViewPane is created but no show notify has been sent?
         //a mean to notify show Viewpane upon dynamic creation, upon creation it looks up ViewDrawable and matches hidden state
         this.init(newX, newY);
         ic.srActionDoneRepaint();
      } else if (draggingMode == DRAG_MODE_1_PINBOARD) {
         this.setStateStyle(ITechDrawable.STYLE_09_DRAGGED, true);
         int newX = pressedX + ic.getDraggedVectorX();
         int newY = pressedY + ic.getDraggedVectorY();
         this.setXY(newX, newY);
         ic.srActionDoneRepaint(this);
      }
   }

   /**
    * 
    */
   public void drawViewDrawableContent(GraphicsX g, int x, int y, ScrollConfig scX, ScrollConfig scY) {
      int w = getContentW();
      int h = getContentH();
      //sets the clip directive to intersection. do not allow overriding
      g.clipSet(x, y, w, h);
      for (int i = 0; i < nextempty; i++) {
         if (drawables[i] != null) {
            //DrawableUtilz.aboutToShow(ds[i]);
            drawables[i].draw(g);
         }
      }
      g.clipReset();
   }

   public IDrawable getPointerFocusDrawable() {
      return drawables[pointerFocusZIndex];
   }

   /**
    * No logic
    */
   public void initScrollingConfig(ScrollConfig scX, ScrollConfig scY) {
      if (scX != null) {
         DrawableUtilz.initConfigX(this, scX);
      }
      if (scY != null) {
         DrawableUtilz.initConfigY(this, scY);
      }
      //DrawableUtilz.debugScrollConfigs("TableView#initScrollingConfig", scX, scY);
   }

   protected void initViewDrawable(LayouterEngineDrawable ls) {
      //compute span of Drawable
      int ph = 0;
      int pw = 0;
      for (int i = 0; i < nextempty; i++) {
         if (drawables[i] != null) {
            IDrawable d = drawables[i];
            int val = (d.getX() - this.getX()) + d.getDrawnWidth();
            if (val > pw) {
               pw = val;
            }
            int valh = (d.getY() - this.getY()) + d.getDrawnHeight();
            if (valh > ph) {
               ph = valh;
            }
         }
      }
      if (ph == 0) {
         //no content
         ph = 10;
      }
      if (pw == 0) {
         //no content
         pw = 10;
      }
      layEngine.setPw(pw);
      layEngine.setPh(ph);

      //update positions
      for (int i = 0; i < nextempty; i++) {
         if (drawables[i] != null) {
            //update every time the style changes
            drawables[i].setXY(this.getContentX() + xs[i], this.getContentY() + ys[i]);
         }
      }
   }

   /**
    * Deals with the visuals of {@link Drawable} transition. 
    * <br>
    * <br>
    * 
    * <br>
    * <br>
    * <b>Type</b>:
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_0REPLACE}
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_1OVER}
    * <li> {@link ITechCanvasDrawable#SHOW_TYPE_2OVER_INACTIVE}
    * <br>
    * In case of Replace, keep an history for the {@link CmdController#CMD_HISTORY_BACK} to work on.
    * <br>
    * <br>
    * Topology
    * <br>
    * @param view
    * @param type
    * 
    */
   public void loadDLayer(IDrawable view, int type) {
      addDrawable(view, view.getX(), view.getY());
      getVC().getTopo().addDLayer(view, type);
   }

   /**
    * {@link PinBoardDrawable} will receive Events from {@link Controller} like any other {@link Drawable}.
    * <br>
    * <br>
    * Send them from TopDLayer down to Bottom.
    * <br>
    * <br>
    * Do a Navigation on currently Key Focused Drawable as Moving TBLR. 
    * <br>
    * Cmd to iterate zindex between keyFocus in addition to Pointer
    */
   public void manageKeyInput(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      if (ic.isSequencedPressed(ITechCodes.KEY_STAR, ITechCodes.KEY_UP)) {
         //cycle front to back most
         ArrayUtils.rotateRight(drawables, 0, nextempty);
         ic.srActionDoneRepaint(this);
         return;
      }
      if (ic.isSequencedPressed(ITechCodes.KEY_STAR, ITechCodes.KEY_DOWN)) {
         //cycle front to -1
         ArrayUtils.rotateLeft(drawables, 0, nextempty);
         ic.srActionDoneRepaint(this);
         return;

      }

      //event amont
      if (keyFocusZIndex >= 0 && drawables[keyFocusZIndex] != null) {
         drawables[keyFocusZIndex].manageKeyInput(ec);
      }
      //iterate over
      for (int i = 0; i < nextempty; i++) {
         if (drawables[i] != null) {
            if (drawables[i].hasStateStyle(ITechDrawable.STYLE_06_FOCUSED_KEY)) {
               drawables[i].manageKeyInput(ec);
               break;
            }
         }
      }
   }

   /**
    * Drawables may be dragged in the {@link PinBoardDrawable}.
    * <br>
    * That happens when Pointer is dragged.
    */
   public void managePointerInput(ExecutionContextCanvasGui ec) {
      InputConfig ic = ec.getInputConfig();
      //implement dragging only when first press 
      if (hasStateStyle(ITechDrawable.STYLE_08_PRESSED)) {
         if (ic.isReleased()) {
            //compute the gesture amplitude and generate a timer for modyfing scroll config start increment 
            GestureDetector pg = ic.getInputStateDrawable().getOrCreateGesture(this);
            pg.simpleRelease(ic.getInputStateDrawable());
            //Controller.getMe().draggedRemover(this, ic);
         }
         if (ic.isDraggedPointer0Button0()) {
            draggingProcess(ec);
         }
         return;
      }

      //drags the PinBoard when pointer is on the border
      if (ic.isPressed() && DrawableUtilz.isInsideBorder(ec, this)) {
         //resize drawable
         draggingMode = DRAG_MODE_0_RESIZE;
         GestureDetector pg = ic.getInputStateDrawable().getOrCreateGesture(this);
         pg.simplePress(getDw(), getDh(), ic.getInputStateDrawable());
         ic.srActionDoneRepaint();
         return;
      }

      pointerFocusZIndex = -1;
      //top down approach to see what drawable is pointed
      for (int i = nextempty - 1; i >= 0; i--) {
         if (drawables[i] != null) {
            IDrawable d = drawables[i];
            if (DrawableUtilz.isInside(ec, d)) {
               d.managePointerInput(ec);

               if (!hasStateStyle(ITechDrawable.STYLE_08_PRESSED) && ic.isPressed()) {
                  //listen for draggin
                  draggingMode = DRAG_MODE_2_DRAWABLE;
                  GestureDetector pg = ic.getInputStateDrawable().getOrCreateGesture(this);
                  pg.simplePress(d.getX(), d.getY(), ic.getInputStateDrawable());
                  ic.srActionDoneRepaint();

               }
               d.managePointerInput(ec);
               pointerFocusZIndex = i;
               break;
            }
         }
      }

      //when pointer is not inside a Drawable
      if (pointerFocusZIndex == -1) {
         if (ic.isPressed()) {
            //listen for dragging on the whole PinBoard
            draggingMode = DRAG_MODE_1_PINBOARD;
            GestureDetector pg = ic.getInputStateDrawable().getOrCreateGesture(this);
            pg.simplePress(this.getX(), this.getY(), ic.getInputStateDrawable());
            this.setStateStyle(ITechDrawable.STYLE_08_PRESSED, true);
         }
         gc.getFocusCtrl().newFocusPointerPress(ec, this);
         ic.srActionDoneRepaint();
         return;
      }

   }

   /**
    * WHen Selected {@link ITechDrawable#STYLE_05_SELECTED} depends on Focus,
    */
   public void notifyEventKeyFocusGain(BusEvent e) {
      super.notifyEventKeyFocusGain(e);
      if (keyFocusZIndex >= 0) {
         drawables[keyFocusZIndex].notifyEvent(ITechDrawable.EVENT_03_KEY_FOCUS_GAIN);
      }
   }

   /**
    * If Tech Requires it, removes selected state from selected cell {@link Drawable}
    */
   public void notifyEventKeyFocusLost(BusEvent e) {
      super.notifyEventKeyFocusLost(e);
      if (keyFocusZIndex >= 0) {
         drawables[keyFocusZIndex].notifyEvent(ITechDrawable.EVENT_04_KEY_FOCUS_LOSS);
      }
   }

   public void setXY(int x, int y) {
      int xd = x - this.getX();
      int yd = y - this.getY();
      for (int i = 0; i < nextempty; i++) {
         if (drawables[i] != null) {
            drawables[i].shiftXY(xd, yd);
         }
      }
      super.setXY(x, y);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, PinBoardDrawable.class, 450);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nl();
      dc.appendVarWithSpace("draggingMode", draggingMode);
      dc.appendVarWithSpace("pointerFocusZIndex", pointerFocusZIndex);

      for (int i = 0; i < nextempty; i++) {
         if (i != nextempty - 1) {
            dc.nl();
         }
         if (drawables[i] != null) {
            dc.append(xs[i] + "," + ys[i] + " : \t");
            dc.append(drawables[i].toString1Line());
         } else {
            dc.append("null");
         }

      }
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("nextempty", nextempty);
      dc.appendVarWithSpace("allowW", allowW);
      dc.appendVarWithSpace("allowH", allowH);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, PinBoardDrawable.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
