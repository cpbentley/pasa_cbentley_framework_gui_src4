package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.CanvasBOHelper;
import pasa.cbentley.framework.gui.src4.canvas.CanvasExtras;
import pasa.cbentley.framework.gui.src4.canvas.ICanvasDrawable;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.mui.AppliGui;

/**
 * Responsibility:  Start a single canvas.
 * 
 * Each Canvas will have its own start.
 * 
 * @author Charles Bentley
 *
 */
public class MCmdGuiNewStart extends MCmdGui implements IEventConsumer {

   public MCmdGuiNewStart(GuiCtx gc) {
      super(gc, ICmdsView.VCMD_00_LAST_LOGIN);
   }

   public void execute(InputConfig ic) {

      //
      ic.checkCmdInstanceNotNull();

      CmdInstanceDrawable cmdInstance = ic.getCmdInstance();
      ICanvasDrawable canvas = gc.getCanvasRoot();

      canvas.show(cmdInstance);

      MCmdGuiChangeMenuLocation de = gc.getCommanderGui().getMCmdGuiChangeMenuLocation();
      AppliGui appliGui = gc.getAppli();
      IDrawable firstDrawable = null;
      //check if  view state
      ByteObject bo = gc.getBOCtxSettings();

      //#debug
      toDLog().pCmd("", bo, ViewCommandListener.class, "cmdNewLogin");

      //are we executing the new start with previous state?
      if (bo.hasFlag(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_01_FLAG1, ITechCtxSettingsAppGui.CTX_GUI_FLAG_1_USE_VIEW_STATE)) {
         ByteObject vs = bo.getSubAtIndexNull(0);
         if (vs != null) {
            ViewState vsd = new ViewState(vs);
            //create 
            int pageid = bo.get2(ITechCtxSettingsAppGui.CTX_GUI_OFFSET_03_VIEW_START_PAGE2);
            gc.getTopLvl().setFirstPage(pageid);
            firstDrawable = gc.getTopLvl().getPage(ic, pageid);
         }
      } else {
         firstDrawable = appliGui.getFirstDrawable();
      }

      if (firstDrawable == null) {
         //there is nothing to show.
         //TODO output message or draw message on drawable

      } else {

         //let the appli queue cmds for the start up and set up state
         appliGui.cmdStart(cmdInstance);

         //animation on the whole root drawable ? no in the constructor.
         //IDrawable root = cv.getRoot();
         //root.shShowDrawableOver(ic, IBase.SHOW_TYPE_0_REPLACE);

         //#debug
         //toLog().ptCmd("Showing ", d, ViewCommandListener.class, "cmdNewLogin");

         //show it
         firstDrawable.shShowDrawableOver(ic);

         //#debug
         //toLog().ptCmd("Root ", gc.getVCRoot(), ViewCommandListener.class, "cmdNewLogin");
         //#debug
         //toLog().ptCmd("Appli", gc.getVCAppli(), ViewCommandListener.class, "cmdNewLogin");
      }

      //after the main canvas is shown
      CanvasExtras ce = canvas.getExtras();
      CanvasBOHelper settingsHelper = ce.getCanvasBOHelper();

      MCmdGuiChangeCanvasDebugger cmdModelDebug = gc.getCommanderGui().getMCmdGuiChangeCanvasDebugger();
      CmdInstanceDrawable cdSetCanvasDebug = new CmdInstanceDrawable(gc, cmdModelDebug);
      int debugMode = settingsHelper.getDebugMode();
      int debugPosition = settingsHelper.getDebugBarPosition();
      cmdModelDebug.setParams(cdSetCanvasDebug, canvas, debugMode, debugPosition);
      cdSetCanvasDebug.parentCmd = cmdInstance;

      CmdInstanceDrawable cdSetMenuBar = new CmdInstanceDrawable(gc, ICmdsView.VCMD_17_MENU_BAR);
      cdSetMenuBar.parentCmd = cmdInstance;
      boolean useMenuBar = settingsHelper.isUsingMenuBar();
      int pos = settingsHelper.getMenuBarPosition();
      cdSetMenuBar.param = pos;
      cdSetMenuBar.setFlag(1, useMenuBar);
      cdSetMenuBar.setParamed();

      gc.getCommanderGui().executeLater(cdSetCanvasDebug);
      gc.getCommanderGui().executeLater(cdSetMenuBar);
   }

   public void consumeEvent(BusEvent e) {
      // TODO Auto-generated method stub

   }

}
