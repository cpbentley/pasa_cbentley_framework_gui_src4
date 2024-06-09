package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.framework.gui.src4.cmd.CmdInstanceDrawable;
import pasa.cbentley.framework.gui.src4.core.TopoViewDrawable;
import pasa.cbentley.framework.input.src4.EventController;

public interface ICanvasDrawable extends IStringable {

   public EventController getEvCtrl();

   public ViewContext getVCAppli();

   public RepaintCtrlGui getRepaintCtrlDraw();

   public CanvasExtras getExtras();

   public void show(CmdInstanceDrawable cmd);

   public TopoViewDrawable getTopoViewDrawable();

   public CanvasBOHelper getCanvasBOHelper();
}
