package pasa.cbentley.framework.gui.src4.ctx.app;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IConfigBO;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.core.framework.src4.app.AppCtx;
import pasa.cbentley.framework.core.framework.src4.app.IBOCtxSettingsAppli;
import pasa.cbentley.framework.core.framework.src4.app.IConfigApp;
import pasa.cbentley.framework.datamodel.src4.ctx.DataModelCtx;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.ctx.config.IConfigAppGui;
import pasa.cbentley.framework.input.src4.ctx.InputCtx;
import pasa.cbentley.powerdata.spec.src4.ctx.PDCtxA;

/**
 * Abstract context class to be used by AppGuis
 * @author Charles Bentley
 *
 */
public abstract class AppGuiCtx extends AppCtx implements ITechCtxSettingsAppGui {

   protected final GuiCtx gc;

   public AppGuiCtx(IConfigApp config, GuiCtx gc, IStringProducer strings) {
      super(config, gc.getCFC(), strings);
      this.gc = gc;
   }

   public AppGuiCtx(IConfigApp config, GuiCtx gc) {
      super(config, gc.getCFC());
      this.gc = gc;
   }

   public abstract int getCtxID();

   public CmdCtx getCC() {
      return gc.getCC();
   }

   public void a_Init() {
      super.a_Init();
   }

   public int getBOCtxSettingSize() {
      return ITechCtxSettingsAppGui.CTX_GUI_BASIC_SIZE;
   }

   protected void matchConfig(IConfigBO config, ByteObject settings) {
      super.matchConfig(configApp, settings);

      IConfigAppGui configApp = (IConfigAppGui) config;
      settings.setFlag(CTX_APP_OFFSET_02_FLAGX, CTX_APP_FLAGX_2_DRAG_DROP, configApp.isAppDragDropEnabled());

      settings.setFlag(CTX_GUI_OFFSET_01_FLAG1, CTX_GUI_FLAG_3_ONE_THUMB, configApp.isOneThumb());
      settings.setFlag(CTX_GUI_OFFSET_01_FLAG1, CTX_GUI_FLAG_2_USER_MENU_BAR, configApp.isUsingMenuBar());

      settings.set1(CTX_GUI_OFFSET_02_VIEW_THEME_ID2, configApp.getThemeID());

      settings.set1(CTX_GUI_OFFSET_04_MENU_BAR_POSITION1, configApp.getMenuBarPosition());
      settings.set1(CTX_GUI_OFFSET_05_DEBUG_BAR_POSITION1, configApp.getDebugBarPosition());
      settings.set1(CTX_GUI_OFFSET_07_DEBUG_MODE1, configApp.getDebugMode());
   }

   public DrwCtx getDC() {
      return gc.getDC();
   }

   public DataModelCtx getDMC() {
      return gc.getDMC();
   }

   public GuiCtx getGC() {
      return gc;
   }

   public InputCtx getIC() {
      return gc.getIC();
   }

   public PDCtxA getPDCa() {
      return getDMC().getPDCa();
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, AppGuiCtx.class);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, AppGuiCtx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
