package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.interfaces.IHostFeature;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdExecutor;
import pasa.cbentley.framework.cmd.src4.interfaces.ICmdsCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmd;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmdViewLog;
import pasa.cbentley.framework.core.framework.src4.interfaces.IHostCoreTools;
import pasa.cbentley.framework.core.ui.src4.tech.IBOCanvasHost;
import pasa.cbentley.framework.core.ui.src4.tech.ITechHostFeatureDrawUI;
import pasa.cbentley.framework.core.ui.src4.tech.ITechInputFeedback;
import pasa.cbentley.framework.core.ui.src4.utils.ViewState;
import pasa.cbentley.framework.coredraw.src4.engine.VisualState;
import pasa.cbentley.framework.drawx.src4.engine.RgbImage;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.CanvasDebugger;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.canvas.RgbImageSaveTask;
import pasa.cbentley.framework.gui.src4.canvas.ViewContext;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeCanvasDebugger;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.ctx.ObjectGC;
import pasa.cbentley.framework.gui.src4.ctx.app.AppliGui;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.menu.IMenus;
import pasa.cbentley.framework.gui.src4.mui.MLogViewer;
import pasa.cbentley.layouter.src4.engine.SizerFactory;

public class CmdExectutorGui extends ObjectGC implements ICmdExecutor, ICmdsCmd, ICmdsGui, ITechCmd, ITechInputFeedback {

   public CmdExectutorGui(GuiCtx gc) {
      super(gc);
   }


   public int commandEvent(int evType, Object param) {
      // TODO Auto-generated method stub
      return 0;
   }

   public CmdNode getCmdNode() {
      return cc.getCmdNodeRoot();
   }

 
   private CmdInstance          activeCmdLang;

   /**
    * Set to the command currently being active.
    * <br>
    * <br>
    * Used for commands that have several steps.
    * <br>
    * <br>
    * 
    */
   private CmdInstance          activeCommand;

   private CmdNode              currentLocation;


   /**
    * Shows helps data.
    * <br>
    * 
    */
   private HelperStringDrawable strDrawableHelpData;


   /**
    * 
    * @param cmd
    */
   protected void cmdAccept(CmdInstanceGui cmd) {
      if (activeCommand != null) {
         //TODO check the context in which accept was called.
         //Help dialog shows OK. MORE.
         MCmd m = cmd.getParentCmdDef();
         if (m != null && m.getCmdId() == CMD_39_HELP) {
            //TODO send back to help? as a listener? HELP. OK. HELP MORE?
            strDrawableHelpData.removeDrawable(null, null);
            activeCommand = null;
            //repaint a drawable that is now hidden = repaint background layer.
            cmd.actionDone(strDrawableHelpData, 0);
            //set back the previous context.

         }
      }
   }

   protected void cmdDebug() {

      //#debug
      toDLog().pAlways("", gc.getCanvasGCRoot(), CommanderGui.class, "cmdDebug@115", LVL_08_INFO, false);
   }

   protected void cmdDebugCanvasChange(CmdInstanceGui cd) {
      CmdFactoryGui cfg = gc.getCmdFactoryGui();
      MCmdGuiChangeCanvasDebugger cmdModelDebug = cfg.getMCmdGuiChangeCanvasDebugger();
      cd.setBluePrint(cmdModelDebug);
      CanvasAppliInputGui canvas = gc.getCanvasGCRoot().getCanvas();
      cmdModelDebug.setParamsDirective(cd, canvas, ITechCmd.DIRECTIVE_4_ASK_GUI);
   }

   protected void cmdDebugCanvasIterateUp(CmdInstanceGui cd) {
      CmdFactoryGui cfg = gc.getCmdFactoryGui();
      MCmdGuiChangeCanvasDebugger cmdModelDebug = cfg.getMCmdGuiChangeCanvasDebugger();
      cd.setBluePrint(cmdModelDebug);
      CanvasAppliInputGui canvas = gc.getCanvasGCRoot().getCanvas();
      cmdModelDebug.setParamsDirective(cd, canvas, ITechCmd.DIRECTIVE_2_ITERATE_UP);
   }

   protected void cmdDebugCanvasIterateDown(CmdInstanceGui cd) {
      CmdFactoryGui cfg = gc.getCmdFactoryGui();
      MCmdGuiChangeCanvasDebugger cmdModelDebug = cfg.getMCmdGuiChangeCanvasDebugger();
      cd.setBluePrint(cmdModelDebug);
      CanvasAppliInputGui canvas = gc.getCanvasGCRoot().getCanvas();
      cmdModelDebug.setParamsDirective(cd, canvas, ITechCmd.DIRECTIVE_3_ITERATE_DOWN);
   }

