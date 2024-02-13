package pasa.cbentley.framework.gui.src4.cmd;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.event.BusEvent;
import pasa.cbentley.core.src4.event.IEventConsumer;
import pasa.cbentley.framework.coreui.src4.utils.ViewState;
import pasa.cbentley.framework.gui.src4.canvas.CanvasAppliInputGui;
import pasa.cbentley.framework.gui.src4.canvas.InputConfig;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.ctx.ITechCtxSettingsAppGui;
import pasa.cbentley.framework.gui.src4.interfaces.ICmdsView;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;
import pasa.cbentley.framework.gui.src4.mui.AppliGui;

public class MCmdGuiNewStart extends MCmdGui implements IEventConsumer {

   public MCmdGuiNewStart(GuiCtx gc) {
      super(gc, ICmdsView.VCMD_00_LAST_LOGIN);
   }

   public void execute(InputConfig ic) {
      CmdInstanceDrawable cmd = ic.getCmdInstance();
      CanvasAppliInputGui cv = gc.getCanvasRoot();
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
         appliGui.cmdStart(cmd);

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

   }

   public void consumeEvent(BusEvent e) {
      // TODO Auto-generated method stub
      
   }

}
