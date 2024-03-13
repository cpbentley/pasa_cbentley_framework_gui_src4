package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.trigger.CmdTrigger;
import pasa.cbentley.framework.coreui.src4.event.GesturePointer;
import pasa.cbentley.framework.coreui.src4.tech.IInput;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.InputState;

/**
 * Command controller for pointer event in resizing a Drawable.
 * <br>
 * 
 * This will set the flag User Resized and size will be written to ViewState.
 * <br>
 * <br>
 * <br>
 * <li> Slide left / right closes the Drawable
 * <li> Pinch increases size within the boundary
 * <li> Drag moves within the boundary
 * <li> CTRL+Drag increases size within the boundary
 * <li> Escape closes/hides the Drawable
 * 
 * <br>
 * <br>
 * When a Drawable is shown that needs this service,
 * a {@link CmdCtx} is set on it.
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class ResizeDragCtrler implements IDrawableListener {

   private Drawable drawable;

   private int      boundaryX;

   private int      boundaryY;

   private int      boundaryW;

   private int      boundaryH;

   private boolean  isResizeMode;

   private int      slack;

   protected final GuiCtx gc;

   public ResizeDragCtrler(GuiCtx gc, Drawable d, int slack) {
      this.gc = gc;
      this.drawable = d;
      this.slack = slack;
   }

   public void setBoundaries(int x, int y, int w, int h) {
      boundaryX = x;
      boundaryY = y;
      boundaryW = w;
      boundaryH = h;
   }

   public int getBoundaryH() {
      return boundaryH;
   }

   public int getBoundaryW() {
      return boundaryW;
   }

   public void notifyEvent(IDrawable d, int event, Object o) {
   }

   /**
    * Nav command
    */
   public void commandAction(CmdInstanceDrawable cd) {
      if (cd.getCmdID() == ICmdsCmd.CMD_18_NAV_PRE_SELECT) {
         cmdCueSelect(cd);
      }
   }

   IDrawable cue;

   /**
    * Command called when a pointer is pressed
    * <br>
    * {@link ICmdsCmd#CMD_18_NAV_PRE_SELECT}
    * @param cd
    */
   public void cmdCueSelect(CmdInstanceDrawable cd) {
      if (DrawableUtilz.isInsideBorder(cd.getIC(), drawable, slack)) {
         //we detected a condition for a new command state.
         //this command result changes future outcome
         isResizeMode = true;
      }
   }

   /**
    * When a pinch, resize is done around the center of gravity.
    * <br>
    * In logical positioning, we don't modify. just invalidate layout
    * <br>
    * <br>
    * 
    * @param cd
    */
   public void cmdResize(CmdInstanceDrawable cd) {

   }

   /**
    * Move Drawable
    * <br>
    * <br>
    * Different {@link CmdTrigger}
    * <li> Something + UP will move
    * <li> 
    * <br>
    * Some parameters are not understandable by the trigger->param mapper, the command
    * use a form to request them.
    * <br>
    * A number indicates what units? UP+2 move up 2 pixels? UP+8 pixels?
    * <br>
    * 
    * Several commands
    * @param cd
    */
   public void cmdMove(CmdInstanceDrawable cd) {
      if (cd.isUndoMode()) {
         int x = cd.getParamInt(0);
         int y = cd.getParamInt(1);

      }
      if (cd.isParamed()) {
         int x = cd.getParamInt(0);
         int y = cd.getParamInt(1);

      } else if (cd.isTriggerParam()) {
         //check if Gesture.. else analyse trigger
         CmdTrigger ct = cd.getTrigger();
         int unit = ct.getLastTriggerCode();
         int pointer = CmdTrigger.getUnitKey1(unit);
         int mod = CmdTrigger.getUnitMode(unit);
         //are we moving.. case of a continuous command
         if (mod == IInput.MOD_3_MOVED) {
            //do we have access to input state here?
            //what if
            InputState is = cd.getIC().is;
            GesturePointer gp = is.getGesturePointer0(pointer);
            //get the parameter from the gesture pointer
            dragIt(cd, drawable, gp);
         }
         int x = cd.getParamInt(0);
         int y = cd.getParamInt(1);
         
         //TODO undo should be at the start of the continuous move.
         cd.setParamUndoInt(0,x);
         cd.setParamUndoInt(0,y);
         cd.actionDoneBefore(drawable);
         drawable.setXY(x, y);
         cd.actionDoneAfter(drawable);

         //what is the most efficient way to repaint the screen for a move?
         //1)erase drawable area in previous position
         //  To do that if no drawable above
         //        Ask drawable below to repaint inside area
      } else {
         //request x and y in a form. 
         //provides a form with 4 buttons to move right, left top bottom.
      }

   }

   /**
    * For Border resize, the x,y don't change until the release confirmation.
    * <br>
    * <br>
    * <li>Alt + Up resize border up until released.
    * <li>Alt + Down resize border up until released.
    * 
    * @param cd
    */
   public void cmdResizeBorder(CmdInstanceDrawable cd) {

   }
