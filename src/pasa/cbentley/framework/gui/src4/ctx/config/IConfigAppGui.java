package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.byteobjects.src4.ctx.IConfigBO;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.core.src4.app.IConfigApp;
import pasa.cbentley.framework.drawx.src4.tech.ITechGraphicsX;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;
import pasa.cbentley.framework.gui.src4.ctx.app.AppGuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.input.src4.interfaces.IBOCanvasAppli;

/**
 * Configuration for {@link AppGuiCtx}
 * 
 * <p>
 * Its a {@link IConfigBO}
 * </p>
 * @author Charles Bentley
 *
 */
public interface IConfigAppGui extends IConfigApp {

   /**
    * {@link IToStringFlagsGui}
    * @return
    */
   public int getFlagsGui();

   /**
    * 
    * @return
    */
   public int getThemeID();

   /**
    * Position of the menu bar.
    * <br>
    * Relevant for Canvas without a specific {@link IBOCanvasAppli}.
    * 
    * <li>{@link C#POS_0_TOP}
    * <li>{@link C#POS_1_BOT}
    * <li>{@link C#POS_2_LEFT}
    * <li>{@link C#POS_3_RIGHT}
    * 
    * @return
    */
   public int getMenuBarPosition();

   /**
    * <li>{@link C#POS_0_TOP}
    * <li>{@link C#POS_1_BOT}
    * <li>{@link C#POS_2_LEFT}
    * <li>{@link C#POS_3_RIGHT}
    * @return
    */
   public int getDebugBarPosition();

   /**
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_0_NONE}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_1_OVERLAY_ANIMATED}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_2_OVERLAY_TIMED}
    * <li>{@link ITechCanvasDrawable#KEY_STATUS_3_COMPLETE}
    * @return
    */
   public int getInputStatusMode();

   /**
    * <li> {@link ITechCanvasDrawable#CMD_PRO_0}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_1}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_2}        
    * @return
    */
   public int getCmdProcessingMode();

   /**
    * <li> {@link ITechGraphicsX#MODE_0_SCREEN}
    * <li> {@link ITechGraphicsX#MODE_1_IMAGE}
    * <li> {@link ITechGraphicsX#MODE_2_RGB_IMAGE}
    * <li> {@link ITechGraphicsX#MODE_3_RGB}
    * @return
    */
   public int getPaintMode();

   /**
    * True when a menu bar is wanted.
    * <br>
    * Relevant for Canvas without a specific {@link IBOCanvasAppli}.
    * @return
    */
   public boolean isUsingMenuBar();

   /**
    * 
    * @return
    */
   public boolean isOneThumb();

   /**
    * Should it display a drawable with debug information
    * <li>{@link ITechCanvasDrawable#DEBUG_0_NONE}
    * <li>{@link ITechCanvasDrawable#DEBUG_1_LIGHT}
    * <li>{@link ITechCanvasDrawable#DEBUG_2_COMPLETE}
    * <li>{@link ITechCanvasDrawable#DEBUG_3_2COMPLETE}
    * @return
    */
   public int getDebugMode();

   /**
    * Kind of cache to be used by {@link StyleClass}
    * 0 for none
    * @return
    */
   public int isUsingCacheStyleClass();
}
