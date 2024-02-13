package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.framework.core.src4.app.ConfigAppAbstract;
import pasa.cbentley.framework.coreui.src4.tech.ITechInputConstants;
import pasa.cbentley.framework.drawx.src4.tech.ITechGraphicsX;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;

public abstract class ConfigAppGuiAbstractFlat extends ConfigAppAbstract implements IConfigAppGui {

   public ConfigAppGuiAbstractFlat(UCtx uc, String name) {
      super(uc, name);
   }

   public int getThemeID() {
      return 0;
   }

   public boolean isOneThumb() {
      return false;
   }

   public int isUsingCacheStyleClass() {
      return 0;
   }

   public boolean isAppDragDropEnabled() {
      return true;
   }

   public int getDebugMode() {
      return ITechCanvasDrawable.DEBUG_0_NONE;
   }

   public int getAllerRetourMinAmplitudePixel() {
      return ITechInputConstants.BF_ALLER_RETOUR_MIN_AMPLITUDE;
   }

   public int getMenuBarPosition() {
      return C.POS_1_BOT;
   }

   /**
    * <li>{@link C#POS_0_TOP}
    * <li>{@link C#POS_1_BOT}
    * <li>{@link C#POS_2_LEFT}
    * <li>{@link C#POS_3_RIGHT}
    * @return
    */
   public int getDebugBarPosition() {
      return C.POS_0_TOP;
   }

   public int getInputStatusMode() {
      return ITechCanvasDrawable.KEY_STATUS_3_COMPLETE;
   }

   public boolean isUsingMenuBar() {
      return false;
   }

   /**
    * <li> {@link ITechCanvasDrawable#CMD_PRO_0}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_1}
    * <li> {@link ITechCanvasDrawable#CMD_PRO_2}        
    * @return
    */
   public int getCmdProcessingMode() {
      return ITechCanvasDrawable.CMD_PRO_0;
   }

   /**
    * <li> {@link ITechGraphicsX#MODE_0_SCREEN}
    * <li> {@link ITechGraphicsX#MODE_1_IMAGE}
    * <li> {@link ITechGraphicsX#MODE_2_RGB_IMAGE}
    * <li> {@link ITechGraphicsX#MODE_3_RGB}
    * @return
    */
   public int getPaintMode() {
      return ITechGraphicsX.MODE_0_SCREEN;
   }
}
