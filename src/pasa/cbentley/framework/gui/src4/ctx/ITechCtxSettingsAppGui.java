package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.interfaces.IBOCtxSettings;
import pasa.cbentley.framework.core.framework.src4.app.IBOCtxSettingsAppli;
import pasa.cbentley.framework.drawx.src4.engine.GraphicsX;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;

public interface ITechCtxSettingsAppGui extends IBOCtxSettingsAppli {

   public static final int CANVAS_ID_LOGVIEWER                    = 2;

   public static final int CTX_GUI_BASIC_SIZE                     = CTX_APP_BASIC_SIZE + 15;

   public static final int OFFSET_START                           = CTX_APP_BASIC_SIZE;

   /**
    * User wants to save view state.
    * <br>
    * When true, byte object sub id 0 is the view state.
    */
   public static final int CTX_GUI_FLAG_1_USE_VIEW_STATE          = 1 << 0;

   /**
    * 
    */
   public static final int CTX_GUI_FLAG_2_USER_MENU_BAR           = 1 << 1;

   /**
    * Optimize for one thumb
    */
   public static final int CTX_GUI_FLAG_3_ONE_THUMB               = 1 << 2;

   /**
    * {@link GuiCtx} level decision to use.
    * Toggle on/off any kind of cache implemented
    */
   public static final int CTX_GUI_FLAGS_1_USE_STYLECLASS_CACHE   = 1 << 1;

   public static final int CTX_GUI_OFFSET_01_FLAG1                = OFFSET_START;

   public static final int CTX_GUI_OFFSET_02_FLAGS1               = OFFSET_START + 1;

   /**
    * Visual theme ID used by the view. convenience spot. Model is not supposed to know about theming. Why not anyways?
    * <br>
    * <br>
    * 
    */
   public static final int CTX_GUI_OFFSET_02_VIEW_THEME_ID2       = OFFSET_START + 1;

   /**
    * The last TopLevel id to be used a start page. What about the ViewState of that toplevel page?
    * The ViewState is stored as a sub.
    */
   public static final int CTX_GUI_OFFSET_03_VIEW_START_PAGE2     = OFFSET_START + 3;

   /**
    * 
    */
   public static final int CTX_GUI_OFFSET_04_MENU_BAR_POSITION1   = OFFSET_START + 5;

   public static final int CTX_GUI_OFFSET_05_DEBUG_BAR_POSITION1  = OFFSET_START + 6;

   public static final int CTX_GUI_OFFSET_06_MENU_BAR_TYPE1       = OFFSET_START + 7;

   /**
    * <li>{@link ITechCanvasDrawable#DEBUG_0_NONE}
    * <li>{@link ITechCanvasDrawable#DEBUG_1_LIGHT}
    * <li>{@link ITechCanvasDrawable#DEBUG_2_COMPLETE_1LINE}
    * <li>{@link ITechCanvasDrawable#DEBUG_3_COMPLETE_2LINES}
    * 
    */
   public static final int CTX_GUI_OFFSET_07_DEBUG_MODE1          = OFFSET_START + 7;

   /**
    * Visual cue to which keys were last pressed
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_0_NONE}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_1_OVERLAY_ANIMATED}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_2_OVERLAY_TIMED}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_3_COMPLETE}
    * 
    */
   public static final int CTX_GUI_OFFSET_08_KEY_STATUS1          = OFFSET_START + 7;

   /**
    * Default painting mode of the root canvas.
    * <br>
    * <li> {@link GraphicsX#MODE_0_SCREEN}
    * <li> {@link GraphicsX#MODE_1_IMAGE}
    * <li> {@link GraphicsX#MODE_2_RGB_IMAGE}
    * <li> {@link GraphicsX#MODE_3_RGB}
    * 
    */
   public static final int CTX_GUI_OFFSET_09_PAINT_MODE1          = OFFSET_START + 8;

   /**
    * When not zero, Fonts must be sized relative to the Screen size given to the Appli
    * <br>
    * A ratio of 10 with 100 pixels, require fonts to be more or less 10 pixels
    */
   public static final int CTX_GUI_OFFSET_10_FONT_SCREEN_RATIO1   = OFFSET_START + 10;

   /**
    * <li> {@link ITechCanvasDrawable#CMD_PRO_0_REPO_NODE_UI}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_2_UI_NODE_REPO}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_1_REPO_UI_NODE}
    * 
    */
   public static final int CTX_GUI_OFFSET_11_CMD_PROCESSING_MODE1 = OFFSET_START + 11;

   /**
    * Kind of cache to be used by {@link StyleClass}
    */
   public static final int CTX_GUI_OFFSET_12_STYLE_CACHE_ID1      = OFFSET_START + 11;

   /**
    * The color used to erase the canvas before drawing
    */
   public static final int CTX_GUI_OFFSET_13_ERASE_BG_COLOR4      = OFFSET_START + 12;

}