//
//   /**
//    * Drag listener.
//    * <br>
//    * <br>
//    * For the Help.
//    * 
//    */
//   public void managePointerEvent(IDrawable d, InputConfig ic) {
//      if (d == drawable) {
//         Drawable g = drawable;
//         //manage the gesture
//         GestureDetector pg = ic.is.getOrCreateGesture(d);
//         //update boundaries at each pass which means we have a dynamic effect
//         pg.setBoundaries(boundaryX, getBoundaryW() - g.getDrawnWidth(), PointerGestureDrawable.ID_0_X);
//         pg.setBoundaries(boundaryY, getBoundaryH() - g.getDrawnHeight(), PointerGestureDrawable.ID_1_Y);
//
//         if (ic.isGestured()) {
//            //#debug
//            drawable.toLog().printFlow("#ViewCommandListener#managePointerEvent InputConfig#isGestured " + pg.toStringOneLine());
//            //do an update on the vector.
//            int x = g.getX();
//            int y = g.getY();
//            if (pg.hasValueFlag(PointerGestureDrawable.ID_0_X, IGestures.FLAG_10_IS_GESTURING)) {
//               x = pg.getPosition(PointerGestureDrawable.ID_0_X);
//            }
//            if (pg.hasValueFlag(PointerGestureDrawable.ID_1_Y, IGestures.FLAG_10_IS_GESTURING)) {
//               y = pg.getPosition(PointerGestureDrawable.ID_1_Y);
//            }
//            g.setXY(x, y);
//            //#debug
//            drawable.toLog().printFlow("#ViewDrawable#managePointerGesture [" + pg.getPosition(PointerGestureDrawable.ID_0_X) + "," + pg.getPosition(PointerGestureDrawable.ID_1_Y) + "]");
//            ic.srActionDoneRepaint(g);
//         }
//         if (ic.isPressed()) {
//            //Gesture is auto started
//            int pressedX = g.getX();
//            int pressedY = g.getY();
//            pg.simplePress(pressedX, pressedY, ic.is);
//            //decides in which mode we are based on position of press.
//            //for resizing, it depends on 
//            if (DrawableUtilz.isInsideBorder(ic, d, slack)) {
//               isResizeMode = true;
//            }
//         } else if (ic.isDragged()) {
//
//
//         } else if (ic.isReleased()) {
//            int relX = g.getX();
//            int relY = g.getY();
//            pg.setFlags(IGestures.FLAG_14_VAL_DO_IT, true, PointerGestureDrawable.ID_0_X);
//            pg.setFlags(IGestures.FLAG_14_VAL_DO_IT, true, PointerGestureDrawable.ID_1_Y);
//            pg.simpleReleaseGesture(ic.is, relX, relY, IGestures.GESTURE_1_BOUNDARY, null);
//         }
//      }
//   }

   protected void dragIt(CmdInstanceDrawable cd, Drawable g, GesturePointer pg) {
      int pressedX = pg.getPressedX();
      int pressedY = pg.getPressedY();
      int modX = pg.getVectorX();
      int modY = pg.getVectorY();
      int valX = pressedX + modX;
      int valY = pressedY + modY;
      if (valX < boundaryX) {
         valX = boundaryX;
      } else if (valX + g.getDrawnWidth() > boundaryX + getBoundaryW()) {
         valX = getBoundaryW() - g.getDrawnWidth();
      }
      if (valY < boundaryY) {
         valY = boundaryY;
      } else if (valY + g.getDrawnHeight() > boundaryY + getBoundaryH()) {
         valY = getBoundaryH() - g.getDrawnHeight();
      }
      //res
      cd.setParamInt(0, valX);
      cd.setParamInt(1, valY);

   }

   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ResizeDragCtrler");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ResizeDragCtrler");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return gc.getUC();
   }

   //#enddebug
   


 
}
