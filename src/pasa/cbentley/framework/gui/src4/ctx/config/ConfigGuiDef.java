package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.byteobjects.src4.ctx.ConfigAbstractBO;
import pasa.cbentley.core.src4.ctx.UCtx;

public class ConfigGuiDef extends ConfigAbstractBO implements IConfigGui {

   public ConfigGuiDef(UCtx uc) {
      super(uc);
   }

   public int toStringGetFlagsGui() {
      return 0;
   }

   public int getCaretBlinkMillisOn() {
      return 800;
   }

   public int getCaretBlinkMillisOff() {
      return 600;
   }

}
