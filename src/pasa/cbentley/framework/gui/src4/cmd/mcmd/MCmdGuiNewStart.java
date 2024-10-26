package pasa.cbentley.framework.gui.src4.cmd.mcmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.cmd.src4.cmd.MCmdAbstract;
import pasa.cbentley.framework.cmd.src4.engine.CmdInstance;
import pasa.cbentley.framework.cmd.src4.engine.CmdProcessor;
import pasa.cbentley.framework.cmd.src4.interfaces.ITechCmd;
import pasa.cbentley.framework.core.ui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.canvas.CanvasBOHelper;
import pasa.cbentley.framework.gui.src4.canvas.CanvasExtras;
import pasa.cbentley.framework.gui.src4.canvas.ICanvasDrawable;
import pasa.cbentley.framework.gui.src4.cmd.CmdFactoryGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.cmd.CmdProcessorGui;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.ctx.app.AppliGui;
import pasa.cbentley.framework.gui.src4.exec.ExecutionContextCanvasGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsGui;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

/**
 * Responsibility:  Start a single canvas.
 * 
 * <p>
 * Each Canvas will have its own start command because each canvas has different settings
 * <li> {@link MCmdGuiChangeCanvasDebugger} for Debug settings
 * <li> {@link MCmdGuiChangeMenuLocation} for Menu bar settings
 * </p>
 * 
 * <p>
 * <b>Usage:</b><br>
 * It is called inside the {@link AppliGui#amsAppStart()} chain of actions.
 * </p>
 * 
 * @author Charles Bentley
 * 
 * @see MCmdAbstractGui
 *
 */
public class MCmdGuiNewStart extends MCmdAbstractGui implements IEventConsumer {

   /**
    * Search on this for direct calls
    */
   public static final int CMD_ID = CMD_GUI_00_LAST_LOGIN;

   public MCmdGuiNewStart(GuiCtx gc) {
      super(gc, CMD_GUI_00_LAST_LOGIN);
      this.setFlagGeneNeverUndoable();
   }

   public void cmdExecuteFinalGui(CmdInstanceGui ci) {

      ExecutionContextCanvasGui ec = ci.getExecutionContextGui();
      ec.checkCmdInstanceNotNull();

      ICanvasDrawable canvas = gc.getCanvasRoot();

      canvas.show(ci);

      CmdProcessor cmdProcessorGui = gc.getCmdProcessorGui();
      CmdFactoryGui cmdFactoryGui = gc.getCmdFactoryGui();
      AppliGui appliGui = gc.getAppli();
      IDrawable firstDrawable = null;

      //check if  view state
      ByteObject bo = gc.getBOCtxSettings();

      //#debug
      toDLog().pCmd("GuiCtx BOCtxSettings", bo, toStringGetLine(MCmdGuiNewStart.class, "cmdExecuteFinalGui", 75));

      //are we executing the new start with previous state?
      if (bo.hasFlag(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_01_FLAG1, ITechCtxSettingsAppGui.CTX_GUI_FLAG_1_USE_VIEW_STATE)) {
         ByteObject vs = bo.getSubAtIndexNull(0);
         if (vs != null) {
            ViewState vsd = new ViewState(vs);
            //create 
            int pageid = bo.get2(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_03_VIEW_START_PAGE2);
            gc.getTopLvl().setFirstPage(pageid);
            firstDrawable = gc.getTopLvl().getPage(ec, pageid);
         }
      } else {
         firstDrawable = appliGui.getFirstDrawable();
      }

      if (firstDrawable == null) {
         //there is nothing to show.
         //TODO output message or draw message on drawable

      } else {

         //let the appli queue cmds for the start up and set up state
         appliGui.cmdStart(ci);

         //animation on the whole root drawable ? no in the constructor.
         //IDrawable root = cv.getRoot();
         //root.shShowDrawableOver(ic, IBase.SHOW_TYPE_0_REPLACE);

         //#debug
         //toLog().ptCmd("Showing ", d, ViewCommandListener.class, "cmdNewLogin");

         //show it
         firstDrawable.shShowDrawableOver(ec);

         //#debug
         //toLog().ptCmd("Root ", gc.getVCRoot(), ViewCommandListener.class, "cmdNewLogin");
         //#debug
         //toLog().ptCmd("Appli", gc.getVCAppli(), ViewCommandListener.class, "cmdNewLogin");
      }

      //after the main canvas is shown
      CanvasExtras ce = canvas.getExtras();
      CanvasBOHelper settingsHelper = ce.getCanvasBOHelper();

      //we set the canvas debuger with a command
      //this allows the user to change it easily by inspecting command history
      MCmdGuiChangeCanvasDebugger cmdModelDebug = cmdFactoryGui.getMCmdGuiChangeCanvasDebugger();
      CmdInstanceGui ciModelDebug = gc.createInstanceFromParent(cmdModelDebug, ci);
      int debugMode = settingsHelper.getDebugMode();
      int debugPosition = settingsHelper.getDebugBarPosition();
      cmdModelDebug.setParams(ciModelDebug, canvas, debugMode, debugPosition);
   

      //same strategy for menu bar position
      MCmdGuiChangeMenuLocation cmdMenuLocation = cmdFactoryGui.getMCmdGuiChangeMenuLocation();
      CmdInstanceGui ciMenuLocation = gc.createInstanceFromParent(cmdMenuLocation,ci);
      boolean useMenuBar = settingsHelper.isUsingMenuBar();
      int pos = settingsHelper.getMenuBarPosition();
      ciMenuLocation.param = pos;
      ciMenuLocation.setFlag(1, useMenuBar);
      ciMenuLocation.setParamed();

      //we don't want them to be undoable because they are slaves
      //why executing later ? because right now there is no canvas yet
      cmdProcessorGui.executeLater(ciModelDebug);
      cmdProcessorGui.executeLater(ciMenuLocation);
   }

   public MCmdAbstract createInstance(CmdInstance ci) {
      return new MCmdGuiNewStart(gc);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, MCmdGuiNewStart.class, toStringGetLine(150));
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, MCmdGuiNewStart.class, toStringGetLine(150));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("CMD_ID", CMD_ID);
   }
   //#enddebug

}
