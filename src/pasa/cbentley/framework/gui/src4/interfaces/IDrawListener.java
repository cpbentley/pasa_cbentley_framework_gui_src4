package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.canvas.GraphicsXD;
import pasa.cbentley.framework.gui.src4.core.Drawable;

/**
 * Allows one class to manage the drawings and initializations of several Drawables itself.
 * <br>
 * <br>
 * Separates the structure from the drawings.<br>
 * Similar to CommandListener model.<br>
 * <br>
 * Listener
 * @author Charles-Philip Bentley
 *
 */
public interface IDrawListener {

   /**
    * Ask the draw controller to draw the {@link Drawable}'s content in the given area. 
    * <br>
    * <br>
    * Call {@link Drawable#draw(GraphicsXD)}  ?
    * <br>
    * <br>
    * @param g
    * @param x coordinate of content 
    * @param y coordinate of content
    * @param w width available for content
    * @param h height available for content
    * @param d reference to Drawable to be drawn. Used to know
    */
   public void drawContentListen(GraphicsXD g, int x, int y, int w, int h, Drawable d);

   /**
    * Computes the width and height of Drawable based on the Sizers.
    * <br>
    * Listener may decide based on its own context to size the Drawable as it wishes.
    * <br>
    * When returning false, Drawable will init using its Sizers.
    * <br>
    * Sets the {@link Drawable} dw, dh with {@link Drawable#setDrawableSize(int, int)}.
    * <br>
    * <br>
    * Called by Listened/Injected Drawable. The method must resolve implicit w and h values (<= 0) 
    * <br>
    * <br>
    * Implementation's responsability to set {@link ITechDrawable#STATE_05_LAYOUTED}
    * <br>
    * <br>
    * {@link Drawable} style has been validated.
    * <br>
    * @param w total width
    * @param h total height
    * @param d
    * @return true if class could initialize the Drawable
    */
   public boolean initListen(ByteObject sizerW, ByteObject sizerH, Drawable d);
}
