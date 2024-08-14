package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceGui;
import pasa.cbentley.framework.gui.src4.core.TopoViewDrawable;
import pasa.cbentley.framework.input.src4.event.ctrl.EventController;

public interface ICanvasDrawable extends IStringable {

   public EventController getEventController();

   public ViewContext getVCAppli();

   public RepaintHelperGui getRepaintCtrlDraw();

   public CanvasExtras getExtras();

   public void show(CmdInstanceGui cmd);

   public TopoViewDrawable getTopoViewDrawable();

   public CanvasBOHelper getCanvasBOHelper();
}
