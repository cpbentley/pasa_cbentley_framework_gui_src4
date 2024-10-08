package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.interfaces.IHostFeature;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdNode;
import pasa.cbentley.framework.cmd.src4.engine.MCmd;
import pasa.cbentley.framework.cmd.src4.input.CommanderAbstract;
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
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeLanguage;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiChangeMenuLocation;
import pasa.cbentley.framework.gui.src4.cmd.mcmd.MCmdGuiNewStart;
import pasa.cbentley.framework.gui.src4.core.StyleClass;
import pasa.cbentley.framework.gui.src4.ctx.CanvasGuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.ITechCanvasDrawable;
import pasa.cbentley.framework.gui.src4.interfaces.IUIView;
import pasa.cbentley.framework.gui.src4.menu.CmdMenuBar;
import pasa.cbentley.framework.gui.src4.menu.IMenus;
import pasa.cbentley.framework.gui.src4.mui.AppliGui;
import pasa.cbentley.framework.gui.src4.mui.MLogViewer;
import pasa.cbentley.layouter.src4.engine.SizerFactory;

/**
 * 
 * @author Charles Bentley
 *
 */
public class CommanderGui extends CommanderAbstract implements ICmdsGui, ITechInputFeedback, ITechCanvasDrawable, IEventConsumer {

   private CmdInstance               activeCmdLang;

   /**
    * Set to the command currently being active.
    * <br>
    * <br>
    * Used for commands that have several steps.
    * <br>
    * <br>
    * 
    */
   private CmdInstance               activeCommand;

   private MCmdGuiChangeLanguage     cmdGuiChangeLanguage;

   private MCmdGuiChangeMenuLocation cmdGuiChangeMenuLocation;

   private CmdNode                   currentLocation;

   protected final GuiCtx            gc;

   /**
    * Shows helps data.
    * <br>
    * 
    */
   private HelperStringDrawable      strDrawableHelpData;

   public CommanderGui(GuiCtx gc) {
      super(gc.getCC());
      this.gc = gc;

   }

   public CmdInstance createNewCommand(int vcmdID) {
      return new CmdInstanceGui(gc, vcmdID);
   }

