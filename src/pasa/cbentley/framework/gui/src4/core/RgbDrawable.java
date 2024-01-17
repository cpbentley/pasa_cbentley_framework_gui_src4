package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;

/**
 * Used to animate a simple image without the need for a {@link ImageDrawable}.
 * @author Charles-Philip Bentley
 *
 */
public class RgbDrawable extends Drawable {

   private RgbImage img;

   public RgbDrawable(GuiCtx gc, StyleClass sc, RgbImage img) {
      this(gc,sc, img, 0, 0);
   }

   public RgbDrawable(GuiCtx gc,StyleClass sc, RgbImage img, int x, int y) {
      super(gc, sc);
      this.img = img;
      this.setX(x);
      this.setY(y);
   }

   public int getInitWidth() {
      return getDrawnWidth();
   }

   public String toStringOneLine() {
      return "#RgbDrawable";
   }

   public int getInitHeight() {
      return getDrawnHeight();
   }

   public boolean isOpaque() {
      boolean isOpaque = super.isOpaque();
      if (!isOpaque) {
         isOpaque = !img.hasTransparentPixels();
      }
      return isOpaque;
   }

   /**
    * 
    */
   public ViewState getViewState() {
      ViewState vs = new ViewState();
      vs.int1States = getX();
      vs.int2Behaviors = getY();
      //TODO a way to retrieve the image
      return vs;
   }

   public void setViewState(ViewState vs) {
      throw new RuntimeException();
   }

   public void drawDrawable(GraphicsX g) {
      g.drawRgbImage(img, getX(), getY());
   }

   public int getDrawnHeight() {
      return img.getHeight();
   }

   public int getDrawnWidth() {
      return img.getWidth();
   }

   public void notifyEvent(int event) {
      super.notifyEvent(event);
   }

   public String getName() {
      return img.getName();
   }

   public RgbImage getRgbImage(int type) {
      return img;
   }

   public String toString(String nl) {
      return "#RgbDrawable x=" + getX() + " y=" + getY() + nl + " " + img.toString();
   }

   /**
    * 
    */
   public RgbImage getRgbCache(int type) {
      return img;
   }

}
