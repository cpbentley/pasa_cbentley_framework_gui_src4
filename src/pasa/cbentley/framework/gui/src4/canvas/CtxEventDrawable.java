package pasa.cbentley.framework.gui.src4.canvas;

import pasa.cbentley.framework.cmd.src4.input.NodeDevice;
import pasa.cbentley.framework.cmd.src4.input.FocusTypeInput;
import pasa.cbentley.framework.gui.src4.ctx.GuiCtx;
import pasa.cbentley.framework.gui.src4.interfaces.IDrawable;

public class CtxEventDrawable extends NodeDevice {

   private IDrawable newDrw;

   private IDrawable oldDrw;

   public CtxEventDrawable(GuiCtx gc, IDrawable newD, IDrawable oldD, FocusTypeInput focus) {
      super(gc.getCC(), newD != null ? newD.getCmdNode() : null, oldD != null ? oldD.getCmdNode() : null, focus);
      this.newDrw = newD;
      this.oldDrw = oldD;
   }

}
