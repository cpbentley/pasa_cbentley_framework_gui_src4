package pasa.cbentley.framework.gui.src4.interfaces;

import pasa.cbentley.core.src4.interfaces.ITech;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechGraphics;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.string.StringDrawable;

public interface ITechCanvasDrawable extends ITech {

   public static final int ANCHOR                        = ITechGraphics.TOP | ITechGraphics.LEFT;

   public static final int DEBUG_0_NONE                  = 0;

   /**
    * A Small overlay in the Top 
    */
   public static final int DEBUG_1_LIGHT                 = 1;

   /**
    * 1 line of text
    */
   public static final int DEBUG_2_COMPLETE_1LINE        = 2;

   /**
    * 2 lines of text
    */
   public static final int DEBUG_3_COMPLETE_2LINES       = 3;

   public static final int KEY_STATUS_3_COMPLETE         = 3;

   /**
    * 
    */
   public static final int KEY_STATUS_0_NONE             = 0;

   public static final int KEY_STATUS_1_OVERLAY_ANIMATED = 1;

   public static final int KEY_STATUS_2_OVERLAY_TIMED    = 2;

   /**
    * Double Buffer for Animations
    * 1 st layer is for normal paint cycle
    * 2 nd layer is for animation repaints.
    * 
    * Switch to this mode, when code needs to run "flying" animation over the screen
    * The animation is able to just reuse 1st layer (as a cache service) of the whole MasterCanvas
    * 
    * 1 layer is the screen intermediary layer in order to access pixel data
    * 
    * 
    * So when an animation must repaint itself, it takes destination buffer, erase itself by taking pixels from Background buffer
    * and blends ifself with the new pixels in the destination buffer
    * At the end of the repaint. The destination buffer is drawn to the Screen Grapchis 
    * 
    * When a full Repaint call occurs, the background buffer is updated. copied to destination buffer
    *  then all animations are drawn and blended on the destination buffer.
    *  
    * The background buffer is equal to the active Drawable buffer + menu bar buffer or top header buffer
    * Uses: Many moving animations at different speeds
    * 
    */
   public static final int PAINT_MODE_ANIMATION_BUFFER   = 3;

   /**
    * Next step up from Simple is for the GraphicsX object to draw on RgbImage
    * At the end of the paint method. The image
    * Yes Rotations. At the end, the RgbImage of GraphicsX is drawn to SCreen Graphics with a ImageTransformation
    * Yes Pixell Queries
    * No Erase
    * Yes Keep Old
    * Uses:
    * #1: when a snapshot of the screen is needed.
    * #2: when a screen rotation is used
    */
   public static final int PAINT_MODE_BACKGROUND         = 1;

   /**
    * Full Service.
    * Everytime, there are 2 layers. When Paint Cycle draws on a Fully Transparent RgbImage
    * It may ask what were the values of the old paint cycle.
    * At the end of the Paint cycle n, pixels of n and n-1 are blended together and the 
    * result is paint to the Screen Graphics 
    * Yes Rotations
    * Yes Pixel Queries
    * Yes Erase
    * Yes Keep Old 
    */
   public static final int PAINT_MODE_FULL_DOUBLE_BUFFER = 2;

   public static final int PAINT_STAT_0_FULL             = 0;

   public static final int PAINT_STAT_1_ANIM             = 1;

   public static final int PAINT_STAT_2_BUSINESS         = 2;

   public static final int PAINT_STAT_3_DRAWABLE         = 3;

   /**
    * Set to true when  all the items (background, drawables, animations, debug, menubar etc.) have to be repainted.
    * <br>
    * The whole screen is erased with Canvas bgColor, background is drawn, drawables unless hidden and then animated images
    */
   public static final int REPAINT_01_FULL               = 1 << 0;

   /**
    * Flag for repainting the content of Canvas
    */
   public static final int REPAINT_02_CONTENT            = 1 << 1;

   /**
    * Flag saying the MenuBar should be repaint
    */
   public static final int REPAINT_03_MENUBAR            = 1 << 2;

   /**
    * Flag telling the paint method to draw animation by the Realisator
    * The advantages of this dissociates between a User Repaint that requires a refresh of business graphical objects
    * and Animation Repaints.
    * <br>
    * <br>
    * When an Animation timer request a repaint, it will draw Business graphical objects layer which is fast 
    */
   public static final int REPAINT_04_ANIMATION          = 1 << 3;

