package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.framework.core.src4.app.ConfigAppAbstract;
import pasa.cbentley.framework.coreui.src4.tech.IInputConstants;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;

public abstract class ConfigAppGuiAbstractFlat extends ConfigAppAbstract {

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
      return IInputConstants.BF_ALLER_RETOUR_MIN_AMPLITUDE;
   }

}