   /**
    * Toggle the debug mode to the canvases in the command ctx
    * The currently active canvas
    */
   protected void cmdDebugCanvasToggle(CmdInstanceGui cd) {

      //ci must be

      CmdFactoryGui cfg = gc.getCmdFactoryGui();
      MCmdGuiChangeCanvasDebugger cmdModelDebug = cfg.getMCmdGuiChangeCanvasDebugger();
      cd.setBluePrint(cmdModelDebug);
      CanvasAppliInputGui canvas = gc.getCanvasGCRoot().getCanvas();
      cmdModelDebug.setParamsToggle(cd, canvas);

      //
   }

   private void cmdDebugFlags() {
      // TODO Auto-generated method stub

   }

   protected void cmdFontSizeDecrease(CmdInstanceGui cd) {
      InputConfig ic = cd.getInputConfig();
      VisualState vs = gc.getCDC().getFontFactory().fontSizeDecrease();

      //font definitions to implementation are refreshed each paint cycle
      ic.srRenewLayout();
      ic.srActionDoneRepaintMessage("Font Sizes changed to " + vs.toString1Line(), 1000);

   }

   /**
    * Increment all the base fonts of the System.
    * <br>
    * The Font size in pixel will modify the borders padding and other pixel size metrics
    * <br>
    * TODO Save Font settings in user profile
    */
   protected void cmdFontSizeIncrease(CmdInstanceGui cd) {
      InputConfig ic = cd.getInputConfig();
      VisualState vs = gc.getCDC().getFontFactory().fontSizeIncrease();

      //font definitions to implementation are refreshed each paint cycle
      ic.srRenewLayout();
      ic.srActionDoneRepaintMessage("Font Sizes changed to " + vs.toString1Line(), 1000);
      //update
      //getDevice().getEventChannel().createEvent(producerID, eventID, producer)
      //
   }

   protected void cmdHelp(CmdInstance cmd) {
      if (strDrawableHelpData == null) {
         StyleClass scMenu = gc.getStyleClass(IUIView.SC_0_BASE_TABLE);
         strDrawableHelpData = new HelperStringDrawable(gc, scMenu);
      }

      //provide the close button and close menu + back command that still works
      activeCommand = cmd;

      //update help string
      String helpString = "Where do i get it?";
      strDrawableHelpData.setStringNoUpdate(helpString);
      //size is a function of screen size. TODO size as a function. use the device driver
      //percentage
      SizerFactory sizerFactory = gc.getLAC().getFactorySizer();
      ByteObject boW = sizerFactory.getSizerRatio100ViewCtx(80);
      //height is 30%
      ByteObject boH = sizerFactory.getSizerRatio100ViewCtx(30);
      strDrawableHelpData.setSizers(boW, boH);
      strDrawableHelpData.initSize();

      ExecutionContextCanvasGui ec = (ExecutionContextCanvasGui) cmd.getExecutionContext();
      strDrawableHelpData.shShowDrawable(ec, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP);

      cmd.actionDone(strDrawableHelpData, 0);
   }

   /**
    * Each Appli has Start Page
    * <br>
    * <br>
    * Naviation to Home page in top level
    */
   private void cmdHomePage(CmdInstanceGui cd) {

   }

   private void cmdLightToggle(CmdInstance cmd) {
      //get the API to the light
      IHostCoreTools l = gc.getCFC().getHostTools();
      //gc.getEC().produceEvent(gc.getDeviceDriver().getProducerIDCamera(), IEvents.DEVICE_VIRT_FLASH_LIGHT, this);
   }

   /**
    * Which Menubar? All, active?
    * 
    * Context in which the command was made
    * 
    * @param cmd
    */
   public void cmdMenuCanvasToggle(CmdInstance cmd) {
      CanvasGuiCtx cv = (CanvasGuiCtx) cmd.paramO;
      CmdMenuBar cm = cv.getMenuBar();
      int mode = cm.getMenuBarTech().get1(IMenus.MENUS_OFFSET_03_SHOW_MODE1);
      if (mode == IMenus.SHOW_MODE_0_ALWAYS_ON) {
         mode = IMenus.SHOW_MODE_1_ON_DEMAND;
      } else {
         mode = IMenus.SHOW_MODE_0_ALWAYS_ON;
      }
      //write feedback. user String Top,Bottom,Left,Right
      cm.getMenuBarTech().set1(IMenus.MENUS_OFFSET_03_SHOW_MODE1, mode);
      int responseType = FLAG_04_RENEW_LAYOUT | FLAG_02_FULL_REPAINT | FLAG_06_DATA_REFRESH;
      cmd.actionDone(null, responseType);
   }