   /**
    * Should the background definition (default is white color)
    * be painted to hide the last painting cycle.
    */
   public static final int REPAINT_05_BACKGROUND         = 1 << 4;

   /**
    * Flags.
    */
   public static final int REPAINT_06_DRAWABLE           = 1 << 5;

   /**
    * Flag for the Message foreground DLayer to display {@link StringDrawable} for that purpose.
    */
   public static final int REPAINT_07_MESSAGE            = 1 << 6;

   /**
    * When No Menubar for hosting the status
    */
   public static final int REPAINT_08_STATUS             = 1 << 7;

   /**
    * Repaint is external
    */
   public static final int REPAINT_09_EXTERN             = 1 << 8;

   public static final int REPAINT_10_SPECIAL            = 1 << 9;

   /**
    * When set, ignores states in the {@link IDrawable#draw(GraphicsX)} method.
    * <li> {@link ITechDrawable#STATE_03_HIDDEN}
    * <li> {@link ITechDrawable#STATE_02_DRAWN}
    * <li> {@link ITechDrawable#STATE_20_ANIMATED_FULL_HIDDEN}
    * 
    * <br>
    * <br>
    * Caching services still work.
    * <br>
    * Sets the {@link ITechDrawable#BEHAVIOR_11_DISABLE_CACHING} to be 100% sure no caching is done.
    */
   public static final int REPAINT_11_NO_FLOW            = 1 << 10;

   public static final int REPAINT_12_NO_FLOW_WITH_CACHE = 1 << 11;

   /**
    * Force the {@link IDrawable#draw(GraphicsX)} to paint raw pixels without ANY flow control or cache draws. 
    * <br>
    * <br>
    * Cannot be defined in {@link GraphicsX}, because that class is not aware of that business logic
    */
   public static final int REPAINT_13_CACHE_UPDATE       = 1 << 12;

   /**
    * Set when {@link GraphicsX} context is used for repainting the user screen
    */
   public static final int REPAINT_14_SYSTEM_REPAINT     = 1 << 13;

   public static final int REPAINT_15_SCREEN_SHOT        = 1 << 14;

   /**
    * Normal rotation
    */
   public static final int SCREEN_0_TOP_NORMAL           = 0;

   public static final int SCREEN_1_BOT_UPSIDEDOWN       = 1;

   public static final int SCREEN_2_LEFT_ROTATED         = 2;

   public static final int SCREEN_3_RIGHT_ROTATED        = 3;

   /**
    * 
    */
   public static final int CMD_PRO_0                     = 0;

   public static final int CMD_PRO_1                     = 1;

   public static final int CMD_PRO_2                     = 2;

   /**
    * Replaces the bottom layer {@link IDrawable} with the new {@link IDrawable}.
    * 
    * All previous DLayer have their exit animation run together.
    * 
    * Old layer state is now hidden
    * <p>
    * A {@link IDrawable} shown with this directive gets its entry animation played.
    * </p>
    */
   public static final int SHOW_TYPE_0_REPLACE_BOTTOM    = 0;

   /**
    * <p>
    * Draws new {@link IDrawable} over the active {@link IDrawable} on a new layer.
    * This is used for a popup {@link IDrawable}. Other layers are not modified and kept active.
    * </p>
    * 
    * <p>
    * TODO If new drawable completely hides the old, it gets a special state {@link ITechDrawable#STATE_19_HIDDEN_OVER}.
    * When reappearing, will entry animation be fired up? We don't always want that.
    * Depends on the animation. When animation is constrained to Drawable area, then it is
    * possible.
    * One side Transition where only new Drawable executes its Entry/Exit animations.
    * </p>
    * <p>
    * A {@link IDrawable} shown with this directive gets its entry animation played.
    * </p>
    */
   public static final int SHOW_TYPE_1_OVER_TOP          = 1;

   /**
    * Same as {@link ITechCanvasDrawable#SHOW_TYPE_1_OVER_TOP} but all layers below
    * are inactive and cannot be interacted. It is the equivalent of activated modal option in J2SE.
    * 
    * <p>
    * TODO Could use a Style state.
    * TODO In this mode it may be desirable to draw a visual cue telling that layer below and its {@link IDrawable} are not selectable anymore.
    * </p>
    * 
    * <p>
    * A {@link IDrawable} shown with this directive gets its entry animation played.
    * </p>
    */
   public static final int SHOW_TYPE_2_OVER_INACTIVE     = 2;

   public static final int CMD_EVENT_4_UIEVENT           = 4;

}
