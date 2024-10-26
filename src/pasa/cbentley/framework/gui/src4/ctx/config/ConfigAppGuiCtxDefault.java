package pasa.cbentley.framework.gui.src4.ctx.config;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.C;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.framework.core.framework.src4.app.ConfigAppAbstract;
import pasa.cbentley.framework.core.framework.src4.app.IConfigApp;
import pasa.cbentley.framework.drawx.src4.tech.ITechGraphicsX;
import pasa.cbentley.framework.gui.src4.ctx.IToStringFlagsGui;
import pasa.cbentley.framework.gui.src4.ctx.app.IConfigAppGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;

public class ConfigAppGuiCtxDefault extends ConfigAppAbstract implements IConfigAppGui {

   public ConfigAppGuiCtxDefault(UCtx uc) {
      super(uc, "DefaultAppName");
   }

   public ConfigAppGuiCtxDefault(UCtx uc, String name) {
      super(uc, name);
   }

   public IConfigApp cloneMe(UCtx uc, String name) {
      // TODO Auto-generated method stub
      return null;
   }

   public String getAppIcon() {
      // TODO Auto-generated method stub
      return null;
   }

   public int getCmdProcessingMode() {
      return ITechCanvasDrawable.CMD_PRO_0_REPO_NODE_UI;
   }

   public int getDebugBarPosition() {
      return C.POS_0_TOP;
   }

   public int getDebugMode() {
      return ITechCanvasDrawable.DEBUG_0_NONE;
   }

   public int toStringGetFlagsGui() {
      int flags = 0;
      flags = BitUtils.setFlag(flags, IToStringFlagsGui.D_FLAG_11_VP_HEADERS, false);
      flags = BitUtils.setFlag(flags, IToStringFlagsGui.D_FLAG_17_VIEW_PANE, true);
      flags = BitUtils.setFlag(flags, IToStringFlagsGui.D_FLAG_01_STYLE, false);
      return flags;
   }

   public int getInputStatusMode() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getMenuBarPosition() {
      return C.POS_1_BOT;
   }

   public int getPaintMode() {
      return ITechGraphicsX.MODE_0_SCREEN;
   }

   public int getThemeID() {
      return 0;
   }

   public boolean isAppDragDropEnabled() {
      // TODO Auto-generated method stub
      return false;
   }

   public boolean isOneThumb() {
      return false;
   }

   public int isUsingCacheStyleClass() {
      return 0;
   }

   public boolean isUsingMenuBar() {
      return false;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ConfigAppGuiCtxDefault.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ConfigAppGuiCtxDefault.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
