package pasa.cbentley.framework.gui.src4.ctx;

import pasa.cbentley.core.src4.ctx.SettingsWrapperAbstract;

public class SettingsWrapperGui extends SettingsWrapperAbstract implements ITechCtxSettingsAppGui {

   protected final GuiCtx gc;

   public SettingsWrapperGui(GuiCtx gc) {
      super(gc);
      this.gc = gc;
   }
   
   public boolean hasStyleClassCache() {
      return gc.getSettingsBO().hasFlag(CTX_GUI_OFFSET_02_FLAGS1, CTX_GUI_FLAGS_1_USE_STYLECLASS_CACHE);
   }
}
