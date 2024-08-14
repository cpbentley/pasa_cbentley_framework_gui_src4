package pasa.cbentley.framework.gui.src4.utils;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.core.ui.src4.input.InputState;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.core.Drawable;
import pasa.cbentley.framework.gui.src4.core.ScrollConfig;
import pasa.cbentley.framework.gui.src4.core.ViewDrawable;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.exec.InputStateCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.ITechDrawable;
import pasa.cbentley.framework.gui.src4.tech.ITechViewPane;

public class DrawableUtilz {

   /**
    * Sets state flag to visible.
    * Sends the event SHOW and then KEY FOCUS GAIN to the drawable.
    * event will start entry and exit animations
    * @param d
    */
   public static void aboutToShow(IDrawable d) {
      if (d != null) {
         //event will start entry and exit animations
         d.notifyEvent(ITechDrawable.EVENT_01_NOTIFY_SHOW);
      }
   }

   public static int[] computeOutsideArea(IDrawable drawable) {
      return null;
   }

   /**
    * null if none
    * @param d
    * @return
    */
   public static IDrawable getFirstNonNullOpaque(IDrawable d) {
      IDrawable my = d.getParent();
      while (my != null && !my.isOpaque()) {
         my = my.getParent();
      }
      return my;
   }

   public static void initConfigPixelPage(ScrollConfig sc, int prefContentSize, int contentSize) {
      int incrementPixelSize = 0;
      int totalScrollingIncrement = 0;
      int visibleScrollingIncrement = 0;
      switch (sc.getTypeUnit()) {
         case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
            incrementPixelSize = 1;
            totalScrollingIncrement = prefContentSize;
            visibleScrollingIncrement = contentSize;
            break;
         case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
            incrementPixelSize = contentSize;
            visibleScrollingIncrement = 1;
            totalScrollingIncrement = prefContentSize / contentSize;
            break;
         default:
            throw new IllegalArgumentException();
      }
      sc.initConfigPixel(incrementPixelSize, visibleScrollingIncrement, totalScrollingIncrement);
   }

   public static void initConfigPixelPageX(ViewDrawable vd, ScrollConfig scX) {
      initConfigPixelPage(scX, vd.getSizePreferredContentWidth(), vd.getContentW());
   }

   public static void initConfigPixelPageY(ViewDrawable vd, ScrollConfig scY) {
      initConfigPixelPage(scY, vd.getSizePreferredContentHeight(), vd.getContentH());
   }

   /**
    * Init pixel/page
    * @param vd
    * @param scX
    */
   public static void initConfigX(ViewDrawable vd, ScrollConfig scX) {
      int incrementPixelSize = 0;
      int totalScrollingIncrement = 0;
      int visibleScrollingIncrement = 0;
      switch (scX.getTypeUnit()) {
         case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
            incrementPixelSize = 1;
            totalScrollingIncrement = vd.getSizePreferredContentWidth();
            visibleScrollingIncrement = vd.getContentW();
            break;
         case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
            incrementPixelSize = 1;
            totalScrollingIncrement = vd.getSizePreferredContentWidth();
            visibleScrollingIncrement = vd.getContentW();
            break;
         case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
            incrementPixelSize = vd.getContentW();
            visibleScrollingIncrement = 1;
            totalScrollingIncrement = vd.getSizePreferredContentWidth() / vd.getContentW();
            break;
         default:
            throw new IllegalArgumentException();
      }
      scX.initConfigPixel(incrementPixelSize, visibleScrollingIncrement, totalScrollingIncrement);
   }

   public static void initConfigY(ViewDrawable vd, ScrollConfig scY) {
      int incrementPixelSize = 0;
      int totalScrollingIncrement = 0;
      int visibleScrollingIncrement = 0;
      switch (scY.getTypeUnit()) {
         case ITechViewPane.SCROLL_TYPE_0_PIXEL_UNIT:
            incrementPixelSize = 1;
            //
            totalScrollingIncrement = vd.getSizePreferredContentHeight();
            visibleScrollingIncrement = vd.getContentH();
            break;
         case ITechViewPane.SCROLL_TYPE_1_LOGIC_UNIT:
            incrementPixelSize = 1;
            totalScrollingIncrement = vd.getSizePreferredContentHeight();
            visibleScrollingIncrement = vd.getContentH();
            break;
         case ITechViewPane.SCROLL_TYPE_2_PAGE_UNIT:
            incrementPixelSize = vd.getContentH();
            visibleScrollingIncrement = 1;
            totalScrollingIncrement = vd.getSizePreferredContentHeight() / vd.getContentH();
            break;
         default:
            throw new IllegalArgumentException();
      }
      scY.initConfigPixel(incrementPixelSize, visibleScrollingIncrement, totalScrollingIncrement);
   }

   /**
    * Is the InputConfig current x,y coordinate inside the drawable drawing area
    * @param ec 
    * @param d
    * @return
    */
   public static boolean isInside(ExecutionContextCanvasGui ec, IDrawable d) {
      InputStateCanvasGui is = ec.getInputStateDrawable();
      return isInside(is, d);
   }

   public static boolean isInside(InputStateCanvasGui is, IDrawable d) {
      if (d == null)
         return false;
      if (is.getX() >= d.getX() && is.getY() >= d.getY()) {
         if (is.getX() < d.getX() + d.getDrawnWidth() && is.getY() < d.getY() + d.getDrawnHeight()) {
            if (d.hasState(ITechDrawable.STATE_29_CLIPPED)) {

            }
            if (d.hasState(ITechDrawable.STATE_24_HOLED)) {
               int[] holes = d.getHoles();
               for (int i = 0; i < holes.length; i += 4) {
                  if (isInside(is, holes[0], holes[1], holes[2], holes[3])) {
                     return false;
                  }
               }
            }
            return true;
         }
      }
      return false;
   }