   /**
    * 
    * @param cmd
    */
   protected void cmdAccept(CmdInstanceGui cmd) {
      if (activeCommand != null) {
         //TODO check the context in which accept was called.
         //Help dialog shows OK. MORE.
         MCmd m = cmd.getParentCmdDef();
         if (m != null && m.getCmdId() == CMD_19_HELP) {
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
      System.out.println(gc.getCanvasGCRoot().toString());
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
      CmdFactoryGui cfg = gc.getCmdFactoryGui();
      MCmd cmd = cd.getCmd(); //if null for some reason. we must def
      MCmdGuiChangeCanvasDebugger cmdModelDebug = cfg.getMCmdGuiChangeCanvasDebugger();
      cd.setBluePrint(cmdModelDebug);
      CanvasAppliInputGui canvas = gc.getCanvasGCRoot().getCanvas();
      cmdModelDebug.setParamsToggle(cd, canvas);

      //
   }

   private void cmdDebugFlags() {
      // TODO Auto-generated method stub

   }

   protected void cmdFontSizedecrease(CmdInstanceGui cd) {
      InputConfig ic = cd.getIC();
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
      InputConfig ic = cd.getIC();
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

      ExecutionContextCanvasGui ec = cmd.getExecutionCtxGui();
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
            bo.setFlag(IBOCanvasHost.TCANVAS_OFFSET_01_FLAG, IBOCanvasHost.TCANVAS_FLAG_2_WINDOW, true);
            //sets the IDs
            bo.set2(IBOCanvasHost.TCANVAS_OFFSET_03_ID2, ITechCtxSettingsAppGui.CANVAS_ID_LOGVIEWER);

            //creation of a canvas goes through the 
            CanvasAppliInputGui canvas = new CanvasAppliInputGui(gc, bo);

            canvas.getCanvasHost().setDefaultStartPosition();
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
      InputConfig ic = cd.getIC();
      ExecutionContextCanvasGui ec = cd.getExecutionCtxGui();
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
      ExecutionContextCanvasGui ec = cd.getExecutionCtxGui();
      int cmdid = cd.getCmdID();
      if (cmdid == CMD_51_SHOW_HOME) {
         cmdHomePage(cd);
      } else if (cmdid == VCMD_00_LAST_LOGIN) {
         MCmdGuiNewStart cmdGuiNewStart = new MCmdGuiNewStart(gc);
         cd.setMCmd(cmdGuiNewStart);
      } else if (cmdid == CMD_60_FONT_SIZE_INCREASE) {
         cmdFontSizedecrease(cd);
      } else if (cmdid == CMD_61_FONT_SIZE_DECREASE) {
         cmdFontSizeIncrease(cd);
      } else if (cmdid == CMD_91_DEBUG_CHANGE) {
         cmdDebugCanvasChange(cd);
      } else if (cmdid == CMD_92_DEBUG_TOGGLE_ON_OFF) {
         cmdDebugCanvasToggle(cd);
      } else if (cmdid == CMD_93_DEBUG_ITERATE_UP) {
         cmdDebugCanvasIterateUp(cd);
      } else if (cmdid == CMD_94_DEBUG_ITERATE_DOWN) {
         cmdDebugCanvasIterateDown(cd);
      } else if (cmdid == VCMD_08_PAGE_NEXT) {
         //TODO part of the navigate framwork.. up down, left right back next, enter
         int nextPageID = cd.getParamUndoInt(0);
         if (!cd.isUndoMode()) {
            nextPageID = gc.getTopLvl().nextPage();
            cd.setParamUndoInt(0, nextPageID);
         }
         cd.getExecutionCtxGui().addPage(nextPageID);
         cd.actionDone();
      } else if (cmdid == VCMD_09_PAGE_PREVIOUS) {
         int prevPageID = cd.getParamUndoInt(0);
         if (!cd.isUndoMode()) {
            prevPageID = gc.getTopLvl().prevPage();
            cd.setParamUndoInt(0, prevPageID);
         }
         cd.getExecutionCtxGui().addPage(prevPageID);
         cd.actionDone();
      } else if (cmdid == CMD_24_SELECT) {
         //select drawable at pointer position
         cmdSelectPointer(cd);
      } else if (cmdid == CMD_19_HELP) {
         cmdHelp(cd);
      } else if (cmdid == CMD_04_OK) {
         cmdAccept(cd);

      } else if (cmdid == CMD_92_DEBUG_TOGGLE_ON_OFF) {
         cmdDebugCanvasToggle(cd);
      } else if (cmdid == CMD_73_DEBUG) {
         cmdDebug();
      } else if (cmdid == CMD_71_DEBUG_FLAGS) {
         cmdDebugFlags();
      } else if (cmdid == CMD_40_LANGUAGE_CHANGE) {
         //
         cmdGuiChangeLanguage.cmdExecuteFinal(cd);
      } else if (cmdid == CMD_75_CHANGE_MENU_POSITION) {
         cmdGuiChangeMenuLocation.cmdExecuteFinal(cd);
      } else if (cmdid == CMD_76_TOGGLE_MENU) {
         cmdMenuCanvasToggle(cd);
      } else if (cmdid == CMD_46_SHOW_CMD_HISTORY) {
         //show a table with cmd history
      } else if (cmdid == CMD_70_CHANGE_DEBUG_INFO) {
         //show a table with cmd history
         cmdDebugCanvasToggle(cd);
      } else if (cmdid == CMD_47_VIEW_LOG) {
         cmdViewLog(cd);
      } else if (cmdid == CMD_28_HISTORY_BACK) {
         if (cd.getCmdNode() == currentLocation) {
            //            fp.setFinished(true);
            //            gc.getTopLvl().reloadLast(gc);
            //            cmd.actionDone(fp, 0);
         } else {
            //gives focus and show the previous top level
            int num = gc.getTopLvl().backCommand(ec);
            cd.actionDone(null, 0);
         }
      } else if (cmdid == CMD_29_HISTORY_FORWARD) {
         //gives focus and show the previous top level
         int num = gc.getTopLvl().forwardCommand(ec);
         cd.actionDone(null, 0);
      } else if (cmdid == CMD_35_SHOW_EASTER_EGG1) {
         //         fp = new FallingPixels(gc);
         //         fp.setBehaviorFlag(IDrawable.BEHAVIOR_20_FULL_CANVAS_W, true);
         //         fp.setBehaviorFlag(IDrawable.BEHAVIOR_21_FULL_CANVAS_H, true);
         //         //automatic size init
         //         fp.init(0, 0);
         //         cc = create("FallingPixels");
         //         cc.addMenuCmd(CMD_HISTORY_BACK);
         //         cc.setListener(this);
         //         fp.setCtx(cc);
         //         getMe();
         //         gc.getController().shShowDrawable(fp, IMaster.SHOW_TYPE_0REPLACE);
         //
         //         cmd.actionDone(null, 0);
      } else if (cmdid == CMD_42_SCREENSHOT) {
         cmdScreenshot(cd);
      } else if (cmdid == CMD_41_LIGHT_TOGGLE) {
         cmdLightToggle(cd);
      } else if (cmdid == CMD_36_TEST) {
         //gc.getEC().produceEvent(gc.getDeviceDriver().getProducerIDCamera(), IEvents.DEVICE_TEST, this);
      } else {
         return false;
      }
      //cmd has been processed
      cc.processCmd(cd);
      return true;
   }

   public void consumeEvent(BusEvent e) {
      Object producer = e.getProducer();
   }

}
