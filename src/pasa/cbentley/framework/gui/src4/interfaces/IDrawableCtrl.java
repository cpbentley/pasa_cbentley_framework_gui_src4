package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.framework.gui.src4.canvas.CanvasDrawControl;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;

/**
 * Interface to the put and remove Drawables .
 * <br>
 * <br>
 * There are two topologies.
 * <br>
 * One topology deals with the whole Canvas Screen available.
 * The second topology deals with the App Rectangle. The usable pixel space
 * for the appli drawables.
 * <br>
 * Who might be surronded by artifacts such as menus, system bars etc.
 * 
 * There is one {@link IDrawableCtrl} for each topologies.
 * <br>
 * 
 * @see CanvasDrawControl
 * 
 * @author Charles Bentley
 *
 */
public interface IDrawableCtrl {


   /**
    * Removes drawable from Canvas if it is there. 
    * <br>
    * <br>
    * 
    * If successful, and if in current KeyFocus, puts parent to focus or newfocus
    * <br>
    * <br>
    * Which brings the window 
    * <br>
    * <br>
    * Only lib classes should call this method
    * <br>s
    * @param ic can be null, if so {@link Controller} is used.
    * @param d
    * @param newFocus the drawable which will get the focus
    */
   public void removeDrawable(ExecutionContextCanvasGui ec, IDrawable d, IDrawable newFocus);

   /**
    * 
    * @param d
    */
   public void shHideDrawable(ExecutionContextCanvasGui ec, IDrawable d);

   /**
    * Shows the Drawable 
    * @param view
    * @param type
    */
   public void shShowDrawable(ExecutionContextCanvasGui ec, IDrawable view, int type);

   /**
    * Draws {@link IDrawable} using {@link ITechCanvasDrawable#SHOW_TYPE_1_OVER_TOP}.
    * <br>
    * on the {@link ViewContext} of the Drawable.
    * <br>
    * Drawable takes the focus and loads its cmd in the Menu bar system.
    * <br>
    * When Drawable does not have a {@link ViewContext}, this is considered an Error.
    * But we don't want to crash the application for that.
    * And it should not force to catch.
    * <br>
    * @param d cannot be null
    */
   public void shShowDrawableOver(ExecutionContextCanvasGui ec, IDrawable d);

   public void shShowDrawableOverCmds(ExecutionContextCanvasGui ec, IDrawable d);

   public void shShowDrawableOverNoCmds(ExecutionContextCanvasGui ec, IDrawable d);

   public void shShowDrawableOverNoFocus(ExecutionContextCanvasGui ec, IDrawable d);
}