   public static boolean isInside(int x, int y, IDrawable d) {
      if (d == null)
         return false;
      //System.out.print("Pointer = (" + ic.x + "," + ic.y + ")");
      //System.out.println("Drawable = (" + d.getX() + "," + d.getY() + ") [" + (d.getX() + d.getDrawnWidth()) + "," + (d.getY() + d.getDrawnHeight()) + "]");
      if (x >= d.getX() && y >= d.getY()) {
         if (x < d.getX() + d.getDrawnWidth() && y < d.getY() + d.getDrawnHeight()) {
            if (d.hasState(ITechDrawable.STATE_29_CLIPPED)) {

            }
            if (d.hasState(ITechDrawable.STATE_24_HOLED)) {
               int[] holes = d.getHoles();
               for (int i = 0; i < holes.length; i += 4) {
                  if (isInside(x, y, holes[0], holes[1], holes[2], holes[3])) {
                     return false;
                  }
               }
            }
            return true;
         }
      }
      return false;
   }

   public static boolean isInside(int xx, int yy, int x, int y, int w, int h) {
      if (xx >= x && yy >= y) {
         if (xx < x + w && yy < y + h) {
            return true;
         }
      }
      return false;
   }

   public static boolean isInside(ExecutionContextCanvasGui ec, int x, int y, int w, int h) {
      InputStateCanvasGui is = ec.getInputStateDrawable();
      if (is.getX() >= x && is.getY() >= y) {
         if (is.getX() < x + w && is.getY() < y + h) {
            return true;
         }
      }
      return false;
   }

   public static boolean isInside(InputStateCanvasGui is, int x, int y, int w, int h) {
      if (is.getX() >= x && is.getY() >= y) {
         if (is.getX() < x + w && is.getY() < y + h) {
            return true;
         }
      }
      return false;
   }

   public static boolean isInside(InputState ic, int x, int y, int w, int h) {
      if (ic.getX() >= x && ic.getY() >= y) {
         if (ic.getX() < x + w && ic.getY() < y + h) {
            return true;
         }
      }
      return false;
   }

   /**
    * Returns true when.
    * <br>
    * Does not made the assumption that x,y is inside drawable.
    * <br>
    * Slack works if pointer is outside Drawable.
    * @param ec
    * @param d
    * @param slack
    * @return
    */
   public static boolean isInsideBorder(ExecutionContextCanvasGui ec, Drawable d, int slack) {
      if (d == null)
         return false;
      ByteObject style = d.getStyle();
      InputStateCanvasGui is = ec.getInputStateDrawable();
      int x = is.getX();
      int y = is.getY();
      int dx = d.getX();
      int dy = d.getY();
      int dh = d.getDrawnHeight();
      int dw = d.getDrawnWidth();
      int sw = d.getStyleWLeftConsumed();
      int sw2 = d.getStyleWRightConsumed();
      int sh = d.getStyleHTopConsumed();
      int sh2 = d.getStyleHBotConsumed();

      if (dx - slack < x && x < dx + sw + slack) {
         if (dy - slack < y && y < dy + dh + slack) {
            return true;
         }
      }
      if (dx + dw - sw2 - slack < x && x < dx + dw + slack) {
         if (dy - slack < y && y < dy + dh + slack) {
            return true;
         }
      }
      if (dy - slack < y && y < dy + sh + slack) {
         if (dx - slack < x && x < dx + dw + slack) {
            return true;
         }
      }
      if (dy + dh - sh2 - slack < y && y < dy + dh + slack) {
         if (dx - slack < x && x < dx + dw + slack) {
            return true;
         }
      }

      return false;

   }

   /**
    * PRE: assume is already inside 
    * @param ec
    * @param d
    * @return
    */
   public static boolean isInsideBorder(ExecutionContextCanvasGui ec, Drawable d) {
      if (d == null)
         return false;
      ByteObject style = d.getStyle();
      InputStateCanvasGui is = ec.getInputStateDrawable();
      if (is.getX() < d.getX() + d.getStyleWLeftConsumed()) {
         return true;
      }
      if (is.getX() > d.getX() + d.getDrawnWidth() - d.getStyleWRightConsumed()) {
         return true;
      }
      if (is.getY() > d.getY() + d.getDrawnHeight() - d.getStyleHBotConsumed()) {
         return true;
      }
      if (is.getY() < d.getY() + d.getStyleHTopConsumed()) {
         return true;
      }
      return false;
   }

   /**
    * True when D is a child of look
    * @param d
    * @param look
    * @return
    */
   public static boolean isUpInFamily(IDrawable d, IDrawable look) {
      if (d == null)
         return false;
      if (d.getParent() == look) {
         return true;
      } else {
         return isUpInFamily(d.getParent(), look);
      }
   }

   public static void setDrawableBounds(int[] ar, int x, int y, int w, int h) {
      ar[0] = x;
      ar[1] = y;
      ar[2] = w;
      ar[3] = h;
   }

   public static void shiftXY(IDrawable dr, int xd, int yd) {
      if (dr != null) {
         int nx = dr.getX() + xd;
         int ny = dr.getY() + yd;
         dr.setXY(nx, ny);
      }
   }

}
