package pasa.cbentley.framework.gui.src4.ctx.app;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.framework.src4.app.ConfigAppDefault;
import pasa.cbentley.framework.drawx.src4.tech.ITechGraphicsX;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;

public class ConfigAppGuiDefault extends ConfigAppDefault implements IConfigAppGui {

   public ConfigAppGuiDefault(UCtx uc, String name) {
      super(uc, name);
   }

   public int getToStringFlagsGui() {
      return IToStringFlagsGui.D_FLAG_06_TABLE_MODEL;
   }

   public int getThemeID() {
      return 0;
   }

   public int getMenuBarPosition() {
      return C.POS_1_BOT;
   }

   public int getDebugBarPosition() {
      return C.POS_0_TOP;
   }

   public int getInputStatusMode() {
      return ITechCanvasDrawable.KEY_STATUS_0_NONE;
   }

   public int getCmdProcessingMode() {
      return ITechCanvasDrawable.CMD_PRO_2_UI_NODE_REPO;
   }

   public int getPaintMode() {
      return ITechGraphicsX.MODE_0_SCREEN;
   }

   public boolean isUsingMenuBar() {
      return false;
   }

   public boolean isOneThumb() {
      return false;
   }

   public int getDebugMode() {
      return ITechCanvasDrawable.DEBUG_0_NONE;
   }

   public int isUsingCacheStyleClass() {
      return 0;
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ConfigAppGuiDefault.class, 70);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ConfigAppGuiDefault.class, 70);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug
   


}
