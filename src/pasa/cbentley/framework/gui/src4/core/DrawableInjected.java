package pasa.cbentley.framework.gui.src4.core;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawListener;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Anonymous drawable whose init and draw logic will be controlled by a {@link IDrawListener}.
 * <br>
 * Kind of an anonymous class lol 
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class DrawableInjected extends Drawable {

   private IDrawListener dl;

   public DrawableInjected(GuiCtx gc, StyleClass sc, IDrawable parent, IDrawListener lis) {
      super(gc, sc);
      this.setParent(parent);
      this.dl = lis;
   }

   /**
    * Case of ColumnTitle.
    * <li> TableView set the header
    * <li> ViewPane init the sattelites with width in pixels and {@link ISizer#ET_TYPE_0_PREFERRED_SIZE} as height.
    * This means height is decided by Drawable content.
    * <li> TableView knows the style used by Titles.
    * {@link ViewPane} gives the dimension
    */
   public void init(ByteObject sizerW, ByteObject sizerH) {
      setSizers(sizerW, sizerH);
      dl.initListen(sizerW, sizerH, this);
   }

   protected void drawDrawableContent(GraphicsX g, int x, int y, int w, int h) {
      dl.drawContentListen(g, x, y, w, h, this);
   }

   public void toString(Dctx sb) {
      sb.root(this, "DrawableInjected");
      super.toString(sb.newLevel());
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "DrawableInjected");
      dc.appendWithSpace(" " + toStringGetName());
   }
}