   private void cmdPause() {

      CanvasAppliInputGui cv = gc.getCanvasGCRoot().getCanvas();
      AppliGui am = gc.getAppli();

      //check if  view state
      ByteObject bo = gc.getBOCtxSettings();
      if (bo.hasFlag(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_01_FLAG1, ITechCtxSettingsAppGui.CTX_GUI_FLAG_1_USE_VIEW_STATE)) {
         //save the view state

         ByteObject vs = bo.getSubAtIndexNull(0);
         if (vs != null) {
            ViewState vsd = new ViewState(vs);
            //create 

         }
      }
   }

   private void cmdResetDebug(ExecutionContextCanvasGui ec) {
      CanvasAppliInputGui cv = gc.getCanvasGCRoot().getCanvas();
      CanvasDebugger debugCanvas = cv.getDebugCanvas();

      debugCanvas.reset();

      ec.cmdActionOnDrawable(debugCanvas);

   }

   /**
    * Copy the screen to a designated place
    * @param cmd
    */
   private void cmdScreenshot(CmdInstance cmd) {
      int bgColor = cmd.param;
      CanvasAppliInputGui canvas = gc.getCanvasGCRoot().getCanvas();
      final RgbImage img = canvas.getSSVirtualCanvasAsImage(bgColor);
      RgbImageSaveTask task = new RgbImageSaveTask(gc, canvas.getRepaintCtrlDraw(), img);
      gc.getUC().getWorkerThread().addToQueue(task);
      cmd.actionDone();
   }

   private void cmdSelectPointer(CmdInstanceGui cmd) {
      //depending on the state of the commands

      ExecutionContextCanvasGui ec = cmd.getExecutionContextGui();
      gc.getCanvasGCRoot().getCanvas().cmdSelectPointer(ec);
   }

   /**
    * Toggle the master log that belongs to {@link GuiCtx}
    * <br>
    * <br>
    * Hide it if it is currently the top drawable.
    * <br>
    * How can we have another window showing the {@link MLogViewer}?
    * <br>
    * Actually, the MLog is unique but can be double to another Frame 
    * <br>
    * Provided the host device allows it. J2SE only.
    * @param cmd
    */
   private void cmdViewLog(CmdInstanceGui cd) {
      //TODO where are stored options of the command? parameters telling how to proceed.

      //So the ctrl keeps state about cmds. the menu displays a string about the current
      //state Show Log/Hide Log/Show Log* ... 
      //sub menu.. generic way of doing a command and if not happy, undo / modify /redo

      //TODO command options to detach/attach 
      if (gc.getCtxLogViewer() == null) {
         //create one
         MLogViewer log = new MLogViewer(gc, gc.getStyleClass(1));
         log.getLay().layFullViewContext();
         log.setViewContext(gc.getCanvasGCRoot().getCanvas().getVCAppli());
         gc.setCtxLogViewer(log);

         IHostFeature feats = gc.getHostFeature();
         boolean hasMultiWindows = feats.isHostFeatureSupported(ITechHostFeatureDrawUI.SUP_ID_24_MULTIPLE_WINDOWS);
         //
         if (hasMultiWindows && cd.param == ITechCmdViewLog.PARAM_1_OUTSIDE) {
            //check if params already have data for this canvas
            ByteObject bo = gc.getCUC().createBOCanvasHostDefault();
            bo.setFlag(IBOCanvasHost.CANVAS_HOST_OFFSET_01_FLAG, IBOCanvasHost.CANVAS_HOST_FLAG_2_WINDOW, true);
            //sets the IDs
            bo.set2(IBOCanvasHost.CANVAS_HOST_OFFSET_03_ID2, ITechCtxSettingsAppGui.CANVAS_ID_LOGVIEWER);

            //creation of a canvas goes through the 
            CanvasAppliInputGui canvas = new CanvasAppliInputGui(gc, bo);

            canvas.getCanvasHost().setStartPositionAndSize();
            canvas.showNotify();
            canvas.getCanvasHost().canvasShow();

            //set the viewctx to this canvas
            log.setViewContext(canvas.getVCAppli());

         } else {
            //get the viewctx of the current canvas

            log.setViewContext(gc.getCanvasGCRoot().getCanvas().getVCAppli());
         }

      }
      MLogViewer log = gc.getCtxLogViewer();
      //physically attach
      InputConfig ic = cd.getInputConfig();
      ExecutionContextCanvasGui ec = cd.getExecutionContextGui();
      gc.getFocusCtrl().newFocusKey(ec, log);
      //topolgy will sort out the type
      ViewContext vc = log.getVC();
      vc.getTopo().addDLayer(log, ITechCanvasDrawable.SHOW_TYPE_1_OVER_TOP);
   }

