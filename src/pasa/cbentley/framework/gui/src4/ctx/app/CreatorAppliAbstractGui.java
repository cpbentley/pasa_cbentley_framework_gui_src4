package pasa.cbentley.framework.gui.src4.ctx.app;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.i8n.IStringProducer;
import pasa.cbentley.framework.cmd.src4.ctx.CmdCtx;
import pasa.cbentley.framework.cmd.src4.ctx.ConfigCmdCtxDef;
import pasa.cbentley.framework.cmd.src4.ctx.IConfigCmdCtx;
import pasa.cbentley.framework.core.data.src4.ctx.CoreDataCtx;
import pasa.cbentley.framework.core.framework.src4.app.IAppli;
import pasa.cbentley.framework.core.framework.src4.ctx.CoreFrameworkCtx;
import pasa.cbentley.framework.core.framework.src4.engine.CreatorAppliAbstract;
import pasa.cbentley.framework.coredraw.src4.ctx.CoreDrawCtx;
import pasa.cbentley.framework.datamodel.src4.ctx.ConfigDataModelDefault;
import pasa.cbentley.framework.datamodel.src4.ctx.DataModelCtx;
import pasa.cbentley.framework.datamodel.src4.ctx.IConfigDataModel;
import pasa.cbentley.framework.drawx.src4.ctx.ConfigDrawXDefault;
import pasa.cbentley.framework.drawx.src4.ctx.DrwCtx;
import pasa.cbentley.framework.drawx.src4.ctx.IConfigDrawX;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.config.ConfigGuiDef;
import pasa.cbentley.framework.gui.src4.ctx.config.IConfigAppGui;
import pasa.cbentley.framework.gui.src4.ctx.config.IConfigGui;
import pasa.cbentley.framework.input.src4.ctx.ConfigInputDefault;
import pasa.cbentley.framework.input.src4.ctx.IConfigInput;
import pasa.cbentley.framework.input.src4.ctx.InputCtx;
import pasa.cbentley.framework.localization.src4.ctx.IConfigLoc;
import pasa.cbentley.framework.localization.src4.ctx.LocalizationCtx;
import pasa.cbentley.layouter.src4.ctx.LayouterCtx;
import pasa.cbentley.powerdata.spec.src4.ctx.PDCtxA;

/**
 * {@link CreatorAppliAbstract}
 * 
 * Specific configurations for a GUI Appli
 * <li> {@link IConfigAppGui}
 * <li> {@link IConfigDataModel}
 * <li> 
 * @author Charles Bentley
 *
 */
public abstract class CreatorAppliAbstractGui extends CreatorAppliAbstract {

   protected final BOCtx boc;

   public CreatorAppliAbstractGui(BOCtx boc) {
      super(boc.getUC());
      this.boc = boc;
   }

   public IAppli createAppOnFramework(CoreFrameworkCtx cfc) {
      CoreDataCtx coreDataCtx = cfc.getCoreDataCtx();
      CoreDrawCtx cdc = cfc.getCUC().getCDC();
      LayouterCtx lac = new LayouterCtx(boc);
      IConfigDrawX configDrawX = getConfigDrawx(cfc);
      DrwCtx dc = new DrwCtx(configDrawX, cdc, lac);

      IConfigInput configInput = getConfigInput(cfc);
      InputCtx ic = new InputCtx(configInput, cfc);

      IStringProducer stringProducer = getStringProducer(cfc);
      PDCtxA pdc = getPDC(cfc);
      IConfigDataModel configData = getConfigDataModel(cfc);
      DataModelCtx dmc = new DataModelCtx(configData, boc, coreDataCtx, pdc);

      IConfigCmdCtx configCtx = getConfigCmd(cfc);
      CmdCtx cc = new CmdCtx(configCtx, cfc, pdc, stringProducer);

      IConfigGui config = getConfigGui(cfc);
      GuiCtx gc = new GuiCtx(config, cfc, ic, cc, boc, dc, dmc, stringProducer);
      return createAppMod(gc);
   }

   /**
    * Override for not a default
    * @param cfc
    * @return
    */
   protected IConfigInput getConfigInput(CoreFrameworkCtx cfc) {
      return new ConfigInputDefault(uc);
   }

   protected IConfigCmdCtx getConfigCmd(CoreFrameworkCtx cfc) {
      return new ConfigCmdCtxDef(uc);
   }

   protected IConfigGui getConfigGui(CoreFrameworkCtx cfc) {
      return new ConfigGuiDef(uc);
   }

   /**
    * Starting local suffix. It must be known in the hardcoded list of {@link LocalizationCtx}.
    * When null, use {@link LocalizationCtx#SUFFIX_01_EN}
    * @return
    */
   public String getLocalSuffix() {
      return "en";
   }

   /**
    * For speed or testing purposes, remove multi local support and force every module to use 
    * this language.
    * 
    * When settings are not null, root file paths are not read, the language saved in settings is used
    * 
    * if suffix file is not found, reverts back to en.
    * @return
    */
   public String getSingleLocaleSuffix() {
      return null;
   }

   /**
    * 
    * @param cfc
    * @return
    */
   protected IConfigDrawX getConfigDrawx(CoreFrameworkCtx cfc) {
      return new ConfigDrawXDefault(uc);
   }

   /**
    * 
    * @param cfc
    * @return
    */
   protected IConfigDataModel getConfigDataModel(CoreFrameworkCtx cfc) {
      return new ConfigDataModelDefault(uc);
   }

   /**
    * 
    * @param cfc
    * @return
    */
   protected abstract IStringProducer getStringProducer(CoreFrameworkCtx cfc);

   /**
    * 
    * @param cfc
    * @return
    */
   protected abstract PDCtxA getPDC(CoreFrameworkCtx cfc);

   /**
    * 
    * @param gc
    * @return
    */
   protected abstract IAppli createAppMod(GuiCtx gc);
}
