package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.coreui.src4.tech.ITechGestures;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.InputStateDrawable;
import pasa.cbentley.framework.gui.src4.canvas.PointerGestureDrawable;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawableListener;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechStringDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;
import pasa.cbentley.framework.gui.src4.utils.DrawableUtilz;
import pasa.cbentley.framework.input.src4.gesture.GestureDetector;

public class HelperStringDrawable extends StringDrawable implements IDrawableListener {

   private StringDrawable titleHelp;

   public HelperStringDrawable(GuiCtx gc) {
      this(gc, gc.getClass(IUIView.SC_0_BASE_TABLE));
   }

   public HelperStringDrawable(GuiCtx gc, StyleClass sc) {
      super(gc, sc, "No Help Data Yet", ITechStringDrawable.PRESET_CONFIG_3_SCROLL_V);

      this.setBehaviorFlag(ITechDrawable.BEHAVIOR_15_CANVAS_POSITIONING, true);
      this.setXYLogic(C.LOGIC_2_CENTER, C.LOGIC_2_CENTER);

      StyleClass scTitle = gc.getClass(IUIView.SC_2_TITLE);
      titleHelp = new StringDrawable(gc, scTitle, "Help!", ITechStringDrawable.PRESET_CONFIG_1_TITLE);
      //set drag controller on title for the help data string

      titleHelp.addDrawableListener(this);

      this.setHeader(titleHelp, C.POS_0_TOP, ITechViewPane.PLANET_MODE_1_EXPAND);

      //commander stuff
      CmdNode helpCtx = gc.getCC().createCmdNode("help");
      //help menu may stay there
      helpCtx.addMenuCmd(ICmdsView.CMD_04_OK);
      helpCtx.addMenuCmd(ICmdsView.CMD_51_SHOW_HOME);
      helpCtx.addMenuCmd(ICmdsView.CMD_50_SHOW_SYSTEM_MENU);

   }

   public void managePointerInputViewPort(InputConfig ic) {
      // TODO Auto-generated method stub
      super.managePointerInputViewPort(ic);
   }

   public void managePointerInput(InputConfig ic) {
      super.managePointerInput(ic);
      Drawable d = this;
      if (DrawableUtilz.isInside(ic, d)) {
         if (!DrawableUtilz.isInsideBorder(ic, d)) {
            //see which border is selected
            GestureDetector pg = ic.is.getOrCreateGesture(d);

            if (ic.isPressed()) {
               //Gesture is auto started
               int pressedX = d.getDrawnWidth();
               int pressedY = d.getDrawnHeight();
               pg.simplePress(pressedX, pressedY, ic.is);
            }
            if (ic.isDraggedPointer0Button0()) {
               int pressedX = pg.getPressed(PointerGestureDrawable.ID_0_X);
               int pressedY = pg.getPressed(PointerGestureDrawable.ID_1_Y);
               int modX = ic.getDraggedDiffX();
               int modY = ic.getDraggedDiffY();
               int valX = pressedX + modX;
               int valY = pressedY + modY;
               //res
               d.init(valX, valY);
               //modify the position
               //d.setXY(x, y);
               ic.srActionDoneRepaint(d);
            } else if (ic.isReleased()) {
               pg.simpleRelease(ic.is);
            }
         }
      }
   }

   public void notifyEvent(IDrawable d, int event, Object o) {
      InputConfig ic = (InputConfig) o;
      InputStateDrawable is = ic.is;
      if (d == titleHelp) {
         Drawable g = this;
         //manage the gesture
         GestureDetector pg = is.getOrCreateGesture(titleHelp);
         ViewContext vc = g.getVC();
         //update boundaries at each pass which means we have a dynamic effect
         pg.setBoundaries(0, vc.getWidth() - g.getDrawnWidth(), PointerGestureDrawable.ID_0_X);
         pg.setBoundaries(0, vc.getHeight() - g.getDrawnHeight(), PointerGestureDrawable.ID_1_Y);

         if (is.isGestured()) {
            //#debug
            toDLog().pFlow("", pg, ViewCommandListener.class, "managePointerEvent", LVL_05_FINE, true);

            //do an update on the vector.
            int x = g.getX();
            int y = g.getY();
            if (pg.hasValueFlag(PointerGestureDrawable.ID_0_X, ITechGestures.FLAG_10_IS_GESTURING)) {
               x = pg.getPosition(PointerGestureDrawable.ID_0_X);
            }
            if (pg.hasValueFlag(PointerGestureDrawable.ID_1_Y, ITechGestures.FLAG_10_IS_GESTURING)) {
               y = pg.getPosition(PointerGestureDrawable.ID_1_Y);
            }
            g.setXY(x, y);
            //#debug
            toDLog().pFlow("[" + pg.getPosition(PointerGestureDrawable.ID_0_X) + "," + pg.getPosition(PointerGestureDrawable.ID_1_Y) + "]", this, ViewCommandListener.class, "managePointerEvent", LVL_05_FINE, true);
            ic.srActionDoneRepaint(g);
         }
         if (is.isModPressed()) {
            //Gesture is auto started
            int pressedX = g.getX();
            int pressedY = g.getY();
            pg.simplePress(pressedX, pressedY, is);
         } else if (ic.isDraggedPointer0Button0()) {
            int pressedX = pg.getPressed(PointerGestureDrawable.ID_0_X);
            int pressedY = pg.getPressed(PointerGestureDrawable.ID_1_Y);
            int modX = ic.getDraggedDiffX();
            int modY = ic.getDraggedDiffY();
            int valX = pressedX + modX;
            int valY = pressedY + modY;
            //            if (valX < 0) {
            //               valX = 0;
            //            }
            //            if (valX + g.getDrawnWidth() > gc.getMasterCanvas().getWidthVirtual()) {
            //               valX = gc.getMasterCanvas().getWidthVirtual() - g.getDrawnWidth();
            //            }
            //            if (valY < 0) {
            //               valY = 0;
            //            }
            //            if (valY + g.getDrawnHeight() > gc.getMasterCanvas().getHeightVirtual()) {
            //               valY = gc.getMasterCanvas().getHeightVirtual() - g.getDrawnHeight();
            //            }
            //res
            g.setXY(valX, valY);
            ic.srActionDoneRepaint(g);
         } else if (is.isModReleased()) {
            int relX = g.getX();
            int relY = g.getY();
            pg.setFlags(ITechGestures.FLAG_14_VAL_DO_IT, true, PointerGestureDrawable.ID_0_X);
            pg.setFlags(ITechGestures.FLAG_14_VAL_DO_IT, true, PointerGestureDrawable.ID_1_Y);
            pg.simpleReleaseGesture(is, relX, relY, ITechGestures.GESTURE_1_BOUNDARY, null);
         }
      }
   }
}