   /**
    * Command action for framework {@link MCmd}.
    * 
    */
   public void commandAction(CmdInstance cmd) {
      if (cmd instanceof CmdInstanceGui) {
         commandActionDrawable((CmdInstanceGui) cmd);
         return;
      }

   }

   /**
    * 
    * @param cd
    * @return
    */
   public boolean commandActionDrawable(CmdInstanceGui cd) {
      ExecutionContextCanvasGui ec = cd.getExecutionContextGui();
      int cmdid = cd.getCmdId();
      switch (cmdid) {
         case CMD_51_SHOW_HOME:
            cmdHomePage(cd);
            break;
         case CMD_60_FONT_SIZE_INCREASE:
            cmdFontSizeIncrease(cd);
            break;
         case CMD_61_FONT_SIZE_DECREASE:
            cmdFontSizeDecrease(cd);
            break;
         case CMD_91_DEBUG_CHANGE:
            cmdDebugCanvasChange(cd);
            break;
         case CMD_92_DEBUG_TOGGLE_ON_OFF:
            cmdDebugCanvasToggle(cd);
            break;
         case CMD_93_DEBUG_ITERATE_UP:
            cmdDebugCanvasIterateUp(cd);
            break;
         case CMD_94_DEBUG_ITERATE_DOWN:
            cmdDebugCanvasIterateDown(cd);
            break;
         case CMD_GUI_08_PAGE_NEXT:
            cmdPageNext(cd);
            break;
         case CMD_GUI_09_PAGE_PREVIOUS:
            cmdPagePrevious(cd);
            break;
         case CMD_24_SELECT:
            cmdSelectPointer(cd);
            break;
         case CMD_04_OK:
            cmdAccept(cd);
            break;
         case CMD_40_LANGUAGE_CHANGE:
            throw new IllegalArgumentException("Cmd has its own class");
         case CMD_71_DEBUG_FLAGS:

            break;
         case CMD_47_VIEW_LOG:
            cmdViewLog(cd);
            break;
         case CMD_39_HELP:
            cmdHelp(cd);
            break;
         case CMD_28_HISTORY_BACK:
            cmdHistoryBack(cd);
            break;
         case CMD_29_HISTORY_FORWARD:
            cmdHistoryForward(cd);
            break;
         case CMD_75_CHANGE_MENU_POSITION:

            break;
         case CMD_76_TOGGLE_MENU:

            break;
         case CMD_42_SCREENSHOT:
            cmdScreenshot(cd);
            break;
         case CMD_41_LIGHT_TOGGLE:
            cmdLightToggle(cd);
            break;
         default:
            break;
      }

      return true;
   }

   private void cmdHistoryForward(CmdInstanceGui cd) {
      ExecutionContextCanvasGui ec = cd.getExecutionContextGui();
      //gives focus and show the previous top level
      int num = gc.getTopLvl().forwardCommand(ec);
      cd.actionDone(null, 0);      
   }

   private void cmdHistoryBack(CmdInstanceGui cd) {
      ExecutionContextCanvasGui ec = cd.getExecutionContextGui();
      if (cd.getCmdNode() == currentLocation) {
         //            fp.setFinished(true);
         //            gc.getTopLvl().reloadLast(gc);
         //            cmd.actionDone(fp, 0);
      } else {
         //gives focus and show the previous top level
         int num = gc.getTopLvl().backCommand(ec);
         cd.actionDone(null, 0);
      }      
   }

   private void cmdPagePrevious(CmdInstanceGui cd) {
      int prevPageID = cd.getParamUndoInt(0);
      if (!cd.isUndoMode()) {
         prevPageID = gc.getTopLvl().prevPage();
         cd.setParamUndoInt(0, prevPageID);
      }
      cd.getExecutionContextGui().addPage(prevPageID);
      cd.actionDone();
   }

   private void cmdPageNext(CmdInstanceGui cd) {
      //TODO part of the navigate framwork.. up down, left right back next, enter
      int nextPageID = cd.getParamUndoInt(0);
      if (!cd.isUndoMode()) {
         nextPageID = gc.getTopLvl().nextPage();
         cd.setParamUndoInt(0, nextPageID);
      }
      cd.getExecutionContextGui().addPage(nextPageID);
      cd.actionDone();
   }

   public void consumeEvent(BusEvent e) {
      Object producer = e.getProducer();
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, CmdExectutorGui.class, 500);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, CmdExectutorGui.class, 500);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      
   }
   //#enddebug
   

}
