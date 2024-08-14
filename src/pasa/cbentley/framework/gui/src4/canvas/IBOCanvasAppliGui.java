package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.input.src4.interfaces.IBOCanvasAppli;

/**
 * Extends {@link IBOCanvasAppli} for drawable application
 * 
 */
public interface IBOCanvasAppliGui extends IBOCanvasAppli {

   public static final int CANVAS_APP_TYPE_SUB_GUI                       = 2;

   public static final int CANVAS_APP_DRW_BASIC_SIZE                     = CANVAS_APP_BASIC_SIZE + 16;

   /**
    * User wants to save view state.
    * <br>
    * When true, byte object sub id 0 is the view state.
    */
   public static final int CANVAS_APP_DRW_FLAG_1_USE_VIEW_STATE          = 1 << 0;

   /**
    * Optimize for one thumb
    */
   public static final int CANVAS_APP_DRW_FLAG_3_ONE_THUMB               = 1 << 2;

   /**
    * 
    */
   public static final int CANVAS_APP_DRW_FLAG_4_USE_MENU_BAR            = 1 << 3;

   public static final int CANVAS_APP_DRW_OFFSET_01_FLAG1                = CANVAS_APP_BASIC_SIZE;

   /**
    * Visual theme ID used by the view. convenience spot. Model is not supposed to know about theming. Why not anyways?
    * <br>
    * <br>
    * 
    */
   public static final int CANVAS_APP_DRW_OFFSET_02_VIEW_THEME_ID2       = CANVAS_APP_BASIC_SIZE + 1;

   /**
    * The last TopLevel id to be used a start page. What about the ViewState of that toplevel page?
    * The ViewState is stored as a sub.
    */
   public static final int CANVAS_APP_DRW_OFFSET_03_VIEW_START_PAGE2     = CANVAS_APP_BASIC_SIZE + 3;

   public static final int CANVAS_APP_DRW_OFFSET_04_MENU_BAR_POSITION1   = CANVAS_APP_BASIC_SIZE + 5;

   /**
    * <li> {@link C#POS_0_TOP}
    * <li> {@link C#POS_1_BOT}
    * <li> {@link C#POS_2_LEFT}
    * <li> {@link C#POS_3_RIGHT}
    */
   public static final int CANVAS_APP_DRW_OFFSET_05_DEBUG_BAR_POSITION1  = CANVAS_APP_BASIC_SIZE + 6;

   /**
    * 
    */
   public static final int CANVAS_APP_DRW_OFFSET_06_MENU_BAR_TYPE1       = CANVAS_APP_BASIC_SIZE + 7;

   /**
    * <li>{@link ITechCanvasDrawable#DEBUG_0_NONE}
    * <li>{@link ITechCanvasDrawable#DEBUG_1_LIGHT}
    * <li>{@link ITechCanvasDrawable#DEBUG_2_COMPLETE_1LINE}
    * <li>{@link ITechCanvasDrawable#DEBUG_3_COMPLETE_2LINES}
    * 
    */
   public static final int CANVAS_APP_DRW_OFFSET_07_DEBUG_MODE1          = CANVAS_APP_BASIC_SIZE + 8;

   /**
    * Visual cue to which keys were last pressed
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_0_NONE}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_1_OVERLAY_ANIMATED}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_2_OVERLAY_TIMED}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_3_COMPLETE}
    * 
    */
   public static final int CANVAS_APP_DRW_OFFSET_08_KEY_STATUS1          = CANVAS_APP_BASIC_SIZE + 9;

   /**
    * Default painting mode of the root canvas.
    * <br>
    * <li> {@link GraphicsX#MODE_0_SCREEN}
    * <li> {@link GraphicsX#MODE_1_IMAGE}
    * <li> {@link GraphicsX#MODE_2_RGB_IMAGE}
    * <li> {@link GraphicsX#MODE_3_RGB}
    * 
    */
   public static final int CANVAS_APP_DRW_OFFSET_09_PAINT_MODE1          = CANVAS_APP_BASIC_SIZE + 10;

   /**
    * When not zero, Fonts must be sized relative to the Screen size given to the Appli
    * <br>
    * A ratio of 10 with 100 pixels, require fonts to be more or less 10 pixels
    */
   public static final int CANVAS_APP_DRW_OFFSET_10_FONT_SCREEN_RATIO1   = CANVAS_APP_BASIC_SIZE + 11;

   /**
    * <li> {@link ITechCanvasDrawable#CMD_PRO_0_REPO_NODE_UI}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_2_UI_NODE_REPO}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_1_REPO_UI_NODE}
    * 
    */
   public static final int CANVAS_APP_DRW_OFFSET_11_CMD_PROCESSING_MODE1 = CANVAS_APP_BASIC_SIZE + 12;

}
